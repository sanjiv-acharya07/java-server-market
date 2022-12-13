# Project5-CS180
## Instructions
The MainServer class needs to be ran before running the MainClient class. If operating as a Seller, user must login as a returning user with the username "Seller1" and password "password"
### Testing

**Note -** All csv files that are imported by the seller must be in the following format and belong to the bracketed datatypes - store name (String), item name (String), price of item (double), description of item (String), quantity of item (int)

## Submissions
 
 
 ### 1. **Seller**
### Basic Methods
The Seller class defines seller objects. Each seller object has the following attributes:
  - name
  - stores <br />

The Seller class contains a constructor, getter methods, and setter methods corresponding to these attributes.
### Functionalities
1. editProduct
- returns void
- parameters:Shop shop, Product product, String productNewName, String productNewDesc, int productNewQuantity, double productNewPrice<br /> 

The editProduct method allows sellers to edit details of the products in their stores. It contains the parameters corresponding to the shop object, product object,and the updated name, description, quantity, and price of the product. It parses through each seller's stores and their respective products.

2. exportProductFile 
- returns void
- parameters: ArrayList<Seller> sellers<br /> 

 This method prints all product details corresponding to a particular seller into a csv file. The user can export the file by downloading it from the working directory.
 
3. editProductsThroughFile
- returns void
- parameters: ArrayList<Seller> sellers, String filenameInitial, String filenameNew<br /> 

This method is used to update the attributes of the products. It reads the contents of the file with old product details, adds them to an ArrayList, and updates a new file with the a newly created, updated ArrayList.

4. readProductFile
- returns void
- parameters: ArrayList<Seller> sellers, String filename<br /> 
 
This method reads the product details from the files imported by the seller. It also associates the products with their corresponding stores.

5. deleteProductFile
- returns void
- parameters: ArrayList<Seller> sellers, String filename<br /> 

This method allows sellers to delete products using a csv file of the products to be deleted. The deleted products are reflected in their respective store objects.

6. updateDetailedProduct
- returns void
- parameters: ArrayList<Seller> sellers

This method updates the detailed product file with changes made to products and stores.

 
 7. toString
 - returns String
 - no parameters
 This method creates a string to describe the sellers' products
 
 8. salesByStorePrint
 - returns String
 - no parameters
 
 This method reads the indivudual seller's transaction history by each store and    puts them all in a formatted string to send to the client.
 
 ### 2. **Product**
### Basic Methods
The Product class defines product objects. Each product object has the following attributes:
  - name
  - store
  - description
  - quantity
  - price<br />
  The Product class contains a constructor, getter methods, and setter methods corresponding to these attributes.
  ### Functionalities
  1. marketInfo
  - returns String
  - no parameters
  
  This method creates a formatted string to describe the market information.
  
  2. shoppingCartInfo
  - returns String
  - no parameters
  
  This method creates a formatted string to describe the shopping cart information.
  
  3. toString
  - returns String
  - no parameters
  
  This method creates a formatted string to describe each individual product.
  
  ### 3. **Customer**
### Basic Methods
The Customer class defines customer objects. There is a constructor and getters and setters in this class. Each customer object has the following attributes:
  - customer name
  - shopping cart
### Functionalities
  
  1. addToShoppingCart
  - returns void
  - parameters: Seller seller, Product product , int amount
  - exceptions: throws IllegalPurchaseException
  
  This method uses the seller, product, and amount the user wants to purchase to add the certain product into the user's personal shopping cart. If the item is already in the user's shopping cart, the method adds the amount the user is adding to the current item in the shopping cart. The method is used when the user chooses the add to shopping cart option.
  
  2. removeFromShoppingCart
  - returns void
  - parameters: Seller seller, Product product
  - exception: throws notInCartException
  
  This method is the exact opposite of the addToShoppingCart method. When the user chooses to remove an item from their shopping cart, the program calls this method. The seller ArrayList is updated accordingly and the Product ArrayList is also updated accordingly.
  
  3. viewTransactionHistory
  - returns void
  - no parameters
  
  This method reads the user's transaction history file and displays it when the user chooses that option.
  
  4. purchase
  - returns void
  - parameters: ArrayList<Seller> sellers , Product product , int amount
  - exception: throws IllegalPurchaseException
  
  This method checks to see if the item is in stock for the amount that the user wants to purchase.
  
  5. checkFile
  - returns boolean
  - no parameters
  
  This method checks to see if the user already has a shopping cart file in use. If the user does, then the method returns true, but if not the method returns false.
  
  6. replenishCart
  - returns void
  - no parameters
  
  This method is used after the checkFile method runs true, and it replenishes the shopping cart object for the customer.
  
  7. clearCart
  - returns void
  - no parameters
  
  This method is used when the user purchases something and we need to clear the cart. We use a reader method that clears the file and resets the shopping cart object.
  
  8. addUpSalesClearSellerCart
  - returns void
  - parameters ArrayList<Seller> sellers
  
  This method is used when the user purchases items and we need to update the statistics of the seller the user bought the items from. First we copy the shopping cart and write it to the Seller and store's transaction history file. Next we add up the total money acquired from the sale. We then clear the seller's cart to reset the cart.
  
  ### 4. **Shop**
