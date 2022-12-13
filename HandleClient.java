import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class HandleClient implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String again = "";

    public static final Object Heeey = new Object();
    public static final Object details = new Object();
    public static final Object giga = new Object();

    public HandleClient(Socket cSocket) throws IOException {
        this.socket = cSocket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
        ArrayList<Seller> sellers = Setup.loadSellers();
        ArrayList<String> marketPlace = MainServer.printMarketPlace();
    }

    @Override
    public void run() {
        ArrayList<String> marketPlace = new ArrayList<>();
        try {
            marketPlace = MainServer.printMarketPlace(); // this puts together an arraylist of strings about the
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // items

        String again = "";
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            ArrayList<Seller> sellers = Setup.loadSellers();


            int option = Integer.parseInt(reader.readLine()); // readline 1

            if (option == 1) {
                String username;

                do {
                    username = reader.readLine(); // readline 2 option 1
                    if (username == null) {
                        break;
                    }
                    if (MainServer.checkInFile("credentials.txt", username)) {
                        writer.println("true");
                        writer.flush();
                    } else {
                        writer.println("false");
                        writer.flush();
                        break;
                    }
                } while (MainServer.checkInFile("credentials.txt", username));
                String password = reader.readLine(); // readline 3 option 1
                if (password == null) {

                }
                String type = reader.readLine(); // readline 4 option 1

                File writeFile = new File("credentials.txt");
                FileReader fr = new FileReader(writeFile);
                BufferedReader bfr = new BufferedReader(fr);

                FileOutputStream fos = new FileOutputStream(writeFile, true);
                PrintWriter pw = new PrintWriter(fos);

                String writeLine = username + " " + password + " " + type;
                pw.println(writeLine);
                pw.close();

            } else if (option == 0) {
                //returning user
                String username = "";
                do {
                    try {
                        username = reader.readLine();
                        if (username == null) {
                            break;
                        }
                        if (MainServer.checkUsername(username).contains("true")) {
                            writer.println(MainServer.checkUsername(username));
                            writer.flush();
                        } else {
                            writer.println("false");
                            writer.flush();
                        }
                    } catch (LineNotFoundException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } while (MainServer.checkUsername(username).contains("false"));
                username = reader.readLine();

                String password = "";
                do {
                    try {
                        if (username == null) {
                            again = "false";
                            break;
                        }
                        password = reader.readLine();
                        if (password == null) {
                            again = "false";
                            break;
                        }
                        if (MainServer.checkPassword(username, password)) {
                            writer.println("true");
                            writer.flush();
                        } else {
                            writer.println("false");
                            writer.flush();
                        }
                    } catch (LineNotFoundException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } while (!MainServer.checkPassword(username, password));
                String type = reader.readLine(); //from type writeline
                boolean keepMenu = true;
                if (type.equals("Seller")) {
                    Seller seller = new Seller(username);

                    for (Seller sel : sellers) {
                        if (sel.getName().equals(seller.getName())) {
                            seller = sel;
                        }
                    }

                    do {
                        String choice = reader.readLine();
                        if (choice == null) {
                            break;
                        }
                        if (choice.equals("Create product")) {
                            String fileName = reader.readLine();
                            seller.readProductFile(sellers, fileName);
                            Setup.updateDetailedProduct(sellers);
                        }
                        if (choice.equals("Edit Product")) {
                            String oldFileName = reader.readLine();
                            if (oldFileName == null) {
                                break;
                            }
                            String newFileName = reader.readLine();
                            if (newFileName == null) {
                                break;
                            }
                            seller.editProductsThroughFile(sellers, oldFileName, newFileName);
                            Setup.updateDetailedProduct(sellers);
                        }
                        if (choice.equals("Delete Product")) {
                            String fileName = reader.readLine();
                            seller.deleteProductFile(sellers, fileName);
                            Setup.updateDetailedProduct(sellers);

                        }
                        if (choice.equals("View Sales")) {
                            if (username.equals("Seller1")) {
                                String sales = sellers.get(0).salesByStorePrint();
                                String[] split = sales.split("\n");
                                writer.println(split.length);
                                writer.flush();
                                for (int i = 0; i < split.length; i++) {
                                    writer.println(split[i]);
                                    writer.flush();
                                }
                            } else if (username == "Seller2") {
                                String sales = sellers.get(1).salesByStorePrint();
                                String[] split = sales.split("\n");
                                writer.println(split.length);
                                writer.flush();
                                for (int i = 0; i < split.length; i++) {
                                    writer.println(split[i]);
                                    writer.flush();
                                }
                            }
                        }
                        if (choice.equals("View Shopping Carts")) {
                            File file = new File(username + "ShoppingCartSeller.txt");
                            FileReader fr = new FileReader(file);
                            BufferedReader bfr = new BufferedReader(fr);
                            String line = bfr.readLine();
                            ArrayList<String> cartLines = new ArrayList<>();
                            while (line != null) {
                                if (!line.contains("TOTAL SALES:")) {
                                    cartLines.add(line);
                                }
                                line = bfr.readLine();
                            }
                            writer.println(cartLines.size());//caught by shoppingcart
                            writer.flush();
                            for (int i = 0; i < cartLines.size(); i++) {
                                writer.println(cartLines.get(i));
                                writer.flush();
                            }
                        }
                        if (choice.equals("Export Product Details File")) {
                            seller.exportProductFile(sellers);
                        }

                    } while (keepMenu);

                } else if (type.equals("Customer")) {

                    Customer customer = new Customer(username);
                    if (customer.checkFile()) {
                        customer.replenishCart();
                    }

                    do {
                        int choice = Integer.parseInt(reader.readLine()); // readCustomer 1

                        if (choice == 0) {
                            int cartChoice = Integer.parseInt(reader.readLine()); // readCustomer 2
                            if (cartChoice == 0) {
                                ArrayList<String> marketplace = MainServer.printMarketplace(sellers);
                                int size = marketplace.size();

                                writer.println(String.valueOf(size));
                                writer.flush();

                                for (String s : marketplace) {
                                    writer.println(s);
                                    writer.flush();
                                }

                                String addItemDetails = reader.readLine();
                                String addItemName = addItemDetails.split(": ")[1].split(", ")[0];
                                String addItemStore = addItemDetails.split(", ")[3].split(":")[1];

                                Product realChoice = null;
                                for (Seller seller : sellers) {
                                    for (Shop shop : seller.getStores()) {
                                        for (Product product : shop.getProducts()) {
                                            if (product.getName().equals(addItemName)) {
                                                realChoice = product;
                                            }
                                        }
                                    }
                                }

                                assert realChoice != null;
                                writer.println("Available:" + realChoice.getQuantity());
                                writer.flush();

                                String amount = reader.readLine();

                                for (Seller sel : sellers) { // going through every seller
                                    for (Shop s : sel.getStores()) {
                                        if (s.getName().equalsIgnoreCase(addItemStore.trim())) {
                                            // this checks if the shop is the same shop as the one the product in
                                            for (Product p : s.getProducts()) {
                                                if (p.getName().toLowerCase().trim().equals(addItemName.toLowerCase().trim())) {
                                                    // checking if the product is the one we need to add to cart
                                                    customer.addToShoppingCart(sel, p, Integer.parseInt(amount));
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (cartChoice == 1) {
                                ArrayList<String> cart = customer.getShoppingCart();

                                if (cart == null || cart.isEmpty()) {
                                    writer.println("false");
                                    writer.flush();
                                } else {
                                    writer.println("true");
                                    writer.flush();

                                    ArrayList<String> tempCart = customer.getShoppingCart();
                                    writer.println(String.valueOf(tempCart.size()));
                                    writer.flush();

                                    for (String s : tempCart) {
                                        writer.println(s);
                                        writer.flush();
                                    }

                                    String removeItem = reader.readLine();
                                    String removeItemName = removeItem.split(", ")[0].split(": ")[1];
                                    removeItemName = removeItemName.trim();

                                    for (Seller sel : sellers) {
                                        for (Shop shop : sel.getStores()) {
                                            for (Product p : shop.getProducts()) {
                                                if (p.getName().toLowerCase().trim().equals(removeItemName.
                                                        toLowerCase().trim())) {
                                                    try {
                                                        customer.removeFromShoppingCart(sel, p);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        MainServer.printErrorPane(e.getMessage());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (cartChoice == 2) {
                                // print the customer's shopping cart
                                ArrayList<String> cart = customer.getShoppingCart();
                                if (cart == null || cart.isEmpty()) {
                                    writer.println("false");
                                    writer.flush();
                                } else {
                                    writer.println("true");
                                    writer.flush();

                                    writer.println(String.valueOf(cart.size()));
                                    writer.flush();

                                    for (String s : cart) {
                                        writer.println(s);
                                        writer.flush();
                                    }
                                }
                            } else if (cartChoice == 3) {
                                //customer.transactionHistory();
                                customer.addUpSalesClearSellerCart(sellers);
                                customer.clearCart();

                                writer.println("All items purchased successfully!");
                                writer.flush();
                            }

                        } else if (choice == 1) {

                            String descSearchName = reader.readLine();

                            ArrayList<Product> resultProducts = MainServer.productDetails(sellers, descSearchName);
                            if (resultProducts == null || resultProducts.isEmpty() || resultProducts.size() == 0) {
                                writer.println("false");
                                writer.flush();
                            } else {
                                writer.println("true");
                                writer.flush();

                                writer.println(resultProducts.size());
                                writer.flush();

                                int counter = 0;
                                for (Product product : resultProducts) {
                                    counter++;
                                    String sendString = String.format("Item %d: %s, Description: %s, Amount " +
                                                    "Available: %d\n", counter,
                                            product.getName(), product.getDescription(), product.getQuantity());
                                    writer.println(sendString);
                                    writer.flush();
                                }

                                String buyStuff = reader.readLine();
                                if (buyStuff.equals("true")) {
                                    String buyItemName = reader.readLine();

                                    int amountWanted = Integer.parseInt(reader.readLine());

                                    try {
                                        if (customer.getShoppingCart().isEmpty()) {
                                            writer.println("empty");
                                            writer.flush();


                                            for (Seller seller : sellers) {
                                                for (Shop store : seller.getStores()) {
                                                    for (Product item : store.getProducts()) {
                                                        if (item.getName().trim().
                                                                equalsIgnoreCase(buyItemName)) {
                                                            if (item.getQuantity() >= amountWanted) {
                                                                customer.addToShoppingCart(seller, item,
                                                                        amountWanted);
                                                                //customer.transactionHistory();
                                                                customer.addUpSalesClearSellerCart(sellers);
                                                                customer.clearCart();
                                                                Setup.updateDetailedProduct(sellers);

                                                                writer.println("Purchase succesful!");
                                                                writer.flush();
                                                            } else {
                                                                throw new IllegalPurchaseException("Purchase failed, "
                                                                        + "too many units were " +
                                                                        "trying to be purchased!");
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else {

                                            writer.println("not empty");
                                            writer.flush();

                                            int purchaseOtherItems = Integer.parseInt(reader.readLine());

                                            if (purchaseOtherItems == 1) {
                                                for (Seller seller : sellers) {
                                                    for (Shop store : seller.getStores()) {
                                                        for (Product item : store.getProducts()) {
                                                            if (item.getName().trim().
                                                                    equalsIgnoreCase(buyItemName)) {
                                                                if (item.getQuantity() >= amountWanted) {
                                                                    try {
                                                                        customer.purchase(sellers,
                                                                                item, amountWanted);

                                                                        writer.println("Purchase successful!");
                                                                        writer.flush();
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else if (purchaseOtherItems == 0) {
                                                for (Seller seller : sellers) {
                                                    for (Shop store : seller.getStores()) {
                                                        for (Product item : store.getProducts()) {
                                                            if (item.getName().trim().
                                                                    equalsIgnoreCase(buyItemName)) {
                                                                if (item.getQuantity() >= amountWanted) {
                                                                    customer.addToShoppingCart(seller, item,
                                                                            amountWanted);
                                                                    //customer.transactionHistory();
                                                                    customer.addUpSalesClearSellerCart(sellers);
                                                                    customer.clearCart();

                                                                    writer.println("Purchase of all items " +
                                                                            "successful!");
                                                                    writer.flush();
                                                                } else {
                                                                    throw new IllegalPurchaseException("Purchase "
                                                                            + "failed, " + "too many units were " +
                                                                            "trying to be purchased!");
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                }
                            }
                        } else if (choice == 2) {
                            int searchChoice = Integer.parseInt(reader.readLine());
                            if (searchChoice == 0) { // search for product
                                String search = reader.readLine();

                                ArrayList<String> searchResults = MainServer.searchProds(sellers, search);

                                if (searchResults == null || searchResults.size() == 0) {
                                    writer.println("0");
                                    writer.flush();
                                } else {
                                    writer.println(String.valueOf(searchResults.size()));
                                    writer.flush();
                                }

                                for (String s : searchResults) {
                                    writer.println(s);
                                    writer.flush();
                                }

                                int searchContinueOption = Integer.parseInt(reader.readLine());
                                if (searchContinueOption == 0) {
                                    String searchPurchaseItem = reader.readLine();
                                    int amountRequested = Integer.parseInt(reader.readLine());

                                    String searchPurchaseItemStore = searchPurchaseItem.split(" ; ")[1].split(": ")[1];

                                    String searchPurchaseItemName = searchPurchaseItem.split(": ")[1].split(", ")[0];

                                    for (Seller seller : sellers) {
                                        for (Shop store : seller.getStores()) {
                                            if (store.getName().equals(searchPurchaseItemStore)) {
                                                for (Product item : store.getProducts()) {
                                                    if (item.getName().equals(searchPurchaseItemName)) {
                                                        customer.purchase(sellers, item, amountRequested);
                                                        writer.println("Purchase successful!");
                                                        writer.flush();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else if (searchContinueOption == 1) {
                                    String searchPurchaseItem = reader.readLine();
                                    int amountRequested = Integer.parseInt(reader.readLine());

                                    String searchPurchaseItemStore = searchPurchaseItem.split(" ; ")[1].split(": ")[1];

                                    String searchPurchaseItemName = searchPurchaseItem.split(": ")[1].split(", ")[0];

                                    for (Seller seller : sellers) {
                                        for (Shop store : seller.getStores()) {
                                            if (store.getName().equals(searchPurchaseItemStore)) {
                                                for (Product product : store.getProducts()) {
                                                    if (product.getName().equals(searchPurchaseItemName)) {
                                                        customer.addToShoppingCart(seller, product,
                                                                amountRequested);
                                                        writer.println("Successfully added to cart!");
                                                        writer.flush();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (searchChoice == 1) { // sort through products
                                int sortChoice = Integer.parseInt(reader.readLine());
                                if (sortChoice == 0) { // sort by quantity
                                    int quantityChoice = Integer.parseInt(reader.readLine());
                                    if (quantityChoice == 0) { // sort quantity in ascending order

                                        ArrayList<String> sortedQuantityAscending = MainServer.sortAscQuantity();

                                        writer.println(String.valueOf(sortedQuantityAscending.size()));
                                        writer.flush();

                                        for (String s : sortedQuantityAscending) {
                                            writer.println(s);
                                            writer.flush();
                                        }
                                    } else if (quantityChoice == 1) { // sort quantity in descending order

                                        ArrayList<String> sortedQuantityDescending = MainServer.sortDescQuantity();

                                        writer.println(String.valueOf(sortedQuantityDescending.size()));
                                        writer.flush();

                                        for (String s : sortedQuantityDescending) {
                                            writer.println(s);
                                            writer.flush();
                                        }
                                    }
                                } else if (sortChoice == 1) { // sort by price
                                    int priceChoice = Integer.parseInt(reader.readLine());
                                    if (priceChoice == 0) {

                                        ArrayList<String> sortedPriceAscending = MainServer.sortAscPrice();

                                        writer.println(String.valueOf(sortedPriceAscending.size()));
                                        writer.flush();

                                        for (String s : sortedPriceAscending) {
                                            writer.println(s);
                                            writer.flush();
                                        }
                                    } else if (priceChoice == 1) {
                                        ArrayList<String> sortedPriceDescending = MainServer.sortDescPrice();

                                        writer.println(String.valueOf(sortedPriceDescending.size()));
                                        writer.flush();

                                        for (String s : sortedPriceDescending) {
                                            writer.println(s);
                                            writer.flush();
                                        }
                                    }
                                }
                            }
                        } else if (choice == 3) {
                            ArrayList<String> transactionHistory = customer.viewTransactionHistory();

                            writer.println(String.valueOf(transactionHistory.size()));
                            writer.flush();

                            for (String s : transactionHistory) {
                                writer.println(s);
                                writer.flush();
                            }
                        } else if (choice == 4) {
                            ArrayList<String> marketplace = MainServer.printMarketplace(sellers);

                            writer.println(String.valueOf(marketplace.size()));
                            writer.flush();

                            for (String s : marketplace) {
                                writer.println(s);
                                writer.flush();
                            }
                        }
                    } while (keepMenu);
                }
            } else if (option == 2) {
            }
        } catch (IOException | LineNotFoundException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Exception",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalPurchaseException e) {
            JOptionPane.showMessageDialog(null, "Illegal purchase, try again",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
        }
        try {
            assert reader != null;
            again = reader.readLine();
        } catch (Exception e) {
        }
        if (again == null) {
            again = "false";
        }

        assert writer != null;
        writer.close();
    }
}

