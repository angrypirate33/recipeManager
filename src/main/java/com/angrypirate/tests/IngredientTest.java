//package com.angrypirate.tests;
//
//import com.angrypirate.models.Ingredient;
//import com.angrypirate.models.NutritionalInfo;
//import com.angrypirate.services.FoodDataApiService;
//import com.angrypirate.services.IngredientService;
//import java.io.IOException;
//import java.time.LocalDateTime;
//
//public class IngredientTest {
//    public static void main(String[] args) {
//        FoodDataApiService apiService = new FoodDataApiService();
//        IngredientService ingredientService = new IngredientService();
//
//        String testFdcId = "2512379";
//
//        try {
//            // Define quantity and unit
//            double quantity = 100; // Example quantity in grams
//            String unit = "g";     // Unit of measurement
//
//            // Fetch Ingredient from API
//            Ingredient ingredient = apiService.getIngredient(testFdcId, quantity, unit);
//
//            // Save to MongoDB
//            ingredientService.addIngredient(ingredient);
//
//            // Retrieve from MongoDB
//            Ingredient retrievedIngredient = ingredientService.getIngredientById(ingredient.getId());
//
//            // Display the retrieved data
//            System.out.println("Ingredient Name: " + retrievedIngredient.getName());
//            System.out.println("Calories: " + retrievedIngredient.getNutritionalInfo().getCalories());
//            System.out.println("Fat: " + retrievedIngredient.getNutritionalInfo().getFat());
//            System.out.println("Protein: " + retrievedIngredient.getNutritionalInfo().getProtein());
//            System.out.println("Carbs: " + retrievedIngredient.getNutritionalInfo().getCarbohydrates());
//            System.out.println("Fiber: " + retrievedIngredient.getNutritionalInfo().getFiber());
//            System.out.println("Sugar: " + retrievedIngredient.getNutritionalInfo().getSugar());
//            System.out.println("Sodium: " + retrievedIngredient.getNutritionalInfo().getSodium());
//
//            // Display other fields as needed
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
