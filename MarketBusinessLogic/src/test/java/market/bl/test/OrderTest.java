package market.bl.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import market.bl.exception.MarketException;
import market.bl.service.impl.MarketService;
import market.model.constant.OrderState;
import market.model.constant.TaskState;
import market.model.order.Order;
import market.model.order.PartOrderForUser;
import market.model.security.User;
import market.model.task.Task;

import org.junit.Test;


public class OrderTest {
	
	MarketService service = new MarketService();
    
    @Test
    public void createAndStart() throws MarketException {
	
	User user = new User();
	user.id = 1;
	user.name = "Bob";
	service.getDao().saveOrUpdate(user);
	
	Order order = new Order();
	order.id = 1;
	order.orderState = OrderState.RUNNUNG;
	order.partOrderForUserList = new ArrayList<PartOrderForUser>();
	PartOrderForUser forUser = new PartOrderForUser();
	forUser.userId = 1;
	forUser.admin = true;
	order.partOrderForUserList.add(forUser);
	service.createAndStartOrder(order);
	
	Order orderFromService = service.getOrder(1);
	
    }
    
    @Test
    public void addUser() throws MarketException {
    	createAndStart();
    	
    	User user = new User();
    	user.id = 2;
    	user.name = "Bill";
    	service.getDao().saveOrUpdate(user);
    	
    	Order order = service.getOrder(1);
    	PartOrderForUser part = new PartOrderForUser();
    	part.admin = false;
    	part.userId = 2;
    	order.partOrderForUserList.add(part);
    	
    	service.modifyOrder(order);
    	
    	Order orderFromService = service.getDao().get(1, Order.class);
    	
    	Assert.assertEquals(2, orderFromService.partOrderForUserList.size());
    	Assert.assertEquals(2, orderFromService.partOrderForUserList.get(1).userId);
    }
    
    @Test
    public void resumeTask0() throws MarketException {
    	addUser();
    	
    	List<Task> taskList = service.getTaskList(1, 1);
    	Assert.assertEquals(TaskState.RUNNING, taskList.get(0).taskState);
    	taskList.get(0).priceListUrl = "http://link-to-doc.doc";
    	
    	service.resumeTask(taskList.get(0));
    	
    	taskList = service.getTaskList(1, 1);
    	Assert.assertEquals(TaskState.PASSED, taskList.get(0).taskState);
    	Assert.assertEquals(TaskState.RUNNING, taskList.get(1).taskState);
    	
    	taskList = service.getTaskList(1, 2);
    	Assert.assertEquals(TaskState.PASSED, taskList.get(0).taskState);
    	Assert.assertEquals(TaskState.RUNNING, taskList.get(1).taskState);
    	
    	
    }
	
}
