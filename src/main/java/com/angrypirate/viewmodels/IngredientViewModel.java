package com.angrypirate.viewmodels;

import com.angrypirate.models.Ingredient;
import javafx.beans.property.*;

public class IngredientViewModel {
    private Ingredient ingredient;

    private StringProperty fdcId;
    private StringProperty name;
    private DoubleProperty quantity;
    private StringProperty unit;

    public IngredientViewModel(Ingredient ingredient) {
        this.ingredient = ingredient;

        this.fdcId = new SimpleStringProperty(ingredient.getFdcId());
        this.name = new SimpleStringProperty(ingredient.getName());
        this.quantity = new SimpleDoubleProperty(ingredient.getQuantity());
        this.unit = new SimpleStringProperty(ingredient.getUnit());

        // Add listeners to update the underlying Ingredient when properties change
        this.fdcId.addListener((observable, oldValue, newValue) -> ingredient.setFdcId(newValue));
        this.name.addListener((observable, oldValue, newValue) -> ingredient.setName(newValue));
        this.quantity.addListener((observable, oldValue, newValue) -> ingredient.setQuantity(newValue.doubleValue()));
        this.unit.addListener((observable, oldValue, newValue) -> ingredient.setUnit(newValue));
    }

    // Getters for properties

    public StringProperty fdcIdProperty() {
        return fdcId;
    }

    public String getFdcId() {
        return fdcId.get();
    }

    public void setFdcId(String fdcId) {
        this.fdcId.set(fdcId);
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

    public DoubleProperty quantityProperty() {
        return quantity;
    }

    public double getQuantity() {
        return quantity.get();
    }

    public void setQuantity(double quantity) {
        this.quantity.set(quantity);
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public String getUnit() {
        return unit.get();
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }
    // Getter for the underlying Ingredient
    public Ingredient getIngredient() {
        return ingredient;
    }
}
