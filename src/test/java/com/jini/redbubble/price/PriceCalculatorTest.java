package com.jini.redbubble.price;

import com.fasterxml.jackson.databind.JsonNode;
import com.jini.redbubble.index.BasePriceIndex;
import com.jini.redbubble.util.Utils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class PriceCalculatorTest {

    BasePriceIndex basePriceIndex;
    private ByteArrayOutputStream testOut;

    @Before
    public void buildBasePrice() {
        basePriceIndex = new BasePriceIndex();
        basePriceIndex.buildPriceIndex("../red-bubble-code-challenge/src/test/resources/base-prices-2-options.json");
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
    public void testPriceCalculator() {
        JsonNode cartJSON = Utils.readBasePriceFile("../red-bubble-code-challenge/src/test/resources/cart-4560.json");
        PriceCalculator calculator = new PriceCalculator();
        calculator.calculateTotalPrice(basePriceIndex, cartJSON);
        BigDecimal totalCartValue = calculator.getTotalCartValue();
        assertEquals(new BigDecimal("4560"), totalCartValue);
    }

    @Test
    public void testItemTotal() {
        PriceCalculator calculator = new PriceCalculator();
        BigDecimal itemTotal = calculator.getItemTotal(1, new BigDecimal(".2"), "3800");
        assertEquals(new BigDecimal("4560.0"), itemTotal);
    }

    @Test
    public void testInvalidCartQty() {
        final String testString = "The item should have valid quantity greater than zero. Please try again.";
        JsonNode cartJSON = Utils.readBasePriceFile("../red-bubble-code-challenge/src/test/resources/cart-Invalid-Qty.json");
        PriceCalculator calculator = new PriceCalculator();
        calculator.calculateTotalPrice(basePriceIndex, cartJSON);
        assertEquals(testString, getOutput().trim());
    }

    @Test
    public void testInvalidCartArtistMarkup() {
        final String testString = "The item should have valid artist markup greater than zero. Please try again.";
        JsonNode cartJSON = Utils.readBasePriceFile("../red-bubble-code-challenge/src/test/resources/cart-Invalid-ArtistMarkup.json");
        PriceCalculator calculator = new PriceCalculator();
        calculator.calculateTotalPrice(basePriceIndex, cartJSON);
        assertEquals(testString, getOutput().trim());
    }

}