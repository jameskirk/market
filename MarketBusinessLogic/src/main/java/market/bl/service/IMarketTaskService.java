package market.bl.service;

import java.util.List;

import market.model.task.Task;

public interface IMarketTaskService {
	
	public List<Task> getTaskList(int orderId, int userId);
	
	public void lockTask(int taskId);
	
	public void resumeTask(Task task);
	
	public void unlockTask(int taskId);

}
