package market.bl.dao;

import java.util.List;

public interface IMarketDao {
	
	public void saveOrUpdate(Object o);

	public <T> T get(int id, Class<T> objectClass);
	
	public <T> List<T> getAll(Class<T> objectClass);

	public void remove(int id, Class<?> objectClass);
		
}
