package market.bl.service;

import market.bl.exception.MarketException;
import market.model.order.Order;

public interface IMarketOrderService {

	void createAndStartOrder(Order order) throws MarketException ;

	Order getOrder(int id);

	void modifyOrder(Order order) throws MarketException;

}
