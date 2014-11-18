package market.bl.service;

import market.bl.exception.MarketException;
import market.model.billing.Billing;
import market.model.catalogue.Catalog;
import market.model.order.PurchaseOrder;
import market.model.order.Report;
import market.model.order.UserOrder;
import market.model.shipping.Shipping;

public interface MarketService {
    
    // actions with order
    public void createPurchaseOrder(PurchaseOrder purchaseOrder) throws MarketException;

    public void addUserOrderWithProducts(UserOrder userOrder, int purchaseOrderId) throws MarketException;

    public void choiceShipping(Shipping shipping, int userOrderId) throws MarketException;

    public void sayStop(int purchaseOrderId) throws MarketException;
    
    public void executeBilling(Billing billing, int userOrderId) throws MarketException;
    
    public void doReport(Report report, int userOrderId) throws MarketException;

    public PurchaseOrder getPurchaseOrder(int id) throws MarketException;

    // actions with catalog
    public Catalog getCatalog() throws MarketException;

}
