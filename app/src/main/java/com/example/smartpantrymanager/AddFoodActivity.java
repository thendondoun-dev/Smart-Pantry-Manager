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

public class AddFoodActivity
        extends AppCompatActivity {

    private EditText etFoodName;
    private EditText etQuantity;
    private EditText etExpiryDate;

    private Spinner spinnerUnit;
    private Spinner spinnerCategory;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_add_food
        );

        etFoodName =
                findViewById(
                        R.id.etFoodName
                );

        etQuantity =
                findViewById(
                        R.id.etQuantity
                );

        etExpiryDate =
                findViewById(
                        R.id.etExpiryDate
                );

        spinnerUnit =
                findViewById(
                        R.id.spinnerUnit
                );

        spinnerCategory =
                findViewById(
                        R.id.spinnerCategory
                );

        Button btnSaveFood =
                findViewById(
                        R.id.btnSaveFood
                );

        databaseHelper =
                new DatabaseHelper(this);

        setupSpinners();

        etExpiryDate.setOnClickListener(
                v -> showDatePicker()
        );

        btnSaveFood.setOnClickListener(
                v -> saveFood()
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

        spinnerUnit.setAdapter(
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

        spinnerCategory.setAdapter(
                categoryAdapter
        );
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

                            etExpiryDate.setText(
                                    date
                            );
                        },
                        year,
                        month,
                        day
                );

        dialog.show();
    }

    private void saveFood() {

        String name =
                etFoodName.getText()
                        .toString()
                        .trim();

        String quantityText =
                etQuantity.getText()
                        .toString()
                        .trim();

        String expiryDate =
                etExpiryDate.getText()
                        .toString()
                        .trim();

        String unit =
                spinnerUnit.getSelectedItem()
                        .toString();

        String category =
                spinnerCategory.getSelectedItem()
                        .toString();

        if (name.isEmpty()) {

            etFoodName.setError(
                    "Enter food name"
            );

            etFoodName.requestFocus();

            return;
        }

        if (quantityText.isEmpty()) {

            etQuantity.setError(
                    "Enter quantity"
            );

            etQuantity.requestFocus();

            return;
        }

        double quantity;

        try {

            quantity =
                    Double.parseDouble(
                            quantityText
                    );

        } catch (NumberFormatException e) {

            etQuantity.setError(
                    "Enter a valid number"
            );

            etQuantity.requestFocus();

            return;
        }

        if (quantity <= 0) {

            etQuantity.setError(
                    "Quantity must be greater than 0"
            );

            etQuantity.requestFocus();

            return;
        }

        long result =
                databaseHelper.addFoodItem(
                        name,
                        quantity,
                        unit,
                        category,
                        expiryDate
                );

        if (result != -1) {

            Toast.makeText(
                    this,
                    "Food item added successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Failed to add food item",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}