package com.angrypirate.viewmodels;

import com.angrypirate.models.Recipe;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RecipeViewModel {
    private Recipe recipe;

    private StringProperty title;

    public RecipeViewModel(Recipe recipe) {
        this.recipe = recipe;
        this.title = new SimpleStringProperty(recipe.getTitle());
    }

    public StringProperty titleProperty() {
        return title;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
