package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PantryActivity extends AppCompatActivity {

    private RecyclerView recyclerPantry;
    private TextView tvEmptyPantry;
    private EditText etSearchPantry;

    private DatabaseHelper databaseHelper;
    private FoodAdapter foodAdapter;

    private final List<FoodItem> foodItems =
            new ArrayList<>();

    private final List<FoodItem> allFoodItems =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pantry);

        Button btnAddFood =
                findViewById(R.id.btnAddFood);

        recyclerPantry =
                findViewById(R.id.recyclerPantry);

        tvEmptyPantry =
                findViewById(R.id.tvEmptyPantry);

        etSearchPantry =
                findViewById(R.id.etSearchPantry);

        databaseHelper =
                new DatabaseHelper(this);

        recyclerPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );

        foodAdapter = new FoodAdapter(
                this,
                foodItems,
                databaseHelper
        );

        recyclerPantry.setAdapter(foodAdapter);

        btnAddFood.setOnClickListener(v -> {

            Intent intent = new Intent(
                    PantryActivity.this,
                    AddFoodActivity.class
            );

            startActivity(intent);
        });

        setupSearch();
        loadFoodItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFoodItems();
    }

    private void loadFoodItems() {

        allFoodItems.clear();

        Cursor cursor =
                databaseHelper.getAllFoodItems();

        if (cursor != null) {

            while (cursor.moveToNext()) {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_ID
                        )
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_NAME
                        )
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_QUANTITY
                        )
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_UNIT
                        )
                );

                String category = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_CATEGORY
                        )
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_EXPIRY_DATE
                        )
                );

                allFoodItems.add(
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

        filterFoodItems(
                etSearchPantry.getText().toString()
        );
    }

    private void setupSearch() {

        etSearchPantry.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        filterFoodItems(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );
    }

    private void filterFoodItems(
            String searchText
    ) {

        String search =
                searchText.trim().toLowerCase();

        foodItems.clear();

        if (search.isEmpty()) {

            foodItems.addAll(
                    allFoodItems
            );

        } else {

            for (FoodItem foodItem :
                    allFoodItems) {

                boolean matchesName =
                        foodItem.getName()
                                .toLowerCase()
                                .contains(search);

                boolean matchesCategory =
                        foodItem.getCategory()
                                .toLowerCase()
                                .contains(search);

                boolean matchesUnit =
                        foodItem.getUnit()
                                .toLowerCase()
                                .contains(search);

                if (matchesName ||
                        matchesCategory ||
                        matchesUnit) {

                    foodItems.add(foodItem);
                }
            }
        }

        foodAdapter.notifyDataSetChanged();

        updateEmptyMessage(search);
    }

    private void updateEmptyMessage(
            String search
    ) {

        if (allFoodItems.isEmpty()) {

            tvEmptyPantry.setText(
                    "Your pantry is empty.\n\n" +
                            "Tap + Add Food Item to get started."
            );

            tvEmptyPantry.setVisibility(
                    TextView.VISIBLE
            );

            recyclerPantry.setVisibility(
                    RecyclerView.GONE
            );

        } else if (foodItems.isEmpty()) {

            tvEmptyPantry.setText(
                    "No pantry items found for \"" +
                            search +
                            "\"."
            );

            tvEmptyPantry.setVisibility(
                    TextView.VISIBLE
            );

            recyclerPantry.setVisibility(
                    RecyclerView.GONE
            );

        } else {

            tvEmptyPantry.setVisibility(
                    TextView.GONE
            );

            recyclerPantry.setVisibility(
                    RecyclerView.VISIBLE
            );
        }
    }
}