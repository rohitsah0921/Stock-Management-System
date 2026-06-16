package stockms.model;

public class Product {
    private String productId;
    private String name;
    private String category;
    private int quantity;
    private double unitPrice;
    private String supplier;
    private String mfgDate;
    private String expDate;
    private String batchNo;
    private int lowStockAlert;

    public Product() {}

    public Product(String productId, String name, String category, int quantity,
                   double unitPrice, String supplier, String mfgDate,
                   String expDate, String batchNo, int lowStockAlert) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.supplier = supplier;
        this.mfgDate = mfgDate;
        this.expDate = expDate;
        this.batchNo = batchNo;
        this.lowStockAlert = lowStockAlert;
    }

    // Getters
    public String getProductId()    { return productId; }
    public String getName()         { return name; }
    public String getCategory()     { return category; }
    public int    getQuantity()     { return quantity; }
    public double getUnitPrice()    { return unitPrice; }
    public String getSupplier()     { return supplier; }
    public String getMfgDate()      { return mfgDate; }
    public String getExpDate()      { return expDate; }
    public String getBatchNo()      { return batchNo; }
    public int    getLowStockAlert(){ return lowStockAlert; }

    // Setters
    public void setProductId(String productId)       { this.productId = productId; }
    public void setName(String name)                 { this.name = name; }
    public void setCategory(String category)         { this.category = category; }
    public void setQuantity(int quantity)            { this.quantity = quantity; }
    public void setUnitPrice(double unitPrice)       { this.unitPrice = unitPrice; }
    public void setSupplier(String supplier)         { this.supplier = supplier; }
    public void setMfgDate(String mfgDate)           { this.mfgDate = mfgDate; }
    public void setExpDate(String expDate)           { this.expDate = expDate; }
    public void setBatchNo(String batchNo)           { this.batchNo = batchNo; }
    public void setLowStockAlert(int lowStockAlert)  { this.lowStockAlert = lowStockAlert; }

    @Override
    public String toString() {
        return name + " [" + productId + "]";
    }
}
