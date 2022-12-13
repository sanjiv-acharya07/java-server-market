import java.io.Reader;
import java.util.ArrayList;

public class Customer {
    private String name;

    private ArrayList<String> shoppingCart = new ArrayList<>();

    public String getName() {
        return name;
    }

    public ArrayList<String> getShoppingCart() {
        return shoppingCart;
    }

    public Customer(String name) {
        this.name = name;
        if(!ReaderAndWriter.isFile(name + "TransactionHistory.txt")) {
            ArrayList<String> temp = new ArrayList<>();
            String shopping = "Shopping";
            temp.add(shopping);
            ReaderAndWriter.writeFile(name + "TransactionHistory.txt" , temp , false );
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public synchronized void setShoppingCart(ArrayList<String> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public synchronized void addToShoppingCart(Seller seller, Product product , int amount) throws IllegalPurchaseException {
        int quantityOfProduct = product.getQuantity();

        //Checks request VS Available amount.
        if (quantityOfProduct < amount || amount <= 0) {
            throw new IllegalPurchaseException("Error, invalid amount.");
        }
        boolean inCart = false;

        //Adds to the shopping carts of both the seller and the customer.
        if(shoppingCart.size() > 0) {
            for(String s : shoppingCart) {
                if(s.contains(product.getName())) {
                    String prevAmount = s.substring(s.indexOf(',') , s.lastIndexOf(','));

                    prevAmount = prevAmount.substring(prevAmount.indexOf(':') + 1);

                    prevAmount = prevAmount.trim();

                    int intPrevAmount = Integer.parseInt(prevAmount);

                    int currentAmount = intPrevAmount + amount;

                    String cartString = Product.shoppingCartInfo(product , currentAmount);

                    inCart = true;

                    int indexOfString = shoppingCart.indexOf(s);

                    shoppingCart.set(indexOfString , cartString);

                }
            }
        }



        if(!inCart){
            shoppingCart.add(Product.shoppingCartInfo(product , amount));
        }
        ArrayList<String> cartWithSales = ReaderAndWriter.readFile(seller.getName() + "ShoppingCartSeller.txt");

        inCart = false;

        for(String s : seller.getProductsInShoppingCarts()) {
            if(s.contains(product.getName())) {
                String prevAmount = s.substring(s.indexOf(',') , s.lastIndexOf(','));

                prevAmount = prevAmount.substring(prevAmount.indexOf(':') + 1);

                prevAmount = prevAmount.trim();

                int intPrevAmount = Integer.parseInt(prevAmount);

                int currentAmount = intPrevAmount + amount;

                String cartString = Product.shoppingCartInfo(product , currentAmount);

                inCart = true;


                int indexOfString = seller.getProductsInShoppingCarts().indexOf(s);

                seller.getProductsInShoppingCarts().set(indexOfString , cartString);

                cartWithSales.set(indexOfString , cartString);

            }
        }

        if(!inCart){
            seller.getProductsInShoppingCarts().add(Product.shoppingCartInfo(product , amount));
            cartWithSales.add(Product.shoppingCartInfo(product , amount));
        }

        //Writing To Files


        cartWithSales.add(String.format("TOTAL SALES: %.2f" , seller.getSales()));
        seller.setProductsInShoppingCarts(cartWithSales);

        int numOfRemoved = 0;

        int initialSize = cartWithSales.size();
        for(int i = 0; i < initialSize; i++) {
            String currentString = cartWithSales.get(i);
            if(currentString.contains("TOTAL SALES:")) {
                if(i != initialSize - 1) {
                    cartWithSales.remove(i);
                }
                initialSize--;
                i--;
            }
        }


        seller.setProductsInShoppingCarts(cartWithSales);


        ReaderAndWriter.writeFile(seller.getName() +
                "ShoppingCartSeller.txt" , cartWithSales , false);
        ReaderAndWriter.writeFile(name + "ShoppingCart.txt" , shoppingCart , false);

        //Changing Product Amount

        product.setQuantity(product.getQuantity() - amount);

    }

    public synchronized void removeFromShoppingCart(Seller seller , Product product) throws notInCartException {
        int indexOfString = -1;
        //Finding Product in Cart
        for(String s : shoppingCart) {
            if(s.contains(product.getName())) {
                indexOfString = shoppingCart.indexOf(s);
            }
        }
        if(indexOfString == -1) {
            throw new notInCartException("Error, not in cart");
        }

        //Changing The Product Amount
        String productInfo = shoppingCart.get(indexOfString);

        productInfo = productInfo.substring(productInfo.indexOf(',') , productInfo.lastIndexOf(','));

        productInfo = productInfo.substring(productInfo.indexOf(':') + 1);

        productInfo = productInfo.trim();

        int amountRemoved = Integer.parseInt(productInfo);

        product.setQuantity(product.getQuantity() + amountRemoved);
        //Removing Product From Cart
        shoppingCart.remove(indexOfString);

        //Repeat of Process For Sellers

        ArrayList<String> cartWithSales = ReaderAndWriter.readFile(seller.getName() + "ShoppingCartSeller.txt");

        indexOfString = -1;

        for(String s : seller.getProductsInShoppingCarts()) {
            if(s.contains(product.getName())) {
                indexOfString = seller.getProductsInShoppingCarts().indexOf(s);
            }
        }

        if(indexOfString == -1) {
            throw new notInCartException("Error not in cart");
        }

        ArrayList<String> sellerShoppingCart = seller.getProductsInShoppingCarts();

        String productInCart = sellerShoppingCart.get(indexOfString);

        //product info is the amount in the customer cart.

        if(productInCart.contains(productInfo)) {
            sellerShoppingCart.remove(indexOfString);
            cartWithSales.remove(indexOfString);
        } else {
            productInCart = productInCart.substring(productInCart.indexOf(',') , productInCart.lastIndexOf(','));

            productInCart = productInCart.substring(productInCart.indexOf(':') + 1);

            productInCart = productInCart.trim();

            int productInCartPrevAmount = Integer.parseInt(productInCart);

            int currentAmount = productInCartPrevAmount - amountRemoved;

            if(currentAmount == 0) {
                sellerShoppingCart.remove(indexOfString);
                cartWithSales.remove(indexOfString);
            } else {
                String tempLine;

                tempLine = Product.shoppingCartInfo( product , currentAmount);

                sellerShoppingCart.set( indexOfString , tempLine);
                cartWithSales.set(indexOfString , tempLine);
            }
        }


        seller.setProductsInShoppingCarts(sellerShoppingCart);


            cartWithSales.add(String.format("TOTAL SALES: %.2f" , seller.getSales()));
            seller.setProductsInShoppingCarts(cartWithSales);

        int numOfRemoved = 0;

        int initialSize = cartWithSales.size();
        for(int i = 0; i < initialSize; i++) {
            String currentString = cartWithSales.get(i);
            if(currentString.contains("TOTAL SALES:")) {
                if(i != initialSize - 1) {
                    cartWithSales.remove(i);
                }
                initialSize--;
                i--;
            }
        }

        seller.setProductsInShoppingCarts(cartWithSales);

        //Writing Changes To Files

        ReaderAndWriter.writeFile(name + "ShoppingCart.txt" , shoppingCart , false);

        ReaderAndWriter.writeFile(seller.getName() + "ShoppingCartSeller.txt" , cartWithSales , false);
    }

    public void transactionHistory() {
        ArrayList<String> shoppingCartCust = new ArrayList<>();

        shoppingCartCust = ReaderAndWriter.readFile(name + "ShoppingCart.txt");

        ReaderAndWriter.writeFile(name + "TransactionHistory.txt" , shoppingCartCust , true);
    }

    public ArrayList<String> viewTransactionHistory() {
        ArrayList<String> transactionHistory = ReaderAndWriter.readFile(name + "TransactionHistory.txt");
        ArrayList<String> returnTransactions = new ArrayList<>();
        int counter = 0;
        for (String s : transactionHistory) {
            if(!s.contains("Shopping")) {
                counter++;
                String addString = String.format("Transaction %d: %s", counter, s);
                returnTransactions.add(addString);
            }
        }
        return returnTransactions;
    }

    public synchronized void purchase(ArrayList<Seller> sellers , Product product , int amount) throws IllegalPurchaseException  {
        int prevAmount = product.getQuantity();

        if(amount > product.getQuantity()) {
            throw new IllegalPurchaseException("Error, invalid amount.");
        } else {
            //Update Product
            product.setQuantity(product.getQuantity() - amount);
        }

        //Add Sales for Sellers
        for(Seller seller : sellers) {
            for(Shop shop : seller.getStores()) {
                if(product.getStoreName().contains(shop.getName())) {
                    double price = product.getPrice();

                    double sales = price * amount;

                    seller.setSales(seller.getSales() + sales);
                }
            }
        }

        //Add the purchase to TransactionHistories.
        ArrayList<String> purchase = new ArrayList<>();

        String purchaseInfo = Product.shoppingCartInfo(product.getName() , amount , product.getPrice());

        purchase.add(purchaseInfo);

        ReaderAndWriter.writeFile(name + "TransactionHistory.txt" , purchase , true);

        purchaseInfo = name + " Purchased " + purchaseInfo;

        String storeName = product.getStoreName();

        purchase.set(0 , purchaseInfo);

        ReaderAndWriter.writeFile(storeName + "TransactionHistory.txt" , purchase , true);

        Setup.specificUpdateDetailedProducts(sellers , product , amount);
    }

    public boolean checkFile() {
        return ReaderAndWriter.isFile(name + "ShoppingCart.txt");
    }

    public void replenishCart() {
        ArrayList<String> shoppingCart = ReaderAndWriter.readFile(name + "ShoppingCart.txt");
        setShoppingCart(shoppingCart);
    }

    public void clearCart() {
        ArrayList<String> temp = new ArrayList<>();

        ReaderAndWriter.writeFile(name + "ShoppingCart.txt" ,temp , false);

        setShoppingCart(temp);
    }

    public synchronized void addUpSalesClearSellerCart(ArrayList<Seller> sellers) {
        for(Seller sell : sellers) {
            double prevSales = sell.getSales();

            ArrayList<String> sellerCart =
                    ReaderAndWriter.readFile(sell.getName() + "ShoppingCartSeller.txt");
            ArrayList<String> customerCart = ReaderAndWriter.readFile(name + "ShoppingCart.txt");

            ArrayList<String> editedSellerCart =
                    ReaderAndWriter.readFile(sell.getName() + "ShoppingCartSeller.txt");

            int numOfRemoved = 0;

            for(String custProd : customerCart) {
                int indexOfString = -1;

                boolean clearFromCart = false;

                String newString = "";

                String prodName = custProd.substring(custProd.indexOf(':') + 1 , custProd.indexOf(','));

                prodName = prodName.trim();

                custProd = custProd.substring(custProd.indexOf(',') + 1);


                for(String sellProd : sellerCart) {
                    if(sellProd.contains(prodName)) {
                        String orgSellProd = sellProd;

                        String purchaseAmount = custProd.substring(custProd.indexOf(':') + 1 , custProd.indexOf(','));

                        purchaseAmount = purchaseAmount.trim();

                        int purchaseAmountInt = Integer.parseInt(purchaseAmount);

                        sellProd = sellProd.substring(sellProd.indexOf(',') + 1 , sellProd.lastIndexOf(','));

                        String sellerAmount = sellProd.substring(sellProd.indexOf(':') + 1);

                        sellerAmount = sellerAmount.trim();

                        int sellerAmountInt = Integer.parseInt(sellerAmount);

                        String priceStr = custProd.substring(custProd.lastIndexOf(':') + 1);

                        priceStr = priceStr.trim();

                        double price = Double.parseDouble(priceStr);

                        prevSales = prevSales + purchaseAmountInt * price;

                        if(sellerAmountInt == purchaseAmountInt) {
                            indexOfString = sellerCart.indexOf(orgSellProd);

                            editedSellerCart.remove(indexOfString - numOfRemoved);
                            numOfRemoved++;
                        } else{
                            indexOfString = sellerCart.indexOf(orgSellProd);

                            newString = Product.shoppingCartInfo(prodName
                                    , sellerAmountInt - purchaseAmountInt , price);

                            indexOfString = indexOfString - numOfRemoved;
                            if(indexOfString < 0) {
                                indexOfString = 0;
                            }
                            editedSellerCart.set(indexOfString , newString);
                        }
                    }
                }

                ReaderAndWriter.writeFile(sell.getName() + "ShoppingCartSeller.txt"
                        , editedSellerCart , false);
            }

            sell.setSales(prevSales);


        }

        ArrayList<String> customerCart = ReaderAndWriter.readFile(name + "ShoppingCart.txt");

        for(String custProd : customerCart) {
            String prodName = custProd.substring(custProd.indexOf(':') + 1 , custProd.indexOf(','));

            prodName = prodName.trim();

            custProd = custProd.substring(custProd.indexOf(',') + 1);

            String purchaseAmount = custProd.substring(custProd.indexOf(':') + 1 , custProd.indexOf(','));

            purchaseAmount = purchaseAmount.trim();

            int purchaseAmountInt = Integer.parseInt(purchaseAmount);
            String priceStr = custProd.substring(custProd.lastIndexOf(':') + 1);
            priceStr = priceStr.trim();
            double price = Double.parseDouble(priceStr);
            String shopName = "";

            for(Seller seller : sellers) {
                for(Shop shop : seller.getStores()) {
                    for(Product product : shop.getProducts()) {
                        if(product.getName().equals(prodName.trim())) {
                            shopName = product.getStoreName();
                        }
                    }
                }
            }

            String purchaseLine = String.format("%s Purchased, Item: %s, Amount: %d, Price: %.2f" , name , prodName
                    ,purchaseAmountInt, price);
            ArrayList<String> temp  = new ArrayList<>();
            temp.add(purchaseLine);
            ReaderAndWriter.writeFile(shopName + "TransactionHistory.txt" , temp , true);
        }

        ReaderAndWriter.writeFile(name + "TransactionHistory.txt" , customerCart , true);
        clearCart();
        Setup.updateDetailedProduct(sellers);
    }
}
