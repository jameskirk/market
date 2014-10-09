package market.bl.test;

import java.io.File;

import market.bl.dao.IMarketDao;
import market.bl.dao.XmlMarketDao;
import market.model.order.Product;

import org.junit.Assert;
import org.junit.Test;

import static market.bl.dao.XmlMarketDao.DEFAULT_DB_FILENAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class XmlDaoTest {

    @Test
    public void testCRUD() {
        System.out.println("executing createAndGetOrder");

        assertTrue("The " + DEFAULT_DB_FILENAME + " file has not been deleted",
                new File(DEFAULT_DB_FILENAME).delete());

        IMarketDao dao = new XmlMarketDao();
        Product p1 = new Product();
        p1.id = 1;
        p1.name = "best product";
        p1.price = 10.0;
        dao.saveOrUpdate(p1);

        Product p2 = new Product();
        p2.id = 2;
        p2.name = "best product2";
        p2.price = 200.0;
        dao.saveOrUpdate(p2);

        Product p2FromXml = dao.get(2, Product.class);
        assertEquals(p2.id, p2FromXml.id);
        assertEquals(p2.name, p2FromXml.name);

        dao.remove(2, Product.class);
        Product p2RemovedFromXml = dao.get(2, Product.class);
        assertNull(p2RemovedFromXml);
    }

}
