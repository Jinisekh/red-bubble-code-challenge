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
### Example
```
gradle run  -Parguments="['/Users/name/RedbubbleCodingChallenge/base-prices.json','/Users/name/RedbubbleCodingChallenge/cart-11356.json']"

```

### A sample output will look like this::::

```
----------------------------------
              RESULT
----------------------------------
The total price of the cart is 11356

```
