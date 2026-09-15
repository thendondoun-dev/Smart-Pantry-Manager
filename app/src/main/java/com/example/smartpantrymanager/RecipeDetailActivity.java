package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity
        extends AppCompatActivity {

    private TextView tvDetailRecipeName;
    private TextView tvDetailRecipeDescription;
    private TextView tvDetailIngredients;
    private TextView tvDetailInstructions;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_recipe_detail
        );

        tvDetailRecipeName =
                findViewById(
                        R.id.tvDetailRecipeName
                );

        tvDetailRecipeDescription =
                findViewById(
                        R.id.tvDetailRecipeDescription
                );

        tvDetailIngredients =
                findViewById(
                        R.id.tvDetailIngredients
                );

        tvDetailInstructions =
                findViewById(
                        R.id.tvDetailInstructions
                );

        databaseHelper =
                new DatabaseHelper(this);

        int recipeId =
                getIntent().getIntExtra(
                        "recipe_id",
                        -1
                );

        if (recipeId == -1) {

            Toast.makeText(
                    this,
                    "Recipe not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        loadRecipe(recipeId);
    }

    private void loadRecipe(int recipeId) {

        Recipe recipe =
                databaseHelper.getRecipe(recipeId);

        if (recipe == null) {

            Toast.makeText(
                    this,
                    "Recipe not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        tvDetailRecipeName.setText(
                recipe.getName()
        );

        tvDetailRecipeDescription.setText(
                recipe.getDescription()
        );

        StringBuilder ingredients =
                new StringBuilder();

        for (RecipeIngredient ingredient :
                recipe.getIngredients()) {

            ingredients
                    .append("• ")
                    .append(ingredient.getName())
                    .append(" - ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append("\n");
        }

        tvDetailIngredients.setText(
                ingredients.toString().trim()
        );

        tvDetailInstructions.setText(
                recipe.getInstructions()
        );
    }
}