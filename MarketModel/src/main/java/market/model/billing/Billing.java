package market.model.billing;

import market.model.order.Report;

public class Billing {
    
    public int id;
    
    public Meeting meeting;
    
    public BankTransfer bankTransfer;
    
    public Double amount;
    
    public Report reportByOrganizer;

}
