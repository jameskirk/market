package market.model.order;

import java.util.Date;
import java.util.List;

import market.model.constant.OrderState;
import market.model.constant.TaskName;

public class Order {

    public int id;

    public List<PartOrderForUser> partOrderForUserList;

    public OrderState orderState;
    
    public Date createdDate;

    public Date stopDate;

    public boolean stopExecuted;

    public Date shippingDate;

    public boolean shippingExecuted;

    public Date endDate;

}
