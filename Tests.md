###Test 1: Create a new account
Steps:
1. User Runs MainServer
2. User Runs MainClient
3. User selects the "No" button
4. User types in username "username" and selects OK button
5. User types in password "password" and selects OK button
6. User uses the dropdown menu to select the Customer option and clicks OK
7. User clicks the "No" button to end the program

Expected result: Application create the user's account and stores the data.
Test Status: Passed

###Test 2: Log in to Created Account
Steps:
1. User Runs MainServer
2. User Runs MainClient
3. User selects the "Yes" button
4. User clicks the textbox and enters the username "username", then clicks "OK"
5. User clicks the textbox and enters the password "password", then clicks "OK"
6. User is taken to the customer menu and clicks the "Cancel" button
7. User clicks the "Ok" button to end the program <br />
**8. User clicks the "No" button.**

Expected result: User Logs in under the created account and views the customer menu.
Tests: Passed

###Test 3: Log in to Seller1 using the menus
Steps:
1. User Runs MainServer
2. User Runs MainClient
3. User selects the "Yes" button
4. User clicks the textbox and enters the username "Seller1", then clicks "OK"
5. User clicks the textbox and enters the password "password", then clicks "OK"
6. User enters the seller menu and uses the dropdown menu to select "Create Product" and then clicks "OK"
7. User types in the textbox "addProduct.csv" and clicks "OK"
8. User gets back to the main menu and uses the dropdown menu to select "Edit Product" then clicks OK.
9. User inputs "oldProducts.txt" into the textbox and clicks ok
10. User inputs "newProducts.txt" into the textbox and clicks ok
11. User returns to the main menu and chooses Delete Product in the dropdown menu and clicks OK
12. User enters "deleteProducts.txt" into textbox and clicks OK
13. User returns to the menu and clicks Export Product Details File in the dropdown menu and click ok
14. User clicks ok
15. User clicks cancel
16. User clicks ok
17. User clicks no
Expected Output: detailedProducts.txt has 3 new products (Watch, keyboard, bottle), cold medicine has been changed to robitussin, and deleted coats from detailed product. New file created  Seller1ProductDetails.txt.
Test: passed

###Test 4: Use the customer and seller functionality
Steps:
1. User Runs MainServer
2. User Runs MainClient
3. User selects the "Yes" button
4. User types in "sanjiv"
5. User types in "password"
6. User uses the dropdown to select Handle shopping cart
7. User selects Add an item to cart
8. User selects Candy
9. User inputs 3
10. User selects Handle shopping cart
11. User selects Checkout
12. User selects cancel
13.User Runs MainServer
14. User Runs MainClient
15. User selects the "Yes" button
16. User clicks the textbox and enters the username "Seller1", then clicks "OK"
17. User clicks the textbox and enters the password "password", then clicks "OK"
18. User selects view sales
19. User clicks the "X" button
20. User clicks cancel, and then ok
21.
