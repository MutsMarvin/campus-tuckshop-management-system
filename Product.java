 /** Product represents one item sold in the tuckshop (e.g. "Mineral Water").
 / Attributes are private (encapsulation) and accessed through getters/setters.
 */
public class Product {

    // A constant: stock at or below this level is flagged as "LOW"
    public static final int LOW_STOCK_LEVEL = 5;

    private int id;
    private String name;
    private double price;   // price per unit in UGX
    private int quantity;   // units currently in stock
    private String category = "General";   // e.g. Food, Drinks, Stationery

    // Constructor: creates a Product object with all its details
    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // ---------- Getters ----------
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    // ---------- Setters (with simple validation) ----------
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        if (price > 0) {
            this.price = price;
        }
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
    }

    public void setCategory(String category) {
        if (category != null && !category.trim().isEmpty()) {
            this.category = category.trim();
        }
    }

    // ---------- Behaviour ----------

    /** Adds stock. Returns true if the amount was valid. */
    public boolean addStock(int amount) {
        if (amount > 0) {
            quantity += amount;
            return true;
        }
        return false;
    }

    /** Removes stock after a sale. Returns false if not enough stock. */
    public boolean reduceStock(int amount) {
        if (amount > 0 && amount <= quantity) {
            quantity -= amount;
            return true;
        }
        return false;
    }

    /** Returns the stock status text shown in the GUI. */
    public String getStatus() {
        if (quantity == 0) {
            return "OUT OF STOCK";
        } else if (quantity <= LOW_STOCK_LEVEL) {
            return "LOW";
        } else {
            return "OK";
        }
    }

    @Override
    public String toString() {
        return id + " - " + name + " (" + quantity + " in stock)";
    }
}