/**
 * Sale records one completed sale (one product, one quantity).
 * A Sale is created every time the cashier sells something.
 */
public class Sale {

    private int receiptNo;
    private String productName;
    private int quantity;
    private double unitPrice;

    public Sale(int receiptNo, String productName, int quantity, double unitPrice) {
        this.receiptNo = receiptNo;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getReceiptNo() {
        return receiptNo;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    /** Total amount for this sale = quantity x unit price. */
    public double getTotal() {
        return quantity * unitPrice;
    }

    /** Builds a small text receipt to show to the customer. */
    public String getReceipt() {
        return "====== CAMPUS TUCKSHOP ======\n"
                + "Receipt No : " + receiptNo + "\n"
                + "Item       : " + productName + "\n"
                + "Quantity   : " + quantity + "\n"
                + "Unit Price : UGX " + String.format("%,.0f", unitPrice) + "\n"
                + "-----------------------------\n"
                + "TOTAL      : UGX " + String.format("%,.0f", getTotal()) + "\n"
                + "=============================\n"
                + "Thank you for shopping with us!";
    }
}