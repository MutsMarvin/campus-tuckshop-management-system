
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


    public Product[] filterProducts(String filter) {
        // First pass: count how many match
        int matches = 0;
        for (int i = 0; i < productCount; i++) {
            if (matchesFilter(products[i], filter)) {
                matches++;
            }
        }
        // Second pass: copy them into a new array
        Product[] result = new Product[matches];
        int index = 0;
        for (int i = 0; i < productCount; i++) {
            if (matchesFilter(products[i], filter)) {
                result[index] = products[i];
                index++;
            }
        }
        return result;
    }

    private boolean matchesFilter(Product p, String filter) {
        switch (filter) {
            case "Low stock":
                return p.getQuantity() > 0 && p.getQuantity() <= Product.LOW_STOCK_LEVEL;
            case "Out of stock":
                return p.getQuantity() == 0;
            default:            // "All products"
                return true;
        }
    }

    /** Sorts products by price (cheapest first) using bubble sort with a DO-WHILE loop. */
    public void sortByPrice() {
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < productCount - 1; i++) {
                if (products[i].getPrice() > products[i + 1].getPrice()) {
                    Product temp = products[i];
                    products[i] = products[i + 1];
                    products[i + 1] = temp;
                    swapped = true;
                }
            }
        } while (swapped);          // repeat until no more swaps are needed
    }

    /** Sorts products A to Z by name using bubble sort with a DO-WHILE loop. */
    public void sortByName() {
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < productCount - 1; i++) {
                if (products[i].getName().compareToIgnoreCase(products[i + 1].getName()) > 0) {
                    Product temp = products[i];
                    products[i] = products[i + 1];
                    products[i + 1] = temp;
                    swapped = true;
                }
            }
        } while (swapped);
    }

    /** Counts products that are low on stock (including out of stock). */
    public int countLowStock() {
        int count = 0;
        for (int i = 0; i < productCount; i++) {
            if (products[i].getQuantity() <= Product.LOW_STOCK_LEVEL) {
                count++;
            }
        }
        return count;
    }

    // =====================================================
    //  SALES OPERATIONS
    // =====================================================

    /** Checks a sale BEFORE it happens. Returns an error message, or null if it is fine. */
    public String validateSale(int productId, int quantity) {
        Product p = findById(productId);
        if (p == null) {
            return "Product not found.";
        } else if (quantity <= 0) {
            return "Quantity must be at least 1.";
        } else if (quantity > p.getQuantity()) {
            return "Not enough stock. Only " + p.getQuantity() + " left.";
        }
        return null;
    }

    /** Performs a sale: reduces stock and records a Sale object. Returns null if it fails. */
    public Sale sellProduct(int productId, int quantity) {
        if (validateSale(productId, quantity) != null) {
            return null;
        }
        Product p = findById(productId);
        p.reduceStock(quantity);

        if (saleCount == sales.length) {
            growSalesArray();
        }
        Sale s = new Sale(nextReceiptNo, p.getName(), quantity, p.getPrice());
        nextReceiptNo++;
        sales[saleCount] = s;
        saleCount++;
        return s;
    }

    private void growSalesArray() {
        Sale[] bigger = new Sale[sales.length * 2];
        for (int i = 0; i < saleCount; i++) {
            bigger[i] = sales[i];
        }
        sales = bigger;
    }

    public Sale[] getSales() {
        Sale[] copy = new Sale[saleCount];
        for (int i = 0; i < saleCount; i++) {
            copy[i] = sales[i];
        }
        return copy;
    }

    /** Adds up the money from every sale. */
    public double getTotalRevenue() {
        double total = 0;
        for (int i = 0; i < saleCount; i++) {
            total += sales[i].getTotal();
        }
        return total;
    }

    /** Adds up the number of items sold. */
    public int getItemsSold() {
        int items = 0;
        for (int i = 0; i < saleCount; i++) {
            items += sales[i].getQuantity();
        }
        return items;
    }
    

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