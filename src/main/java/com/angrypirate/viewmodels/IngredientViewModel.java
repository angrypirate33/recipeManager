package com.angrypirate.viewmodels;

import com.angrypirate.models.Ingredient;
import com.angrypirate.models.NutritionalInfo;
import javafx.beans.property.*;

public class IngredientViewModel {
    private Ingredient ingredient;

    private StringProperty fdcId;
    private StringProperty name;
    private DoubleProperty calories;
    private DoubleProperty fat;
    private DoubleProperty protein;
    private DoubleProperty carbohydrates;
    private DoubleProperty fiber;
    private DoubleProperty sugar;
    private DoubleProperty sodium;

    public IngredientViewModel(Ingredient ingredient) {
        this.ingredient = ingredient;

        this.name = new SimpleStringProperty(ingredient.getName());
    }

    // Getters for properties

    public StringProperty fdcIdProperty() {
        return fdcId;
    }

    public String getFdcId() {
        return fdcId.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public DoubleProperty caloriesProperty() {
        return calories;
    }

    public double getCalories() {
        return calories.get();
    }

    public void setCalories(double calories) {
        this.calories.set(calories);
    }

    public DoubleProperty fatProperty() {
        return fat;
    }

    public double getFat() {
        return fat.get();
    }

    public void setFat(double fat) {
        this.fat.set(fat);
    }

    public DoubleProperty proteinProperty() {
        return protein;
    }

    public double getProtein() {
        return protein.get();
    }

    public void setProtein(double protein) {
        this.protein.set(protein);
    }

    public DoubleProperty carbohydratesProperty() {
        return carbohydrates;
    }

    public double getCarbohydrates() {
        return carbohydrates.get();
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates.set(carbohydrates);
    }

    public DoubleProperty fiberProperty() {
        return fiber;
    }

    public double getFiber() {
        return fiber.get();
    }

    public void setFiber(double fiber) {
        this.fiber.set(fiber);
    }

    public DoubleProperty sugarProperty() {
        return sugar;
    }

    public double getSugar() {
        return sugar.get();
    }

    public void setSugar(double sugar) {
        this.sugar.set(sugar);
    }

    public DoubleProperty sodiumProperty() {
        return sodium;
    }

    public double getSodium() {
        return sodium.get();
    }

    public void setSodium(double sodium) {
        this.sodium.set(sodium);
    }

    // Getter for the underlying Ingredient
    public Ingredient getIngredient() {
        return ingredient;
    }
}
