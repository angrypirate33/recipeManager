package com.angrypirate.services;

import com.angrypirate.models.Ingredient;
import com.angrypirate.models.NutritionalInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodDataApiService {
    private static final String API_KEY = System.getenv("FOOD_API_KEY");
    private static final String API_BASE_URL = "https://api.nal.usda.gov/fdc/v1/";

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FoodDataApiService() {
        httpClient = new OkHttpClient();
        objectMapper = new ObjectMapper();
    }

    public NutritionalInfo getNutritionalInfo(String fdcId) throws IOException {
        String url = API_BASE_URL + "food/" + fdcId + "?api_key=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            assert response.body() != null;
            String responseBody = response.body().string();
            Map<String, Object> apiResponse = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});

            Map<String, Double> nutrients = parseNutrients(apiResponse);

            NutritionalInfo nutritionalInfo = new NutritionalInfo();
            nutritionalInfo.populateFromApiData(nutrients);

            return nutritionalInfo;
        }
    }
    public Ingredient getIngredient(String fdcId, double quantity, String unit) throws IOException {
        String url = API_BASE_URL + "food/" + fdcId + "?api_key=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            assert response.body() != null;
            String responseBody = response.body().string();

            // Parse the response body into a Map
            Map<String, Object> apiResponse = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});

            // Extract the ingredient name
            String name = (String) apiResponse.get("description");

            // Extract nutrient data
            Map<String, Double> nutrients = parseNutrients(apiResponse);

            // Create and populate NutritionalInfo object
            NutritionalInfo nutritionalInfo = new NutritionalInfo();
            nutritionalInfo.populateFromApiData(nutrients);

            // Create Ingredient object
            Ingredient ingredient = new Ingredient();
            ingredient.setFdcId(fdcId);
            ingredient.setName(name);
            ingredient.setQuantity(quantity);
            ingredient.setUnit(unit);
            ingredient.setNutritionalInfo(nutritionalInfo);
            ingredient.setLastUpdated(LocalDateTime.now());

            return ingredient;
        }
    }
    private Map<String, Double> parseNutrients(Map<String, Object> apiResponse) {
        Map<String, Double> nutrientsMap = new HashMap<>();

        // The structure of the response may vary; adjust accordingly
        List<Map<String, Object>> foodNutrients = (List<Map<String, Object>>) apiResponse.get("foodNutrients");

        for (Map<String, Object> nutrientInfo : foodNutrients) {
            Map<String, Object> nutrient = (Map<String, Object>) nutrientInfo.get("nutrient");
            String nutrientName = (String) nutrient.get("name");
            Double amount = nutrientInfo.get("amount") != null ? ((Number) nutrientInfo.get("amount")).doubleValue() : 0.0;

            nutrientsMap.put(nutrientName, amount);
        }

        return nutrientsMap;
    }

}
