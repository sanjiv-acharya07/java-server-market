import com.sun.source.tree.ArrayAccessTree;

import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

public class MainServer {
    public static void main(String[] args) throws IOException {

        ArrayList<Seller> sellers = Setup.loadSellers();
        ArrayList<String> marketPlace = printMarketPlace(); // this puts together an arraylist of strings about the
        // items

        String again = "";
        BufferedReader reader = null;
        PrintWriter writer = null;

        ServerSocket serverSocket = null;
        Socket socket = null;
        serverSocket = new ServerSocket(4242);
        while (true) {
            socket = serverSocket.accept();
            HandleClient client = new HandleClient(socket);
            Thread t = new Thread(client);
            t.start();
        }
    }


    public static String checkUsername(String username) throws LineNotFoundException { // boolean, usertype
        String returnString;
        boolean exists = false;
        String type = "";
        try {
            File f = new File("credentials.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();

            while (line != null) {
                String[] split = line.split(" ");

                if (username.equalsIgnoreCase(split[0])) {
                    exists = true;
                    type = split[2];
                }
                line = bfr.readLine();
            }
        } catch (Exception e) {
            throw new LineNotFoundException("Sorry, the username you entered was wrong");
        }
        if (exists) {
            returnString = "true, " + type;
        } else {
            returnString = "false";
        }
        return returnString;
    }

    public static boolean checkPassword(String username, String password) throws LineNotFoundException {
        boolean returnBool = false;
        try {
            if (checkUsername(username).contains("true")) {
                File f = new File("credentials.txt");
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);
                String line = bfr.readLine();
                while (line != null) {
                    String[] split = line.split(" ");

                    if (password.equals(split[1])) {
                        returnBool = true;
                        break;
                    }
                    line = bfr.readLine();
                }
            }
        } catch (Exception e) {
            throw new LineNotFoundException("Sorry, the password you entered was wrong");
        }
        return returnBool;
    }

