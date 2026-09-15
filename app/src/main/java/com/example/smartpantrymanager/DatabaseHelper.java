package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // Pantry table
    public static final String TABLE_FOOD = "food";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";

    // Recipe table
    public static final String TABLE_RECIPES = "recipes";

    public static final String RECIPE_ID = "id";
    public static final String RECIPE_NAME = "name";
    public static final String RECIPE_DESCRIPTION = "description";
    public static final String RECIPE_INSTRUCTIONS = "instructions";

    // Recipe ingredients table
    public static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";

    public static final String RI_ID = "id";
    public static final String RI_RECIPE_ID = "recipe_id";
    public static final String RI_NAME = "name";
    public static final String RI_QUANTITY = "quantity";
    public static final String RI_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createFoodTable =
                "CREATE TABLE " + TABLE_FOOD + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_UNIT + " TEXT NOT NULL, " +
                        COLUMN_CATEGORY + " TEXT NOT NULL, " +
                        COLUMN_EXPIRY_DATE + " TEXT" +
                        ")";

        db.execSQL(createFoodTable);

        String createRecipeTable =
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        RECIPE_ID + " INTEGER PRIMARY KEY, " +
                        RECIPE_NAME + " TEXT NOT NULL, " +
                        RECIPE_DESCRIPTION + " TEXT, " +
                        RECIPE_INSTRUCTIONS + " TEXT NOT NULL" +
                        ")";

        db.execSQL(createRecipeTable);

        String createIngredientTable =
                "CREATE TABLE " +
                        TABLE_RECIPE_INGREDIENTS + " (" +
                        RI_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RI_RECIPE_ID +
                        " INTEGER NOT NULL, " +
                        RI_NAME +
                        " TEXT NOT NULL, " +
                        RI_QUANTITY +
                        " REAL NOT NULL, " +
                        RI_UNIT +
                        " TEXT NOT NULL, " +
                        "FOREIGN KEY (" +
                        RI_RECIPE_ID +
                        ") REFERENCES " +
                        TABLE_RECIPES +
                        "(" +
                        RECIPE_ID +
                        ")" +
                        ")";

        db.execSQL(createIngredientTable);

        insertDefaultRecipes(db);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        db.execSQL(
                "DROP TABLE IF EXISTS "
                        + TABLE_RECIPE_INGREDIENTS
        );

        db.execSQL(
                "DROP TABLE IF EXISTS "
                        + TABLE_RECIPES
        );

        db.execSQL(
                "DROP TABLE IF EXISTS "
                        + TABLE_FOOD
        );

        onCreate(db);
    }

    // =========================================================
    // FOOD CRUD
    // =========================================================

    public long addFoodItem(
            String name,
            double quantity,
            String unit,
            String category,
            String expiryDate
    ) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);

        return db.insert(
                TABLE_FOOD,
                null,
                values
        );
    }

    public Cursor getAllFoodItems() {

        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                TABLE_FOOD,
                null,
                null,
                null,
                null,
                null,
                COLUMN_ID + " DESC"
        );
    }

    public FoodItem getFoodItem(int id) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_FOOD,
                null,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                FoodItem foodItem = new FoodItem(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_ID
                                )
                        ),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_NAME
                                )
                        ),
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_QUANTITY
                                )
                        ),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_UNIT
                                )
                        ),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_CATEGORY
                                )
                        ),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_EXPIRY_DATE
                                )
                        )
                );

                cursor.close();

                return foodItem;
            }

            cursor.close();
        }

        return null;
    }

    public boolean updateFoodItem(
            int id,
            String name,
            double quantity,
            String unit,
            String category,
            String expiryDate
    ) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);

        int result = db.update(
                TABLE_FOOD,
                values,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }

    public boolean deleteFoodItem(int id) {

        SQLiteDatabase db = getWritableDatabase();

        int result = db.delete(
                TABLE_FOOD,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }

    // =========================================================
    // RECIPES
    // =========================================================

    public List<Recipe> getAllRecipes() {

        List<Recipe> recipes = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                RECIPE_ID + " ASC"
        );

        if (cursor != null) {

            while (cursor.moveToNext()) {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                RECIPE_ID
                        )
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                RECIPE_NAME
                        )
                );

                String description = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                RECIPE_DESCRIPTION
                        )
                );

                String instructions = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                RECIPE_INSTRUCTIONS
                        )
                );

                List<RecipeIngredient> ingredients =
                        getRecipeIngredients(id);

                recipes.add(
                        new Recipe(
                                id,
                                name,
                                description,
                                ingredients,
                                instructions
                        )
                );
            }

            cursor.close();
        }

        return recipes;
    }

    public Recipe getRecipe(int recipeId) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPES,
                null,
                RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                null
        );

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                RECIPE_NAME
                        )
                );

                String description = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                RECIPE_DESCRIPTION
                        )
                );

                String instructions = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                RECIPE_INSTRUCTIONS
                        )
                );

                List<RecipeIngredient> ingredients =
                        getRecipeIngredients(recipeId);

                Recipe recipe = new Recipe(
                        recipeId,
                        name,
                        description,
                        ingredients,
                        instructions
                );

                cursor.close();

                return recipe;
            }

            cursor.close();
        }

        return null;
    }

    private List<RecipeIngredient> getRecipeIngredients(
            int recipeId
    ) {

        List<RecipeIngredient> ingredients =
                new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                RI_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                RI_ID + " ASC"
        );

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                RI_NAME
                        )
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                                RI_QUANTITY
                        )
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                RI_UNIT
                        )
                );

                ingredients.add(
                        new RecipeIngredient(
                                name,
                                quantity,
                                unit
                        )
                );
            }

            cursor.close();
        }

        return ingredients;
    }

    // =========================================================
    // DEFAULT RECIPES
    // =========================================================

    private void insertDefaultRecipes(
            SQLiteDatabase db
    ) {

        // 1
        addRecipe(
                db,
                1,
                "Chicken Fried Rice",
                "A delicious fried rice meal.",
                "Cook the rice. Fry the chicken until cooked. Add vegetables and eggs. Add rice and soy sauce. Stir-fry everything together.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Rice", 0.5, "kg"),
                        new RecipeIngredient("Chicken", 0.3, "kg"),
                        new RecipeIngredient("Eggs", 2, "pieces"),
                        new RecipeIngredient("Carrots", 0.1, "kg"),
                        new RecipeIngredient("Soy Sauce", 0.05, "L")
                }
        );

        // 2
        addRecipe(
                db,
                2,
                "Spaghetti Bolognese",
                "Classic spaghetti with meat sauce.",
                "Cook spaghetti. Brown the beef. Add tomato sauce and seasonings. Simmer and serve over spaghetti.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Spaghetti", 0.3, "kg"),
                        new RecipeIngredient("Beef Mince", 0.3, "kg"),
                        new RecipeIngredient("Tomato Sauce", 0.2, "L"),
                        new RecipeIngredient("Onion", 1, "pieces")
                }
        );

        // 3
        addRecipe(
                db,
                3,
                "Vegetable Stir Fry",
                "Quick and healthy mixed vegetables.",
                "Heat oil in a pan. Add vegetables and stir-fry until tender. Add soy sauce and serve.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Carrots", 0.1, "kg"),
                        new RecipeIngredient("Broccoli", 0.1, "kg"),
                        new RecipeIngredient("Peppers", 0.1, "kg"),
                        new RecipeIngredient("Soy Sauce", 0.03, "L")
                }
        );

        // 4
        addRecipe(
                db,
                4,
                "Beef Stew",
                "A warm and hearty beef stew.",
                "Brown the beef. Add vegetables and stock. Cover and simmer until the beef is tender.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Beef", 0.5, "kg"),
                        new RecipeIngredient("Potatoes", 0.3, "kg"),
                        new RecipeIngredient("Carrots", 0.2, "kg"),
                        new RecipeIngredient("Beef Stock", 0.5, "L")
                }
        );

        // 5
        addRecipe(
                db,
                5,
                "Chicken Pasta",
                "Creamy chicken pasta.",
                "Cook the pasta. Cook chicken in a pan. Add cream and seasoning. Mix with pasta and serve.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Pasta", 0.3, "kg"),
                        new RecipeIngredient("Chicken", 0.3, "kg"),
                        new RecipeIngredient("Cream", 0.2, "L"),
                        new RecipeIngredient("Cheese", 0.1, "kg")
                }
        );

        // 6
        addRecipe(
                db,
                6,
                "Tuna Sandwich",
                "Simple tuna sandwich.",
                "Mix tuna with mayonnaise. Place the mixture between slices of bread and serve.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Bread", 4, "pieces"),
                        new RecipeIngredient("Tuna", 1, "pieces"),
                        new RecipeIngredient("Mayonnaise", 0.05, "L")
                }
        );

        // 7
        addRecipe(
                db,
                7,
                "Omelette",
                "Simple egg omelette.",
                "Beat the eggs. Add cheese and vegetables. Cook in a hot pan until set.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Eggs", 3, "pieces"),
                        new RecipeIngredient("Cheese", 0.05, "kg"),
                        new RecipeIngredient("Peppers", 0.05, "kg")
                }
        );

        // 8
        addRecipe(
                db,
                8,
                "Pancakes",
                "Fluffy homemade pancakes.",
                "Mix flour, milk and eggs. Heat a pan and cook pancakes on both sides.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Flour", 0.2, "kg"),
                        new RecipeIngredient("Milk", 0.25, "L"),
                        new RecipeIngredient("Eggs", 2, "pieces")
                }
        );

        // 9
        addRecipe(
                db,
                9,
                "French Toast",
                "Golden French toast.",
                "Beat eggs with milk. Dip bread into the mixture and fry until golden.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Bread", 4, "pieces"),
                        new RecipeIngredient("Eggs", 2, "pieces"),
                        new RecipeIngredient("Milk", 0.1, "L")
                }
        );

        // 10
        addRecipe(
                db,
                10,
                "Chicken Curry",
                "A tasty chicken curry.",
                "Fry onions and spices. Add chicken and cook. Add curry sauce and simmer until fully cooked.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Chicken", 0.5, "kg"),
                        new RecipeIngredient("Onion", 1, "pieces"),
                        new RecipeIngredient("Curry Sauce", 0.2, "L"),
                        new RecipeIngredient("Rice", 0.3, "kg")
                }
        );

        // 11
        addRecipe(
                db,
                11,
                "Beef Pasta",
                "Pasta with seasoned beef.",
                "Cook pasta. Fry beef and onions. Combine with pasta and sauce.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Pasta", 0.3, "kg"),
                        new RecipeIngredient("Beef", 0.3, "kg"),
                        new RecipeIngredient("Onion", 1, "pieces"),
                        new RecipeIngredient("Tomato Sauce", 0.15, "L")
                }
        );

        // 12
        addRecipe(
                db,
                12,
                "Vegetable Pasta",
                "Pasta with fresh vegetables.",
                "Cook pasta. Fry vegetables. Add sauce and mix with cooked pasta.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Pasta", 0.3, "kg"),
                        new RecipeIngredient("Carrots", 0.1, "kg"),
                        new RecipeIngredient("Broccoli", 0.1, "kg"),
                        new RecipeIngredient("Tomato Sauce", 0.15, "L")
                }
        );

        // 13
        addRecipe(
                db,
                13,
                "Rice and Beans",
                "Easy rice and beans meal.",
                "Cook rice. Heat beans with seasoning. Serve beans with rice.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Rice", 0.3, "kg"),
                        new RecipeIngredient("Beans", 0.2, "kg")
                }
        );

        // 14
        addRecipe(
                db,
                14,
                "Chicken Wrap",
                "Chicken and vegetable wrap.",
                "Cook chicken. Add vegetables and sauce. Place everything inside the wrap and roll.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Chicken", 0.2, "kg"),
                        new RecipeIngredient("Wrap", 2, "pieces"),
                        new RecipeIngredient("Lettuce", 0.05, "kg"),
                        new RecipeIngredient("Tomato", 1, "pieces")
                }
        );

        // 15
        addRecipe(
                db,
                15,
                "Beef Burger",
                "Homemade beef burger.",
                "Cook the beef patty. Toast the bun. Add lettuce, tomato and sauce.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Beef Mince", 0.2, "kg"),
                        new RecipeIngredient("Burger Buns", 2, "pieces"),
                        new RecipeIngredient("Lettuce", 0.05, "kg"),
                        new RecipeIngredient("Tomato", 1, "pieces")
                }
        );

        // 16
        addRecipe(
                db,
                16,
                "Tomato Pasta",
                "Simple tomato pasta.",
                "Cook pasta. Heat tomato sauce. Mix the pasta with sauce and serve.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Pasta", 0.3, "kg"),
                        new RecipeIngredient("Tomato Sauce", 0.2, "L")
                }
        );

        // 17
        addRecipe(
                db,
                17,
                "Potato Curry",
                "Comforting potato curry.",
                "Cook potatoes. Fry onion and curry spices. Add potatoes and simmer.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Potatoes", 0.5, "kg"),
                        new RecipeIngredient("Onion", 1, "pieces"),
                        new RecipeIngredient("Curry Sauce", 0.2, "L")
                }
        );

        // 18
        addRecipe(
                db,
                18,
                "Egg Fried Rice",
                "Fried rice with eggs.",
                "Fry vegetables and eggs. Add cooked rice and soy sauce. Stir-fry together.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Rice", 0.5, "kg"),
                        new RecipeIngredient("Eggs", 2, "pieces"),
                        new RecipeIngredient("Carrots", 0.1, "kg"),
                        new RecipeIngredient("Soy Sauce", 0.05, "L")
                }
        );

        // 19
        addRecipe(
                db,
                19,
                "Chicken Sandwich",
                "Chicken sandwich with fresh vegetables.",
                "Cook chicken. Place chicken, lettuce and tomato between bread slices.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Bread", 4, "pieces"),
                        new RecipeIngredient("Chicken", 0.2, "kg"),
                        new RecipeIngredient("Lettuce", 0.05, "kg"),
                        new RecipeIngredient("Tomato", 1, "pieces")
                }
        );

        // 20
        addRecipe(
                db,
                20,
                "Vegetable Soup",
                "Healthy vegetable soup.",
                "Chop vegetables. Add to stock and simmer until vegetables are soft.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Carrots", 0.2, "kg"),
                        new RecipeIngredient("Potatoes", 0.2, "kg"),
                        new RecipeIngredient("Broccoli", 0.1, "kg"),
                        new RecipeIngredient("Vegetable Stock", 0.5, "L")
                }
        );
    }

    private void addRecipe(
            SQLiteDatabase db,
            int id,
            String name,
            String description,
            String instructions,
            RecipeIngredient[] ingredients
    ) {

        ContentValues recipeValues =
                new ContentValues();

        recipeValues.put(
                RECIPE_ID,
                id
        );

        recipeValues.put(
                RECIPE_NAME,
                name
        );

        recipeValues.put(
                RECIPE_DESCRIPTION,
                description
        );

        recipeValues.put(
                RECIPE_INSTRUCTIONS,
                instructions
        );

        db.insert(
                TABLE_RECIPES,
                null,
                recipeValues
        );

        for (RecipeIngredient ingredient : ingredients) {

            ContentValues ingredientValues =
                    new ContentValues();

            ingredientValues.put(
                    RI_RECIPE_ID,
                    id
            );

            ingredientValues.put(
                    RI_NAME,
                    ingredient.getName()
            );

            ingredientValues.put(
                    RI_QUANTITY,
                    ingredient.getQuantity()
            );

            ingredientValues.put(
                    RI_UNIT,
                    ingredient.getUnit()
            );

            db.insert(
                    TABLE_RECIPE_INGREDIENTS,
                    null,
                    ingredientValues
            );
        }
    }
}