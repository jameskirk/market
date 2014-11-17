package market.model.order;

import java.util.List;

import market.model.billing.Billing;
import market.model.shipping.Shipping;

public class UserOrder {

    public int id;

    public int userId;

    public List<ProductGroup> productGroupList;
    
    public Shipping shipping;
    
    public Billing billing;

}