    public static boolean checkInFile(String filename, String search) throws LineNotFoundException {
        boolean returnBool = false;
        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();

            while (line != null) {
                String[] splits = line.split(" ");
                if (splits[0].equalsIgnoreCase(search)) {
                    returnBool = true;
                }
                line = bfr.readLine();
            }
            return returnBool;
        } catch (Exception e) {
            throw new LineNotFoundException("An error occurred");
        }
    }

    public static String getUserType (String username) throws Exception {
        String type = "";
        try {
            FileReader fr = new FileReader("credentials.txt");
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            while (line != null) {
                String[] splits = line.split(" ");
                if (splits[0].equals(username)) {
                    type = splits[2];
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return type;
    }
    public static ArrayList<String> printMarketplace(ArrayList<Seller> sellers) {
        ArrayList<String> returnObj = new ArrayList<>();
        int counter = 1;
        for (Seller seller : sellers) {
            //returnObj.add(("SELLER: " + seller.getName()));
            for (Shop shop : seller.getStores()) {
                //returnObj.add("STORE: " + shop.getName() + "\n");
                for (Product product : shop.getProducts()) {
                    String temp = String.format("Item %d: %s, Price: %.2f, Amt Available: %d, Store: %s", counter,
                            product.getName(), product.getPrice(), product.getQuantity(), product.getStoreName());
                    returnObj.add(temp);
                    counter++;
                }
            }
        }
        return returnObj;
    }

    public static ArrayList<String> printMarketPlace() throws IOException {
        ArrayList<String> cart = new ArrayList<>();

        File marketFile = new File("detailedProducts.txt");
        FileReader marketfr = new FileReader(marketFile);
        BufferedReader bfrMarket = new BufferedReader(marketfr);

        ArrayList<String> marketPlace = new ArrayList<>();
        String line = bfrMarket.readLine();
        while (line != null) {
            marketPlace.add(line);
            line = bfrMarket.readLine();
        }
        return marketPlace;
    }

    public static void printErrorPane(String message) {
        JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    public static ArrayList<Product> productDetails(ArrayList<Seller> sellers, String search) throws
            IOException { // takes the product name as input
        ArrayList<Product> results = new ArrayList<>();
        try {
            for (Seller sel : sellers) {
                for (Shop store : sel.getStores()) {
                    for (Product product : store.getProducts()) {
                        if (product.getName().equalsIgnoreCase(search) || product.getName().toLowerCase()
                                .contains(search)) {
                            results.add(product);
                        }
                    }
                }
            }

            if (results == null || results.isEmpty()) {
            } else {
                int counter = 1;
                for (Product item : results) {
                    counter++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public static ArrayList<String> searchProds(ArrayList<Seller> sellers, String query) throws FileNotFoundException {
        ArrayList<Product> newArray = new ArrayList<>();
        ArrayList<String> returnArray = new ArrayList<>();

        for (Seller seller : sellers) {
            for (Shop store : seller.getStores()) {
                for (Product product : store.getProducts()) {
                    if (product.getName().equalsIgnoreCase(query)) {
                        newArray.add(product);
                    } else if (product.getName().toLowerCase().contains(query)) {
                        newArray.add(product);
                    }
                }
            }
        }

        int counter = 0;
        for (Product item : newArray) {
            counter++;
            String addString = String.format("Item %d: %s, Price: $%.2f, Details: %s, Quantity: %d ; Store: %s",
                    counter, item.getName(), item.getPrice(), item.getDescription(), item.getQuantity(),
                    item.getStoreName());
            returnArray.add(addString);
        }
        return returnArray;
    }

    public static ArrayList<String> sortAscQuantity() {
        ArrayList<String> newArrayList = new ArrayList<>();
        try {
            File f = new File("detailedProducts.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            ArrayList<Integer> integerArrayList = new ArrayList<>();
            while (line != null) {
                String[] split = line.split(",");
                integerArrayList.add(Integer.valueOf(split[2]));
                line = bfr.readLine();
            }
            int[] intArray = new int[integerArrayList.size()];
            for (int i = 0; i < integerArrayList.size(); i++) {
                intArray[i] = integerArrayList.get(i);
            }
            Arrays.sort(intArray);
            String[] newStringArray = new String[intArray.length];
            File newf = new File("detailedProducts.txt");
            FileReader newfr = new FileReader(newf);
            BufferedReader newbfr = new BufferedReader(newfr);
            String newLine = newbfr.readLine();
            int i = 0;
            String[] sortedArray = new String[newStringArray.length];
            while (newLine != null) {
                newStringArray[i] = newLine;
                newLine = newbfr.readLine();
                i++;

            }
            for (int j = 0; j < intArray.length; j++) {
                for (int k = 0; k < newStringArray.length; k++) {
                    String[] split = newStringArray[k].split(",");
                    if (intArray[j] == Integer.parseInt(split[2]) && !Arrays.toString(sortedArray).contains(newStringArray[k])) {
                        sortedArray[j] = newStringArray[k];
                    }
                }
            }
            ArrayList<String> returnArraySorted = new ArrayList<>();
            for (int o = 0; o < sortedArray.length; o++) {
                String[] split = sortedArray[o].split(",");
                returnArraySorted.add(String.format("Item %d: %s, Price: $%.2f, Quantity: %s", o+1, split[1],
                        Double.parseDouble(split[4]), split[2]));
            }
            return returnArraySorted;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<String> sortDescQuantity() {
        ArrayList<String> newArrayList = new ArrayList<>();
        try {
            File f = new File("detailedProducts.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            ArrayList<Integer> integerArrayList = new ArrayList<>();
            while (line != null) {
                String[] split = line.split(",");
                integerArrayList.add(Integer.valueOf(split[2]));
                line = bfr.readLine();
            }
            int[] intArray = new int[integerArrayList.size()];
            for (int i = 0; i < integerArrayList.size(); i++) {
                intArray[i] = integerArrayList.get(i);
            }
            Arrays.sort(intArray);
            String[] newStringArray = new String[intArray.length];
            File newf = new File("detailedProducts.txt");
            FileReader newfr = new FileReader(newf);
            BufferedReader newbfr = new BufferedReader(newfr);
            String newLine = newbfr.readLine();
            int i = 0;
            String[] sortedArray = new String[newStringArray.length];
            while (newLine != null) {
                newStringArray[i] = newLine;
                newLine = newbfr.readLine();
                i++;
            }
            int l = 0;
            for (int j = intArray.length; j >= 1; j--) {
                for (int k = 0; k < newStringArray.length; k++) {
                    String[] split = newStringArray[k].split(",");
                    if (intArray[j - 1] == Integer.parseInt(split[2]) && !Arrays.toString(sortedArray).contains(newStringArray[k])) {
                        sortedArray[l] = newStringArray[k];
                    }
                }
                l++;
            }

            ArrayList<String> returnArraySorted = new ArrayList<>();
            for (int o = 0; o < sortedArray.length; o++) {
                String[] split = sortedArray[o].split(",");
                returnArraySorted.add(String.format("Item %d: %s, Price: $%.2f, Quantity: %s", o+1, split[1],
                        Double.parseDouble(split[4]), split[2]));
            }

            return returnArraySorted;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<String> sortAscPrice() {
        try {
            File f = new File("detailedProducts.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            ArrayList<Double> doubleArrayList = new ArrayList<>();
            while (line != null) {
                String[] split = line.split(",");
                doubleArrayList.add(Double.valueOf(split[4]));
                line = bfr.readLine();
            }
            double[] doubleArray = new double[doubleArrayList.size()];
            for (int i = 0; i < doubleArrayList.size(); i++) {
                doubleArray[i] = doubleArrayList.get(i);
            }
            Arrays.sort(doubleArray);
            String[] newStringArray = new String[doubleArray.length];
            File newf = new File("detailedProducts.txt");
            FileReader newfr = new FileReader(newf);
            BufferedReader newbfr = new BufferedReader(newfr);
            String newLine = newbfr.readLine();
            int i = 0;
            String[] sortedArray = new String[newStringArray.length];
            while (newLine != null) {
                newStringArray[i] = newLine;
                newLine = newbfr.readLine();
                i++;

            }
            for (int j = 0; j < doubleArray.length; j++) {
                for (int k = 0; k < newStringArray.length; k++) {
                    String[] split = newStringArray[k].split(",");
                    if (doubleArray[j] == Double.parseDouble(split[4]) && !Arrays.toString(sortedArray).contains(newStringArray[k])) {
                        sortedArray[j] = newStringArray[k];
                    }
                }
            }
            ArrayList<String> returnSortedArray = new ArrayList<>();
            for (int o = 0; o < sortedArray.length; o++) {
                String[] split = sortedArray[o].split(",");
                returnSortedArray.add(String.format("Item %d: %s, Price: $%.2f, Quantity: %s", o+1, split[1],
                        Double.parseDouble(split[4]), split[2]));
            }
            return returnSortedArray;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<String> sortDescPrice() {
        ArrayList<String> newArrayList = new ArrayList<>();
        try {
            File f = new File("detailedProducts.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            ArrayList<Double> doubleArrayList = new ArrayList<>();
            while (line != null) {
                String[] split = line.split(",");
                doubleArrayList.add(Double.valueOf(split[4]));
                line = bfr.readLine();
            }
            double[] doubleArray = new double[doubleArrayList.size()];
            for (int i = 0; i < doubleArrayList.size(); i++) {
                doubleArray[i] = doubleArrayList.get(i);
            }
            Arrays.sort(doubleArray);
            String[] newStringArray = new String[doubleArray.length];
            File newf = new File("detailedProducts.txt");
            FileReader newfr = new FileReader(newf);
            BufferedReader newbfr = new BufferedReader(newfr);
            String newLine = newbfr.readLine();
            int i = 0;
            String[] sortedArray = new String[newStringArray.length];
            while (newLine != null) {
                newStringArray[i] = newLine;
                newLine = newbfr.readLine();
                i++;

            }
            int l = 0;
            for (int j = doubleArray.length; j >= 1; j--) {
                for (int k = 0; k < newStringArray.length; k++) {
                    String[] split = newStringArray[k].split(",");
                    if (doubleArray[j - 1] == Double.parseDouble(split[4]) && !Arrays.toString(sortedArray).contains(newStringArray[k])) {
                        sortedArray[l] = newStringArray[k];

                    }
                }
                l++;
            }
            ArrayList<String> returnSortedArray = new ArrayList<>();
            for (int o = 0; o < sortedArray.length; o++) {
                String[] split = sortedArray[o].split(",");
                returnSortedArray.add(String.format("Item %d: %s, Price: $%.2f, Quantity: %s", o+1, split[1],
                        Double.parseDouble(split[4]), split[2]));}
            return returnSortedArray;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
