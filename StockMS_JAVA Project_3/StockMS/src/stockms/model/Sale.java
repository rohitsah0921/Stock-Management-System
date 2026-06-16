package stockms.model;

public class Sale {
    private int saleId;
    private String productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private String customer;
    private String saleDate;

    public Sale() {}

    public Sale(String productId, String productName, int quantity,
                double unitPrice, double totalPrice, String customer) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.customer = customer;
    }

    public int    getSaleId()       { return saleId; }
    public String getProductId()    { return productId; }
    public String getProductName()  { return productName; }
    public int    getQuantity()     { return quantity; }
    public double getUnitPrice()    { return unitPrice; }
    public double getTotalPrice()   { return totalPrice; }
    public String getCustomer()     { return customer; }
    public String getSaleDate()     { return saleDate; }

    public void setSaleId(int saleId)           { this.saleId = saleId; }
    public void setProductId(String productId)  { this.productId = productId; }
    public void setProductName(String name)     { this.productName = name; }
    public void setQuantity(int quantity)       { this.quantity = quantity; }
    public void setUnitPrice(double price)      { this.unitPrice = price; }
    public void setTotalPrice(double total)     { this.totalPrice = total; }
    public void setCustomer(String customer)    { this.customer = customer; }
    public void setSaleDate(String date)        { this.saleDate = date; }
}
