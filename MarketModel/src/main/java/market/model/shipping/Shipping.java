package market.model.shipping;

import market.model.order.Report;

public class Shipping {

    public int id;

    public SelfDelivery selfDelivery;
    
    public CarrierDelivery carrierDelivery;
    
    public Report reportByOrganizer;
    
    public Report reportByMember;
    
}
