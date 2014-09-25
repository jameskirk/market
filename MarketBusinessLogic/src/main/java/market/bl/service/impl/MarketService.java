package market.bl.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import market.bl.dao.IMarketDao;
import market.bl.dao.XmlMarketDao;
import market.bl.exception.MarketException;
import market.bl.service.IMarketOrderService;
import market.bl.service.IMarketTaskService;
import market.model.constant.OrderState;
import market.model.constant.TaskName;
import market.model.constant.TaskState;
import market.model.order.Order;
import market.model.order.PartOrderForUser;
import market.model.security.User;
import market.model.task.Task;

public class MarketService implements IMarketOrderService, IMarketTaskService {

    private IMarketDao dao = new XmlMarketDao("build/resource/db.xml");

    @Override
    public void createAndStartOrder(Order order) throws MarketException {
	if (order.orderState != OrderState.RUNNUNG) {
	    throw new MarketException("order must be in running state");
	}
	order.createdDate = new Date();
	order.stopExecuted = false;
	order.shippingExecuted = false;
	
	if (order.partOrderForUserList == null || order.partOrderForUserList.size() > 1 || order.partOrderForUserList.get(0) == null) {
	    throw new MarketException("only admin user must be exist in order");
	}
	if (dao.get(order.partOrderForUserList.get(0).userId, User.class) == null) {
	    throw new MarketException("admin user must be exist in database");
	}
	order.partOrderForUserList.get(0).admin = true;
	dao.saveOrUpdate(order);
	
	//create tasks
	order.partOrderForUserList.get(0).taskList = generateDefaultTaskList();
	
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
	int oldSize = oldOrder.partOrderForUserList.size();
	int size = order.partOrderForUserList.size();
	if (oldSize < size) {
		
		TaskName mainTaskName = null;
		for (Task task: order.partOrderForUserList.get(0).taskList) {
			if (task.taskState == TaskState.RUNNING) {
				mainTaskName = task.taskName;
				break;
			}
		}
		
	    if (mainTaskName != TaskName.REQUEST_PRICE_LIST && mainTaskName != TaskName.CHOICE_PRODUCTS ) {
	    	throw new MarketException("can not add new users when order is not in " 
	    			+ TaskName.REQUEST_PRICE_LIST + TaskName.CHOICE_PRODUCTS);
	    }
	    
	    for (int i = oldSize; i < size; i++) {
			PartOrderForUser partOrder = order.partOrderForUserList.get(i);
			if (dao.get(partOrder.userId, User.class) == null) {
			    throw new MarketException("user must be exist in database");
			}
			partOrder.admin = false;
			
			partOrder.taskList = generateDefaultTaskList();
			if (mainTaskName == TaskName.CHOICE_PRODUCTS) {
			    partOrder.taskList.get(0).taskState = TaskState.PASSED;
			    partOrder.taskList.get(1).taskState = TaskState.RUNNING;
			}
	    }
	}
	dao.saveOrUpdate(oldOrder);
    }

    @Override
    public void resumeTask(Task task) {
    	Order order = dao.get(task.orderId, Order.class);
    	
    	PartOrderForUser part = null;
    	for (PartOrderForUser p: order.partOrderForUserList) {
    		for (Task t: p.taskList) {
    			if (t.id == task.id) {
    				part = p;
    			}
    		}
    	}
    	
    	// handle admin's task
    	if (part.admin) {
    		if (task.taskName == TaskName.REQUEST_PRICE_LIST) {
    			
    		} else if (task.taskName == TaskName.CHOICE_PRODUCTS) {

    		} else if(task.taskName == TaskName.WAITING_RESPONCE_FROM_FIRM) {

    		} else if(task.taskName == TaskName.WAITING_BITTING) {

    		} else if(task.taskName == TaskName.SHIPPING) {
    			
    		}

    	}
	
    }

    public IMarketDao getDao() {
        return dao;
    }
    
    private List<Task> generateDefaultTaskList() {
	Task task0 = new Task();
	task0.id = 0; // lastId
	task0.taskName = TaskName.REQUEST_PRICE_LIST;
	task0.taskState = TaskState.RUNNING;
	
	Task task1 = new Task();
	task1.id = 1; // lastId
	task1.taskName = TaskName.CHOICE_PRODUCTS;
	task1.taskState = TaskState.NOT_STARTED;
	
	Task task2 = new Task();
	task2.id = 2; // lastId
	task2.taskName = TaskName.WAITING_RESPONCE_FROM_FIRM;
	task2.taskState = TaskState.NOT_STARTED;
	
	Task task3 = new Task();
	task3.id = 3; // lastId
	task3.taskName = TaskName.WAITING_BITTING;
	task3.taskState = TaskState.NOT_STARTED;
	
	Task task4 = new Task();
	task4.id = 4; // lastId
	task4.taskName = TaskName.SHIPPING;
	task4.taskState = TaskState.NOT_STARTED;

	return Arrays.asList(task1, task2, task3, task4);
    }
    

}
