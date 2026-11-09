
/**
 * Inventory manages all Products and Sales using ARRAYS.
 * The GUI never touches the arrays directly; it calls the methods here.
 * (This separates the "logic" from the "display".)
 */
public class Inventory {

    private Product[] products = new Product[10];   // stores all products
    private int productCount = 0;                   // how many slots are used

    private Sale[] sales = new Sale[20];            // stores all sales
    private int saleCount = 0;

    private int nextProductId = 1;                  // auto-increment product IDs
    private int nextReceiptNo = 1001;               // auto-increment receipt numbers

    // =====================================================
    //  PRODUCT OPERATIONS
    // =====================================================

    /** Adds a new product. Returns the Product, or null if the details are invalid. */
    public Product addProduct(String name, double price, int quantity) {
        if (name == null || name.trim().isEmpty() || price <= 0 || quantity < 0) {
            return null;
        }
        if (productCount == products.length) {
            growProductArray();                     // array is full, make it bigger
        }
        Product p = new Product(nextProductId, name.trim(), price, quantity);
        nextProductId++;
        products[productCount] = p;
        productCount++;
        return p;
    }

    /** Doubles the size of the products array (arrays cannot grow by themselves). */
    private void growProductArray() {
        Product[] bigger = new Product[products.length * 2];
        for (int i = 0; i < productCount; i++) {
            bigger[i] = products[i];
        }
        products = bigger;
    }

    /** Finds a product by ID using a WHILE loop. Returns null if not found. */
    public Product findById(int id) {
        int i = 0;
        while (i < productCount) {
            if (products[i].getId() == id) {
                return products[i];
            }
            i++;
        }
        return null;
    }

    /** Adds stock to an existing product. */
    public boolean restock(int id, int amount) {
        Product p = findById(id);
        if (p == null) {
            return false;
        }
        return p.addStock(amount);
    }

    /** Removes a product and shifts the remaining ones left to close the gap. */
    public boolean removeProduct(int id) {
        for (int i = 0; i < productCount; i++) {
            if (products[i].getId() == id) {
                for (int j = i; j < productCount - 1; j++) {
                    products[j] = products[j + 1];
                }
                productCount--;
                products[productCount] = null;
                return true;
            }
        }
        return false;
    }

    /** Returns a correctly-sized copy of the products array (no empty slots). */
    public Product[] getProducts() {
        Product[] copy = new Product[productCount];
        for (int i = 0; i < productCount; i++) {
            copy[i] = products[i];
        }
        return copy;
    }

    /** Finds all products whose name contains the keyword (not case-sensitive). */
    public Product[] findByName(String keyword) {
        String key = keyword.trim().toLowerCase();

        // First pass: count the matches
        int matches = 0;
        for (int i = 0; i < productCount; i++) {
            if (products[i].getName().toLowerCase().contains(key)) {
                matches++;
            }
        }
        // Second pass: copy them into a new array
        Product[] result = new Product[matches];
        int index = 0;
        for (int i = 0; i < productCount; i++) {
            if (products[i].getName().toLowerCase().contains(key)) {
                result[index] = products[i];
                index++;
            }
        }
        return result;
    }


    // ==================================================================
    // >>> MEMBER 4: PASTE YOUR Inventory CODE ON THE LINES BELOW THIS BOX <<<
    // ==================================================================
    // ----- end of Member 4's code -----

    // =====================================================
    //  SAMPLE DATA (so the app is not empty when it opens)
    // =====================================================
    public void loadSampleData() {
        String[] names = {"Mineral Water 500ml", "Soda 300ml", "Chapati", "Rolex", "Biscuits", "Exercise Book", "Pen"};
        double[] prices = {1000, 1500, 1000, 2500, 500, 1500, 500};
        int[] quantities = {40, 25, 30, 4, 50, 20, 0};
        String[] categories = {"Drinks", "Drinks", "Food", "Food", "Food", "Stationery", "Stationery"};

        for (int i = 0; i < names.length; i++) {
            Product p = addProduct(names[i], prices[i], quantities[i]);
            if (p != null) {
                p.setCategory(categories[i]);
            }
        }
    }
}
`