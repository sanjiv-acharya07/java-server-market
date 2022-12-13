import java.util.ArrayList;

public class Testinfg {
    public static void main(String[] args) {
        Customer customer = new Customer("Saleh");

        Customer zaid = new Customer("zaid");

        ArrayList<Seller> sellers = Setup.loadSellers();

        try {
            customer.addToShoppingCart(sellers.get(0) , sellers.get(0).getStores().get(0).getProducts().get(0) , 2 );

            zaid.addToShoppingCart(sellers.get(0) , sellers.get(0).getStores().get(0).getProducts().get(0) , 2 );

            customer.removeFromShoppingCart(sellers.get(0) , sellers.get(0).getStores().get(0).getProducts().get(0));

            zaid.addToShoppingCart(sellers.get(0) , sellers.get(0).getStores().get(0).getProducts().get(1) , 2 );

            zaid.purchase(sellers , sellers.get(0).getStores().get(0).getProducts().get(0) , 2);

            zaid.addUpSalesClearSellerCart(sellers);

            Setup.updateDetailedProduct(sellers);
        } catch (IllegalPurchaseException | notInCartException e) {
            throw new RuntimeException(e);
        }

    }
}
