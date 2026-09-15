package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private TextView tvDetailRecipeName;
    private TextView tvDetailRecipeDescription;
    private TextView tvDetailIngredients;
    private TextView tvDetailInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        databaseHelper = new DatabaseHelper(this);

        tvDetailRecipeName =
                findViewById(R.id.tvDetailRecipeName);

        tvDetailRecipeDescription =
                findViewById(R.id.tvDetailRecipeDescription);

        tvDetailIngredients =
                findViewById(R.id.tvDetailIngredients);

        tvDetailInstructions =
                findViewById(R.id.tvDetailInstructions);

        int recipeId =
                getIntent().getIntExtra("recipe_id", -1);

        if (recipeId == -1) {
            tvDetailRecipeName.setText("Recipe not found");
            tvDetailRecipeDescription.setText(
                    "The selected recipe could not be found."
            );
            tvDetailIngredients.setText("");
            tvDetailInstructions.setText("");
            return;
        }

        loadRecipe(recipeId);
    }

    private void loadRecipe(int recipeId) {

        Recipe recipe =
                databaseHelper.getRecipeById(recipeId);

        if (recipe == null) {

            tvDetailRecipeName.setText("Recipe not found");

            tvDetailRecipeDescription.setText(
                    "The selected recipe could not be found."
            );

            tvDetailIngredients.setText("");
            tvDetailInstructions.setText("");

            return;
        }

        tvDetailRecipeName.setText(
                recipe.getName()
        );

        tvDetailRecipeDescription.setText(
                recipe.getDescription()
        );

        displayIngredients(
                recipe.getIngredients()
        );

        tvDetailInstructions.setText(
                recipe.getInstructions()
        );
    }

    private void displayIngredients(
            List<RecipeIngredient> ingredients) {

        StringBuilder ingredientText =
                new StringBuilder();

        if (ingredients == null ||
                ingredients.isEmpty()) {

            ingredientText.append(
                    "No ingredients listed."
            );

        } else {

            for (RecipeIngredient ingredient : ingredients) {

                ingredientText.append("• ")
                        .append(ingredient.getName())
                        .append(" - ")
                        .append(ingredient.getQuantity())
                        .append(" ")
                        .append(ingredient.getUnit())
                        .append("\n");
            }
        }

        tvDetailIngredients.setText(
                ingredientText.toString()
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}