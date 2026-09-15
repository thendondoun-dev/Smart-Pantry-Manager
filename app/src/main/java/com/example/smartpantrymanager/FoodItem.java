package com.example.smartpantrymanager;

public class FoodItem {

    private final int id;
    private final String name;
    private final double quantity;
    private final String unit;
    private final String category;
    private final String expiryDate;

    public FoodItem(
            int id,
            String name,
            double quantity,
            String unit,
            String category,
            String expiryDate
    ) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.category = category;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getCategory() {
        return category;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}