package market.bl.service;

import java.util.List;

import market.bl.exception.MarketException;
import market.model.task.Task;

public interface IMarketTaskService {

    List<Task> getTaskList(int orderId, int userId) throws MarketException;
    
    void resumeTask(Task task) throws MarketException;

}
