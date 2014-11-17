package market.bl.service.impl;

import market.bl.dao.IMarketDao;
import market.bl.dao.XmlMarketDao;
import market.bl.exception.MarketException;
import market.bl.service.MarketService;
import market.model.billing.Billing;
import market.model.order.PurchaseOrder;
import market.model.order.UserOrder;
import market.model.shipping.Shipping;

public class MarketServiceImpl implements MarketService {

    private IMarketDao dao = new XmlMarketDao("build/resource/db.xml");

    @Override
    public void createPurchaseOrder(PurchaseOrder purchaseOrder) throws MarketException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void addUserOrderWithProducts(UserOrder userOrder, int purchaseOrderId) throws MarketException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void choiceShipping(Shipping shipping, int userOrderId) throws MarketException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void choiceBilling(Billing billing, int userOrderId) throws MarketException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void sayStop(int purchaseOrderId, int userId) throws MarketException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void executeBilling(int userOrderId) throws MarketException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateDeliveryState(Shipping shipping, int userOrderId) throws MarketException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public PurchaseOrder getPurchaseOrder(int id) throws MarketException {
	// TODO Auto-generated method stub
	return null;
    }


}
