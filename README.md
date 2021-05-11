# Red-Bubble-Coding-Challenge

## AIM:
## A command line application to calculate total price of the items in cart.json using the formula :::: (base_price + round(base_price * artist_markup)) * quantity
## The application takes 2 input arguments : base-prices.json, cart.json

### PROJECT DETAILS:
This is a Java project built using gradle. To the run the application you must have gradle installed in your system. 

### What you’ll need
+ JDK 8 or later
+ Install Gradle

### Clone project from github

 - Open terminal in any suitable folder
 - run below command 
```
 git clone https://github.com/Jinisekh/red-bubble-code-challenge.git
```

### Install Gradle

  + [Installation guide for gradle](https://gradle.org/install/).

### Build and Run the Project

 - Open terminal on the project folder
```
cd <your-folder-path>/red-bubble-code-challenge/
```
 - run below command 
```
 gradle clean build
```
```
 gradle run  -Parguments="['<Path>-base-prices.json','<Path>-cart.json']"
```
### Example - In Mac
```
gradle run  -Parguments="['/Users/name/RedbubbleCodingChallenge/base-prices.json','/Users/name/RedbubbleCodingChallenge/cart-11356.json']"

```

### A sample output will look like this::::

```
----------------------------------
RESULT :::
----------------------------------
The total price of the cart is 11356

```
### Assumptions made for the application
```
1. There will be 2 arguments passed each time the application is called . 
   Argument[1] -> base-price.json , Argument[2] -> cart.json
2. The items in the cart will have atleast all the options in base-price.json 
   which is necessary to resolve the SKU. For example if base-price.json contains 
   item "hoodie" with 2 options the hoodie item in cart.json should have those 2 options atleast.
3. Base-price JSON is indexed initially to create an in-memory Table to 
   lookup base price at o(1) Time complexity. Therefore the base-price runs 
   only 1 time and take O(nm) time complexity.
4. The total cart value is round down to the nearest value at all times . Eg: 4560.8 will be displayed as 4560.
```
