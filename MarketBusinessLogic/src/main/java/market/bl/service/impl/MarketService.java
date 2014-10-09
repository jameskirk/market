package market.bl.service.impl;

import market.bl.dao.IMarketDao;
import market.bl.dao.XmlMarketDao;
import market.bl.exception.MarketException;
import market.bl.service.IMarketOrderService;
import market.bl.service.IMarketTaskService;
import market.model.constant.TaskName;
import market.model.order.Order;
import market.model.order.UserOrder;
import market.model.security.User;
import market.model.task.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static market.model.constant.OrderState.RUNNING;
import static market.model.constant.TaskName.CHOICE_PRODUCTS;
import static market.model.constant.TaskName.REQUEST_PRICE_LIST;
import static market.model.constant.TaskName.SHIPPING;
import static market.model.constant.TaskName.WAITING_BITTING;
import static market.model.constant.TaskName.WAITING_RESPONSE_FROM_FIRM;
import static market.model.constant.TaskState.IN_PROGRESS;
import static market.model.constant.TaskState.NOT_STARTED;
import static market.model.constant.TaskState.PASSED;

public class MarketService implements IMarketOrderService, IMarketTaskService {

    private IMarketDao dao = new XmlMarketDao("build/resource/db.xml");

    @Override
    public void createAndStartOrder(Order order) throws MarketException {
        if (order.orderState != RUNNING) {
            throw new MarketException("order must be in running state");
        }
        order.createdDate = new Date();
        order.stopExecuted = false;
        order.shippingExecuted = false;

        // validate user order for admin
        UserOrder orderForAdmin;
        if (order.userOrderList == null || order.userOrderList.size() > 1 ||
                (orderForAdmin = order.userOrderList.get(0)) == null) {
            throw new MarketException("only admin user must be exist in order");
        }
        if (dao.get(orderForAdmin.userId, User.class) == null) {
            throw new MarketException("admin user must be exist in database");
        }
        if (orderForAdmin.productList != null) {
            throw new MarketException("product list must be empty for admin in order");
        }
        orderForAdmin.organizer = true;
        dao.saveOrUpdate(order);

        // create tasks
        List<Task> adminTaskList = generateDefaultTaskList(order.id, orderForAdmin.userId);
        for (Task t : adminTaskList) {
            dao.saveOrUpdate(t);
        }

    }

    @Override
    public Order getOrder(int id) {
        return dao.get(id, Order.class);
    }

    @Override
    public void modifyOrder(Order order) throws MarketException {
        Order oldOrder = getOrder(order.id);

        if (oldOrder == null) {
            throw new MarketException("order with id does not exist: " + order.id);
        }
        if (oldOrder.orderState != order.orderState) {
            throw new MarketException("you can not change order state");
        }

        // handle new user
        int oldSize = oldOrder.userOrderList.size();
        int size = order.userOrderList.size();
        if (oldSize < size) {

            TaskName mainTaskName = null;
            for (Task task : getTaskList(order.id, order.userOrderList.get(0).userId)) {
                if (task.taskState == IN_PROGRESS) {
                    mainTaskName = task.taskName;
                    break;
                }
            }

            if (mainTaskName != REQUEST_PRICE_LIST && mainTaskName != CHOICE_PRODUCTS) {
                throw new MarketException("can not add new users when order is not in " + REQUEST_PRICE_LIST + CHOICE_PRODUCTS);
            }

            for (int i = oldSize; i < size; i++) {
                UserOrder partOrder = order.userOrderList.get(i);
                if (dao.get(partOrder.userId, User.class) == null) {
                    throw new MarketException("user must be exist in database");
                }
                partOrder.organizer = false;

                // add tasks for new user
                List<Task> adminTaskList = generateDefaultTaskList(order.id, partOrder.userId);
                if (mainTaskName == CHOICE_PRODUCTS) {
                    adminTaskList.get(0).taskState = PASSED;
                    adminTaskList.get(1).taskState = IN_PROGRESS;
                }
                for (Task t : adminTaskList) {
                    dao.saveOrUpdate(t);
                }
            }
        }
        dao.saveOrUpdate(order);
    }

