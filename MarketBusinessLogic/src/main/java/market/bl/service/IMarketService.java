package market.bl.service;

import market.bl.exception.MarketException;
import market.model.billing.Billing;
import market.model.order.PurchaseOrder;
import market.model.order.UserOrder;
import market.model.shipping.Shipping;

public interface IMarketService {

    public void createPurchaseOrder(PurchaseOrder purchaseOrder) throws MarketException;

    public void addUserOrderWithProducts(UserOrder userOrder, int purchaseOrderId) throws MarketException;

    public void choiceShipping(Shipping shipping, int userOrderId) throws MarketException;

    public void choiceBilling(Billing billing, int userOrderId) throws MarketException;

    public PurchaseOrder getPurchaseOrder(int id) throws MarketException;

}
