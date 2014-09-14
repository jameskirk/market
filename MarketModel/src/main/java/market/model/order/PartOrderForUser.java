package market.model.order;

import java.util.List;

import market.model.security.User;
import market.model.task.Task;

public class PartOrderForUser {
	
	public User user;
	
	public List<ProductWithQuantity> productList;
	
	public List<Task> taskList;
	

}
