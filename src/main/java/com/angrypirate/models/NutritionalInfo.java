package com.angrypirate.models;

import java.util.Map;

public class NutritionalInfo {
    private double calories;
    private double fat;
    private double protein;
    private double carbohydrates;
    private double fiber;
    private double sugar;
    private double sodium; // only field in milligrams

    public NutritionalInfo() {
        // No-argument constructor for serialization in MongoDB.
    }

    public void populateFromApiData(Map<String, Double> nutrients) {
        this.calories = nutrients.getOrDefault("Energy", 0.0);
        this.fat = nutrients.getOrDefault("Total lipid (fat)", 0.0);
        this.protein = nutrients.getOrDefault("Protein", 0.0);
        this.carbohydrates = nutrients.getOrDefault("Carbohydrate, by difference", 0.0);
        this.fiber = nutrients.getOrDefault("Fiber, total dietary", 0.0);
        this.sugar = nutrients.getOrDefault("Sugars, total including NLEA", 0.0);
        this.sodium = nutrients.getOrDefault("Sodium, Na", 0.0);
        // Map additional nutrients as needed
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

}
