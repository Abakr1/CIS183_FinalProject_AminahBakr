package com.example.cis183_finalproject_aminahbakr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "resources.db";
    public static final int DATABASE_VERSION = 4; // bumped for UNIQUE + seeding

    // -------- USERS --------
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_USER_USERNAME = "username";
    public static final String COL_USER_PASSWORD = "password";
    public static final String COL_USER_NAME = "name";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_CITY = "city";
    public static final String COL_USER_ORG_ROLE = "org_role"; // "Yes"/"No" or role text

    // -------- CATEGORIES --------
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COL_CATEGORY_ID = "category_id";
    public static final String COL_CATEGORY_NAME = "category_name";

    // -------- RESOURCES --------
    public static final String TABLE_RESOURCES = "resources";
    public static final String COL_RESOURCE_ID = "resource_id";
    public static final String COL_RESOURCE_USER_ID = "user_id";
    public static final String COL_RESOURCE_CAT_ID = "category_id";
    public static final String COL_RESOURCE_ORG_NAME = "org_name";
    public static final String COL_RESOURCE_ADDRESS = "address";
    public static final String COL_RESOURCE_CITY = "city";
    public static final String COL_RESOURCE_CONTACT = "contact_info";
    public static final String COL_RESOURCE_DESC = "description";
    public static final String COL_RESOURCE_DATE = "date_added";

    // -------- FAVORITES --------
    public static final String TABLE_FAVORITES = "favorites";
    public static final String COL_FAV_USER_ID = "user_id";
    public static final String COL_FAV_RESOURCE_ID = "resource_id";
    public static final String COL_FAV_DATE = "date_added";

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

        String createUsers =
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_USER_USERNAME + " TEXT UNIQUE NOT NULL, " +
                        COL_USER_PASSWORD + " TEXT NOT NULL, " +
                        COL_USER_NAME + " TEXT, " +
                        COL_USER_EMAIL + " TEXT, " +
                        COL_USER_CITY + " TEXT, " +
                        COL_USER_ORG_ROLE + " TEXT" +
                        ");";

        String createCategories =
                "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                        COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_CATEGORY_NAME + " TEXT UNIQUE NOT NULL" +
                        ");";

        // UNIQUE(org_name, city, category_id) ensures no duplicates per city/category
        String createResources =
                "CREATE TABLE " + TABLE_RESOURCES + " (" +
                        COL_RESOURCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_RESOURCE_USER_ID + " INTEGER NOT NULL, " +
                        COL_RESOURCE_CAT_ID + " INTEGER NOT NULL, " +
                        COL_RESOURCE_ORG_NAME + " TEXT NOT NULL, " +
                        COL_RESOURCE_ADDRESS + " TEXT, " +
                        COL_RESOURCE_CITY + " TEXT NOT NULL, " +
                        COL_RESOURCE_CONTACT + " TEXT, " +
                        COL_RESOURCE_DESC + " TEXT, " +
                        COL_RESOURCE_DATE + " TEXT, " +
                        "UNIQUE(" + COL_RESOURCE_ORG_NAME + ", " + COL_RESOURCE_CITY + ", " + COL_RESOURCE_CAT_ID + "), " +
                        "FOREIGN KEY(" + COL_RESOURCE_USER_ID + ") REFERENCES " +
                        TABLE_USERS + "(" + COL_USER_ID + ") ON DELETE CASCADE, " +
                        "FOREIGN KEY(" + COL_RESOURCE_CAT_ID + ") REFERENCES " +
                        TABLE_CATEGORIES + "(" + COL_CATEGORY_ID + ") ON DELETE CASCADE" +
                        ");";

        String createFavorites =
                "CREATE TABLE " + TABLE_FAVORITES + " (" +
                        COL_FAV_USER_ID + " INTEGER NOT NULL, " +
                        COL_FAV_RESOURCE_ID + " INTEGER NOT NULL, " +
                        COL_FAV_DATE + " TEXT, " +
                        "PRIMARY KEY(" + COL_FAV_USER_ID + ", " + COL_FAV_RESOURCE_ID + "), " +
                        "FOREIGN KEY(" + COL_FAV_USER_ID + ") REFERENCES " +
                        TABLE_USERS + "(" + COL_USER_ID + ") ON DELETE CASCADE, " +
                        "FOREIGN KEY(" + COL_FAV_RESOURCE_ID + ") REFERENCES " +
                        TABLE_RESOURCES + "(" + COL_RESOURCE_ID + ") ON DELETE CASCADE" +
                        ");";

        db.execSQL(createUsers);
        db.execSQL(createCategories);
        db.execSQL(createResources);
        db.execSQL(createFavorites);

        insertDefaultCategories(db);
        insertDefaultUsers(db);
        insertDefaultResources(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // simplest for class projects: rebuild
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESOURCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ===================== SEED DATA =====================

    private void insertDefaultCategories(SQLiteDatabase db) {
        insertCategory(db, "Food");
        insertCategory(db, "Shelter");
        insertCategory(db, "Health");
    }

    private void insertCategory(SQLiteDatabase db, String name) {
        ContentValues cv = new ContentValues();
        cv.put(COL_CATEGORY_NAME, name);
        db.insertWithOnConflict(TABLE_CATEGORIES, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private void insertDefaultUsers(SQLiteDatabase db) {
        insertUserSeed(db, "user1", "pass123", "Ava Johnson", "ava1@example.com", "Monroe", "No");
        insertUserSeed(db, "user2", "pass123", "Mia Carter", "mia2@example.com", "Monroe", "No");
        insertUserSeed(db, "user3", "pass123", "Noah Lee", "noah3@example.com", "Monroe", "Yes");
        insertUserSeed(db, "user4", "pass123", "Liam Smith", "liam4@example.com", "Monroe", "No");
        insertUserSeed(db, "user5", "pass123", "Emma Davis", "emma5@example.com", "Monroe", "Yes");

        insertUserSeed(db, "user6", "pass123", "Olivia Brown", "olivia6@example.com", "Ypsilanti", "No");
        insertUserSeed(db, "user7", "pass123", "Ethan Wilson", "ethan7@example.com", "Ypsilanti", "No");
        insertUserSeed(db, "user8", "pass123", "Sophia Moore", "sophia8@example.com", "Ypsilanti", "Yes");
        insertUserSeed(db, "user9", "pass123", "James Taylor", "james9@example.com", "Ypsilanti", "No");
        insertUserSeed(db, "user10", "pass123", "Isabella Martin", "isabella10@example.com", "Ypsilanti", "Yes");
    }

    private long insertUserSeed(SQLiteDatabase db, String username, String password, String name,
                                String email, String city, String orgRole) {
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_USERNAME, username);
        cv.put(COL_USER_PASSWORD, password);
        cv.put(COL_USER_NAME, name);
        cv.put(COL_USER_EMAIL, email);
        cv.put(COL_USER_CITY, city);
        cv.put(COL_USER_ORG_ROLE, orgRole);
        return db.insertWithOnConflict(TABLE_USERS, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private void insertDefaultResources(SQLiteDatabase db) {
        long foodId = getCategoryIdByNameSeed(db, "Food");
        long shelterId = getCategoryIdByNameSeed(db, "Shelter");
        long healthId = getCategoryIdByNameSeed(db, "Health");

        long ownerUserId = getUserIdByUsernameSeed(db, "user3");
        String date = "2025-12-13";

        // --- MONROE (Food) ---
        insertResourceSeed(db, ownerUserId, foodId,
                "Monroe Food Pantry", "123 Main St", "Monroe", "555-1111",
                "Weekly groceries and emergency food boxes.", date);

        insertResourceSeed(db, ownerUserId, foodId,
                "Fresh Start Groceries", "9 Market St", "Monroe", "555-1212",
                "Produce days and grocery support.", date);

        // --- MONROE (Shelter) ---
        insertResourceSeed(db, ownerUserId, shelterId,
                "Safe Harbor Shelter", "45 Oak Ave", "Monroe", "555-2222",
                "Emergency shelter and referrals.", date);

        insertResourceSeed(db, ownerUserId, shelterId,
                "Hope Housing Support", "18 River Dr", "Monroe", "555-2323",
                "Short-term housing support and placement help.", date);

        // --- MONROE (Health) ---
        insertResourceSeed(db, ownerUserId, healthId,
                "Community Care Clinic", "50 Health Blvd", "Monroe", "555-3333",
                "Low-cost checkups and basic services.", date);

        // --- YPSILANTI (Food) ---
        insertResourceSeed(db, ownerUserId, foodId,
                "Ypsilanti Meal Support", "200 Cross St", "Ypsilanti", "555-4444",
                "Meal pickup program and community meals.", date);

        insertResourceSeed(db, ownerUserId, foodId,
                "Ypsi Food Share", "77 Depot Town Rd", "Ypsilanti", "555-4545",
                "Food pantry assistance and produce boxes.", date);

        // --- YPSILANTI (Shelter) ---
        insertResourceSeed(db, ownerUserId, shelterId,
                "Ypsi Warm Nights Program", "12 Michigan Ave", "Ypsilanti", "555-5555",
                "Overnight warming shelter during cold months.", date);

        // --- YPSILANTI (Health) ---
        insertResourceSeed(db, ownerUserId, healthId,
                "Ypsilanti Health Outreach", "600 Wellness Way", "Ypsilanti", "555-6666",
                "Screenings, referrals, and health resources.", date);

        insertResourceSeed(db, ownerUserId, healthId,
                "Washtenaw Community Clinic", "310 Care Dr", "Ypsilanti", "555-6767",
                "Low-cost clinic services and support.", date);
    }

    private long insertResourceSeed(SQLiteDatabase db, long userId, long categoryId, String orgName,
                                    String address, String city, String contactInfo,
                                    String description, String dateAdded) {
        ContentValues cv = new ContentValues();
        cv.put(COL_RESOURCE_USER_ID, userId);
        cv.put(COL_RESOURCE_CAT_ID, categoryId);
        cv.put(COL_RESOURCE_ORG_NAME, orgName);
        cv.put(COL_RESOURCE_ADDRESS, address);
        cv.put(COL_RESOURCE_CITY, city);
        cv.put(COL_RESOURCE_CONTACT, contactInfo);
        cv.put(COL_RESOURCE_DESC, description);
        cv.put(COL_RESOURCE_DATE, dateAdded);

        // never duplicate: UNIQUE + IGNORE
        return db.insertWithOnConflict(TABLE_RESOURCES, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private long getCategoryIdByNameSeed(SQLiteDatabase db, String categoryName) {
        long id = -1;
        Cursor c = db.query(TABLE_CATEGORIES,
                new String[]{COL_CATEGORY_ID},
                COL_CATEGORY_NAME + "=?",
                new String[]{categoryName},
                null, null, null);
        if (c != null) {
            if (c.moveToFirst()) id = c.getLong(c.getColumnIndexOrThrow(COL_CATEGORY_ID));
            c.close();
        }
        return id;
    }

    private long getUserIdByUsernameSeed(SQLiteDatabase db, String username) {
        long id = -1;
        Cursor c = db.query(TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_USER_USERNAME + "=?",
                new String[]{username},
                null, null, null);
        if (c != null) {
            if (c.moveToFirst()) id = c.getLong(c.getColumnIndexOrThrow(COL_USER_ID));
            c.close();
        }
        return id;
    }

    // ===================== USERS =====================

    public long insertUser(String username, String password, String name, String email, String city, String orgRole) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_USERNAME, username);
        cv.put(COL_USER_PASSWORD, password);
        cv.put(COL_USER_NAME, name);
        cv.put(COL_USER_EMAIL, email);
        cv.put(COL_USER_CITY, city);
        cv.put(COL_USER_ORG_ROLE, orgRole);
        return db.insert(TABLE_USERS, null, cv);
    }

    public long loginAndGetUserId(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        long id = -1;

        Cursor c = db.query(TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_USER_USERNAME + "=? AND " + COL_USER_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);

        if (c != null) {
            if (c.moveToFirst()) id = c.getLong(c.getColumnIndexOrThrow(COL_USER_ID));
            c.close();
        }
        return id;
    }

    public long getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        long id = -1;

        Cursor c = db.query(TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_USER_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        if (c != null) {
            if (c.moveToFirst()) id = c.getLong(c.getColumnIndexOrThrow(COL_USER_ID));
            c.close();
        }
        return id;
    }

    public Cursor getUserById(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null,
                COL_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);
    }

    public boolean updateUserProfile(long userId, String name, String email, String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_NAME, name);
        cv.put(COL_USER_EMAIL, email);
        cv.put(COL_USER_CITY, city);
        int rows = db.update(TABLE_USERS, cv, COL_USER_ID + "=?",
                new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    // ===================== CATEGORIES =====================

    public long getCategoryIdByName(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        long id = -1;

        Cursor c = db.query(TABLE_CATEGORIES,
                new String[]{COL_CATEGORY_ID},
                COL_CATEGORY_NAME + "=?",
                new String[]{categoryName},
                null, null, null);

        if (c != null) {
            if (c.moveToFirst()) id = c.getLong(c.getColumnIndexOrThrow(COL_CATEGORY_ID));
            c.close();
        }
        return id;
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CATEGORIES,
                new String[]{COL_CATEGORY_ID, COL_CATEGORY_NAME},
                null, null, null, null,
                COL_CATEGORY_NAME + " ASC");
    }

    // ===================== RESOURCES  =====================

    // NOTE: uses CONFLICT_IGNORE so manual adds won't duplicate either.
    // Returns -1 if it's a duplicate (or insert failed).
    public long insertResource(long userId, long categoryId, String orgName, String address, String city,
                               String contactInfo, String description, String dateAdded) {
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

        return db.insertWithOnConflict(TABLE_RESOURCES, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public Cursor getResourceById(long resourceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RESOURCES,
                null,
                COL_RESOURCE_ID + "=?",
                new String[]{String.valueOf(resourceId)},
                null, null, null);
    }

    public Cursor getResourcesByCityAndCategory(String city, long categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RESOURCES,
                null,
                COL_RESOURCE_CITY + "=? AND " + COL_RESOURCE_CAT_ID + "=?",
                new String[]{city, String.valueOf(categoryId)},
                null, null,
                COL_RESOURCE_ORG_NAME + " ASC");
    }

    public Cursor getResourcesByCityAndCategories(String city, long[] categoryIds) {
        SQLiteDatabase db = this.getReadableDatabase();

        if (categoryIds == null || categoryIds.length == 0) {
            return db.rawQuery("SELECT * FROM " + TABLE_RESOURCES + " WHERE 1=0", null);
        }

        StringBuilder in = new StringBuilder();
        String[] args = new String[1 + categoryIds.length];
        args[0] = city;

        for (int i = 0; i < categoryIds.length; i++) {
            in.append("?");
            if (i < categoryIds.length - 1) in.append(",");
            args[i + 1] = String.valueOf(categoryIds[i]);
        }

        String sql = "SELECT * FROM " + TABLE_RESOURCES +
                " WHERE " + COL_RESOURCE_CITY + "=? AND " +
                COL_RESOURCE_CAT_ID + " IN (" + in + ")" +
                " ORDER BY " + COL_RESOURCE_ORG_NAME + " ASC";

        return db.rawQuery(sql, args);
    }

    // ===================== FAVORITES =====================

    public boolean addFavorite(long userId, long resourceId, String dateAdded) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_FAV_USER_ID, userId);
        cv.put(COL_FAV_RESOURCE_ID, resourceId);
        cv.put(COL_FAV_DATE, dateAdded);
        return db.insert(TABLE_FAVORITES, null, cv) != -1;
    }

    public boolean removeFavorite(long userId, long resourceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_FAVORITES,
                COL_FAV_USER_ID + "=? AND " + COL_FAV_RESOURCE_ID + "=?",
                new String[]{String.valueOf(userId), String.valueOf(resourceId)});
        return rows > 0;
    }

    public boolean isFavorite(long userId, long resourceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_FAVORITES,
                new String[]{COL_FAV_RESOURCE_ID},
                COL_FAV_USER_ID + "=? AND " + COL_FAV_RESOURCE_ID + "=?",
                new String[]{String.valueOf(userId), String.valueOf(resourceId)},
                null, null, null);

        boolean ok = (c != null && c.moveToFirst());
        if (c != null) c.close();
        return ok;
    }

    public Cursor getFavoriteResourcesCursor(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql =
                "SELECT r.* " +
                        "FROM " + TABLE_RESOURCES + " r " +
                        "INNER JOIN " + TABLE_FAVORITES + " f " +
                        "ON r." + COL_RESOURCE_ID + " = f." + COL_FAV_RESOURCE_ID + " " +
                        "WHERE f." + COL_FAV_USER_ID + " = ? " +
                        "ORDER BY r." + COL_RESOURCE_ORG_NAME + " ASC";

        return db.rawQuery(sql, new String[]{String.valueOf(userId)});
    }
}




