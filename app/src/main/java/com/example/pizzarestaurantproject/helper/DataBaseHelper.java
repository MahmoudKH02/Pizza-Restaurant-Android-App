package com.example.pizzarestaurantproject.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.pizzarestaurantproject.Order;
import com.example.pizzarestaurantproject.R;
import com.example.pizzarestaurantproject.models.Pizzas;
import com.example.pizzarestaurantproject.models.SpecialOffer;
import com.example.pizzarestaurantproject.models.User;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends android.database.sqlite.SQLiteOpenHelper {

    public DataBaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE users(EMAIL TEXT PRIMARY KEY, PASSWORD TEXT, SALT TEXT," +
                        "FIRST_NAME TEXT, LAST_NAME TEXT," +
                        "PHONE TEXT, GENDER TEXT," +
                        "PROFILE_PIC TEXT," +
                        "IS_ADMIN INTEGER)"
        );
        db.execSQL(
                "CREATE TABLE orders(ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "EMAIL TEXT," +
                        "PRICE REAL," +
                        "TIME_OF_ORDER DATETIME, " +
                        "QUANTITY INTEGER," +
                        "PIZZA_TYPE TEXT," +
                        "SIZE TEXT," +
                        "IMAGE_RESOURCE_ID INTEGER," +
                        "FOREIGN KEY (EMAIL) REFERENCES users(EMAIL))"
        );
        db.execSQL(
                "CREATE TABLE favorites(" +
                        "FAVORITE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "EMAIL TEXT," +
                        "PIZZA_TYPE TEXT," +
                        "DESCRIPTION TEXT," + // Add missing comma and specify TEXT data type
                        "PRICE REAL," + // Change data type to REAL for price
                        "SIZE TEXT," +
                        "CATEGORY TEXT," + // Specify TEXT data type
                        "IMAGE_RESOURCE_ID INTEGER," + // Change data type to INTEGER for imageResourceId
                        "FOREIGN KEY (EMAIL) REFERENCES users(EMAIL))"
        );
        db.execSQL(
                "CREATE TABLE special_offers(" +
                        "OFFER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "TITLE TEXT," +
                        "DESCRIPTION TEXT," +
                        "PRICE REAL," +
                        "IMAGE_URL INTEGER," +
                        "QUANTITY INTEGER," +
                        "CATEGORY TEXT," +
                        "SIZE TEXT)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS favorites");
        db.execSQL("DROP TABLE IF EXISTS special_offers");
        onCreate(db);
    }

    public void insertUser(User user, Bitmap profilePicture) throws NoSuchAlgorithmException {
//        String filePath = saveImageToInternalStorage(profilePicture);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String salt = PasswordHashing.getSalt();
        String hashedPassword = PasswordHashing.hashPassword(user.getPassword(), salt);

        Log.d("Password: ", hashedPassword);

        values.put("EMAIL", user.getEmail());
        values.put("PASSWORD", hashedPassword);
        values.put("SALT", salt);
        values.put("FIRST_NAME", user.getFirstName());
        values.put("LAST_NAME", user.getLastName());
        values.put("PHONE", user.getPhoneNumber());
        values.put("GENDER", user.getGender());
        values.put("PROFILE_PIC", user.getProfilePicturePath());
        values.put("IS_ADMIN", (user.isAdmin())? 1:0);

        db.insert("users", null, values);
        db.close();
    }

    public Cursor getAllUsers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM users", null);
    }

    public Cursor getUser(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM users WHERE users.EMAIL=='" + email + "'", null);
    }
    public void insertOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("EMAIL", order.getUserEmail());
        values.put("PRICE", order.getPrice());
        values.put("TIME_OF_ORDER", order.getFormattedDate());
        values.put("QUANTITY", order.getQuantity());
        values.put("PIZZA_TYPE", order.getPizzaType());
        values.put("SIZE", order.getSize());
        values.put("IMAGE_RESOURCE_ID", order.getImageResourceId());
        db.insert("orders", null, values);
        db.close();
    }

    public List<Order> getOrders(String email) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orders WHERE EMAIL = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String pizzaType = cursor.getString(cursor.getColumnIndex("PIZZA_TYPE"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("PRICE"));
                @SuppressLint("Range") String timeOfOrder = cursor.getString(cursor.getColumnIndex("TIME_OF_ORDER"));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex("SIZE"));
                @SuppressLint("Range") int imageResourceId = cursor.getInt(cursor.getColumnIndex("IMAGE_RESOURCE_ID"));
                orders.add(new Order(email, pizzaType, price, timeOfOrder, quantity, size, imageResourceId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orders;
    }

    public void insertFavorite(String email, String pizzaType, String description, double price, String size, String category, int imageResourceId) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            // Check if the pizza already exists in favorites
            if (!isPizzaInFavorites(email, pizzaType, db)) {
                ContentValues values = new ContentValues();
                values.put("EMAIL", email);
                values.put("PIZZA_TYPE", pizzaType);
                values.put("DESCRIPTION", description);
                values.put("PRICE", price);
                values.put("SIZE", size);
                values.put("CATEGORY", category);
                values.put("IMAGE_RESOURCE_ID", imageResourceId);
                db.insert("favorites", null, values);
            }
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    private boolean isPizzaInFavorites(String email, String pizzaType, SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM favorites WHERE EMAIL = ? AND PIZZA_TYPE = ?", new String[]{email, pizzaType});
            return cursor.getCount() > 0;
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void deleteFavorite(String email, String pizzaType) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("favorites", "EMAIL = ? AND PIZZA_TYPE = ?", new String[]{email, pizzaType});
        db.close();
    }

    public List<Pizzas> getFavorites(String email) {
        List<Pizzas> favorites = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM favorites WHERE EMAIL = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String pizzaType = cursor.getString(cursor.getColumnIndex("PIZZA_TYPE"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("PRICE"));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex("SIZE"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("CATEGORY"));
                @SuppressLint("Range") int imageResourceId = cursor.getInt(cursor.getColumnIndex("IMAGE_RESOURCE_ID"));

                Pizzas pizza = new Pizzas(pizzaType, description, price, size, category, imageResourceId);
                favorites.add(pizza);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return favorites;
    }

    public void updateUser(User user) {
        // Get a writable instance of the database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PASSWORD", user.getPassword());
        values.put("FIRST_NAME", user.getFirstName());
        values.put("LAST_NAME", user.getLastName());
        values.put("PHONE", user.getPhoneNumber());
        values.put("GENDER", user.getGender());
        values.put("PROFILE_PIC", user.getProfilePicturePath());
        values.put("IS_ADMIN", user.isAdmin() ? 1 : 0); // Assuming `isAdmin` returns a boolean

        String whereClause = "EMAIL = ?";
        String[] whereArgs = { user.getEmail() };

        int rowsAffected = db.update("users", values, whereClause, whereArgs);

        Log.d("Database", "Number of rows updated: " + rowsAffected);

        db.close();
    }
    public void insertSpecialOffer(String title, String description, double price, int imageUrl, int quantity, String size, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TITLE", title);
        values.put("DESCRIPTION", description);
        values.put("PRICE", price);
        values.put("IMAGE_URL", imageUrl);
        values.put("QUANTITY", quantity);
        values.put("SIZE", size);
        values.put("CATEGORY", category); // Add the category to the ContentValues
        db.insert("special_offers", null, values);
        db.close();
    }



    public List<SpecialOffer> getSpecialOffers() {
        List<SpecialOffer> specialOffers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM special_offers", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int offerId = cursor.getInt(cursor.getColumnIndex("OFFER_ID"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("TITLE"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("PRICE"));
                @SuppressLint("Range") int imageUrl = cursor.getInt(cursor.getColumnIndex("IMAGE_URL"));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex("SIZE"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("CATEGORY")); // Retrieve the category
                SpecialOffer specialOffer = new SpecialOffer(offerId, title, description, price, imageUrl, quantity, size, category);
                specialOffers.add(specialOffer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return specialOffers;
    }


} // end class
