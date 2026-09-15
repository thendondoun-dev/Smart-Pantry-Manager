package com.example.smartpantrymanager;

import java.util.List;
import java.util.Locale;

public class RecipeMatcher {

    /**
     * Checks whether the user has enough of EVERY ingredient
     * required by the recipe.
     *
     * A recipe will only match when:
     * 1. Every required ingredient exists in the pantry.
     * 2. The available quantity is enough.
     * 3. Compatible units can be converted.
     */
    public static boolean canMakeRecipe(
            Recipe recipe,
            List<FoodItem> pantry) {

        if (recipe == null || pantry == null) {
            return false;
        }

        for (RecipeIngredient required : recipe.getIngredients()) {

            double availableQuantity = 0;

            for (FoodItem food : pantry) {

                if (!sameName(
                        food.getName(),
                        required.getName())) {
                    continue;
                }

                double convertedQuantity =
                        convertQuantity(
                                food.getQuantity(),
                                food.getUnit(),
                                required.getUnit()
                        );

                // -1 means the units cannot be converted.
                if (convertedQuantity >= 0) {
                    availableQuantity += convertedQuantity;
                }
            }

            // If even one ingredient is missing or insufficient,
            // the entire recipe cannot be made.
            if (availableQuantity + 0.000001
                    < required.getQuantity()) {

                return false;
            }
        }

        return true;
    }

    /**
     * Compares ingredient names without being case-sensitive.
     */
    private static boolean sameName(
            String first,
            String second) {

        if (first == null || second == null) {
            return false;
        }

        return first.trim()
                .equalsIgnoreCase(second.trim());
    }

    /**
     * Converts compatible measurement units.
     *
     * Supported conversions:
     * g <-> kg
     * ml <-> l
     *
     * If units are already the same, the original
     * quantity is returned.
     *
     * Returns -1 when the units are incompatible.
     */
    private static double convertQuantity(
            double value,
            String fromUnit,
            String toUnit) {

        String from = fromUnit == null
                ? ""
                : fromUnit.trim().toLowerCase(Locale.ROOT);

        String to = toUnit == null
                ? ""
                : toUnit.trim().toLowerCase(Locale.ROOT);

        // Same unit
        if (from.equals(to)) {
            return value;
        }

        // Kilograms -> grams
        if (from.equals("kg")
                && to.equals("g")) {

            return value * 1000.0;
        }

        // Grams -> kilograms
        if (from.equals("g")
                && to.equals("kg")) {

            return value / 1000.0;
        }

        // Litres -> millilitres
        if (from.equals("l")
                && to.equals("ml")) {

            return value * 1000.0;
        }

        // Millilitres -> litres
        if (from.equals("ml")
                && to.equals("l")) {

            return value / 1000.0;
        }

        // Unsupported conversion
        return -1;
    }
}