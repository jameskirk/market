package market.bl.service.impl;

import market.bl.service.IMarketOrderService;
import market.model.order.Order;

public class MarketService implements IMarketOrderService {

	@Override
	public void createAndStartOrder(Order order) {
		//store to database
	}

	@Override
	public Order getOrder() {
		//get from database
		return null;
		
	}

	@Override
	public void modifyOrder(Order order) {
		//get from database
		//compare, validation
		//store to database
		
	}

}
