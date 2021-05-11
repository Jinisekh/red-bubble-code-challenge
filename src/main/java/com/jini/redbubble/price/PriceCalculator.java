package com.jini.redbubble.price;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jini.redbubble.index.BasePriceIndex;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Calculates the total price of the cart based on the formula :
 * (base_price + round(base_price * artist_markup)) * quantity
 */

public class PriceCalculator {

    private static String optionCombination;
    private BigDecimal totalCartValue = BigDecimal.ZERO;
    private Map<String, Map<String, String>> categoryToOptionsPriceMap;
    private Map<String, List<String>> categoryToPriceMap;
    private Map<String, Set<String>> categoryToOptionsMap;
    private ObjectMapper objectMapper = new ObjectMapper();

    public BigDecimal getTotalCartValue() {
        return totalCartValue;
    }

    public void calculateTotalPrice(BasePriceIndex priceIndex, JsonNode cartJSON) {
        try {
            categoryToOptionsPriceMap = priceIndex.getCategoryToOptionsPriceMap();
            categoryToPriceMap = priceIndex.getCategoryToPriceMap();
            categoryToOptionsMap = priceIndex.getCategoryToOptionsMap();

            for (JsonNode item : cartJSON) {
                optionCombination = null;

                if (!item.has("quantity")) {
                    System.out.println("The item should have valid quantity greater than zero. Please try again.");
                    return;
                }

                if (!item.has("artist-markup")) {
                    System.out.println("The item should have valid artist markup greater than zero. Please try again.");
                    return;
                }

                String productType = item.get("product-type").asText();

                if (!categoryToOptionsPriceMap.containsKey(productType) && !categoryToPriceMap.containsKey(productType) && !categoryToOptionsMap.containsKey(productType)) {
                    System.out.println("The item in the cart do not exist in base price JSON. Please update the cart and try again");
                    return;
                }

                Integer artistMarkup = item.get("artist-markup").asInt();
                Integer quantity = item.get("quantity").asInt();
                BigDecimal totalItemPrice = BigDecimal.ZERO;
                BigDecimal artistMarkupPercentage = new BigDecimal(artistMarkup).divide(new BigDecimal("100"));

                //Get item total for items having no options
                if (!categoryToOptionsPriceMap.containsKey(productType) && categoryToPriceMap.containsKey(productType)) {
                    String basePrice = categoryToPriceMap.get(productType).get(0);
                    if (basePrice == null) return;
                    totalItemPrice = getItemTotal(quantity, artistMarkupPercentage, basePrice);
                }

                //Get item total for items having multiple options in base-price JSON
                if (categoryToOptionsMap.containsKey(productType)) {
                    String basePrice = getBaseForItemWithOptions(item, productType);
                    if (basePrice == null) return;
                    totalItemPrice = getItemTotal(quantity, artistMarkupPercentage, basePrice);
                }

                calculateTotalCartValue(totalItemPrice);
            }
        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessingException exception has occurred while calculating the total price. ");
        }
    }

    private void calculateTotalCartValue(BigDecimal totalItemPrice) {
        totalCartValue = totalCartValue.add(totalItemPrice);
        totalCartValue = totalCartValue.setScale(0, BigDecimal.ROUND_DOWN);
    }

    public BigDecimal getItemTotal(Integer quantity, BigDecimal artistMarkupPercentage, String basePrice) {
        BigDecimal totalItemPrice;
        totalItemPrice = new BigDecimal(basePrice);
        totalItemPrice = totalItemPrice.add(totalItemPrice.multiply(artistMarkupPercentage)).multiply(new BigDecimal(quantity));
        return totalItemPrice;
    }

    private String getBaseForItemWithOptions(JsonNode jsonNode, String productType) throws JsonProcessingException {
        Set<String> options = categoryToOptionsMap.get(productType);
        JsonNode optionMapper = objectMapper.readTree(jsonNode.get("options").toString());
        options.forEach(e -> {
            getOptionsCombination(optionMapper.get(e).asText());
        });

        String basePrice = categoryToOptionsPriceMap.get(productType).get(optionCombination);

        if (basePrice == null) {
            System.out.println("Could not find the item in the base price list. Please check the cart and try again");
            return null;
        }
        return basePrice;
    }

    private void getOptionsCombination(String option) {
        if (null == optionCombination) {
            optionCombination = option;
        } else {
            optionCombination = optionCombination + "-" + option;
        }
    }
}
