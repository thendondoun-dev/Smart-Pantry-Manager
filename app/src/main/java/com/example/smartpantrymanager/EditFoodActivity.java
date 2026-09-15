package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditFoodActivity
        extends AppCompatActivity {

    private EditText etEditFoodName;
    private EditText etEditQuantity;
    private EditText etEditExpiryDate;

    private Spinner spinnerEditUnit;
    private Spinner spinnerEditCategory;

    private DatabaseHelper databaseHelper;

    private int foodId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_edit_food
        );

        etEditFoodName =
                findViewById(
                        R.id.etEditFoodName
                );

        etEditQuantity =
                findViewById(
                        R.id.etEditQuantity
                );

        etEditExpiryDate =
                findViewById(
                        R.id.etEditExpiryDate
                );

        spinnerEditUnit =
                findViewById(
                        R.id.spinnerEditUnit
                );

        spinnerEditCategory =
                findViewById(
                        R.id.spinnerEditCategory
                );

        Button btnUpdateFood =
                findViewById(
                        R.id.btnUpdateFood
                );

        databaseHelper =
                new DatabaseHelper(this);

        setupSpinners();

        foodId =
                getIntent().getIntExtra(
                        "food_id",
                        -1
                );

        if (foodId == -1) {

            Toast.makeText(
                    this,
                    "Food item not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        loadFoodItem();

        etEditExpiryDate.setOnClickListener(
                v -> showDatePicker()
        );

        btnUpdateFood.setOnClickListener(
                v -> updateFood()
        );
    }

    private void setupSpinners() {

        String[] units = {
                "kg",
                "g",
                "L",
                "ml",
                "pieces"
        };

        String[] categories = {
                "Vegetables",
                "Fruit",
                "Meat",
                "Dairy",
                "Grains",
                "Canned",
                "Sauces",
                "Other"
        };

        ArrayAdapter<String> unitAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        units
                );

        unitAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerEditUnit.setAdapter(
                unitAdapter
        );

        ArrayAdapter<String> categoryAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categories
                );

        categoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerEditCategory.setAdapter(
                categoryAdapter
        );
    }

    private void loadFoodItem() {

        FoodItem foodItem =
                databaseHelper.getFoodItem(
                        foodId
                );

        if (foodItem == null) {

            Toast.makeText(
                    this,
                    "Food item not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        etEditFoodName.setText(
                foodItem.getName()
        );

        etEditQuantity.setText(
                String.valueOf(
                        foodItem.getQuantity()
                )
        );

        etEditExpiryDate.setText(
                foodItem.getExpiryDate()
        );

        setSpinnerValue(
                spinnerEditUnit,
                foodItem.getUnit()
        );

        setSpinnerValue(
                spinnerEditCategory,
                foodItem.getCategory()
        );
    }

    private void setSpinnerValue(
            Spinner spinner,
            String value
    ) {

        if (value == null) {
            return;
        }

        for (int i = 0;
             i < spinner.getCount();
             i++) {

            if (spinner.getItemAtPosition(i)
                    .toString()
                    .equalsIgnoreCase(value)) {

                spinner.setSelection(i);

                break;
            }
        }
    }

    private void showDatePicker() {

        Calendar calendar =
                Calendar.getInstance();

        int year =
                calendar.get(Calendar.YEAR);

        int month =
                calendar.get(Calendar.MONTH);

        int day =
                calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog =
                new DatePickerDialog(
                        this,
                        (view, selectedYear,
                         selectedMonth,
                         selectedDay) -> {

                            String date =
                                    String.format(
                                            "%04d-%02d-%02d",
                                            selectedYear,
                                            selectedMonth + 1,
                                            selectedDay
                                    );

                            etEditExpiryDate.setText(
                                    date
                            );
                        },
                        year,
                        month,
                        day
                );

        dialog.show();
    }

    private void updateFood() {

        String name =
                etEditFoodName.getText()
                        .toString()
                        .trim();

        String quantityText =
                etEditQuantity.getText()
                        .toString()
                        .trim();

        String expiryDate =
                etEditExpiryDate.getText()
                        .toString()
                        .trim();

        String unit =
                spinnerEditUnit.getSelectedItem()
                        .toString();

        String category =
                spinnerEditCategory.getSelectedItem()
                        .toString();

        if (name.isEmpty()) {

            etEditFoodName.setError(
                    "Enter food name"
            );

            return;
        }

        if (quantityText.isEmpty()) {

            etEditQuantity.setError(
                    "Enter quantity"
            );

            return;
        }

        double quantity;

        try {

            quantity =
                    Double.parseDouble(
                            quantityText
                    );

        } catch (NumberFormatException e) {

            etEditQuantity.setError(
                    "Enter a valid number"
            );

            return;
        }

        if (quantity <= 0) {

            etEditQuantity.setError(
                    "Quantity must be greater than 0"
            );

            return;
        }

        boolean updated =
                databaseHelper.updateFoodItem(
                        foodId,
                        name,
                        quantity,
                        unit,
                        category,
                        expiryDate
                );

        if (updated) {

            Toast.makeText(
                    this,
                    "Food item updated",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Unable to update food item",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}