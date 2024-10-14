package com.angrypirate.models;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;
import javafx.beans.property.*;

public class Ingredient {
    private StringProperty name;
    private DoubleProperty quantity;
    private StringProperty unit;

    public Ingredient() {
        this.name = new SimpleStringProperty();
        this.quantity = new SimpleDoubleProperty();
        this.unit = new SimpleStringProperty();
    }

    public Ingredient(String name, double quantity, String unit) {
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleDoubleProperty(quantity);
        this.unit = new SimpleStringProperty(unit);
    }

    // Name property
    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    // Quantity property
    public DoubleProperty quantityProperty() {
        return quantity;
    }

    public double getQuantity() {
        return quantity.get();
    }

    public void setQuantity(double quantity) {
        this.quantity.set(quantity);
    }

    // Unit property
    public StringProperty unitProperty() {
        return unit;
    }

    public String getUnit() {
        return unit.get();
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }
}
