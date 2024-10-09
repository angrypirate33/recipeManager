package com.angrypirate.models;

import org.bson.types.ObjectId;
import java.util.List;

public class Recipe {
    private ObjectId id;
    private String title;
    private List<Ingredient> ingredients;
    private List<String> instructions;
//    private NutritionalInfo nutritionalInfo;
    private String sourceUrl;

    // Constructors, getters, and setters
}
