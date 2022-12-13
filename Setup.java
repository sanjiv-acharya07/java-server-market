import javax.sound.sampled.Port;
import java.io.*;
import java.util.ArrayList;

public class Setup {

    public static synchronized ArrayList<Product> loadProducts() {
        File f = new File("detailedProducts.txt");

        FileReader fr = null;

        try {
            fr = new FileReader(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader bfr = new BufferedReader(fr);

        ArrayList<String> unformattedList = new ArrayList<>();

        while(true) {
            String line = null;
            try {
                line = bfr.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(line == null) { break; }
            unformattedList.add(line);
        }

        ArrayList<Product> products = new ArrayList<>();

        for(String s : unformattedList) {
            String storeName = s.substring(0, s.indexOf(','));

            s = s.substring(s.indexOf(',') + 1);

            String name = s.substring(0, s.indexOf(','));

            s = s.substring(s.indexOf(',') + 1);

            int quantity = Integer.parseInt(s.substring(0, s.indexOf(',')).trim());

            s = s.substring(s.indexOf(',') + 1);

            String description = s.substring(0, s.indexOf(','));

            s = s.substring(s.indexOf(',') + 1);

            double price = Double.parseDouble(s.trim());

            Product prod = new Product(storeName, name, description, quantity, price);

            products.add(prod);
        }

        return products;
    }

    public static synchronized ArrayList<Shop> loadShops() {
        ArrayList<String> unformattedList = ReaderAndWriter.readFile("detailedProducts.txt");

        ArrayList<String> storeNames = new ArrayList<>();

        ArrayList<Shop> shops = new ArrayList<>();

        for(String s : unformattedList) {
            String storeName = s.substring(0 , s.indexOf(','));

            if(!storeNames.contains(storeName)) {
                storeNames.add(storeName);
                Shop shop = new Shop(storeName);
                shops.add(shop);
            }
        }

        ArrayList<Product> unmatchedProducts = loadProducts();

        for(Product prod : unmatchedProducts) {
            String prodStoreName = prod.getStoreName();

            for(Shop shop : shops) {
                if(shop.getName().equals(prodStoreName)) {
                    ArrayList<Product> shopProds = shop.getProducts();

                    shopProds.add(prod);

                    shop.setProducts(shopProds);
                }
            }
        }

        return shops;
    }

    public static synchronized ArrayList<Seller> loadSellers() {
        ArrayList<String> formattedSellers = ReaderAndWriter.readFile("gigaSellers.txt");


        ArrayList<Shop> allShops = loadShops();

        ArrayList<Seller> allSellers = new ArrayList<>();

        for(String s : formattedSellers) {
            ArrayList<Shop> sellerShops = new ArrayList<>();

            String sellerName = s.substring(0 , s.indexOf(':'));

            Seller seller = new Seller(sellerName);

            s = s.substring(s.indexOf(':') + 1);

            while(true) {
                String storeName;
                if (s.contains(",")) {
                    storeName = s.substring(0 , s.indexOf(','));
                } else {
                    storeName = s;
                }

                for(Shop shop : allShops) {
                    String shopName = shop.getName();

                    if(shopName.equals(storeName)) {
                        sellerShops.add(shop);
                    }
                }

                if(s.contains(",")) {
                    s = s.substring(s.indexOf(',') + 1);
                } else {
                    break;
                }
            }

            seller.setStores(sellerShops);



            if(ReaderAndWriter.isFile(sellerName + "ShoppingCartSeller.txt")) {
                ArrayList<String> sellerShoppingCart = ReaderAndWriter.readFile(sellerName + "ShoppingCartSeller.txt");

                String totalSales = sellerShoppingCart.get(sellerShoppingCart.size() - 1);

                totalSales = totalSales.substring(totalSales.indexOf(':') + 1);

                totalSales = totalSales.trim();

                double sellerSales = Double.parseDouble(totalSales);

                seller.setSales(sellerSales);

                seller.setProductsInShoppingCarts(sellerShoppingCart);

                ArrayList<String> editedCart =ReaderAndWriter.readFile(sellerName + "ShoppingCartSeller.txt");

                int numOfRemoved = 0;

                for(String a : seller.getProductsInShoppingCarts()) {
                    if(a.contains("TOTAL SALES:")) {
                        int indexOfString = seller.getProductsInShoppingCarts().indexOf(a);
                        if(indexOfString != seller.getProductsInShoppingCarts().size() - 1) {
                            indexOfString = indexOfString - numOfRemoved;

                            if(indexOfString < 0) {
                                indexOfString = 0;
                            }
                            editedCart.remove(indexOfString - numOfRemoved);
                        }
                    }
                }

                seller.setProductsInShoppingCarts(editedCart);




            } else {
                String temp = "TOTAL SALES: 0.00";

                ArrayList<String> tempArray = new ArrayList<>();

                tempArray.add(temp);

                ReaderAndWriter.writeFile(seller.getName() + "ShoppingCartSeller.txt" , tempArray , false);

                seller.setSales(0);
            }



            allSellers.add(seller);

        }
        return allSellers;
    }

    public static void updateDetailedProduct(ArrayList<Seller> sellers ) {
        synchronized (HandleClient.details) {
            ArrayList<String> allProducts = new ArrayList<>();

            for(Seller seller : sellers) {
                String totalSales = String.format("TOTAL SALES: %.2f" , seller.getSales());
                for(Shop shop : seller.getStores()) {
                    for(Product prod : shop.getProducts()) {
                        String productInfo = prod.toString();
                        allProducts.add(productInfo);
                    }
                }
                ArrayList<String> prevCart = ReaderAndWriter.readFile(seller.getName() + "ShoppingCartSeller.txt");

                boolean hasSales = false;

                int sizeArray = prevCart.size();

                if(sizeArray != 0) {
                    hasSales = prevCart.get(sizeArray - 1).contains("TOTAL SALES:");
                }

                if(!hasSales) {

                    ArrayList<String> temp = new ArrayList<>();

                    temp.add(totalSales);

                    ReaderAndWriter.writeFile(seller.getName() + "ShoppingCartSeller.txt" , temp , true);
                } else {
                    prevCart.set(sizeArray - 1 , totalSales);

                    ReaderAndWriter.writeFile(seller.getName() + "ShoppingCartSeller.txt" , prevCart , false);
                }
            }
            ReaderAndWriter.writeFile("detailedProducts.txt" , allProducts , false);

            updateGigaSellers(sellers);
        }
    }

    public static void specificUpdateDetailedProducts(ArrayList<Seller> sellers, Product product,
                                                                int amount) {
        synchronized (HandleClient.details) {
            {
                ArrayList<String> currentDetailedProduct = ReaderAndWriter.readFile("detailedProducts.txt");


                //find product
                int indexOfProduct = -1;
                for(String s : currentDetailedProduct) {
                    if(s.contains(product.getName())) {
                        indexOfProduct = currentDetailedProduct.indexOf(s);
                    }
                }

                //Find set new prod info
                if(indexOfProduct != -1) {
                    currentDetailedProduct.set(indexOfProduct , product.toString());
                } else {
                    currentDetailedProduct.add(product.toString());
                }

                //Write to file
                ReaderAndWriter.writeFile("detailedProducts.txt" , currentDetailedProduct , false);

                for(Seller seller : sellers) {
                    for(Shop shop : seller.getStores()) {
                        if(product.getStoreName().contains(shop.getName())) {

                            ArrayList<String> prevCart = ReaderAndWriter.readFile(seller.getName() + "ShoppingCartSeller.txt");

                            boolean hasSales;

                            int sizeArray = prevCart.size();

                            if(sizeArray == 0) {
                                hasSales = false;
                            } else {
                                hasSales = prevCart.get(sizeArray - 1).contains("TOTAL SALES:");
                            }

                            double sales = product.getPrice() * amount;

                            String totalSales = String.format("TOTAL SALES: %.2f" , seller.getSales() + sales);

                            if(!hasSales) {

                                ArrayList<String> temp = new ArrayList<>();

                                temp.add(totalSales);

                                ReaderAndWriter.writeFile(seller.getName() + "ShoppingCartSeller.txt" , temp , true);
                            } else {
                                prevCart.set(sizeArray - 1 , totalSales);

                                ReaderAndWriter.writeFile(seller.getName() + "ShoppingCartSeller.txt" , prevCart , false);
                            }

                        }
                    }
                }
            }
        }
    }


    public static void updateGigaSellers(ArrayList<Seller> sellers) {
        synchronized (HandleClient.giga) {
            ArrayList<String> gigaSellers = new ArrayList<>();
            for(Seller seller : sellers) {
                StringBuilder sellerLine = new StringBuilder();

                sellerLine.append(seller.getName()).append(":");

                for(Shop shop : seller.getStores()) {
                    if(sellerLine.toString().indexOf(':') == sellerLine.length() - 1) {
                        sellerLine.append(shop.getName());
                    } else {
                        sellerLine.append(",").append(shop.getName());
                    }
                }

                String finalString = sellerLine.toString();
                gigaSellers.add(finalString);
            }

            ReaderAndWriter.writeFile("gigaSellers.txt" , gigaSellers , false);
        }
    }
}
