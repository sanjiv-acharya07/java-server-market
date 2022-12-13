import java.util.ArrayList;

public class Shop {
    private String name;

    private ArrayList<Product> products = new ArrayList<>();

    public Shop(String name , ArrayList<Product> products) {
        this.name = name;
        this.products = products;
    }

    public Shop(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String marketInfo() {

        StringBuilder shopString = new StringBuilder(getName() + "\n");

        for(Product product : products) {
            shopString.append(product.marketInfo()).append("\n");
        }

        return String.valueOf(shopString);
    }

}
