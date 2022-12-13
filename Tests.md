IMPORTANT: "selects" means User chooses the option in the dropdown menu, AND CLICKS OK.

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


Expected result: User Logs in under the created account and views the customer menu.
Tests: Passed

###Test 3: Log in to Seller1 using the menus
Steps:
1. User Runs MainServer
2. User Runs MainClient
3. User selects the "Yes" button
4. User clicks the textbox and enters the username "Seller1", then clicks "OK"
5. User clicks the textbox and enters the password "password", then clicks "OK"
6. User enters the seller menu and uses the dropdown menu to select "Create Product""
7. User types in the textbox "addProduct.csv" and clicks "OK"
8. User gets back to the main menu and uses the dropdown menu to select "Edit Product"
9. User inputs "oldProducts.txt" into the textbox and clicks ok
10. User inputs "newProducts.txt" into the textbox and clicks ok
11. User returns to the main menu and chooses Delete Product in the dropdown menu
12. User enters "deleteProducts.txt" into textbox and clicks OK
13. User returns to the menu and clicks Export Product Details File in the dropdown menu
14. User clicks ok
15. User clicks cancel
16. User clicks ok

Expected Output: detailedProducts.txt has 3 new products (Watch, keyboard, bottle), cold medicine has been changed to robitussin, and deleted coats from detailed product. New file created  Seller1ProductDetails.txt.
Test: passed

###Test 4: Use the customer and seller functionality
Steps:
1. User Runs MainServer
2. User Runs MainClient
3. User selects the "Yes" button
4. User types in "sanjiv" clicks ok
5. User types in "password" clicks ok
6. User uses the dropdown to select Handle shopping cart
7. User selects Add an item to cart
8. User selects Candy
9. User inputs 3 clicks ok
10. User selects Handle shopping cart
11. User selects Checkout
12. User selects cancel
13. User Runs MainServer
14. User Runs MainClient
15. User selects the "Yes" button
16. User clicks the textbox and enters the username "Seller1", then clicks "OK"
17. User clicks the textbox and enters the password "password", then clicks "OK"
18. User selects view sales
19. User clicks the "X" button
20. User clicks cancel, and then ok

Expected Result: Seller is able to view the sale in the transaction history

###Test 5: Seller views Customer Cart
1. User Runs MainServer
2. User Runs MainClient
3. User selects the "Yes" button
4. User types in "sanjiv" clicks ok
5. User types in "password" clicks ok
6. User uses the dropdown to select Handle shopping cart
7. User selects Add an item to cart
8. User selects Candy
9. User inputs 3 clicks ok
10. User selects cancel
11. User selects ok
12. User Runs MainServer
13. User Runs MainClient
14. User selects the "Yes" button
15. User types in "Seller1" clicks ok
16. User types in "password" clicks ok
17. User selects "View Shopping Carts"
18. sanjiv's Shopping cart is displayed
19. User clicks ok
20. User clicks cancel
21. User clicks ok
22. User clicks ok
23. User Runs MainServer
24. User Runs MainClient
25. User selects the "Yes" button
26. User types in "sanjiv" clicks ok
27. User types in "password" clicks ok
28. User selects Handle Shopping Cart
29. User selects Print the shopping cart
30. User's shopping cart prints
31. User selects Handle Shopping Cart
32. User selects Checkout
33. User selects ok
34. User selects cancel
35. User selects ok
36. User Runs MainServer
37. User Runs MainClient
38. User selects the "Yes" button
39. User types in "Seller1" clicks ok
40. User types in "password" clicks ok
41. User selects View Sales
42. Customer purchases shown in popup
43. User selects ok
44. User selects cancel
45. User selects ok
46. User selects ok

Expected Result: Customer adds item to shopping cart and leaves. Seller1 logs in and views that item in Seller cart, logs out. Customer logs back in, purchases item. Seller1 logs in, views sales, sees item just bought.

Test: passed.
