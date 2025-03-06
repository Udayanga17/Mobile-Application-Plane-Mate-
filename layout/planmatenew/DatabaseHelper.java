package com.example.planmatenew;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PlanMateDataBase.db";
    private static final int DATABASE_VERSION = 1;

    //table names
    public static final String TABLE_SELLERS = "sellers";
    private static final String TABLE_ITEMS = "Items";

    //common column



    //sellers table column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_SELLER_NAME = "name";
    public static final String COLUMN_SELLER_CATEGORY = "category";
    public static final String COLUMN_SELLER_PHONE = "phone";
    public static final String COLUMN_SELLER_ADDRESS = "address";
    public static final String COLUMN_SELLER_DESCRIPTION = "description";
    public static final String COLUMN_SELLER_IMAGE = "image"; // Store image as BLOB


    //Items table column names
    private static final String COLUMN_ID_ITEM = "id";
    public static final String COLUMN_SELLER_USERNAME = "username";
    private static final String COLUMN_ITEM_NO = "item_no";
    private static final String COLUMN_ITEM_NAME = "item_name";
    private static final String COLUMN_ITEM_DESCRIPTION = "description";
    private static final String COLUMN_ITEM_PRICE = "price";
    public static final String COLUMN_ITEM_IMAGE = "image";


    //table creation queries
    private static final String CREATE_SELLERS_TABLE = "CREATE TABLE " + TABLE_SELLERS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + " TEXT, " +
            COLUMN_SELLER_NAME + " TEXT, " +
            COLUMN_SELLER_CATEGORY + " TEXT, " +
            COLUMN_SELLER_PHONE + " TEXT, " +
            COLUMN_SELLER_ADDRESS + " TEXT, " +
            COLUMN_SELLER_DESCRIPTION + " TEXT, " +
            COLUMN_SELLER_IMAGE + " BLOB)";


    //Item Table
    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + " (" +
            COLUMN_ID_ITEM + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SELLER_USERNAME + " TEXT, " +
            COLUMN_ITEM_NO + " TEXT, " +
            COLUMN_ITEM_NAME + " TEXT, " +
            COLUMN_ITEM_DESCRIPTION + " TEXT, " +
            COLUMN_ITEM_PRICE + " REAL, " +
            COLUMN_ITEM_IMAGE + " BLOB,"+
            "FOREIGN KEY(" + COLUMN_SELLER_USERNAME + ") REFERENCES " + TABLE_SELLERS + "(" + COLUMN_USERNAME + "))";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SELLERS_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELLERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    // Method to insert user data
    public boolean insertUser( String username, String name, String category, String phone, String address, String description, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_SELLER_NAME, name);
        contentValues.put(COLUMN_SELLER_CATEGORY, category);
        contentValues.put(COLUMN_SELLER_PHONE, phone);
        contentValues.put(COLUMN_SELLER_ADDRESS, address);
        contentValues.put(COLUMN_SELLER_DESCRIPTION, description);
        contentValues.put(COLUMN_SELLER_IMAGE, image);

        long result = db.insert(TABLE_SELLERS, null, contentValues);
        return result != -1; // Return true if insertion is successful

    }


    // Insert Item data into SQLite
    public boolean insertItem(String itemNo,String itemName, String description, String price, String username, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ITEM_NO, itemNo);
        values.put(COLUMN_ITEM_NAME, itemName);
        values.put(COLUMN_ITEM_DESCRIPTION, description);
        double priceValue = Double.parseDouble(price);
        values.put(COLUMN_ITEM_PRICE, priceValue);
        values.put(COLUMN_SELLER_USERNAME, username);
        values.put(COLUMN_ITEM_IMAGE, image);

        long result = db.insert(TABLE_ITEMS, null, values);
        return result != -1; // Return true if insertion is successful


    }


    // Method to retrieve seller details using phone number
    public Cursor getSellerByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SELLERS + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});

    }



    // Retrieve all users
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SELLERS, null);
    }


    ///update seller
    public boolean updateUser(String username, String name, String phone, String address, String description, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_SELLER_NAME, name);
        contentValues.put(COLUMN_SELLER_PHONE, phone);
        contentValues.put(COLUMN_SELLER_ADDRESS, address);
        contentValues.put(COLUMN_SELLER_DESCRIPTION, description);
        contentValues.put(COLUMN_SELLER_IMAGE, image);

        int result = db.update(TABLE_SELLERS, contentValues, COLUMN_USERNAME + "=?", new String[]{username});

        return result > 0;  // Returns true if update is successful
    }

    //recyclerview
    // Retrieve all items for a specific seller (filtered by username)
    public Cursor getAllItems(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_SELLER_USERNAME + " = ?", new String[]{username});
    }




}


