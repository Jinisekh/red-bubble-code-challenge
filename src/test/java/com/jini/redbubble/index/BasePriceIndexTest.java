package com.jini.redbubble.index;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class BasePriceIndexTest {

    private ByteArrayOutputStream testOut;

    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private String getOutput() {
        return testOut.toString();
    }

    @Test
    public void testCategoryToOptionsMap() {
        BasePriceIndex basePriceIndex = new BasePriceIndex();
        basePriceIndex.buildPriceIndex("../RedbubbleCoding/src/test/resources/base-prices-3-options.json");
        Map<String, Set<String>> categoryToOptionsMap = basePriceIndex.getCategoryToOptionsMap();
        assertEquals("[colour, print, size]", categoryToOptionsMap.get("hoodie").toString());
    }

    @Test
    public void testCategoryToPriceMap() {
        BasePriceIndex basePriceIndex = new BasePriceIndex();
        basePriceIndex.buildPriceIndex("../RedbubbleCoding/src/test/resources/base-prices-3-options.json");
        Map<String, List<String>> categoryToPriceMap = basePriceIndex.getCategoryToPriceMap();
        assertEquals("[3800, 3848, 4108, 4212, 4368]", categoryToPriceMap.get("hoodie").toString());
    }

    @Test
    public void testCategoryToOptionsPriceMap() {
        Map<String, Map<String, String>> categoryToOptionsPriceMap;
        BasePriceIndex basePriceIndex;
        basePriceIndex = new BasePriceIndex();
        basePriceIndex.buildPriceIndex("../RedbubbleCoding/src/test/resources/base-prices-3-options.json");
        categoryToOptionsPriceMap = basePriceIndex.getCategoryToOptionsPriceMap();
        assertEquals("{white-dots-large=3848, white-dots-2xl=4108, dark-dots-xl=4368, white-floral-small=3800, dark-dots-large=4212, white-floral-medium=3800, dark-floral-small=3800, white-dots-xl=4108, dark-dots-2xl=4368, dark-floral-medium=3800, white-dots-3xl=4108, dark-dots-3xl=4368}", categoryToOptionsPriceMap.get("hoodie").toString());
        assertEquals("3800", categoryToOptionsPriceMap.get("hoodie").get("dark-floral-medium"));
        assertEquals("3848", categoryToOptionsPriceMap.get("hoodie").get("white-dots-large"));

        basePriceIndex = new BasePriceIndex();
        basePriceIndex.buildPriceIndex("../RedbubbleCoding/src/test/resources/base-prices-2-options.json");
        categoryToOptionsPriceMap = basePriceIndex.getCategoryToOptionsPriceMap();
        assertEquals("{dark-small=3800, white-medium=3800, dark-3xl=4368, white-small=3800, dark-xl=4368, white-large=3848, white-xl=4108, dark-medium=3800, white-2xl=4108, dark-large=4212, dark-2xl=4368, white-3xl=4108}", categoryToOptionsPriceMap.get("hoodie").toString());
        assertEquals("3800", categoryToOptionsPriceMap.get("hoodie").get("dark-medium"));
        assertEquals("3848", categoryToOptionsPriceMap.get("hoodie").get("white-large"));
    }

    @Test
    public void testEmptyFile() {
        final String testString = "Looks like base price JSON is empty. Please check base price JSON file provided.";
        BasePriceIndex basePriceIndex = new BasePriceIndex();
        basePriceIndex.buildPriceIndex("../RedbubbleCoding/src/test/resources/base-prices-empty.json");
        assertEquals(testString, getOutput().trim());
    }

    @Test
    public void testBasePriceInvalidFile() {
        final String testString = "Oops was unable to read ../RedbubbleCoding/src/test/resources/base-prices-invalid.json. Please make sure the file path and the content are correct.";
        BasePriceIndex basePriceIndex = new BasePriceIndex();
        basePriceIndex.buildPriceIndex("../RedbubbleCoding/src/test/resources/base-prices-invalid.json");
        assertEquals(testString, getOutput().trim());
    }

}