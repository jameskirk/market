package market.bl.service.impl;

import java.util.ArrayList;
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
	order.partOrderForUserList.get(0).taskList = new ArrayList<Task>();
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

	order.partOrderForUserList.get(0).taskList.addAll(Arrays.asList(task1, task2, task3, task4));
	
    }

    @Override
    public Order getOrder(int id) {
	return dao.get(id, Order.class);
    }

    @Override
    public void modifyOrder(Order order) {
	throw new UnsupportedOperationException();
    }

    @Override
    public void resumeTask(Task task) {
	// TODO Auto-generated method stub
	
    }

    public IMarketDao getDao() {
        return dao;
    }
    

}
