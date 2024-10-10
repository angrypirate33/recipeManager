package com.angrypirate.services;

import com.angrypirate.models.Ingredient;
import com.angrypirate.models.NutritionalInfo;
import com.angrypirate.services.MongoDBConnection;
import com.angrypirate.models.Recipe;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;

import java.util.ArrayList;
import java.util.List;
import java.lang.Exception;


public class RecipeService {
    private MongoCollection<Recipe> recipeCollection;

    public RecipeService() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        recipeCollection = database.getCollection("recipes", Recipe.class);
    }

    public void addRecipe(Recipe recipe) {
        try {
            Document doc = new Document("title", recipe.getTitle())
                    .append("ingredients", recipe.getIngredients())
                    .append("instructions", recipe.getInstructions())
                    .append("nutritionalInfo", recipe.getNutritionalInfo())
                    .append("sourceUrl", recipe.getSourceUrl());
            recipeCollection.insertOne(recipe);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add recipe: " + e.getMessage());
        }
    }

    public NutritionalInfo calculateRecipeNutrition(Recipe recipe) {
        NutritionalInfo totalNutrition = new NutritionalInfo();

        for (Ingredient ingredient : recipe.getIngredients()) {
            NutritionalInfo ingNutrition = ingredient.getNutritionalInfo();
            if (ingNutrition == null) {
                continue; // Skip if nutritional info is not available
            }
            double quantityFactor = ingredient.getQuantity() / 100.0; // Assuming API data is per 100g

            totalNutrition.setCalories(
                    totalNutrition.getCalories() + ingNutrition.getCalories() * quantityFactor);
            totalNutrition.setFat(
                    totalNutrition.getFat() + ingNutrition.getFat() * quantityFactor);
            totalNutrition.setProtein(
                    totalNutrition.getProtein() + ingNutrition.getProtein() * quantityFactor);
            totalNutrition.setCarbohydrates(
                    totalNutrition.getCarbohydrates() + ingNutrition.getCarbohydrates() * quantityFactor);
            totalNutrition.setFiber(
                    totalNutrition.getFiber() + ingNutrition.getFiber() * quantityFactor);
            totalNutrition.setSugar(
                    totalNutrition.getSugar() + ingNutrition.getSugar() * quantityFactor);
            totalNutrition.setSodium(
                    totalNutrition.getSodium() + ingNutrition.getSodium() * quantityFactor);
            // Add calculations for any additional nutrients as needed
        }

        return totalNutrition;
    }

    public List<Recipe> getAllRecipes() {
        try {
            List<Recipe> recipes = new ArrayList<>();
            FindIterable<Recipe> docs = recipeCollection.find(Recipe.class);
            for (Recipe recipe : docs) {
                recipes.add(recipe);
            }
            return recipes;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load recipes: " + e.getMessage());
        }
    }

    private Recipe convertDocumentToRecipe(Document doc) {
        try {
            Recipe recipe = new Recipe();
            recipe.setId(doc.getObjectId("_id"));
            recipe.setTitle(doc.getString("title"));
            List<Document> ingredientDocs = (List<Document>) doc.get("ingredients");
            List<Ingredient> ingredients = new ArrayList<>();
            for (Document ingredientDoc : ingredientDocs) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(ingredientDoc.getString("name"));
                ingredient.setQuantity(ingredientDoc.getDouble("quantity"));
                ingredient.setUnit(ingredientDoc.getString("unit"));
                ingredients.add(ingredient);
            }
            recipe.setIngredients(ingredients);
            recipe.setInstructions((List<String>) doc.get("instructions"));
            Document nutritionDoc = (Document) doc.get("nutritionalInfo");
            if (nutritionDoc != null) {
                NutritionalInfo nutritionalInfo = new NutritionalInfo();
                nutritionalInfo.setCalories(nutritionDoc.getDouble("calories"));
                nutritionalInfo.setFat(nutritionDoc.getDouble("fat"));
                nutritionalInfo.setProtein(nutritionDoc.getDouble("protein"));
                nutritionalInfo.setCarbohydrates(nutritionDoc.getDouble("carbohydrates"));
                nutritionalInfo.setFiber(nutritionDoc.getDouble("fiber"));
                nutritionalInfo.setSugar(nutritionDoc.getDouble("sugar"));
                nutritionalInfo.setSodium(nutritionDoc.getDouble("sodium"));
                recipe.setNutritionalInfo(nutritionalInfo);
            }
            recipe.setSourceUrl(doc.getString("sourceUrl"));
            return recipe;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert document to recipe: " + e.getMessage());
        }
    }

}
