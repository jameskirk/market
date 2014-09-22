package market.bl.dao;

public interface IMarketDao {
	
	public void saveOrUpdate(Object o);

	public <T> T get(int id, Class<T> objectClass);

	public void remove(int id, Class<?> objectClass);
		
}
