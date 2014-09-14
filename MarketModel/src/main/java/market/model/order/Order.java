package market.model.order;

import java.util.Date;
import java.util.List;

import market.model.constant.OrderState;

public class Order {
	
	public List<PartOrderForUser> partOrderForUserList;
	
	public OrderState orderState;
	
	public Date createdDate;
	
	public Date stopDate;
	
	public boolean stopExecuted;
	
	public Date shippingDate;
	
	public boolean shippingExecuted;
	
	public Date endDate;

}
