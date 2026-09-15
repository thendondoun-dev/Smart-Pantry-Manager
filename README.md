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
- Store information using a local SQLite database

---

## Technologies Used

- Android Studio
- Java
- SQLite
- Android Activities
- RecyclerView
- Custom RecyclerView Adapters
- SQLiteOpenHelper
- Intents
- EditText
- Button
- Spinner
- TextView
- AlertDialog

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

Users can also edit and delete existing pantry items.

---

### 2. Search Pantry

The pantry screen includes a search feature.

Users can search for pantry items using:

- Food name
- Category
- Unit

This makes it easier to find ingredients when the pantry contains many items.

---

### 3. Recipe Suggestions

The application contains 20 preloaded recipes.

The application checks the ingredients stored in the pantry before displaying a recipe as a suggestion.

A recipe is only suggested when all required ingredients are available in sufficient quantities.

---

### 4. Recipe Matching

The recipe matching system compares the ingredients required by a recipe with the ingredients available in the pantry.

For example:

Recipe requires:

Chicken - 200 g

Pantry contains:

Chicken - 250 g

Result:

Recipe can be made.

However, if the pantry contains:

Chicken - 100 g

Result:

Recipe cannot be made.

The application does not suggest a recipe when an ingredient is missing or when the available quantity is insufficient.

The application also supports compatible unit conversions.

Supported conversions include:

- kg to g
- g to kg
- L to ml
- ml to L

---

## 5. Recipe Details

Users can select a suggested recipe to view:

- Recipe name
- Recipe description
- Required ingredients
- Required quantities
- Measurement units
- Preparation instructions

---

## 6. Expiry Dates

Users can optionally add an expiry date to a pantry item.

The pantry screen can indicate when an item:

- Has not specified an expiry date
- Is still valid
- Is close to expiry
- Has expired

---

# Application Screens

The application contains multiple screens.

## Main Dashboard

The main dashboard provides navigation to:

- My Pantry
- Suggested Recipes
- Settings

---

## My Pantry

The pantry screen allows users to:

- View pantry items
- Search ingredients
- Add ingredients
- Edit ingredients
- Delete ingredients

---

## Add Food Item

The Add Food Item screen allows users to enter a new pantry ingredient.

Users can enter:

- Food name
- Quantity
- Unit
- Category
- Expiry date

---

## Edit Food Item

The Edit Food Item screen allows users to modify existing pantry information.

---

## Suggested Recipes

The Suggested Recipes screen displays recipes that can currently be prepared using the ingredients in the pantry.

---

## Recipe Details

The Recipe Details screen displays the selected recipe's:

- Name
- Description
- Ingredients
- Quantities
- Instructions

---

## Settings

The Settings screen provides application information such as:

- Application name
- Application version
- Technology used

---

# Database

Smart Pantry Manager uses SQLite as its local database.

The database is managed using the `DatabaseHelper` class.

The application uses three main tables.

---

## Food Items Table

The `food_items` table stores pantry ingredients.

Fields:

```text
id
name
quantity
unit
category
expiry_date