import java.util.ArrayList;

public class Product {
    private String name;

    private String storeName;

    private String description;

    private int quantity;

    private double price;

    public Product() {

    }

    public String getName() {
        return name;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(String storeName , String name , String description , int quantity , double price) {
        this.name = name;
        this.storeName = storeName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public String marketInfo() {
        return String.format("Item: %s, Amount: %d, Price: %.2f " , getName() , getQuantity() , getPrice());
    }

    public static String shoppingCartInfo(Product product, int amount) {
        return String.format("Item: %s, Amount: %d, Price: %.2f " , product.getName() , amount , product.getPrice());
    }

    public static String shoppingCartInfo(String name , int amount , double price) {
        return String.format("Item: %s, Amount: %d, Price: %.2f " , name , amount , price);
    }



    public String toString() {
        return String.format("%s,%s,%d,%s,%.2f" , getStoreName() , getName() , getQuantity() ,
                getDescription() , getPrice());
    }
}
