/**
 * Assignment2_YourName.java
 * Online Shopping Cart System
 * Demonstrates object relationships, static members, and menu-driven interaction.
 */

import java.util.Scanner;

/**
 * Represents a product in the online shop.
 */
class Product {
    private String productId;
    private String productName;
    private double price;
    private String category;
    private int stockQuantity;

    private static int totalProducts = 0;
    private static String[] categories = {"Electronics", "Clothing", "Books", "Home"};

    /**
     * Constructor to create a product.
     */
    public Product(String productId, String productName, double price, String category, int stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
        totalProducts++;
    }

    // Getters
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public int getStockQuantity() { return stockQuantity; }

    // Update stock
    public void reduceStock(int qty) { this.stockQuantity -= qty; }
    public void increaseStock(int qty) { this.stockQuantity += qty; }

    public static int getTotalProducts() { return totalProducts; }
    public static String[] getCategories() { return categories; }

    // Static methods
    public static Product findProductById(Product[] products, String productId) {
        for (Product p : products) {
            if (p != null && p.getProductId().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    public static void getProductsByCategory(Product[] products, String category) {
        System.out.println("Products in category: " + category);
        for (Product p : products) {
            if (p != null && p.getCategory().equalsIgnoreCase(category)) {
                System.out.println(p.getProductId() + " - " + p.getProductName() + " ($" + p.getPrice() + ")");
            }
        }
    }

    @Override
    public String toString() {
        return productId + " - " + productName + " | $" + price + " | " + category + " | Stock: " + stockQuantity;
    }
}

/**
 * Represents a customer's shopping cart.
 */
class ShoppingCart {
    private String cartId;
    private String customerName;
    private Product[] products;
    private int[] quantities;
    private double cartTotal;
    private int itemCount;

    public ShoppingCart(String cartId, String customerName) {
        this.cartId = cartId;
        this.customerName = customerName;
        this.products = new Product[20];  // capacity for 20 items
        this.quantities = new int[20];
        this.cartTotal = 0;
        this.itemCount = 0;
    }

    public void addProduct(Product product, int quantity) {
        if (product.getStockQuantity() < quantity) {
            System.out.println("Not enough stock available for " + product.getProductName());
            return;
        }
        // Check if already in cart
        for (int i = 0; i < itemCount; i++) {
            if (products[i].getProductId().equals(product.getProductId())) {
                quantities[i] += quantity;
                product.reduceStock(quantity);
                calculateTotal();
                System.out.println(quantity + " more added to existing " + product.getProductName());
                return;
            }
        }
        // Add new product
        products[itemCount] = product;
        quantities[itemCount] = quantity;
        product.reduceStock(quantity);
        itemCount++;
        calculateTotal();
        System.out.println(product.getProductName() + " added to cart.");
    }

    public void removeProduct(String productId) {
        for (int i = 0; i < itemCount; i++) {
            if (products[i].getProductId().equals(productId)) {
                products[i].increaseStock(quantities[i]); // return stock
                System.out.println(products[i].getProductName() + " removed from cart.");
                // Shift arrays left
                for (int j = i; j < itemCount - 1; j++) {
                    products[j] = products[j + 1];
                    quantities[j] = quantities[j + 1];
                }
                products[itemCount - 1] = null;
                quantities[itemCount - 1] = 0;
                itemCount--;
                calculateTotal();
                return;
            }
        }
        System.out.println("Product not found in cart.");
    }

    public void calculateTotal() {
        cartTotal = 0;
        for (int i = 0; i < itemCount; i++) {
            cartTotal += products[i].getPrice() * quantities[i];
        }
    }

    public void displayCart() {
        System.out.println("==== Cart Summary for " + customerName + " ====");
        if (itemCount == 0) {
            System.out.println("Cart is empty.");
        } else {
            for (int i = 0; i < itemCount; i++) {
                System.out.println(products[i].getProductName() + " x " + quantities[i] + " = $" + (products[i].getPrice() * quantities[i]));
            }
            System.out.println("Cart Total: $" + cartTotal);
        }
        System.out.println("==============================");
    }

    public void checkout() {
        if (itemCount == 0) {
            System.out.println("Cart is empty. Nothing to checkout.");
            return;
        }
        displayCart();
        System.out.println("Checkout successful! Thank you for shopping, " + customerName + "!");
        // Empty the cart
        for (int i = 0; i < itemCount; i++) {
            products[i] = null;
            quantities[i] = 0;
        }
        itemCount = 0;
        cartTotal = 0;
    }
}

/**
 * Main class for the Shopping Cart system (menu-driven).
 */
public class Assignment2_YourName {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Create some products
        Product[] products = new Product[10];
        products[0] = new Product("P101", "Laptop", 800.0, "Electronics", 5);
        products[1] = new Product("P102", "Smartphone", 500.0, "Electronics", 10);
        products[2] = new Product("P103", "Headphones", 50.0, "Electronics", 15);
        products[3] = new Product("P201", "T-shirt", 20.0, "Clothing", 30);
        products[4] = new Product("P202", "Jeans", 40.0, "Clothing", 25);
        products[5] = new Product("P301", "Novel", 15.0, "Books", 20);
        products[6] = new Product("P302", "Textbook", 60.0, "Books", 10);
        products[7] = new Product("P401", "Cookware Set", 100.0, "Home", 8);
        products[8] = new Product("P402", "Lamp", 35.0, "Home", 12);
        products[9] = new Product("P403", "Chair", 45.0, "Home", 7);

        // Create a shopping cart
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        ShoppingCart cart = new ShoppingCart("C001", name);

        int choice;
        do {
            System.out.println("\n==== Online Shopping Menu ====");
            System.out.println("1. View all products");
            System.out.println("2. Browse products by category");
            System.out.println("3. Add product to cart");
            System.out.println("4. Remove product from cart");
            System.out.println("5. View cart");
            System.out.println("6. Checkout");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.println("Available Products:");
                    for (Product p : products) {
                        if (p != null) System.out.println(p);
                    }
                    break;

                case 2:
                    System.out.print("Enter category (Electronics, Clothing, Books, Home): ");
                    String cat = sc.nextLine();
                    Product.getProductsByCategory(products, cat);
                    break;

                case 3:
                    System.out.print("Enter Product ID to add: ");
                    String pid = sc.nextLine();
                    Product prod = Product.findProductById(products, pid);
                    if (prod != null) {
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        sc.nextLine();
                        cart.addProduct(prod, qty);
                    } else {
                        System.out.println("Invalid Product ID.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Product ID to remove: ");
                    String rid = sc.nextLine();
                    cart.removeProduct(rid);
                    break;

                case 5:
                    cart.displayCart();
                    break;

                case 6:
                    cart.checkout();
                    break;

                case 7:
                    System.out.println("Exiting system. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 7);

        sc.close();
    }
}
