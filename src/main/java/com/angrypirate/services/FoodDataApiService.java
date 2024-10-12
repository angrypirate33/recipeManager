package com.angrypirate.services;

import com.angrypirate.models.Ingredient;
import com.angrypirate.models.NutritionalInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public List<Ingredient> searchIngredients(String query) throws IOException {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = API_BASE_URL + "foods/search?api_key=" + API_KEY + "&query=" + encodedQuery;

        Request request = new Request.Builder()
                .url(url)
                .build();

        List<Ingredient> ingredients = new ArrayList<>();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code " + response);
            }
            assert response.body() != null;
            String responseBody = response.body().string();

            Map<String, Object> apiResponse = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
            List<Map<String, Object>> foods = (List<Map<String, Object>>) apiResponse.get("foods");

            if (foods != null && !foods.isEmpty()) {
                // Extract fdcIds
                List<Integer> fdcIds = new ArrayList<>();
                for (Map<String, Object> food : foods) {
                    Integer fdcId = (Integer) food.get("fdcId");
                    if (fdcId != null) {
                        fdcIds.add(fdcId);
                    }
                }
                // Fetch detailed nutrient data for these fdcIds
                ingredients = fetchIngredientsWithDetails(fdcIds);
            }
        }

        return ingredients;
    }

    private List<Ingredient> fetchIngredientsWithDetails(List<Integer> fdcIds) throws IOException {
        String url = API_BASE_URL + "foods?api_key=" + API_KEY;

        // Build the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fdcIds", fdcIds);
        requestBody.put("format", "full");
        requestBody.put("nutrients", List.of(208, 204, 203, 205, 291, 269, 307));

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code " + response);
            }
            assert response.body() != null;
            String responseBody = response.body().string();

            List<Map<String, Object>> foods = objectMapper.readValue(responseBody, new TypeReference<List<Map<String, Object>>>() {});
            List<Ingredient> ingredients = new ArrayList<>();
            for (Map<String, Object> food : foods) {
                Ingredient ingredient = parseFoodDetails(food);
                ingredients.add(ingredient);
            }
            return ingredients;
        }
    }

    private Ingredient parseFoodDetails(Map<String, Object> food) {
        Ingredient ingredient = new Ingredient();
        ingredient.setFdcId(String.valueOf(food.get("fdcId")));
        ingredient.setName((String) food.get("description"));

        // Parse nutrients
        List<Map<String, Object>> foodNutrients = (List<Map<String, Object>>) food.get("foodNutrients");
        NutritionalInfo nutritionalInfo = new NutritionalInfo();

        if (foodNutrients != null) {
            for (Map<String, Object> nutrientInfo : foodNutrients) {
                Map<String, Object> nutrient = (Map<String, Object>) nutrientInfo.get("nutrient");
                String nutrientNumber = (String) nutrient.get("number");
                Double amount = nutrientInfo.get("amount") != null ? ((Number) nutrientInfo.get("amount")).doubleValue() : null;

                // Debugging print statements
                System.out.println("Nutrient Info: " + nutrientInfo);
                System.out.println("Nutrient ID: " + nutrientNumber);

                if (nutrientNumber != null && amount != null) {
                    switch (nutrientNumber) {
                        case "208": // Calories
                            nutritionalInfo.setCalories(amount);
                            break;
                        case "204": // Fat
                            nutritionalInfo.setFat(amount);
                            break;
                        case "203": // Protein
                            nutritionalInfo.setProtein(amount);
                            break;
                        case "205": // Carbohydrates
                            nutritionalInfo.setCarbohydrates(amount);
                            break;
                        case "291": // Fiber
                            nutritionalInfo.setFiber(amount);
                            break;
                        case "269": // Sugar
                            nutritionalInfo.setSugar(amount);
                            break;
                        case "307": // Sodium
                            nutritionalInfo.setSodium(amount);
                            break;
                        default:
                            // Ignore other nutrients
                            break;
                    }
                }
            }
        }

        ingredient.setNutritionalInfo(nutritionalInfo);
        return ingredient;
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
