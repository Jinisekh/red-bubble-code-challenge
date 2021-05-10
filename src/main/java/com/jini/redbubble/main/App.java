package com.jini.redbubble.main;

import com.jini.redbubble.cart.Cart;
import com.jini.redbubble.index.BasePriceIndex;

import java.math.BigDecimal;

public class App {

    private static BasePriceIndex priceIndex = new BasePriceIndex();
    private static BigDecimal totalCartValue = BigDecimal.ZERO;
    private static Cart cart = new Cart();

    public static void main(String[] args) {
        String basePricePath = args[0];
        String cartPath = args[1];
        invokeBasePriceIndex(basePricePath);
        calculateCartTotal(cartPath);
        displayResult();
    }

    private static void invokeBasePriceIndex(String basePricePath) {
        priceIndex.buildPriceIndex(basePricePath);
    }

    private static void calculateCartTotal(String cartPath) {
            cart.calculateTotalCart(priceIndex,cartPath);
            totalCartValue = cart.getTotalCartValue();
    }

    private static void displayResult() {
        if(!totalCartValue.equals(BigDecimal.ZERO)) {
            System.out.println("\n");
            System.out.println("----------------------------------");
            System.out.println("\t\t\tRESULT");
            System.out.println("----------------------------------");
            System.out.println("The total price of the cart is " + totalCartValue + "\n");
        }
    }

}


