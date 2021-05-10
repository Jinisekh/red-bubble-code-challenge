package com.jini.redbubble.cart;

import com.fasterxml.jackson.databind.JsonNode;
import com.jini.redbubble.index.BasePriceIndex;
import com.jini.redbubble.price.PriceCalculator;
import com.jini.redbubble.util.Utils;

import java.math.BigDecimal;

public class Cart {

    private BigDecimal totalCartValue = BigDecimal.ZERO;

    public BigDecimal getTotalCartValue() {
        return totalCartValue;
    }

    public void calculateTotalCart(BasePriceIndex priceIndex, String cartPath) {
        try {
            JsonNode cartJSON = Utils.readBasePriceFile(cartPath);

            if (null == cartJSON) {
                return;
            }

            if (cartJSON.isEmpty()) {
                System.out.println("The cart is empty. Please add items and try again.");
                return;
            }

            if (null != cartJSON) {
                PriceCalculator calculator = new PriceCalculator();
                calculator.calculateTotalPrice(priceIndex, cartJSON);
                totalCartValue = calculator.getTotalCartValue();
            }
        } catch (Exception e) {
            System.out.println("Unable to resolve the item due to invalid options. Please check the item options in the cart and try again !!!");
            return;
        }
    }

}
