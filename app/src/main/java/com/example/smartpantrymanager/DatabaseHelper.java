package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // =========================================================
    // DATABASE
    // =========================================================

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2;

    // =========================================================
    // FOOD TABLE
    // =========================================================

    public static final String TABLE_FOOD_ITEMS = "food_items";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";

    // =========================================================
    // RECIPE TABLE
    // =========================================================

    public static final String TABLE_RECIPES = "recipes";

    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_RECIPE_DESCRIPTION = "description";
    public static final String COLUMN_RECIPE_INSTRUCTIONS = "instructions";

    // =========================================================
    // RECIPE INGREDIENT TABLE
    // =========================================================

    public static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";

    public static final String COLUMN_RECIPE_INGREDIENT_ID =
            "id";

    public static final String COLUMN_INGREDIENT_NAME =
            "ingredient_name";

    public static final String COLUMN_REQUIRED_QUANTITY =
            "required_quantity";

    public static final String COLUMN_REQUIRED_UNIT =
            "required_unit";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public DatabaseHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    // =========================================================
    // CREATE DATABASE
    // =========================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Food / pantry table
        String createFoodTable =
                "CREATE TABLE " + TABLE_FOOD_ITEMS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_UNIT + " TEXT NOT NULL, " +
                        COLUMN_CATEGORY + " TEXT NOT NULL, " +
                        COLUMN_EXPIRY_DATE + " TEXT" +
                        ")";

        db.execSQL(createFoodTable);

        // Recipes table
        String createRecipeTable =
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                        COLUMN_RECIPE_DESCRIPTION + " TEXT, " +
                        COLUMN_RECIPE_INSTRUCTIONS + " TEXT" +
                        ")";

        db.execSQL(createRecipeTable);

        // Recipe ingredients table
        String createRecipeIngredientsTable =
                "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                        COLUMN_RECIPE_INGREDIENT_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COLUMN_RECIPE_ID +
                        " INTEGER NOT NULL, " +

                        COLUMN_INGREDIENT_NAME +
                        " TEXT NOT NULL, " +

                        COLUMN_REQUIRED_QUANTITY +
                        " REAL NOT NULL, " +

                        COLUMN_REQUIRED_UNIT +
                        " TEXT NOT NULL, " +

                        "FOREIGN KEY (" +
                        COLUMN_RECIPE_ID +
                        ") REFERENCES " +
                        TABLE_RECIPES +
                        "(" +
                        COLUMN_RECIPE_ID +
                        ") ON DELETE CASCADE" +
                        ")";

        db.execSQL(createRecipeIngredientsTable);

        // Add the preloaded recipes
        seedRecipes(db);
    }

    // =========================================================
    // DATABASE UPGRADE
    // =========================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        if (oldVersion < 2) {

            // Create recipes table
            String createRecipeTable =
                    "CREATE TABLE IF NOT EXISTS " +
                            TABLE_RECIPES +
                            " (" +
                            COLUMN_RECIPE_ID +
                            " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                            COLUMN_RECIPE_NAME +
                            " TEXT NOT NULL, " +

                            COLUMN_RECIPE_DESCRIPTION +
                            " TEXT, " +

                            COLUMN_RECIPE_INSTRUCTIONS +
                            " TEXT" +
                            ")";

            db.execSQL(createRecipeTable);

            // Create recipe ingredients table
            String createRecipeIngredientsTable =
                    "CREATE TABLE IF NOT EXISTS " +
                            TABLE_RECIPE_INGREDIENTS +
                            " (" +

                            COLUMN_RECIPE_INGREDIENT_ID +
                            " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                            COLUMN_RECIPE_ID +
                            " INTEGER NOT NULL, " +

                            COLUMN_INGREDIENT_NAME +
                            " TEXT NOT NULL, " +

                            COLUMN_REQUIRED_QUANTITY +
                            " REAL NOT NULL, " +

                            COLUMN_REQUIRED_UNIT +
                            " TEXT NOT NULL, " +

                            "FOREIGN KEY (" +
                            COLUMN_RECIPE_ID +
                            ") REFERENCES " +
                            TABLE_RECIPES +
                            "(" +
                            COLUMN_RECIPE_ID +
                            ") ON DELETE CASCADE" +

                            ")";

            db.execSQL(createRecipeIngredientsTable);

            // Add recipes after upgrading
            seedRecipes(db);
        }
    }

    // =========================================================
    // FOOD CRUD
    // =========================================================

    public long addFoodItem(
            String name,
            double quantity,
            String unit,
            String category,
            String expiryDate) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);

        return db.insert(
                TABLE_FOOD_ITEMS,
                null,
                values
        );
    }

    public Cursor getAllFoodItems() {

        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                TABLE_FOOD_ITEMS,
                null,
                null,
                null,
                null,
                null,
                COLUMN_ID + " DESC"
        );
    }

    public FoodItem getFoodItemById(int id) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_FOOD_ITEMS,
                null,
                COLUMN_ID + "=?",
                new String[]{
                        String.valueOf(id)
                },
                null,
                null,
                null
        );

        FoodItem foodItem = null;

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_NAME
                                )
                        );

                double quantity =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_QUANTITY
                                )
                        );

                String unit =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_UNIT
                                )
                        );

                String category =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_CATEGORY
                                )
                        );

                String expiryDate =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_EXPIRY_DATE
                                )
                        );

                foodItem = new FoodItem(
                        id,
                        name,
                        quantity,
                        unit,
                        category,
                        expiryDate
                );
            }

            cursor.close();
        }

        return foodItem;
    }

    public boolean updateFoodItem(
            int id,
            String name,
            double quantity,
            String unit,
            String category,
            String expiryDate) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);

        int rowsUpdated =
                db.update(
                        TABLE_FOOD_ITEMS,
                        values,
                        COLUMN_ID + "=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        return rowsUpdated > 0;
    }

    public boolean deleteFoodItem(int id) {

        SQLiteDatabase db = getWritableDatabase();

        int rowsDeleted =
                db.delete(
                        TABLE_FOOD_ITEMS,
                        COLUMN_ID + "=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        return rowsDeleted > 0;
    }

    // =========================================================
    // GET ALL RECIPES
    // =========================================================

    public List<Recipe> getAllRecipes() {

        List<Recipe> recipes =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_RECIPES,
                        null,
                        null,
                        null,
                        null,
                        null,
                        COLUMN_RECIPE_ID + " ASC"
                );

        if (cursor != null) {

            while (cursor.moveToNext()) {

                int recipeId =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_ID
                                )
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_NAME
                                )
                        );

                String description =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_DESCRIPTION
                                )
                        );

                String instructions =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_INSTRUCTIONS
                                )
                        );

                List<RecipeIngredient> ingredients =
                        getRecipeIngredients(recipeId);

                Recipe recipe =
                        new Recipe(
                                recipeId,
                                name,
                                description,
                                ingredients,
                                instructions
                        );

                recipes.add(recipe);
            }

            cursor.close();
        }

        return recipes;
    }

    // =========================================================
    // GET RECIPE BY ID
    // =========================================================

    public Recipe getRecipeById(int recipeId) {

        SQLiteDatabase db =
                getReadableDatabase();

        Recipe recipe = null;

        Cursor recipeCursor =
                db.query(
                        TABLE_RECIPES,

                        new String[]{
                                COLUMN_RECIPE_ID,
                                COLUMN_RECIPE_NAME,
                                COLUMN_RECIPE_DESCRIPTION,
                                COLUMN_RECIPE_INSTRUCTIONS
                        },

                        COLUMN_RECIPE_ID + "=?",

                        new String[]{
                                String.valueOf(recipeId)
                        },

                        null,
                        null,
                        null
                );

        if (recipeCursor != null) {

            if (recipeCursor.moveToFirst()) {

                int id =
                        recipeCursor.getInt(
                                recipeCursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_ID
                                )
                        );

                String name =
                        recipeCursor.getString(
                                recipeCursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_NAME
                                )
                        );

                String description =
                        recipeCursor.getString(
                                recipeCursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_DESCRIPTION
                                )
                        );

                String instructions =
                        recipeCursor.getString(
                                recipeCursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_INSTRUCTIONS
                                )
                        );

                List<RecipeIngredient> ingredients =
                        getRecipeIngredients(recipeId);

                recipe =
                        new Recipe(
                                id,
                                name,
                                description,
                                ingredients,
                                instructions
                        );
            }

            recipeCursor.close();
        }

        return recipe;
    }

    // =========================================================
    // GET RECIPE INGREDIENTS
    // =========================================================

    public List<RecipeIngredient> getRecipeIngredients(
            int recipeId) {

        List<RecipeIngredient> ingredients =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_RECIPE_INGREDIENTS,

                        new String[]{
                                COLUMN_INGREDIENT_NAME,
                                COLUMN_REQUIRED_QUANTITY,
                                COLUMN_REQUIRED_UNIT
                        },

                        COLUMN_RECIPE_ID + "=?",

                        new String[]{
                                String.valueOf(recipeId)
                        },

                        null,
                        null,

                        COLUMN_RECIPE_INGREDIENT_ID + " ASC"
                );

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_INGREDIENT_NAME
                                )
                        );

                double quantity =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_REQUIRED_QUANTITY
                                )
                        );

                String unit =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_REQUIRED_UNIT
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
    // ADD RECIPE
    // =========================================================

    private void addRecipe(
            SQLiteDatabase db,
            String name,
            String description,
            String instructions,
            RecipeIngredient... ingredients) {

        ContentValues recipeValues =
                new ContentValues();

        recipeValues.put(
                COLUMN_RECIPE_NAME,
                name
        );

        recipeValues.put(
                COLUMN_RECIPE_DESCRIPTION,
                description
        );

        recipeValues.put(
                COLUMN_RECIPE_INSTRUCTIONS,
                instructions
        );

        long recipeId =
                db.insert(
                        TABLE_RECIPES,
                        null,
                        recipeValues
                );

        if (recipeId == -1) {
            return;
        }

        for (RecipeIngredient ingredient :
                ingredients) {

            ContentValues ingredientValues =
                    new ContentValues();

            ingredientValues.put(
                    COLUMN_RECIPE_ID,
                    recipeId
            );

            ingredientValues.put(
                    COLUMN_INGREDIENT_NAME,
                    ingredient.getName()
            );

            ingredientValues.put(
                    COLUMN_REQUIRED_QUANTITY,
                    ingredient.getQuantity()
            );

            ingredientValues.put(
                    COLUMN_REQUIRED_UNIT,
                    ingredient.getUnit()
            );

            db.insert(
                    TABLE_RECIPE_INGREDIENTS,
                    null,
                    ingredientValues
            );
        }
    }

    // =========================================================
    // SEED 20 RECIPES
    // =========================================================

    private void seedRecipes(SQLiteDatabase db) {

        // Prevent duplicate recipes
        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM " +
                                TABLE_RECIPES,
                        null
                );

        int count = 0;

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }

            cursor.close();
        }

        if (count > 0) {
            return;
        }

        // -----------------------------------------------------
        // 1. Chicken Fried Rice
        // -----------------------------------------------------

        addRecipe(
                db,
                "Chicken Fried Rice",
                "A quick and tasty fried rice with chicken and vegetables.",
                "Cook the rice. Stir-fry the chicken until cooked. Add vegetables and egg. Add rice and soy sauce, then stir-fry everything together.",
                new RecipeIngredient("Chicken", 200, "g"),
                new RecipeIngredient("Rice", 2, "cup"),
                new RecipeIngredient("Egg", 2, "pcs"),
                new RecipeIngredient("Carrot", 1, "pcs"),
                new RecipeIngredient("Soy Sauce", 2, "tbsp")
        );

        // -----------------------------------------------------
        // 2. Spaghetti Bolognese
        // -----------------------------------------------------

        addRecipe(
                db,
                "Spaghetti Bolognese",
                "Classic spaghetti served with a rich beef tomato sauce.",
                "Cook spaghetti. Brown the beef. Add onion and tomato sauce. Simmer and serve over spaghetti.",
                new RecipeIngredient("Spaghetti", 250, "g"),
                new RecipeIngredient("Beef Mince", 250, "g"),
                new RecipeIngredient("Tomato Sauce", 200, "ml"),
                new RecipeIngredient("Onion", 1, "pcs")
        );

        // -----------------------------------------------------
        // 3. Vegetable Stir Fry
        // -----------------------------------------------------

        addRecipe(
                db,
                "Vegetable Stir Fry",
                "Healthy mixed vegetables cooked quickly in a pan.",
                "Heat oil in a pan. Add vegetables and stir-fry until tender. Add soy sauce and serve.",
                new RecipeIngredient("Carrot", 1, "pcs"),
                new RecipeIngredient("Broccoli", 100, "g"),
                new RecipeIngredient("Bell Pepper", 1, "pcs"),
                new RecipeIngredient("Soy Sauce", 2, "tbsp")
        );

        // -----------------------------------------------------
        // 4. Beef Stew
        // -----------------------------------------------------

        addRecipe(
                db,
                "Beef Stew",
                "Tender beef cooked with vegetables in a rich gravy.",
                "Brown the beef. Add vegetables and stock. Cover and simmer until the beef is tender.",
                new RecipeIngredient("Beef", 500, "g"),
                new RecipeIngredient("Potato", 3, "pcs"),
                new RecipeIngredient("Carrot", 2, "pcs"),
                new RecipeIngredient("Onion", 1, "pcs"),
                new RecipeIngredient("Beef Stock", 500, "ml")
        );

        // -----------------------------------------------------
        // 5. Chicken Pasta
        // -----------------------------------------------------

        addRecipe(
                db,
                "Chicken Pasta",
                "Creamy pasta with tender chicken.",
                "Cook pasta. Fry chicken until cooked. Add cream and seasoning. Mix with pasta and serve.",
                new RecipeIngredient("Chicken", 250, "g"),
                new RecipeIngredient("Pasta", 250, "g"),
                new RecipeIngredient("Cream", 200, "ml"),
                new RecipeIngredient("Onion", 1, "pcs")
        );

        // -----------------------------------------------------
        // 6. Tuna Sandwich
        // -----------------------------------------------------

        addRecipe(
                db,
                "Tuna Sandwich",
                "Simple tuna sandwich perfect for lunch.",
                "Mix tuna with mayonnaise. Add lettuce and place between slices of bread.",
                new RecipeIngredient("Tuna", 1, "can"),
                new RecipeIngredient("Bread", 4, "slices"),
                new RecipeIngredient("Mayonnaise", 2, "tbsp"),
                new RecipeIngredient("Lettuce", 2, "leaves")
        );

        // -----------------------------------------------------
        // 7. Omelette
        // -----------------------------------------------------

        addRecipe(
                db,
                "Omelette",
                "Fluffy eggs filled with vegetables and cheese.",
                "Beat the eggs. Cook in a pan. Add vegetables and cheese. Fold and serve.",
                new RecipeIngredient("Egg", 3, "pcs"),
                new RecipeIngredient("Cheese", 50, "g"),
                new RecipeIngredient("Tomato", 1, "pcs"),
                new RecipeIngredient("Onion", 1, "pcs")
        );

        // -----------------------------------------------------
        // 8. Pancakes
        // -----------------------------------------------------

        addRecipe(
                db,
                "Pancakes",
                "Soft homemade pancakes.",
                "Mix flour, milk and eggs. Heat a pan and cook pancakes on both sides.",
                new RecipeIngredient("Flour", 200, "g"),
                new RecipeIngredient("Milk", 250, "ml"),
                new RecipeIngredient("Egg", 2, "pcs"),
                new RecipeIngredient("Sugar", 2, "tbsp")
        );

        // -----------------------------------------------------
        // 9. French Toast
        // -----------------------------------------------------

        addRecipe(
                db,
                "French Toast",
                "Golden bread dipped in an egg mixture.",
                "Beat eggs with milk and sugar. Dip bread into the mixture and fry until golden.",
                new RecipeIngredient("Bread", 4, "slices"),
                new RecipeIngredient("Egg", 2, "pcs"),
                new RecipeIngredient("Milk", 100, "ml"),
                new RecipeIngredient("Sugar", 1, "tbsp")
        );

        // -----------------------------------------------------
        // 10. Chicken Curry
        // -----------------------------------------------------

        addRecipe(
                db,
                "Chicken Curry",
                "A flavourful chicken curry.",
                "Brown the chicken. Add onion, curry spices and tomatoes. Simmer until cooked.",
                new RecipeIngredient("Chicken", 500, "g"),
                new RecipeIngredient("Onion", 1, "pcs"),
                new RecipeIngredient("Tomato", 2, "pcs"),
                new RecipeIngredient("Curry Powder", 2, "tbsp")
        );

        // -----------------------------------------------------
        // 11. Beef Pasta
        // -----------------------------------------------------

        addRecipe(
                db,
                "Beef Pasta",
                "Pasta combined with seasoned beef.",
                "Cook pasta. Fry beef with onion. Add tomato sauce and combine with pasta.",
                new RecipeIngredient("Pasta", 250, "g"),
                new RecipeIngredient("Beef Mince", 250, "g"),
                new RecipeIngredient("Tomato Sauce", 200, "ml"),
                new RecipeIngredient("Onion", 1, "pcs")
        );

        // -----------------------------------------------------
        // 12. Vegetable Pasta
        // -----------------------------------------------------

        addRecipe(
                db,
                "Vegetable Pasta",
                "Easy pasta packed with vegetables.",
                "Cook pasta. Stir-fry vegetables. Mix vegetables with pasta and sauce.",
                new RecipeIngredient("Pasta", 250, "g"),
                new RecipeIngredient("Carrot", 1, "pcs"),
                new RecipeIngredient("Bell Pepper", 1, "pcs"),
                new RecipeIngredient("Tomato Sauce", 150, "ml")
        );

        // -----------------------------------------------------
        // 13. Rice and Beans
        // -----------------------------------------------------

        addRecipe(
                db,
                "Rice and Beans",
                "Simple rice served with seasoned beans.",
                "Cook rice. Heat beans with onion and seasoning. Serve together.",
                new RecipeIngredient("Rice", 2, "cup"),
                new RecipeIngredient("Beans", 1, "can"),
                new RecipeIngredient("Onion", 1, "pcs")
        );

        // -----------------------------------------------------
        // 14. Chicken Wrap
        // -----------------------------------------------------

        addRecipe(
                db,
                "Chicken Wrap",
                "Chicken and salad wrapped in a soft tortilla.",
                "Cook chicken. Place chicken and vegetables inside the tortilla. Add sauce and roll.",
                new RecipeIngredient("Chicken", 200, "g"),
                new RecipeIngredient("Tortilla", 2, "pcs"),
                new RecipeIngredient("Lettuce", 2, "leaves"),
                new RecipeIngredient("Tomato", 1, "pcs"),
                new RecipeIngredient("Mayonnaise", 1, "tbsp")
        );

        // -----------------------------------------------------
        // 15. Beef Burger
        // -----------------------------------------------------

        addRecipe(
                db,
                "Beef Burger",
                "Homemade beef burger with salad.",
                "Shape the beef into patties. Cook thoroughly. Place inside burger buns with lettuce and tomato.",
                new RecipeIngredient("Beef Mince", 250, "g"),
                new RecipeIngredient("Burger Bun", 2, "pcs"),
                new RecipeIngredient("Lettuce", 2, "leaves"),
                new RecipeIngredient("Tomato", 1, "pcs"),
                new RecipeIngredient("Cheese", 50, "g")
        );

        // -----------------------------------------------------
        // 16. Tomato Pasta
        // -----------------------------------------------------

        addRecipe(
                db,
                "Tomato Pasta",
                "Simple pasta with tomato sauce.",
                "Cook pasta. Heat tomato sauce with onion and seasoning. Mix together and serve.",
                new RecipeIngredient("Pasta", 250, "g"),
                new RecipeIngredient("Tomato Sauce", 250, "ml"),
                new RecipeIngredient("Onion", 1, "pcs")
        );

        // -----------------------------------------------------
        // 17. Potato Curry
        // -----------------------------------------------------

        addRecipe(
                db,
                "Potato Curry",
                "Spicy potatoes cooked in a tomato curry sauce.",
                "Cook onions with curry powder. Add potatoes and tomatoes. Add water and simmer until potatoes are soft.",
                new RecipeIngredient("Potato", 4, "pcs"),
                new RecipeIngredient("Tomato", 2, "pcs"),
                new RecipeIngredient("Onion", 1, "pcs"),
                new RecipeIngredient("Curry Powder", 1, "tbsp")
        );

        // -----------------------------------------------------
        // 18. Egg Fried Rice
        // -----------------------------------------------------

        addRecipe(
                db,
                "Egg Fried Rice",
                "Quick fried rice with scrambled egg.",
                "Cook the egg. Add vegetables and cooked rice. Stir-fry with soy sauce.",
                new RecipeIngredient("Rice", 2, "cup"),
                new RecipeIngredient("Egg", 2, "pcs"),
                new RecipeIngredient("Carrot", 1, "pcs"),
                new RecipeIngredient("Soy Sauce", 2, "tbsp")
        );

        // -----------------------------------------------------
        // 19. Chicken Sandwich
        // -----------------------------------------------------

        addRecipe(
                db,
                "Chicken Sandwich",
                "Chicken sandwich with fresh vegetables.",
                "Cook the chicken. Place chicken, lettuce and tomato between slices of bread. Add mayonnaise.",
                new RecipeIngredient("Chicken", 150, "g"),
                new RecipeIngredient("Bread", 4, "slices"),
                new RecipeIngredient("Lettuce", 2, "leaves"),
                new RecipeIngredient("Tomato", 1, "pcs"),
                new RecipeIngredient("Mayonnaise", 1, "tbsp")
        );

        // -----------------------------------------------------
        // 20. Vegetable Soup
        // -----------------------------------------------------

        addRecipe(
                db,
                "Vegetable Soup",
                "Warm and healthy mixed vegetable soup.",
                "Chop the vegetables. Add them to stock and simmer until tender. Season and serve.",
                new RecipeIngredient("Carrot", 2, "pcs"),
                new RecipeIngredient("Potato", 2, "pcs"),
                new RecipeIngredient("Onion", 1, "pcs"),
                new RecipeIngredient("Vegetable Stock", 500, "ml")
        );
    }

    // =========================================================
    // CLOSE DATABASE
    // =========================================================

    @Override
    public synchronized void close() {
        super.close();
    }
}