package com.example.smartpantrymanager;

import java.util.List;

public class Recipe {

    private final int id;
    private final String name;
    private final String description;
    private final List<RecipeIngredient> ingredients;
    private final String instructions;

    public Recipe(
            int id,
            String name,
            String description,
            List<RecipeIngredient> ingredients,
            String instructions
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }
}