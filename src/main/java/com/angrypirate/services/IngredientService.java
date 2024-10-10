package com.angrypirate.services;

import com.angrypirate.services.MongoDBConnection;
import com.angrypirate.models.Ingredient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.eq;

public class IngredientService {
    private MongoCollection<Ingredient> ingredientCollection;

    public IngredientService() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        ingredientCollection = database.getCollection("ingredients", Ingredient.class);
    }

    public void addIngredient(Ingredient ingredient) {
        ingredientCollection.insertOne(ingredient);
    }

    public Ingredient getIngredientById(ObjectId id) {
        return ingredientCollection.find(eq("_id", id)).first();
    }

    // Other methods as needed
}
