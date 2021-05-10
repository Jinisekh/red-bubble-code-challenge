package com.jini.redbubble.cart;

import com.jini.redbubble.index.BasePriceIndex;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class CartTest {

    BasePriceIndex basePriceIndex;
    private ByteArrayOutputStream testOut;

    @Before
    public void buildBasePrice() {
        basePriceIndex = new BasePriceIndex();
        basePriceIndex.buildPriceIndex("../RedbubbleCoding/src/test/resources/base-prices-2-options.json");
    }

    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private String getOutput() {
        return testOut.toString();
    }

    @Test
    public void testCalculateTotalCart() {

        BigDecimal cartValue;
        Cart cart = new Cart();

        cart.calculateTotalCart(basePriceIndex, "../RedbubbleCoding/src/test/resources/cart-4560.json");
        cartValue = cart.getTotalCartValue();
        assertEquals(new BigDecimal("4560"), cartValue);

        cart.calculateTotalCart(basePriceIndex, "../RedbubbleCoding/src/test/resources/cart-9363.json");
        cartValue = cart.getTotalCartValue();
        assertEquals(new BigDecimal("9363"), cartValue);

        cart.calculateTotalCart(basePriceIndex, "../RedbubbleCoding/src/test/resources/cart-9500.json");
        cartValue = cart.getTotalCartValue();
        assertEquals(new BigDecimal("9500"), cartValue);

        cart.calculateTotalCart(basePriceIndex, "../RedbubbleCoding/src/test/resources/cart-11356.json");
        cartValue = cart.getTotalCartValue();
        assertEquals(new BigDecimal("11356"), cartValue);
    }

    @Test
    public void testEmptyFile() {
        final String testString = "The cart is empty. Please add items and try again.";
        Cart cart = new Cart();
        cart.calculateTotalCart(basePriceIndex, "../RedbubbleCoding/src/test/resources/cart-empty.json");
        assertEquals(testString, getOutput().trim());
    }

    @Test
    public void testCartInvalidOption() {
        final String testString = "Unable to resolve the item due to invalid options. Please check the item options in the cart and try again !!!";
        Cart cart = new Cart();
        cart.calculateTotalCart(basePriceIndex, "../RedbubbleCoding/src/test/resources/cart-invalid-option.json");
        assertEquals(testString, getOutput().trim());
    }

    @Test
    public void testCartInvalidJson() {
        final String testString = "Oops was unable to read ../RedbubbleCoding/src/test/resources/cart-invalid-json.json. Please make sure the file path and the content are correct.";
        Cart cart = new Cart();
        cart.calculateTotalCart(basePriceIndex, "../RedbubbleCoding/src/test/resources/cart-invalid-json.json");
        assertEquals(testString, getOutput().trim());
    }

}