package com.example.pizzarestaurantproject.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.pizzarestaurantproject.User;

import java.security.NoSuchAlgorithmException;

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
                        "TIME_OF_ORDER DATETIME, " +
                        "FOREIGN KEY (EMAIL) REFERENCES users(EMAIL))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS orders");
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
} // end class
