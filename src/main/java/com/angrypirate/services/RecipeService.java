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

    public void updateRecipe(Recipe recipe) {
        try {
            recipeCollection.replaceOne(eq("_id", recipe.getId()), recipe);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update recipe: " + e.getMessage());
        }
    }

    public List<Recipe> searchRecipes(String query) {
        return recipeCollection.find(regex("title", ".*" + query + ".*", "i")).into(new ArrayList<>());
    }

    public void deleteRecipe(ObjectId id) {
        recipeCollection.deleteOne(eq("_id", id));
    }
}
