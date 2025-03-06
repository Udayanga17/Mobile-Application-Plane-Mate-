package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    // Database name and version (increment version to trigger onUpgrade)
    private static final String DB_NAME = "userdb2";
    private static final int DB_VERSION = 5; // Incremented version to add shop_details table

    // User table details
    private static final String USER_TABLE = "myUsers";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String PHONE_COL = "phoneNumber";
    private static final String PASSWORD_COL = "password";



    private static final String SELLER_TABLE = "sellers";
    private static final String SELLERID_COL = "sellerid";
    private static final String SELLERNAME_COL = "sellername";
    private static final String SELLERPHONE_COL = "sellerphoneNumber";
    private static final String SELLERPASSWORD_COL = "sellerpassword";
    private static final String SELLERID_CATEGORY = "sellorcategory";

    // Event table details
    private static final String EVENT_TABLE = "events";
    private static final String EVENT_ID_COL = "event_id";
    private static final String EVENT_NAME_COL = "event_name";
    private static final String EVENT_DATE_COL = "event_date";
    private static final String EVENT_IMAGE_COL = "event_image"; // Store image URI as TEXT
    private static final String EVENT_BUDGET_COL = "event_budget"; // New column
    private static final String EVENT_TYPE_COL = "event_type"; // New column for event type
    private static final String EVENT_TASKS_COL = "event_tasks"; // New column for tasks

    // Shop details table details
    private static final String SHOP_DETAILS_TABLE = "shop_details";
    private static final String REG_NO_COL = "regNo";
    private static final String SHOP_CATEGORY_COL = "shopCategory";
    private static final String SHOP_NAME_COL = "shopName";
    private static final String SHOP_IMAGE_COL = "shopImage";
    private static final String EVENT_TYPE_COL_SHOP = "eventType";


    // Constructor

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create user table
        String createUserTable = "CREATE TABLE " + USER_TABLE + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT, "
                + PHONE_COL + " TEXT, "
                + PASSWORD_COL + " TEXT);";
        db.execSQL(createUserTable);

        // Create seller table
        String createSellerTable = "CREATE TABLE " + SELLER_TABLE + " ("
                + SELLERID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SELLERNAME_COL + " TEXT, "
                + SELLERPHONE_COL + " TEXT, "
                + SELLERPASSWORD_COL + " TEXT, "
                + SELLERID_CATEGORY + " TEXT);";
        db.execSQL(createSellerTable);
        System.out.println("Seller table created");

        // Create event table
        String createEventTable = "CREATE TABLE " + EVENT_TABLE + " ("
                + EVENT_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EVENT_NAME_COL + " TEXT, "
                + EVENT_DATE_COL + " TEXT, "
                + EVENT_IMAGE_COL + " TEXT, "
                + EVENT_BUDGET_COL + " REAL, "
                + EVENT_TYPE_COL + " TEXT, "
                + EVENT_TASKS_COL + " TEXT);";
        db.execSQL(createEventTable);

        // Create shop_details table
        String createShopDetailsTable = "CREATE TABLE " + SHOP_DETAILS_TABLE + " ("
                + REG_NO_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SHOP_CATEGORY_COL + " TEXT, "
                + SHOP_NAME_COL + " TEXT, "
                + SHOP_IMAGE_COL + " TEXT, "
                + EVENT_TYPE_COL_SHOP + " TEXT);";
        db.execSQL(createShopDetailsTable);

        // Insert sample data
        insertSampleShops(db);
        insertSampleSellers(db);
    }

    // Upgrade database to add new columns or tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + EVENT_TABLE + " ADD COLUMN " + EVENT_BUDGET_COL + " REAL DEFAULT 0;");
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + EVENT_TABLE + " ADD COLUMN " + EVENT_TYPE_COL + " TEXT;");
            db.execSQL("ALTER TABLE " + EVENT_TABLE + " ADD COLUMN " + EVENT_TASKS_COL + " TEXT;");
        }
        if (oldVersion < 4) {
            // Add shop_details table if upgrading from a version less than 4
            String createShopDetailsTable = "CREATE TABLE IF NOT EXISTS " + SHOP_DETAILS_TABLE + " ("
                    + REG_NO_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SHOP_CATEGORY_COL + " TEXT, "
                    + SHOP_NAME_COL + " TEXT, "
                    + SHOP_IMAGE_COL + " TEXT, "
                    + EVENT_TYPE_COL_SHOP + " TEXT);";
            db.execSQL(createShopDetailsTable);
        }
        if (oldVersion < 5) {
            // Add seller table if upgrading from a version less than 5
            String createSellerTable = "CREATE TABLE IF NOT EXISTS " + SELLER_TABLE + " ("
                    + SELLERID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SELLERNAME_COL + " TEXT, "
                    + SELLERPHONE_COL + " TEXT, "
                    + SELLERPASSWORD_COL + " TEXT, "
                    + SELLERID_CATEGORY + " TEXT);";
            db.execSQL(createSellerTable);
        }
    }

    // Add new user
    public void addNewUser(String userName, String phoneNumber, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, userName);
        values.put(PHONE_COL, phoneNumber);
        values.put(PASSWORD_COL, password);
        db.insert(USER_TABLE, null, values);
        db.close();
    }

    // Validate user login
    public boolean checkPassword(String userName, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + NAME_COL + " = ? AND " + PASSWORD_COL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userName, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }
    public boolean checkSellerPassword(String sellerName, String sellerPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + SELLER_TABLE + " WHERE " + SELLERNAME_COL + " = ? AND " + SELLERPASSWORD_COL + " = ?";
            cursor = db.rawQuery(query, new String[]{sellerName, sellerPassword});
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }
    // Modify addNewEvent method to include budget, event type, and tasks
    public void addNewEvent(String eventName, String eventDate, String imagePath, double eventBudget, String eventType, String tasks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_NAME_COL, eventName);
        values.put(EVENT_DATE_COL, eventDate);
        values.put(EVENT_IMAGE_COL, imagePath);
        values.put(EVENT_BUDGET_COL, eventBudget);
        values.put(EVENT_TYPE_COL, eventType);
        values.put(EVENT_TASKS_COL, tasks);

        db.insert(EVENT_TABLE, null, values);
        db.close();
    }

    // Retrieve all events from the database
    public Cursor getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + EVENT_TABLE, null);
    }

    // Delete an event from the database
    public void deleteEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EVENT_TABLE, EVENT_ID_COL + "=?", new String[]{String.valueOf(eventId)});
        db.close();
    }

    public List<WeddingShopModel> getShopsByCategory(String category) {
        List<WeddingShopModel> shops = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SHOP_DETAILS_TABLE + " WHERE " + SHOP_CATEGORY_COL + " = ?", new String[]{category});

        if (cursor.moveToFirst()) {
            do {
                int regNo = cursor.getInt(cursor.getColumnIndexOrThrow(REG_NO_COL));
                String shopCategory = cursor.getString(cursor.getColumnIndexOrThrow(SHOP_CATEGORY_COL));
                String shopName = cursor.getString(cursor.getColumnIndexOrThrow(SHOP_NAME_COL));
                String shopImage = cursor.getString(cursor.getColumnIndexOrThrow(SHOP_IMAGE_COL));
                String eventType = cursor.getString(cursor.getColumnIndexOrThrow(EVENT_TYPE_COL_SHOP));

                shops.add(new WeddingShopModel(regNo, shopCategory, shopName, shopImage, eventType));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return shops;
    }
    // Method to insert sample data
    private void insertSampleShops(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + SHOP_DETAILS_TABLE + " (" +
                SHOP_CATEGORY_COL + ", " + SHOP_NAME_COL + ", " + SHOP_IMAGE_COL + ", " + EVENT_TYPE_COL_SHOP + ") VALUES " +
                "('Hotel', 'Grand Palace Hotel', 'hotel1.jpg', 'Wedding'), " +
                "('Hotel', 'Luxury Inn', 'hotel2.jpg', 'Wedding'), " +
                "('Decoration', 'Elegant Events Decor', 'decor1.jpg', 'Birthday'), " +
                "('Photography', 'Dream Capture Studio', 'photo1.jpg', 'Corporate'), " +
                "('Catering', 'Delicious Bites Catering', 'catering1.jpg', 'Engagement');");
    }
    // Method to insert sample seller data
    // Method to insert sample seller data
    private void insertSampleSellers(SQLiteDatabase db) {
        // Seller 1
        ContentValues seller1 = new ContentValues();
        seller1.put(SELLERNAME_COL, "seller1");
        seller1.put(SELLERPHONE_COL, "1234567890"); // Dummy phone number
        seller1.put(SELLERPASSWORD_COL, "123"); // Password
        seller1.put(SELLERID_CATEGORY, "Category1"); // Dummy category
        long seller1Id = db.insert(SELLER_TABLE, null, seller1);
        System.out.println("Seller 1 inserted with ID: " + seller1Id);

        // Seller 2
        ContentValues seller2 = new ContentValues();
        seller2.put(SELLERNAME_COL, "seller2");
        seller2.put(SELLERPHONE_COL, "0987654321"); // Dummy phone number
        seller2.put(SELLERPASSWORD_COL, "321"); // Password
        seller2.put(SELLERID_CATEGORY, "Category2"); // Dummy category
        long seller2Id = db.insert(SELLER_TABLE, null, seller2);
        System.out.println("Seller 2 inserted with ID: " + seller2Id);
    }
    // Add new seller
    public void addNewSeller(String sellerName, String sellerPhone, String sellerPassword, String sellerCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SELLERNAME_COL, sellerName);
        values.put(SELLERPHONE_COL, sellerPhone);
        values.put(SELLERPASSWORD_COL, sellerPassword);
        values.put(SELLERID_CATEGORY, sellerCategory);
        db.insert(SELLER_TABLE, null, values);
        db.close();
    }
    // Add new shop
    public void addNewShop(String shopName, String shopCategory, String shopImage, String eventType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SHOP_NAME_COL, shopName);
        values.put(SHOP_CATEGORY_COL, shopCategory);
        values.put(SHOP_IMAGE_COL, shopImage);
        values.put(EVENT_TYPE_COL_SHOP, eventType);
        db.insert(SHOP_DETAILS_TABLE, null, values);
        db.close();
    }

}