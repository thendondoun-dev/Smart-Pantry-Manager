package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final Context context;
    private final List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position) {

        Recipe recipe = recipes.get(position);

        holder.tvRecipeName.setText(recipe.getName());

        holder.tvRecipeInfo.setText(
                recipe.getDescription()
        );

        holder.btnViewRecipe.setOnClickListener(v -> {

            Intent intent = new Intent(
                    context,
                    RecipeDetailActivity.class
            );

            intent.putExtra(
                    "recipe_id",
                    recipe.getId()
            );

            context.startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    context,
                    RecipeDetailActivity.class
            );

            intent.putExtra(
                    "recipe_id",
                    recipe.getId()
            );

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvRecipeName;
        TextView tvRecipeInfo;
        Button btnViewRecipe;

        public RecipeViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvRecipeName =
                    itemView.findViewById(
                            R.id.tvRecipeName
                    );

            tvRecipeInfo =
                    itemView.findViewById(
                            R.id.tvRecipeInfo
                    );

            btnViewRecipe =
                    itemView.findViewById(
                            R.id.btnViewRecipe
                    );
        }
    }
}