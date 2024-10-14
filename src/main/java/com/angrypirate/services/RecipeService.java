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

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;


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

    public List<Recipe> searchRecipes(String query) {
        return recipeCollection.find(regex("title", ".*" + query + ".*", "i")).into(new ArrayList<>());
    }

    public void deleteRecipe(ObjectId id) {
        recipeCollection.deleteOne(eq("_id", id));
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
