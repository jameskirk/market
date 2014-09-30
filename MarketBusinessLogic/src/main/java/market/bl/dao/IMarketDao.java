package market.bl.dao;

import java.util.List;

public interface IMarketDao {
	
	void saveOrUpdate(Object o);

	<T> T get(int id, Class<T> objectClass);
	
	<T> List<T> getAll(Class<T> objectClass);

	void remove(int id, Class<?> objectClass);
		
}
