package market.bl.service;

import market.bl.exception.MarketException;
import market.model.order.Order;

public interface IMarketOrderService {

	public void createAndStartOrder(Order order) throws MarketException ;

	public Order getOrder(int id);

	public void modifyOrder(Order order) throws MarketException;

}
