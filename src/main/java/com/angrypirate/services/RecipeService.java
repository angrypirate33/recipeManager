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

public class RecipeService {
    private MongoCollection<Document> recipeCollection;

    public RecipeService() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        recipeCollection = database.getCollection("recipes");
    }

    public void addRecipe(Recipe recipe) {
        Document doc = new Document("title", recipe.getTitle())
                .append("ingredients", recipe.getIngredients())
                .append("instructions", recipe.getInstructions())
                .append("nutritionalInfo", recipe.getNutritionalInfo())
                .append("sourceUrl", recipe.getSourceUrl());
        recipeCollection.insertOne(doc);
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        FindIterable<Document> docs = recipeCollection.find();
        for (Document doc : docs) {
            Recipe recipe = convertDocumentToRecipe(doc);
            recipes.add(recipe);
        }
        return recipes;
    }

    private Recipe convertDocumentToRecipe(Document doc) {
        Recipe recipe = new Recipe();
        recipe.setId(doc.getObjectId("_id"));
        recipe.setTitle(doc.getString("title"));
        recipe.setIngredients((List<Ingredient>) doc.get("ingredients"));
        recipe.setInstructions((List<String>) doc.get("instructions"));
        recipe.setNutritionalInfo((NutritionalInfo) doc.get("nutritionalInfo"));
        recipe.setSourceUrl(doc.getString("sourceUrl"));
        return recipe;
    }

}
