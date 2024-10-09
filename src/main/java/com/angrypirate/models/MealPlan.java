package com.angrypirate.models;

import org.bson.types.ObjectId;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class MealPlan {
    private ObjectId id;
    private String name;
    private Map<LocalDate, List<Recipe>> dailyRecipes;
}