### Basic Methods
The Shop method defines shop objects. Each shop object has the following attributes:
  - name
  - products<br />
 
The Shop class contains a constructor, getter methods, and setter methods corresponding to these attributes.
### Functionalities
1. marketInfo
- returns String
- no parameters

This method returns a string with the store name and product details all in one string.

### 5. **Setup**
The setup method associates all the sellers with their respective stores, and the stores with their respective products. It also reads the product details and store details from their respecitve files and adds them to ArrayLists. 
### Functionalities
 1. loadProducts
 -Returns and ArrayList<Product>
 -no Parameters.
 
 Reads detailedProduct.txt, creates Product object from every line. Returns all created Product objects in an ArrayList<Product>.
 
2. loadStores
- returns ArrayList<Shop>
- no parameters
 
The loadStores method calls loadProducts, recieving an ArrayList<Product>. Iterates through the ArrayList, creating Shop objects and adding Product objects to each shop.
 
 3. loadSellers
 -Returns ArrayList<Seller>
 -no parameters
 
 Calls loadStores, reads from gigaSellers.txt to match the Shop objects returned from the loadStores methods to their respective seller.
 
 4. updateDetailedProduct
 -void
 -ArrayList<Seller>
 
 Itterates through the sellers ArrayList, using the Products toString() method on each Product contained in each shop under each seller, adding the String to an ArrayList, then writing that ArrayList to detailedProduct using ReaderAndWriter's writeFile().
 
 5. specificUpdateDetailedProducts
 -void
 -ArrayList<Seller> , Product , int amount
 
 handles the special case of the customer deciding to purchase only one item but not their shoppingCart. This method asjusts the amount value directly in detailedProducts.txt, changes the Product objects amount, and adds the resulting sales to the Seller.
 
 6. updateGigaSellers
 -void
 -ArrayList<Seller>
 
 updates gigaSellers.txt by iterating through the Seller's in the ArrayList and writing the relevant information to gigaSellers.txt.
 
 ### 6. **ReaderAndWriter**
 Has methods that read and write to files, as well as check if files exist.
### Basic Methods
 
 1. readFile
 -ArrayList<String>
 -String fileName
 
 creates a BufferedReader that converts the file to Strings and stores those Strings in an ArrayList that the method then return.
 
 2. writeFile
 -void
 -String fileName , ArrayList<String> file , boolean append 
 
 Creates a fileOutPutStream using the fileName and append, then uses a PrintWriter object to write the ArrayList<String> to the File.
 
 3. isFile()
 -boolean
 -String fileName
 
 Checks if if the file exists without creating it, returns a boolean.
 
 ### 6. **Exceptions**
 Although technically two classes, I've lumped them in here from brevity
### Classes
 
1. IllegalPurchaseException
 
 Is thrown when a customer tries to purchase a product in amount not availible.
 
2. LineNotFoundException 
 
 Is thrown if the user Input is a blank line.
 
3. NotInCartException 
 
 Is thrown if a product is being removed that is not in a customers cart.
 
 ### 7. **MainClient**
 Communicates with the MainServer by connecting to the Mainserver Socket, receives output from MainServer. Does not process an data.
 
 ### 8. **MainServer**
 Communicates with MainClient, recieves input, process input, then sends output back to the MainClient to be Displayed. Creates a thread for each MainClient that connects to the MainServer.
 
 ### 9. **HandleClient** 
 Implements Runnable, has the run method that is used for threads created in the MainServer.
 
 
 
 





