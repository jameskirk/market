package market.bl.service;

import market.model.order.Order;

public interface IMarketOrderService {

	public void createAndStartOrder(Order order);

	public Order getOrder();

	public void modifyOrder(Order order);

}
