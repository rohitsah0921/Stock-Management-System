package stockms.model;

public class Purchase {
    private int purchaseId;
    private String productId;
    private String productName;
    private int quantity;
    private double unitCost;
    private double totalCost;
    private String supplier;
    private String purchaseDate;

    public Purchase() {}

    public Purchase(String productId, String productName, int quantity,
                    double unitCost, double totalCost, String supplier) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.totalCost = totalCost;
        this.supplier = supplier;
    }

    public int    getPurchaseId()   { return purchaseId; }
    public String getProductId()    { return productId; }
    public String getProductName()  { return productName; }
    public int    getQuantity()     { return quantity; }
    public double getUnitCost()     { return unitCost; }
    public double getTotalCost()    { return totalCost; }
    public String getSupplier()     { return supplier; }
    public String getPurchaseDate() { return purchaseDate; }

    public void setPurchaseId(int id)           { this.purchaseId = id; }
    public void setProductId(String id)         { this.productId = id; }
    public void setProductName(String name)     { this.productName = name; }
    public void setQuantity(int qty)            { this.quantity = qty; }
    public void setUnitCost(double cost)        { this.unitCost = cost; }
    public void setTotalCost(double total)      { this.totalCost = total; }
    public void setSupplier(String supplier)    { this.supplier = supplier; }
    public void setPurchaseDate(String date)    { this.purchaseDate = date; }
}
