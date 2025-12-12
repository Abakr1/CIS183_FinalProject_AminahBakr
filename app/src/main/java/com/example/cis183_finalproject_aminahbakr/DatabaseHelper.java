package com.example.cis183_finalproject_aminahbakr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper{

    //database configuration
    static final String DATABASE_NAME = "resources.db";
    static final int DATABASE_VERSION = 1;

    // users table
    static String TABLE_USERS  = "users";
    String COL_USER_ID  = "users_id";
    static String COL_USER_USERNAME = "username";
    String COL_USER_PASSWORD = "password";
    String COL_USER_NAME     = "name";
    static String COL_USER_EMAIL    = "email";
    static String COL_USER_CITY     = "city";
    static String COL_USER_ORG_ROLE = "org_rule";

    //categories table
    String TABLE_CATEGORIES  = "categories";
    static String COL_CATEGORY_ID   = "category_id";
    static String COL_CATEGORY_NAME = "category_name";

    //resources table
    String TABLE_RESOURCES   = "resources";
    String COL_RESOURCE_ID   = "resource_id";
    String COL_RESOURCE_USER_ID = "user_id";
    String COL_RESOURCE_CAT_ID  = "category_id";
    String COL_RESOURCE_ORG_NAME = "org_name";
    String COL_RESOURCE_ADDRESS  = "address";
    String COL_RESOURCE_CITY     = "city";
    String COL_RESOURCE_CONTACT  = "contact_info";
    String COL_RESOURCE_DESC     = "description";
    String COL_RESOURCE_DATE     = "date_added";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //users
        String createUsers = "CREATE TABLE " + TABLE_USERS + "("
        + COL_USER_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COL_USER_USERNAME + " TEXT UNIQUE NOT NULL, "
        + COL_USER_PASSWORD + " TEXT NOT NULL, "
        + COL_USER_NAME     + " TEXT, "
        + COL_USER_EMAIL    + " TEXT,"
        + COL_USER_CITY     + "TEXT,"
         + COL_USER_ORG_ROLE + "TEXT"
        + ");";

        //categories
        String createCategories = "CREATE TABLE " + TABLE_CATEGORIES + " ("
        + COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COL_CATEGORY_NAME + "TEXT UNIQUE NOT NULL" + ");";

        //resources
        String createResources = "CREATE TABLE" + TABLE_RESOURCES + "("
        + COL_RESOURCE_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COL_RESOURCE_USER_ID  + " INTEGER NOT NULL,"
        + COL_RESOURCE_CAT_ID   + "INTEGER NOT NULL,"
        + COL_RESOURCE_ORG_NAME + "TEXT NOT NULL,"
        + COL_RESOURCE_ADDRESS  + "TEXT,"
        + COL_RESOURCE_CITY     + "TEXT,"
        + COL_RESOURCE_CONTACT  + "TEXT,"
        + COL_RESOURCE_DESC     + "TEXT,"
        + COL_RESOURCE_DATE     + "TEXT,"
        + "FOREIGN KEY(" + COL_RESOURCE_USER_ID + ") REFERENCES "
        + TABLE_USERS + "(" + COL_USER_ID + "), "
        + "FOREIGN KEY(" + COL_RESOURCE_CAT_ID + ") REFRENCES "
        + TABLE_CATEGORIES + "(" + COL_CATEGORY_ID + ")" + ");";

        db.execSQL(createUsers);
        db.execSQL(createCategories);
        db.execSQL(createResources);

        insertDefaultCategories(db);
    }

    private void insertDefaultCategories(SQLiteDatabase db)
    {
        insertCategory(db, "Food");
        insertCategory(db, "Shelter");
        insertCategory(db, "Health");
    }

    private void insertCategory(SQLiteDatabase db, String name)
    {
        ContentValues cv = new ContentValues();
        cv.put(COL_CATEGORY_NAME, name);
        db.insert(TABLE_CATEGORIES, null, cv);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESOURCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);

    }

    public long insertUser(String username, String password, String name, String email, String city, String orgRole) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put(COL_USER_USERNAME, username);
        cv.put(COL_USER_PASSWORD, password);
        cv.put(COL_USER_NAME, name);
        cv.put(COL_USER_EMAIL, email);
        cv.put(COL_USER_CITY, city);
        cv.put(COL_USER_ORG_ROLE, orgRole);
        return db.insert(TABLE_USERS, null, cv);
    }

    //for the login screen
    public boolean checkUserLogin(String username, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cols = { COL_USER_ID };
        String sel = COL_USER_USERNAME + "=? AND " + COL_USER_PASSWORD + "=?";
        String[] args = { username, password };

        Cursor c = db.query(TABLE_USERS, cols, sel, args, null, null, null);
        boolean ok = (c != null && c.moveToFirst());
        if (c != null)
        {
            c.close();
        }

        return ok;
    }

    //helpers for resources
    public long insertResource(long userId, long categoryId, String orgName, String address, String city, String contactInfo, String description, String dateAdded) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_RESOURCE_USER_ID, userId);
        cv.put(COL_RESOURCE_CAT_ID, categoryId);
        cv.put(COL_RESOURCE_ORG_NAME, orgName);
        cv.put(COL_RESOURCE_ADDRESS, address);
        cv.put(COL_RESOURCE_CITY, city);
        cv.put(COL_RESOURCE_CONTACT, contactInfo);
        cv.put(COL_RESOURCE_DESC, description);
        cv.put(COL_RESOURCE_DATE, dateAdded);
        return db.insert(TABLE_RESOURCES, null, cv);
    }

    //for the spinner
    public Cursor getALLCategories()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CATEGORIES, new String[]{ COL_CATEGORY_ID, COL_CATEGORY_NAME}, null, null, null, null,COL_CATEGORY_NAME);
    }

    public Cursor getResourcesByCityAndCategory(String city, long categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sel = COL_RESOURCE_CITY + "=? AND " + COL_RESOURCE_CAT_ID + "=?";
        String[] args = { city, String.valueOf(categoryId) };
        return db.query(TABLE_RESOURCES, null, sel, args, null, null, COL_RESOURCE_ORG_NAME);
    }
}
