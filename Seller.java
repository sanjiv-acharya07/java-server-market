import java.io.*;
import java.util.ArrayList;

public class Seller {
    private final String sellerName;

    private ArrayList<Shop> stores = new ArrayList<>();
    private double sales;

    private ArrayList<String> productsInShoppingCarts = new ArrayList<>();

    public Seller(String sellerName) {
        this.sellerName = sellerName;
    }

    public double getSales() {
        return sales;
    }

    public ArrayList<Shop> getStores() {
        return stores;
    }

    public ArrayList<String> getProductsInShoppingCarts() {
        return productsInShoppingCarts;
    }

    public String getName() {
        return sellerName;
    }

    public void setProductsInShoppingCarts(ArrayList<String> productsInShoppingCarts) {
        this.productsInShoppingCarts = productsInShoppingCarts;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }

    public void setStores(ArrayList<Shop> stores) {
        this.stores = stores;
    }

    public String toString() {
        String sellerToString = "";
        for(Shop shop : stores) {
            for(Product prod : shop.getProducts()) {
                if(sellerToString.equals("")) {
                    sellerToString = prod.toString();
                } else {
                    sellerToString += "\n" + prod.toString();
                }
            }
        }
        return sellerToString;
    }
    public void exportProductFile(ArrayList<Seller> sellers) {
        try {
            String filename = sellerName + "ProductDetails.txt";
            File file = new File(filename);
            FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos);

            for (Seller seller : sellers) {
                if (seller.getName().equals(sellerName)) {
                    for (Shop store : seller.getStores()) {
                        for (Product product : store.getProducts()) {
                            String writeString = String.format("%s,%s,%.2f,%s,%d", product.getStoreName(),
                                    product.getName(), product.getPrice(), product.getDescription(),
                                    product.getQuantity());
                            pw.write(writeString);
                            pw.println();
                        }
                    }
                }
            }
            pw.flush();
            pw.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void readProductFile(ArrayList<Seller> sellers, String filename) {
        try {
            ArrayList<Product> products = new ArrayList<>();
            File file = new File(filename);
            FileReader fr = new FileReader(file);
            BufferedReader bfr = new BufferedReader(fr);

            String line = bfr.readLine();
            while (line != null) {
                String[] splitLines = line.split(",");
                String store = splitLines[0];
                String itemName = splitLines[1];
                double price = Double.parseDouble(splitLines[2]);
                String description = splitLines[3];
                int quantity = Integer.parseInt(splitLines[4]);

                products.add(new Product(store, itemName, description, quantity, price));
                line = bfr.readLine();
            }

            for (Product product : products) {
                for (Seller seller : sellers) {
                    for (Shop store : seller.getStores()) {
                        if (store.getName().equals(product.getStoreName())) {
                            ArrayList<Product> tempProducts = store.getProducts();
                            tempProducts.add(product);
                            store.setProducts(tempProducts);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void editProductsThroughFile(ArrayList<Seller> sellers, String filenameInitial, String filenameNew) {
        try {
            ArrayList<Product> oldProducts = new ArrayList<>();
            ArrayList<Product> newProducts = new ArrayList<>();
            File fileOld = new File(filenameInitial);
            FileReader frOld = new FileReader(fileOld);
            BufferedReader bfrOld = new BufferedReader(frOld);

            File fileNew = new File(filenameNew);
            FileReader frNew = new FileReader(fileNew);
            BufferedReader bfrNew = new BufferedReader(frNew);

            String line = bfrOld.readLine();
            while (line != null) {
                String[] splitLines = line.split(",");
                String store = splitLines[0];
                String itemName = splitLines[1];
                double price = Double.parseDouble(splitLines[2]);
                String description = splitLines[3];
                int quantity = Integer.parseInt(splitLines[4]);

                oldProducts.add(new Product(store, itemName, description, quantity, price));
                line = bfrOld.readLine();
            }

            String line1 = bfrNew.readLine();
            while (line1 != null) {
                String[] splitLines = line1.split(",");
                String store = splitLines[0];
                String itemName = splitLines[1];
                double price = Double.parseDouble(splitLines[2]);
                String description = splitLines[3];
                int quantity = Integer.parseInt(splitLines[4]);

                newProducts.add(new Product(store, itemName, description, quantity, price));
                line1 = bfrNew.readLine();
            }

            for (Seller seller : sellers) {
                for (Shop store : seller.getStores()) {
                    ArrayList<Product> temp = new ArrayList<>();
                    for (Product item : store.getProducts()) {
                        temp.add(item);
                        for (Product product : oldProducts) {
                            if (item.getName().equals(product.getName())) {
                                int index = oldProducts.indexOf(product);
                                int tempIndex = temp.indexOf(item);
                                temp.set(tempIndex, newProducts.get(index));
                            }
                        }
                    }
                    store.setProducts(temp);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized void deleteProductFile(ArrayList<Seller> sellers, String filename) {
        try {
            ArrayList<String> details = new ArrayList<>();
            File file = new File(filename);
            FileReader fr = new FileReader(file);
            BufferedReader bfr = new BufferedReader(fr);

            String line = bfr.readLine();
            while (line != null) {
                details.add(line);
                line = bfr.readLine();
            }

            for (String string : details) {
                String name = string.split(",")[0];
                String store = string.split(",")[1];
                ArrayList<Product> temp = null;
                for (Seller seller : sellers) {
                    for (Shop shop : seller.getStores()) {
                        if (shop.getName().equals(store)) {
                            temp = shop.getProducts();
                        }
                    }
                }
                Product tempProduct = new Product();

                for (Seller seller : sellers) {
                    for (Shop shop : seller.getStores()) {
                        if (shop.getName().equals(store)) {
                            for (Product product : shop.getProducts()) {
                                if (product.getName().equals(name)) {
                                    tempProduct = product;
                                }
                            }
                        }
                    }
                }
                assert temp != null;
                temp.remove(tempProduct);
                for (Seller seller : sellers) {
                    for (Shop shop : seller.getStores()) {
                        if (shop.getName().equals(store)) {
                            shop.setProducts(temp);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String salesByStorePrint() {
        StringBuilder sales = new StringBuilder();
        sales.append(sellerName).append("\n");
        for (Shop shop : stores) {
            sales.append("---------\n");
            sales.append(shop.getName()).append(":").append("\n");

            if (ReaderAndWriter.isFile(shop.getName() + "TransactionHistory.txt")) {
                ArrayList<String> shopTH = ReaderAndWriter.readFile(shop.getName() + "TransactionHistory.txt");
                for (String s : shopTH) {
                    sales.append(s).append("\n");
                }
            } else {
                sales.append("No Sales\n");
            }
        }
        return sales.toString();
    }
}
