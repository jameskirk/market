package market.model.order;

import java.util.Date;
import java.util.List;

import market.model.billing.Billing;
import market.model.constant.OrderState;
import market.model.shipping.Shipping;

public class PurchaseOrder {

    public int id;

    public List<UserOrder> userOrderList;

    public OrderState orderState;
    
    public Date createdDate;

    public Date stopDate;

    public boolean stopExecuted;

    public Date shippingDate;

    public boolean shippingExecuted;

    public Date endDate;
    
    public List<Product> possibleProducts;
    
    public List<Shipping> possibleShipping;
    
    public List<Billing> possibleBilling;

}
