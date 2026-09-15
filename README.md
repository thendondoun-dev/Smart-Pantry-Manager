# Smart Pantry Manager

## Project Description

Smart Pantry Manager is an Android application developed using Java and SQLite.

The purpose of the application is to help users manage food ingredients in their pantry and discover recipes that can be prepared using the ingredients they currently have available.

The application allows users to:

- Add food items to their pantry
- Edit food items
- Delete food items
- Store food quantities
- Select measurement units
- Select food categories
- Record optional expiry dates
- Search pantry items
- View suggested recipes
- View recipe ingredients
- View recipe preparation instructions
- Use a local SQLite database for persistent storage

---

## Technologies Used

### Programming Language

- Java

### Development Environment

- Android Studio

### Database

- SQLite

### Android Components

- Activities
- RecyclerView
- Custom RecyclerView Adapters
- Intents
- SQLiteOpenHelper
- AlertDialog
- EditText
- Button
- Spinner
- TextView

---

## Main Features

### 1. Pantry Management

Users can add food items to their pantry.

Each food item can contain:

- Food name
- Quantity
- Unit
- Category
- Expiry date

Users can also edit or delete existing pantry items.

---

### 2. Search Pantry

The pantry screen includes a search function.

Users can search pantry items by:

- Name
- Category
- Unit

---

### 3. Recipe Suggestions

The application contains 20 preloaded recipes.

Recipes are stored in the SQLite database.

The application checks the ingredients in the user's pantry before suggesting a recipe.

A recipe is only suggested when the user has every required ingredient in a sufficient quantity.

---

### 4. Recipe Matching

The recipe matching system compares the required recipe ingredients with the ingredients stored in the pantry.

For example:

If a recipe requires:

    Chicken - 200 g

and the pantry contains:

    Chicken - 250 g

the ingredient is considered available.

However, if the pantry contains:

    Chicken - 100 g

the recipe will not be suggested.

The application also supports compatible unit conversions such as:

- grams to kilograms
- kilograms to grams
- millilitres to litres
- litres to millilitres

---

### 5. Recipe Details

Users can select a suggested recipe to view:

- Recipe name
- Description
- Required ingredients
- Required quantities
- Preparation instructions

---

## Application Screens

The application contains multiple screens.

### Main Dashboard

Provides navigation to:

- My Pantry
- Suggested Recipes
- Settings

### Pantry Screen

Allows users to:

- View pantry items
- Search ingredients
- Add ingredients
- Edit ingredients
- Delete ingredients

### Add Food Screen

Allows users to enter a new pantry item.

### Edit Food Screen

Allows users to modify an existing pantry item.

### Suggested Recipes Screen

Displays recipes that can currently be made using the pantry.

### Recipe Details Screen

Displays the selected recipe's ingredients and preparation instructions.

### Settings Screen

Displays application information and version details.

---

## Database Structure

The application uses SQLite for local data storage.

### Food Items Table

The `food_items` table stores pantry information.

Fields include:

- id
- name
- quantity
- unit
- category
- expiry_date

### Recipes Table

The `recipes` table stores recipe information.

Fields include:

- id
- name
- description
- instructions

### Recipe Ingredients Table

The `recipe_ingredients` table stores the ingredients required by each recipe.

Fields include:

- id
- recipe_id
- ingredient_name
- required_quantity
- required_unit

---

## Preloaded Recipes

The application includes 20 recipes:

1. Chicken Fried Rice
2. Spaghetti Bolognese
3. Vegetable Stir Fry
4. Beef Stew
5. Chicken Pasta
6. Tuna Sandwich
7. Omelette
8. Pancakes
9. French Toast
10. Chicken Curry
11. Beef Pasta
12. Vegetable Pasta
13. Rice and Beans
14. Chicken Wrap
15. Beef Burger
16. Tomato Pasta
17. Potato Curry
18. Egg Fried Rice
19. Chicken Sandwich
20. Vegetable Soup

---

## How to Run the Application

### Step 1

Open the project in Android Studio.

### Step 2

Allow Android Studio to complete Gradle synchronization.

### Step 3

Connect an Android device or start an Android Emulator.

### Step 4

Click the Run button in Android Studio.

### Step 5

The Smart Pantry Manager application will launch.

---

## How to Use the Application

### Adding Food

1. Open the application.
2. Select **My Pantry**.
3. Select **Add Food Item**.
4. Enter the food name.
5. Enter the quantity.
6. Select the unit.
7. Select the category.
8. Optionally enter an expiry date.
9. Select **Save Food**.

### Editing Food

1. Open **My Pantry**.
2. Find the food item.
3. Select **Edit**.
4. Change the required information.
5. Select **Update Food**.

### Deleting Food

1. Open **My Pantry**.
2. Find the food item.
3. Select **Delete**.
4. Confirm the deletion.

### Finding Recipes

1. Add ingredients to the pantry.
2. Return to the main dashboard.
3. Select **Suggested Recipes**.
4. The application checks the pantry.
5. Only recipes that can be completely prepared are displayed.
6. Select a recipe to view its details.

---

## Input Validation

The application validates user input when adding and editing food items.

Examples include:

- Food name cannot be empty.
- Quantity must be entered.
- Quantity must be a valid number.
- Quantity must be greater than zero.
- Required selections must be provided.

This helps prevent invalid data from being stored in the database.

---

## Data Persistence

The application uses SQLite so that pantry information is stored locally on the device.

The data remains available when the user closes and reopens the application.

The database is managed using the `DatabaseHelper` class, which extends `SQLiteOpenHelper`.

---

## Project Structure

```text
SmartPantryManager
│
├── app
│   └── src
│       └── main
│           ├── java
│           │   └── com.example.smartpantrymanager
│           │       ├── MainActivity.java
│           │       ├── PantryActivity.java
│           │       ├── AddFoodActivity.java
│           │       ├── EditFoodActivity.java
│           │       ├── SuggestedRecipesActivity.java
│           │       ├── RecipeDetailActivity.java
│           │       ├── SettingsActivity.java
│           │       ├── DatabaseHelper.java
│           │       ├── FoodAdapter.java
│           │       ├── RecipeAdapter.java
│           │       ├── FoodItem.java
│           │       ├── Recipe.java
│           │       ├── RecipeIngredient.java
│           │       └── RecipeMatcher.java
│           │
│           └── res
│               ├── layout
│               ├── drawable
│               └── values
│
└── README.md