package com.angrypirate.models;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;

public class Ingredient {
    private ObjectId id;
    private String fdcId;
    private String name;
    private double quantity;
    private String unit;
    private NutritionalInfo nutritionalInfo;
    private LocalDateTime lastUpdated;

    public Ingredient() {
        // No-argument constructor for serialization in MongoDB.
    }

    public Ingredient(String fdcId, String name, double quantity, String unit, NutritionalInfo nutritionalInfo) {
        this.fdcId = fdcId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.nutritionalInfo = nutritionalInfo;
        this.lastUpdated = LocalDateTime.now();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFdcId() {
        return fdcId;
    }

    public void setFdcId(String fdcId) {
        this.fdcId = fdcId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public NutritionalInfo getNutritionalInfo() {
        return nutritionalInfo;
    }

    public void setNutritionalInfo(NutritionalInfo nutritionalInfo) {
        this.nutritionalInfo = nutritionalInfo;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
