package com.example.smartpantrymanager;

import java.util.List;
import java.util.Locale;

public class RecipeMatcher {

    public static boolean canMakeRecipe(
            Recipe recipe,
            List<FoodItem> pantry
    ) {

        if (recipe == null || pantry == null) {
            return false;
        }

        for (RecipeIngredient required : recipe.getIngredients()) {

            double availableQuantity = 0;

            for (FoodItem food : pantry) {

                if (!sameName(
                        food.getName(),
                        required.getName()
                )) {
                    continue;
                }

                double convertedQuantity = convertQuantity(
                        food.getQuantity(),
                        food.getUnit(),
                        required.getUnit()
                );

                if (convertedQuantity >= 0) {
                    availableQuantity += convertedQuantity;
                }
            }

            if (availableQuantity + 0.000001
                    < required.getQuantity()) {

                return false;
            }
        }

        return true;
    }

    private static boolean sameName(
            String first,
            String second
    ) {

        if (first == null || second == null) {
            return false;
        }

        return first.trim()
                .equalsIgnoreCase(second.trim());
    }

    private static double convertQuantity(
            double quantity,
            String fromUnit,
            String toUnit
    ) {

        String from = fromUnit == null
                ? ""
                : fromUnit.trim().toLowerCase(Locale.ROOT);

        String to = toUnit == null
                ? ""
                : toUnit.trim().toLowerCase(Locale.ROOT);

        if (from.equals(to)) {
            return quantity;
        }

        if (from.equals("kg") && to.equals("g")) {
            return quantity * 1000;
        }

        if (from.equals("g") && to.equals("kg")) {
            return quantity / 1000;
        }

        if (from.equals("l") && to.equals("ml")) {
            return quantity * 1000;
        }

        if (from.equals("ml") && to.equals("l")) {
            return quantity / 1000;
        }

        return -1;
    }
}