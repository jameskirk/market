package market.bl.test;

import market.bl.exception.MarketException;
import market.bl.service.impl.MarketService;
import market.model.order.Order;
import market.model.order.UserOrder;
import market.model.security.User;
import market.model.task.Task;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static market.model.constant.OrderState.RUNNING;
import static market.model.constant.TaskState.IN_PROGRESS;
import static market.model.constant.TaskState.PASSED;
import static org.junit.Assert.assertEquals;


public class OrderTest {

    private MarketService service = new MarketService();

    @Test
    public void createAndStart() throws MarketException {
        User user = new User();
        user.id = 1;
        user.name = "Bob";
        service.getDao().saveOrUpdate(user);

        Order order = new Order();
        order.id = 1;
        order.orderState = RUNNING;
        order.userOrderList = new ArrayList<>();
        UserOrder forUser = new UserOrder();
        forUser.userId = 1;
        forUser.organizer = true;
        order.userOrderList.add(forUser);
        service.createAndStartOrder(order);

        service.getOrder(1);
    }

    @Test
    public void addUser() throws MarketException {
        createAndStart();

        User user = new User();
        user.id = 2;
        user.name = "Bill";
        service.getDao().saveOrUpdate(user);

        Order order = service.getOrder(1);
        UserOrder part = new UserOrder();
        part.organizer = false;
        part.userId = 2;
        order.userOrderList.add(part);

        service.modifyOrder(order);

        Order orderFromService = service.getDao().get(1, Order.class);

        assertEquals(2, orderFromService.userOrderList.size());
        assertEquals(2, orderFromService.userOrderList.get(1).userId);
    }

    @Test
    public void resumeTask0() throws MarketException {
        addUser();

        List<Task> taskList = service.getTaskList(1, 1);
        assertEquals(IN_PROGRESS, taskList.get(0).taskState);
        taskList.get(0).priceListUrl = "http://link-to-doc.doc";

        service.resumeTask(taskList.get(0));

        taskList = service.getTaskList(1, 1);
        assertEquals(PASSED, taskList.get(0).taskState);
        assertEquals(IN_PROGRESS, taskList.get(1).taskState);

        taskList = service.getTaskList(1, 2);
        assertEquals(PASSED, taskList.get(0).taskState);
        assertEquals(IN_PROGRESS, taskList.get(1).taskState);
    }

}
