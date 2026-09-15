package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity
        extends AppCompatActivity {

    private RecyclerView recyclerRecipes;
    private TextView tvNoRecipes;

    private DatabaseHelper databaseHelper;

    private final List<Recipe> suggestedRecipes =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_suggested_recipes
        );

        recyclerRecipes =
                findViewById(
                        R.id.recyclerRecipes
                );

        tvNoRecipes =
                findViewById(
                        R.id.tvNoRecipes
                );

        databaseHelper =
                new DatabaseHelper(this);

        recyclerRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );
    }

    @Override
    protected void onResume() {

        super.onResume();

        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {

        suggestedRecipes.clear();

        List<FoodItem> pantryItems =
                loadPantryItems();

        List<Recipe> allRecipes =
                databaseHelper.getAllRecipes();

        for (Recipe recipe : allRecipes) {

            if (RecipeMatcher.canMakeRecipe(
                    recipe,
                    pantryItems
            )) {

                suggestedRecipes.add(recipe);
            }
        }

        RecipeAdapter adapter =
                new RecipeAdapter(
                        this,
                        suggestedRecipes
                );

        recyclerRecipes.setAdapter(adapter);

        if (suggestedRecipes.isEmpty()) {

            tvNoRecipes.setVisibility(
                    TextView.VISIBLE
            );

            recyclerRecipes.setVisibility(
                    RecyclerView.GONE
            );

        } else {

            tvNoRecipes.setVisibility(
                    TextView.GONE
            );

            recyclerRecipes.setVisibility(
                    RecyclerView.VISIBLE
            );
        }
    }

    private List<FoodItem> loadPantryItems() {

        List<FoodItem> pantryItems =
                new ArrayList<>();

        Cursor cursor =
                databaseHelper.getAllFoodItems();

        if (cursor != null) {

            while (cursor.moveToNext()) {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_ID
                                )
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_NAME
                                )
                        );

                double quantity =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_QUANTITY
                                )
                        );

                String unit =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_UNIT
                                )
                        );

                String category =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_CATEGORY
                                )
                        );

                String expiryDate =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_EXPIRY_DATE
                                )
                        );

                pantryItems.add(
                        new FoodItem(
                                id,
                                name,
                                quantity,
                                unit,
                                category,
                                expiryDate
                        )
                );
            }

            cursor.close();
        }

        return pantryItems;
    }
}