    public List<Task> getTaskList(int orderId, int userId) throws MarketException {
        // SELECT task t from Task t WHERE t.orderId = :orderId and t.userId =:
        // userId
        List<Task> retVal = new ArrayList<>();
        List<Task> allTask = dao.getAll(Task.class);
        // dirty hack, need field "order" in task. Now it sorted by id
        Collections.sort(allTask, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.id > o2.id ? 1 : -1;
            }
        });

        for (Task t : allTask) {
            if (t.orderId == orderId && t.userId == userId) {
                retVal.add(t);
            }
        }
        return retVal;
    }

    @Override
    public void resumeTask(Task task) throws MarketException {
        Order order = dao.get(task.orderId, Order.class);

        UserOrder userOrder = null;
        for (UserOrder p : order.userOrderList) {
            if (p.userId == task.userId) {
                userOrder = p;
            }
        }

        // handle admin's task
        if (task.taskName == REQUEST_PRICE_LIST) {
            handleRequestPriceList(order, userOrder, task);
        } else if (task.taskName == CHOICE_PRODUCTS) {
            List<Task> taskList = getTaskList(task.orderId, task.userId);
            for (int i = 0; i < taskList.size(); i++) {
                if (taskList.get(i).id == task.id) {
                    taskList.get(i).taskState = PASSED;
                    taskList.get(i + 1).taskState = IN_PROGRESS;
                    dao.saveOrUpdate(taskList.get(i));
                    dao.saveOrUpdate(taskList.get(i + 1));
                }
            }

        } else if (task.taskName == WAITING_RESPONSE_FROM_FIRM) {
        } else if (task.taskName == WAITING_BITTING) {
        } else if (task.taskName == SHIPPING) {
        }
    }

    public IMarketDao getDao() {
        return dao;
    }

    public void handleRequestPriceList(Order order, UserOrder userOrder, Task task) throws MarketException {
        if (userOrder.organizer) {
            // admin
            if (task.priceListUrl == null || task.priceListUrl.isEmpty()) {
                throw new MarketException("price list url must be filled");
            }
            // go to next task for all users
            for (UserOrder p : order.userOrderList) {
                List<Task> taskList = getTaskList(task.orderId, p.userId);
                for (int i = 0; i < taskList.size() - 1; i++) {
                    Task t = taskList.get(i);
                    Task tNext = taskList.get(i + 1);
                    if (t.taskName == REQUEST_PRICE_LIST && t.taskState == IN_PROGRESS) {
                        t.taskState = PASSED;
                        tNext.taskState = IN_PROGRESS;
                        dao.saveOrUpdate(t);
                        dao.saveOrUpdate(tNext);
                    }
                }
            }
        } else {
            // no admin
            if (task.taskName == REQUEST_PRICE_LIST) {
                throw new MarketException("task " + REQUEST_PRICE_LIST + " can be resumed only by admin");
            }
        }

    }

    private List<Task> generateDefaultTaskList(int orderId, int userId) {
        int taskStartId = orderId * 1000 + userId * 100;
        Task task0 = new Task();
        task0.id = taskStartId; // lastId
        task0.orderId = orderId;
        task0.userId = userId;
        task0.taskName = REQUEST_PRICE_LIST;
        task0.taskState = IN_PROGRESS;

        Task task1 = new Task();
        task1.id = taskStartId + 1; // lastId
        task1.orderId = orderId;
        task1.userId = userId;
        task1.taskName = CHOICE_PRODUCTS;
        task1.taskState = NOT_STARTED;

        Task task2 = new Task();
        task2.id = taskStartId + 2; // lastId
        task2.orderId = orderId;
        task2.userId = userId;
        task2.taskName = WAITING_RESPONSE_FROM_FIRM;
        task2.taskState = NOT_STARTED;

        Task task3 = new Task();
        task3.id = taskStartId + 3; // lastId
        task3.orderId = orderId;
        task3.userId = userId;
        task3.taskName = WAITING_BITTING;
        task3.taskState = NOT_STARTED;

        Task task4 = new Task();
        task4.id = taskStartId + 4; // lastId
        task4.orderId = orderId;
        task4.userId = userId;
        task4.taskName = SHIPPING;
        task4.taskState = NOT_STARTED;

        return Arrays.asList(task0, task1, task2, task3, task4);
    }

}
