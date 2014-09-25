package market.bl.service;

import java.util.List;

import market.bl.exception.MarketException;
import market.model.task.Task;

public interface IMarketTaskService {

    public List<Task> getTaskList(int orderId, int userId) throws MarketException;
    
    public void resumeTask(Task task) throws MarketException;

}
