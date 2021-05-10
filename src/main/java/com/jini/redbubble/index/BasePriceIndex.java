package com.jini.redbubble.index;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jini.redbubble.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Reads Base Price JSON and performs an index operation in order to search base price
 * at O(1) time complexity.
 * This class creates 3 main Tables required for base price search
 * 1. categoryToOptionsPriceMap eg: {"hoodie" = {"white-small"=[3800],"dark-small"=[3800]}}
 * 2. categoryToOptionsMap eg: {"hoodie" = [colour,size], "sticker" = [size]}
 * 3. categoryToPriceMap eg: {"hoodie" = [3800,4310,5000], "leggings"=[5000]}
 */
public class BasePriceIndex {
    private Map<String, Map<String, String>> categoryToOptionsPriceMap = new HashMap<>();
    private Map<String, List<String>> categoryToPriceMap = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Set<String>> categoryToOptionsMap = new HashMap<>();
    private Set<String> optionList = new HashSet<>();
    private List<String> finalList = new ArrayList<>();
    private List<String> priceList = new ArrayList<>();

    public Map<String, Map<String, String>> getCategoryToOptionsPriceMap() {
        return categoryToOptionsPriceMap;
    }

    public Map<String, List<String>> getCategoryToPriceMap() {
        return categoryToPriceMap;
    }

    public Map<String, Set<String>> getCategoryToOptionsMap() {
        return categoryToOptionsMap;
    }

    public void buildPriceIndex(String basePricePath) {
        try {
            JsonNode basePriceJson = Utils.readBasePriceFile(basePricePath);

            if (null == basePriceJson) {
                return;
            }

            if (basePriceJson.isEmpty()) {
                System.out.println("Looks like base price JSON is empty. Please check base price JSON file provided.");
                return;
            }

            for (JsonNode itemBasePriceJson : basePriceJson) {
                finalList = new ArrayList<>();
                String productType = itemBasePriceJson.get("product-type").asText();
                String basePrice = itemBasePriceJson.get("base-price").asText();

                //Builds a table of product-type to options. Eg: hoodie=[colour,size] , sticker=[size]
                buildProductTypeToOptions(itemBasePriceJson, productType);

                //Builds a list of all possible options combination eg: if option list = [colour,size], then combination = [white-small,dark-small,white-xl] etc
                buildOptionsCombination(itemBasePriceJson, productType);

                //Maps category to possible option combination and price.
                finalList.forEach(e -> {
                    createCategoryToOptionPrice(e, basePrice, productType);
                });

                //Creates a table of category and possible base prices.
                buildCategoryToPriceTable(itemBasePriceJson);
            }

        } catch (IOException e) {
            System.out.println("Oops there was an error while reading base-price JSON. Please check the path and content of the file and try again!!!");
        }
    }

    private JsonNode readBasePriceFile(String basePricePath) throws IOException {
        File file = new File(basePricePath);
        return objectMapper.readTree(file);
    }

    private void buildCategoryToPriceTable(JsonNode itemBasePricejson) {
        if (!categoryToPriceMap.containsKey(itemBasePricejson.get("product-type").asText())) {
            priceList = new ArrayList<>();
        }
        priceList.add(itemBasePricejson.get("base-price").asText());
        categoryToPriceMap.put(itemBasePricejson.get("product-type").asText(), priceList);
    }

    private void buildOptionsCombination(JsonNode json, String productType) throws JsonProcessingException {
        JsonNode optionNode = objectMapper.readTree(json.get("options").toString());
        if (categoryToOptionsMap.containsKey(productType)) {
            Set<String> optionSet = categoryToOptionsMap.get(productType);
            optionSet.forEach(e -> {
                try {
                    List<String> optionsList = objectMapper.readValue(optionNode.get(e).toString(), new TypeReference<List<String>>() {
                    });
                    createOptionsCombination(optionsList);
                } catch (JsonProcessingException ex) {
                    System.out.println("JsonProcessingException has occurred while indexing base-price JSON. Please recheck the format of optionList in base-price JSON !!!");
                }
            });
        }
    }

    private void buildProductTypeToOptions(JsonNode json, String productType) throws JsonProcessingException {
        JsonNode optionNode = objectMapper.readTree(json.get("options").toString());
        optionNode.fields().forEachRemaining(e -> {
            addCategoryToOptionMap(e.getKey(), productType);
        });
    }

    private void createCategoryToOptionPrice(String optionList, String basePrice, String productType) {
        Map<String, String> optionsToPriceMap;
        if (categoryToOptionsPriceMap.containsKey(productType)) {
            optionsToPriceMap = categoryToOptionsPriceMap.get(productType);
        } else {
            optionsToPriceMap = new HashMap<>();
        }
        optionsToPriceMap.put(optionList, basePrice);
        categoryToOptionsPriceMap.put(productType, optionsToPriceMap);
    }

    private void addCategoryToOptionMap(String option, String category) {
        if (!categoryToOptionsMap.containsKey(category)) {
            optionList = new HashSet<>();
        }
        optionList.add(option);
        categoryToOptionsMap.put(category, optionList);
    }

    private void createOptionsCombination(List<String> toTestList) {
        if (finalList.isEmpty()) {
            finalList = toTestList;
        } else {
            finalList = finalList.stream()
                    .flatMap(s1 -> toTestList.stream().map(s2 -> s1 + "-" + s2))
                    .collect(Collectors.toList());
        }
    }
}
