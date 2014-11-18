package market.model.catalogue;

import java.util.List;

import market.model.order.Product;
import market.model.order.PurchaseOrder;

public class Category {
    
    public String name;
    
    public Category parentCategory;
    
    public List<Category> childCategories;
    
    public List<Product> products;
    
    public List<PurchaseOrder> purchaseOrders;
    
}
