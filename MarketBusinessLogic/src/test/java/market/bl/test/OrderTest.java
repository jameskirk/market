package market.bl.test;

import java.util.ArrayList;

import market.bl.exception.MarketException;
import market.bl.service.impl.MarketService;
import market.model.constant.OrderState;
import market.model.order.Order;
import market.model.order.PartOrderForUser;
import market.model.security.User;

import org.junit.Test;


public class OrderTest {
    
    @Test
    public void createAndStart() throws MarketException {
	MarketService service = new MarketService();
	
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
	
}
