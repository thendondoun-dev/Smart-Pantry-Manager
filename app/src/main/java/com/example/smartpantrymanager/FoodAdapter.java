package com.example.smartpantrymanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FoodAdapter
        extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private final List<FoodItem> foodItems;
    private final DatabaseHelper databaseHelper;
    private final Context context;

    public FoodAdapter(
            Context context,
            List<FoodItem> foodItems,
            DatabaseHelper databaseHelper
    ) {

        this.context = context;
        this.foodItems = foodItems;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_food,
                                parent,
                                false
                        );

        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull FoodViewHolder holder,
            int position
    ) {

        FoodItem foodItem =
                foodItems.get(position);

        holder.tvFoodName.setText(
                foodItem.getName()
        );

        holder.tvFoodQuantity.setText(
                "Quantity: " +
                        foodItem.getQuantity() +
                        " " +
                        foodItem.getUnit()
        );

        holder.tvFoodCategory.setText(
                "Category: " +
                        foodItem.getCategory()
        );

        String expiryDate =
                foodItem.getExpiryDate();

        if (expiryDate == null ||
                expiryDate.trim().isEmpty()) {

            holder.tvFoodExpiry.setText(
                    "Expiry: Not specified"
            );

        } else {

            checkExpiryStatus(
                    holder.tvFoodExpiry,
                    expiryDate
            );
        }

        holder.btnEditFood.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            context,
                            EditFoodActivity.class
                    );

            intent.putExtra(
                    "food_id",
                    foodItem.getId()
            );

            context.startActivity(intent);
        });

        holder.btnDeleteFood.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete Food Item")
                    .setMessage(
                            "Are you sure you want to delete " +
                                    foodItem.getName() +
                                    "?"
                    )
                    .setPositiveButton(
                            "Delete",
                            (dialog, which) -> {

                                boolean deleted =
                                        databaseHelper
                                                .deleteFoodItem(
                                                        foodItem.getId()
                                                );

                                if (deleted) {

                                    int currentPosition =
                                            holder.getBindingAdapterPosition();

                                    if (currentPosition !=
                                            RecyclerView.NO_POSITION) {

                                        foodItems.remove(
                                                currentPosition
                                        );

                                        notifyItemRemoved(
                                                currentPosition
                                        );
                                    }

                                    Toast.makeText(
                                            context,
                                            "Food item deleted",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                } else {

                                    Toast.makeText(
                                            context,
                                            "Unable to delete food item",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                    )
                    .setNegativeButton(
                            "Cancel",
                            null
                    )
                    .show();
        });
    }

    private void checkExpiryStatus(
            TextView textView,
            String expiryDate
    ) {

        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                );

        dateFormat.setLenient(false);

        try {

            Date expiry =
                    dateFormat.parse(expiryDate);

            if (expiry == null) {
                return;
            }

            Date today = new Date();

            long difference =
                    expiry.getTime() -
                            today.getTime();

            long daysRemaining =
                    difference /
                            (1000 * 60 * 60 * 24);

            if (expiry.before(today)) {

                textView.setText(
                        "Expiry: " +
                                expiryDate +
                                " ⚠ EXPIRED"
                );

                textView.setTextColor(
                        0xFFFF5252
                );

            } else if (daysRemaining <= 3) {

                textView.setText(
                        "Expiry: " +
                                expiryDate +
                                " ⚠ Expires soon"
                );

                textView.setTextColor(
                        0xFFFF9800
                );

            } else {

                textView.setText(
                        "Expiry: " +
                                expiryDate
                );

                textView.setTextColor(
                        0xFF69F0AE
                );
            }

        } catch (ParseException e) {

            textView.setText(
                    "Expiry: " +
                            expiryDate
            );
        }
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public static class FoodViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvFoodName;
        TextView tvFoodQuantity;
        TextView tvFoodCategory;
        TextView tvFoodExpiry;

        Button btnEditFood;
        Button btnDeleteFood;

        public FoodViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            tvFoodName =
                    itemView.findViewById(
                            R.id.tvFoodName
                    );

            tvFoodQuantity =
                    itemView.findViewById(
                            R.id.tvFoodQuantity
                    );

            tvFoodCategory =
                    itemView.findViewById(
                            R.id.tvFoodCategory
                    );

            tvFoodExpiry =
                    itemView.findViewById(
                            R.id.tvFoodExpiry
                    );

            btnEditFood =
                    itemView.findViewById(
                            R.id.btnEditFood
                    );

            btnDeleteFood =
                    itemView.findViewById(
                            R.id.btnDeleteFood
                    );
        }
    }
}