import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class MainClient {
    static ImageIcon icon = new ImageIcon("marketIcon.jpg");
    public static void main(String[] args) throws IOException {
        boolean again = false;
        BufferedReader reader = null;
        PrintWriter writer = null;

        Socket socket = new Socket("localhost", 4242);


        try {
            int option = JOptionPane.showConfirmDialog(null, "Are you a returning user?",
                    null, JOptionPane.YES_NO_CANCEL_OPTION); // yes = 0, no = 1, cancel = 2

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            writer.println(option); // caught by readline 1
            writer.flush();

            if (option == 1) {
                // first time user

                String line = "";
                String username;
                do {
                    username = JOptionPane.showInputDialog(null, "Create a username",
                            "Signup",
                            JOptionPane.PLAIN_MESSAGE);
                    writer.println(username); // caught by readline 2 option 1
                    writer.flush();
                    if (username == null) {
                        JOptionPane.showMessageDialog(null, "Thank you for using the marketplace!");
                        break;
                    }
                    line = reader.readLine();
                    if (line.equals("true")) {
                        JOptionPane.showMessageDialog(null, "Username taken: Please enter a " +
                                "new username", "Reenter Username", JOptionPane.ERROR_MESSAGE);
                    } else {
                        break;
                    }
                } while (line.equals("true"));
                if (username == null) {
                }

                String password = JOptionPane.showInputDialog(null, "Create a password",
                        "Signup",
                        JOptionPane.PLAIN_MESSAGE);
                writer.println(password);
                if (password == null) {
                    JOptionPane.showMessageDialog(null, "Thank you for using the marketplace!");
                }// caught by readline 3 option 1
                String[] types = {"Customer", "Seller"};
                String userChoice = (String) JOptionPane.showInputDialog(null,
                        "What type of user are you?",
                        "Type", JOptionPane.QUESTION_MESSAGE, icon, types, types[0]);
                writer.println(userChoice); // caught by readline 4 option 1
            } else if (option == 0) {
                // returning user
                String username;
                String line;
                String type = "";
                do {
                    username = JOptionPane.showInputDialog(null, "Please enter your username",
                            "USERNAME", JOptionPane.QUESTION_MESSAGE);
                    writer.println(username);
                    writer.flush();
                    if (username == null) {
                        JOptionPane.showMessageDialog(null, "Thank you for using the marketplace!");
                        break;
                    }
                    line = reader.readLine(); // boolean, usertype

                    if (line.contains("false")) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid username",
                                null, JOptionPane.ERROR_MESSAGE);
                    } else {
                        String[] split = line.split(", ");
                        type = split[1];
                    }
                } while (line.contains("false"));
                writer.println(username);
                if (username == null) {
                }

                String line2;
                String password;
                do {
                    password = JOptionPane.showInputDialog(null, "Please enter your password",
                            "PASSWORD", JOptionPane.QUESTION_MESSAGE);
                    writer.println(password);
                    writer.flush();
                    if (password == null) {
                        JOptionPane.showMessageDialog(null,
                                "Thank you for using the marketplace!", "Thank you",
                                JOptionPane.INFORMATION_MESSAGE, icon);
                        break;
                    }
                    line2 = reader.readLine();
                    if (line2.equals("false")) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid password",
                                null, JOptionPane.ERROR_MESSAGE, icon);
                    }
                } while (line2.equals("false"));
                if (password == null) {
                }
                writer.println(type); //type writeline
                writer.flush();

                if (type.equals("Customer")) {

                    boolean keepMenu = true;
                    do {
                        String[] choices = {"Handle shopping cart",
                                "Check an item's description",
                                "Search",
                                "Print transaction history",
                                "Display entire marketplace"
                        };
                        String choice = (String) JOptionPane.showInputDialog(null, "MENU", "MENU",
                                JOptionPane.INFORMATION_MESSAGE, icon, choices, choices[0]);
                        if (choice == null) {
                            break;
                        }
                        if (choice.equals("Handle shopping cart")) {
                            writer.println("0");
                            writer.flush();

                            String[] cartChoices = {"Add an item to cart",
                                    "Remove an item from cart",
                                    "Print the shopping cart",
                                    "Checkout - Purchase all items in cart"};
                            String cartChoice = (String) JOptionPane.showInputDialog(null, "Cart Menu",
                                    "MENU", JOptionPane.INFORMATION_MESSAGE, icon, cartChoices,
                                    cartChoices[0]);
                            if (cartChoice == null) {
                                break;
                            }
                            if (cartChoice.equals("Add an item to cart")) {
                                writer.println("0");
                                writer.flush();

                                int size = Integer.parseInt(reader.readLine());

                                ArrayList<String> tempMarketplace = new ArrayList<>();
                                for (int i = 0; i < size; i++) {
                                    tempMarketplace.add(reader.readLine());
                                }

                                String[] marketplace = new String[size];
                                for (int i = 0; i < size; i++) {
                                    marketplace[i] = tempMarketplace.get(i);
                                }

                                String addItem = (String) JOptionPane.showInputDialog(null,
                                        "Select the item you want",
                                        "Add an item to cart",
                                        JOptionPane.INFORMATION_MESSAGE, icon, marketplace, marketplace[0]);

                                String addItemName = addItem.split(": ")[1].split(", ")[0];

                                writer.println(addItem); // sending the string as is to the server
                                writer.flush();

                                int amountAvailable = Integer.parseInt(reader.readLine().split(":")[1]);

                                String amount = "";
                                do {
                                    amount = JOptionPane.showInputDialog(null, "Please " +
                                                    "enter the amount of the item you would like: ",
                                            "Amount", JOptionPane.QUESTION_MESSAGE);
                                    if (Integer.parseInt(amount) > amountAvailable) {
                                        JOptionPane.showMessageDialog(null, "Please enter " +
                                                "a valid amount", "ERROR", JOptionPane.ERROR_MESSAGE);
                                    }
                                } while (Integer.parseInt(amount) > amountAvailable);

                                writer.println(amount);
                                writer.flush();

                            } else if (cartChoice.equals("Remove an item from cart")) {
                                writer.println("1");
                                writer.flush();

                                String go = reader.readLine();
                                if (go.equals("true")) { // cart has things in it
                                    int size = Integer.parseInt(reader.readLine());

                                    ArrayList<String> tempCart = new ArrayList<>();
                                    for (int i = 0; i < size; i++) {
                                        tempCart.add(reader.readLine());
                                    }

                                    String[] cart = new String[size];
                                    for (int i = 0; i < size; i++) {
                                        cart[i] = tempCart.get(i);
                                    }

                                    String removeItem = (String) JOptionPane.showInputDialog(null, "Select the item you " +
                                                    "need to remove", "Remove item", JOptionPane.INFORMATION_MESSAGE,
                                            icon, cart, cart[0]);


                                    writer.println(removeItem);
                                    writer.flush();

                                } else if (go.equals("false")) { // cart is null or empty
                                    JOptionPane.showMessageDialog(null, "The cart is empty",
                                            "ERROR", JOptionPane.ERROR_MESSAGE);
                                    break;
                                }
                            } else if (cartChoice.equals("Print the shopping cart")) {
                                writer.println("2");
                                writer.flush();

                                String moveOn = reader.readLine();
                                String cartString = "";
                                if (moveOn.equals("true")) {
                                    int size = Integer.parseInt(reader.readLine());
                                    for (int i = 0; i < size; i++) {
                                        String readline = reader.readLine();
                                        cartString = cartString.concat(readline + "\n");
                                    }
                                    JOptionPane.showMessageDialog(null, cartString,
                                            "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "The cart is empty!",
                                            "Empty cart", JOptionPane.ERROR_MESSAGE);
                                }

                            } else if (cartChoice.equals("Checkout - Purchase all items in cart")) {
                                writer.println("3");
                                writer.flush();

                                JOptionPane.showMessageDialog(null, reader.readLine(),
                                        "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else if (choice.equals("Check an item's description")) {
                            writer.println("1");
                            writer.flush();

                            String itemName = JOptionPane.showInputDialog(null, "Please " +
                                            "enter the " +
                                            "name of the " +
                                            "item you need details of", "Description Search",
                                    JOptionPane.QUESTION_MESSAGE);

                            writer.println(itemName);
                            writer.flush();

                            String moveOn = reader.readLine();
                            if (moveOn.equals("true")) {
                                int size = Integer.parseInt(reader.readLine());
                                String[] searchResults = new String[size];
                                String stringResults = "";
                                for (int i = 0; i < size; i++) {
                                    searchResults[i] = reader.readLine();
                                    stringResults = stringResults.concat(searchResults[i]);
                                }

                                JOptionPane.showMessageDialog(null, stringResults,
                                        "Search Results", JOptionPane.INFORMATION_MESSAGE);

                                int letsGo = (int) JOptionPane.showConfirmDialog(null,
                                        "Would you like to buy any of these items?",
                                        "Buy?", JOptionPane.YES_NO_OPTION);
                                if (letsGo == 0) {
                                    writer.println("true");
                                    writer.flush();
                                    // yes
                                    String buyItem = (String) JOptionPane.showInputDialog(null,
                                            "Select an item", "Buy item", JOptionPane.QUESTION_MESSAGE
                                            , icon, searchResults, searchResults[0]);
                                    String buyItemName = buyItem.split(": ")[1].split(", ")[0].trim();

                                    int amountAvailable = Integer.parseInt(buyItem.split(":")[3].trim());
                                    writer.println(buyItemName);
                                    writer.flush();

                                    String amountWanted = "";
                                    do {
                                        amountWanted = JOptionPane.showInputDialog(null, "Please " +
                                                        "enter the amount of the item you would like: ",
                                                "Amount", JOptionPane.QUESTION_MESSAGE);
                                        if (Integer.parseInt(amountWanted) > amountAvailable) {
                                            JOptionPane.showMessageDialog(null, "Please enter " +
                                                    "a valid amount", "ERROR", JOptionPane.ERROR_MESSAGE);
                                        }
                                    } while (Integer.parseInt(amountWanted) > amountAvailable);

                                    writer.println(amountWanted);
                                    writer.flush();


                                    reader.readLine();
                                    String empty = reader.readLine();

                                    if (empty.equals("empty")) {
                                        JOptionPane.showMessageDialog(null, reader.readLine(),
                                                "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                                        continue;
                                    } else if (empty.equals("not empty")) {

                                        int purchaseOtherItems = JOptionPane.showConfirmDialog(null, "Would you like"
                                                        + "to purchase the other items in your cart as well?",
                                                "Purchase", JOptionPane.YES_NO_OPTION); // yes = 0, no = 1
                                        if (purchaseOtherItems == JOptionPane.YES_OPTION) {
                                            writer.println("0"); // telling server that the user wants to purchase
                                            // the other items
                                            writer.flush();

                                            JOptionPane.showMessageDialog(null, reader.readLine(),
                                                    "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                                        } else if (purchaseOtherItems == JOptionPane.NO_OPTION) {
                                            writer.println("1");
                                            writer.flush();

                                            JOptionPane.showMessageDialog(null, reader.readLine(),
                                                    "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                                        }
                                    }

                                } else {
                                    writer.println("false");
                                    writer.flush();

                                    break;
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Sorry, no results found!",
                                        "ERROR",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                            if (itemName == null) {
                                break;
                            }
                        } else if (choice.equals("Search")) {
                            writer.println("2");
                            writer.flush();

                            String[] searchOptions = {"Search for product", "Sort through products"};
                            String searchChoice = (String) JOptionPane.showInputDialog(null,
                                    "Search Menu",
                                    "MENU", JOptionPane.INFORMATION_MESSAGE, null, searchOptions,
                                    searchOptions[0]);
                            if (searchChoice == null) {
                                break;
                            }
                            if (searchChoice.equals("Search for product")) {
                                writer.println("0");
                                writer.flush();

                                String search = JOptionPane.showInputDialog(null,
                                        "Enter your search: ", "Item Search",
                                        JOptionPane.QUESTION_MESSAGE);
                                if (search == null) {
                                    break;
                                } else {
                                    writer.println(search);
                                    writer.flush();

                                    int size = Integer.parseInt(reader.readLine());

                                    if (size == 0) {
                                        JOptionPane.showMessageDialog(null, "Sorry, no results" +
                                                " found!", "No results", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        String[] searchResults = new String[size];
                                        String printSearchResults = "";
                                        for (int i = 0; i < size; i++) {
                                            searchResults[i] = reader.readLine();
                                            printSearchResults = printSearchResults.concat(searchResults[i] + "\n");
                                        }
                                        JOptionPane.showMessageDialog(null, printSearchResults, "Search " +
                                                "Results", JOptionPane.INFORMATION_MESSAGE);

                                        String[] searchContinueOptions = {"Would you like to purchase an item?",
                                                "Would you like to add an item to cart?"};
                                        String searchContinueOption = (String) JOptionPane.showInputDialog(null,
                                                "Continue options", "Search Options", JOptionPane.QUESTION_MESSAGE,
                                                icon, searchContinueOptions, searchContinueOptions[0]);

                                        if (searchContinueOption.equals("Would you like to purchase an item?")) {
                                            writer.println("0");
                                            writer.flush();

                                            String searchPurchaseItem = (String) JOptionPane.showInputDialog(null, "Select item to purchase",
                                                    "Purchase an item", JOptionPane.QUESTION_MESSAGE, icon,
                                                    searchResults, searchResults[0]);


                                            writer.println(searchPurchaseItem);
                                            writer.flush();

                                            int purchaseItemAmountAvailable = Integer.parseInt(searchPurchaseItem.
                                                    split(" ; ")[0].split(": ")[4]);

                                            String amountRequested;
                                            do {
                                                amountRequested = JOptionPane.showInputDialog(null, "Please " +
                                                                "enter the amount of the item you would like: ",
                                                        "Amount", JOptionPane.QUESTION_MESSAGE);
                                                if (Integer.parseInt(amountRequested) > purchaseItemAmountAvailable) {
                                                    JOptionPane.showMessageDialog(null, "Please enter " +
                                                            "a valid amount", "ERROR", JOptionPane.ERROR_MESSAGE);
                                                }
                                            } while (Integer.parseInt(amountRequested) > purchaseItemAmountAvailable);

                                            writer.println(amountRequested);
                                            writer.flush();

                                            JOptionPane.showMessageDialog(null, reader.readLine(),
                                                    "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                                        } else if (searchContinueOption.equals("Would you like to add an item to " +
                                                "cart?")) {
                                            writer.println("1");
                                            writer.flush();

                                            String searchPurchaseItem = (String) JOptionPane.showInputDialog(null, "Select item to purchase",
                                                    "Purchase an item", JOptionPane.QUESTION_MESSAGE, icon,
                                                    searchResults, searchResults[0]);


                                            writer.println(searchPurchaseItem);
                                            writer.flush();

                                            int purchaseItemAmountAvailable = Integer.parseInt(searchPurchaseItem.
                                                    split(" ; ")[0].split(": ")[4]);

                                            String amountRequested;
                                            do {
                                                amountRequested = JOptionPane.showInputDialog(null, "Please " +
                                                                "enter the amount of the item you would like: ",
                                                        "Amount", JOptionPane.QUESTION_MESSAGE);
                                                if (Integer.parseInt(amountRequested) > purchaseItemAmountAvailable) {
                                                    JOptionPane.showMessageDialog(null, "Please enter " +
                                                            "a valid amount", "ERROR", JOptionPane.ERROR_MESSAGE);
                                                }
                                            } while (Integer.parseInt(amountRequested) > purchaseItemAmountAvailable);

                                            writer.println(amountRequested);
                                            writer.flush();

                                            JOptionPane.showMessageDialog(null, reader.readLine(),
                                                    "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                                        }
                                    }
                                }
                            } else if (searchChoice.equals("Sort through products")) {
                                writer.println("1");
                                writer.flush();

                                String[] sortOptions = {"Sort by quantity", "Sort by price"};
                                String sortChoice = (String) JOptionPane.showInputDialog(null,
                                        "Sort Menu",
                                        "MENU", JOptionPane.INFORMATION_MESSAGE, null, sortOptions,
                                        sortOptions[0]);
                                if (sortChoice == null) {
                                    break;
                                }
                                if (sortChoice.equals("Sort by quantity")) {
                                    writer.println("0");
                                    writer.flush();

                                    String[] sortQuantityOptions = {"Sort in ascending order",
                                            "Sort in descending order"};
                                    String quantityChoice = (String) JOptionPane.showInputDialog(null,
                                            "Sort by Quantity", "MENU", JOptionPane.INFORMATION_MESSAGE, null,
                                            sortQuantityOptions, sortQuantityOptions[0]);
                                    if (quantityChoice == null) {
                                        break;
                                    }
                                    else if (quantityChoice.equals("Sort in ascending order")) {
                                        writer.println("0");
                                        writer.flush();

                                        int size = Integer.parseInt(reader.readLine());

                                        String sortedQuantityAscendingString = "";
                                        for (int i = 0; i < size; i++) {
                                            sortedQuantityAscendingString =
                                                    sortedQuantityAscendingString.concat(reader.readLine() +
                                                            "\n");
                                        }

                                        JOptionPane.showMessageDialog(null,
                                                sortedQuantityAscendingString,"Sorted by Quantity in Ascending " +
                                                        "Order", JOptionPane.INFORMATION_MESSAGE);
                                    } else if (quantityChoice.equals("Sort in descending order")) {
                                        writer.println("1");
                                        writer.flush();

                                        int size = Integer.parseInt(reader.readLine());

                                        String sortedQuantityDescendingString = "";
                                        for (int i = 0; i < size; i++) {
                                            sortedQuantityDescendingString =
                                                    sortedQuantityDescendingString.concat(reader.readLine() +
                                                            "\n");
                                        }

                                        JOptionPane.showMessageDialog(null,
                                                sortedQuantityDescendingString,"Sorted by Quantity in " +
                                                        "Descending " +
                                                        "Order", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                } else if (sortChoice.equals("Sort by price")) {
                                    writer.println("1");
                                    writer.flush();

                                    String[] sortPriceOptions = {"Sort in ascending order",
                                            "Sort in descending order"};
                                    String priceChoice = (String) JOptionPane.showInputDialog(null,
                                            "Sort by Price", "MENU", JOptionPane.INFORMATION_MESSAGE,
                                            null, sortPriceOptions, sortPriceOptions[0]);
                                    if (priceChoice == null) {
                                        break;
                                    } else if (priceChoice.equals("Sort in ascending order")) {
                                        writer.println("0");
                                        writer.flush();

                                        int size = Integer.parseInt(reader.readLine());

                                        String sortedPriceAscendingString = "";
                                        for (int i = 0; i < size; i++) {
                                            sortedPriceAscendingString =
                                                    sortedPriceAscendingString.concat(reader.readLine() +
                                                            "\n");
                                        }

                                        JOptionPane.showMessageDialog(null,
                                                sortedPriceAscendingString,"Sorted by Price in " +
                                                        "Ascending " +
                                                        "Order", JOptionPane.INFORMATION_MESSAGE);
                                    } else if (priceChoice.equals("Sort in descending order")) {
                                        writer.println("1");
                                        writer.flush();

                                        int size = Integer.parseInt(reader.readLine());

                                        String sortedPriceDescendingString = "";
                                        for (int i = 0; i < size; i++) {
                                            sortedPriceDescendingString =
                                                    sortedPriceDescendingString.concat(reader.readLine() +
                                                            "\n");
                                        }

                                        JOptionPane.showMessageDialog(null,
                                                sortedPriceDescendingString,"Sorted by Price in " +
                                                        "Ascending " +
                                                        "Order", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                }
                            }
                        } else if (choice.equals("Print transaction history")) {
                            writer.println("3");
                            writer.flush();

                            int size = Integer.parseInt(reader.readLine());

                            String transactionString = "";
                            for (int i = 0; i < size; i++) {
                                transactionString = transactionString.concat(reader.readLine() + "\n");
                            }

                            JOptionPane.showMessageDialog(null, transactionString, "Transaction " +
                                    "History", JOptionPane.INFORMATION_MESSAGE);

                        } else if (choice.equals("Display entire marketplace")) {
                            writer.println("4");
                            writer.flush();

                            int size = Integer.parseInt(reader.readLine());

                            String marketPlaceString = "";
                            for (int i = 0; i < size; i++) {
                                marketPlaceString = marketPlaceString.concat(reader.readLine() + "\n");
                            }

                            JOptionPane.showMessageDialog(null, marketPlaceString,
                                    "The Marketplace", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } while (keepMenu);


                } else if (type.equals("Seller")) {
                    boolean keepMenu = true;
                    do {
                        String[] choices = {"Create product",
                                "Edit Product",
                                "Delete Product",
                                "View Sales",
                                "View Shopping Carts",
                                "Export Product Details File"

                        };

                        String choice = (String) JOptionPane.showInputDialog(null, "Sellers Stuff", "Sellers", JOptionPane.INFORMATION_MESSAGE,
                                icon, choices, choices[0]);
                        writer.println(choice); //choice writeLine
                        writer.flush();
                        if (choice == null) {
                            JOptionPane.showMessageDialog(null, "Thank you for using the marketplace!");
                            break;
                            //exitPane method
                        }
                        if (choice.equals("Create product")) {
                            String fileName = JOptionPane.showInputDialog(null, "Enter file to input product(s)", "Input", JOptionPane.INFORMATION_MESSAGE);
                            if (fileName != null) {
                                writer.println(fileName); //import create product fileName
                                writer.flush();
                            }
                        }
                        if (choice.equals("Edit Product")) {
                            String oldFileName = JOptionPane.showInputDialog(null, "Enter the filename of the old product details " +
                                    "(the ones which need to be changed)", "Input", JOptionPane.INFORMATION_MESSAGE);
                            if (oldFileName != null) {
                                writer.println(oldFileName); //import edit product fileName
                                writer.flush();
                            } else {
                                break;
                            }
                            String newFileName = JOptionPane.showInputDialog(null, "Enter the filename of the new product details " +
                                    "(the details which those of the old products will change to)", "Input", JOptionPane.INFORMATION_MESSAGE);
                            if (newFileName != null) {
                                writer.println(newFileName);
                                writer.flush();
                            } else {
                                break;
                            }
                        }
                        if (choice.equals("Delete Product")) {
                            String fileName = JOptionPane.showInputDialog(null, "Enter file with the product to delete", "Input", JOptionPane.INFORMATION_MESSAGE);
                            if (fileName != null) {
                                writer.println(fileName); //import delete product fileName
                                writer.flush();
                            }
                        }
                        if (choice.equals("View Sales")) {
                            int salesSize = Integer.parseInt(reader.readLine()); //reader for seller's shopping cart
                            String salesReader = "";
                            for (int i = 0; i < salesSize; i++) {
                                String read = reader.readLine();
                                salesReader = salesReader.concat(read + "\n");
                            }
                            JOptionPane.showMessageDialog(null, salesReader, "Sales", JOptionPane.INFORMATION_MESSAGE);

                        }
                        if (choice.equals("View Shopping Carts")) {
                            int shoppingCartSellerArray = Integer.parseInt(reader.readLine()); //reader for seller's shopping cart
                            String cartReader = "";
                            for (int i = 0; i < shoppingCartSellerArray; i++) {
                                String read = reader.readLine();
                                cartReader = cartReader.concat(read + "\n");
                            }
                            JOptionPane.showMessageDialog(null, cartReader, "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                        }
                        if (choice.equals("Export Product Details File")) {

                            JOptionPane.showMessageDialog(null, "Please view the exported file from the working directory", "Export", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } while (keepMenu);
                } else {
                    JOptionPane.showMessageDialog(null,"Invalid user", "ERROR",
                            JOptionPane.ERROR_MESSAGE, icon);
                }

            } else if (option == 2) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
        exitPane();
        writer.close();
    }

    public static void exitPane() {
        JOptionPane.showMessageDialog(null, "Thank you for using the marketplace!",
                "exit", JOptionPane.INFORMATION_MESSAGE,
                icon);
    }
}