package com.angrypirate.models;

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

    public NutritionalInfo(double calories, double fat, double protein, double carbohydrates,
                           double fiber, double sugar, double sodium) {
        this.calories = calories;
        this.fat = fat;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
        this.sugar = sugar;
        this.sodium = sodium;
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
