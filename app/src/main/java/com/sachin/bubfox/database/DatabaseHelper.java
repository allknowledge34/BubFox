package com.sachin.bubfox.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sachin.bubfox.item.Item;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "star.db";
    private static final int DATABASE_VERSION = 1;
    private static final int INITIAL_COIN = 100;

    // Star table schema
    private static final String STAR_TABLE_NAME = "STAR_TABLE";
    private static final String STAR_COLUMN_LEVEL = "LEVEL";
    private static final String STAR_COLUMN_STAR = "STAR";

    // Item table schema
    private static final String ITEM_TABLE_NAME = "ITEM_TABLE";
    private static final String ITEM_COLUMN_NAME = "NAME";
    private static final String ITEM_COLUMN_NUM = "NUMBER";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // This method will call only when the app first init
        String statementCreateStar = "CREATE TABLE " + STAR_TABLE_NAME + " ("
                + STAR_COLUMN_LEVEL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STAR_COLUMN_STAR + " INTEGER"
                + ")";

        String statementCreateItem = "CREATE TABLE " + ITEM_TABLE_NAME + " ("
                + ITEM_COLUMN_NAME + " TEXT PRIMARY KEY, "
                + ITEM_COLUMN_NUM + " INTEGER"
                + ")";

        sqLiteDatabase.execSQL(statementCreateStar);
        sqLiteDatabase.execSQL(statementCreateItem);
        initItem(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This method will call when new version release
        // if (oldVersion == 1 && newVersion == 2) ...
    }

    private void initItem(SQLiteDatabase sqLiteDatabase) {
        // Insert coin to db
        ContentValues valuesCoin = new ContentValues();
        valuesCoin.put(ITEM_COLUMN_NAME, Item.COIN);
        valuesCoin.put(ITEM_COLUMN_NUM, INITIAL_COIN);

        // Insert color ball to db
        ContentValues valuesColorBall = new ContentValues();
        valuesColorBall.put(ITEM_COLUMN_NAME, Item.COLOR_BALL);
        valuesColorBall.put(ITEM_COLUMN_NUM, 0);

        // Insert fireball to db
        ContentValues valuesFireball = new ContentValues();
        valuesFireball.put(ITEM_COLUMN_NAME, Item.FIREBALL);
        valuesFireball.put(ITEM_COLUMN_NUM, 0);

        // Insert bomb to db
        ContentValues valuesBomb = new ContentValues();
        valuesBomb.put(ITEM_COLUMN_NAME, Item.BOMB);
        valuesBomb.put(ITEM_COLUMN_NUM, 0);

        sqLiteDatabase.insert(ITEM_TABLE_NAME, null, valuesCoin);
        sqLiteDatabase.insert(ITEM_TABLE_NAME, null, valuesColorBall);
        sqLiteDatabase.insert(ITEM_TABLE_NAME, null, valuesFireball);
        sqLiteDatabase.insert(ITEM_TABLE_NAME, null, valuesBomb);
    }

    //-----------------------------------------------------------
    // Method of item
    //-----------------------------------------------------------
    public boolean updateItemNum(String name, int num) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(ITEM_COLUMN_NUM, num);

            int rows = db.update(ITEM_TABLE_NAME, values,
                    ITEM_COLUMN_NAME + " = ? ",
                    new String[]{name});
            return rows > 0;
        } finally {
            db.close();
        }
    }

    public int getItemNum(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int number = -1;
        try {
            cursor = db.query(ITEM_TABLE_NAME,
                    new String[]{ITEM_COLUMN_NUM},
                    ITEM_COLUMN_NAME + " =? ",
                    new String[]{name},
                    null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                number = cursor.getInt(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return number;
    }
    //===========================================================

    //-----------------------------------------------------------
    // Method of level star
    //-----------------------------------------------------------
    public boolean insertLevelStar(int star) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(STAR_COLUMN_STAR, star);
            long id = db.insert(STAR_TABLE_NAME, null, values);
            return id != -1;
        } finally {
            db.close();
        }
    }

    public boolean insertLevelStar(int levelID, int star) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(STAR_COLUMN_LEVEL, levelID);
            values.put(STAR_COLUMN_STAR, star);
            long id = db.insertWithOnConflict(STAR_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            return id != -1;
        } finally {
            db.close();
        }
    }

    public boolean updateLevelStar(int levelID, int star) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(STAR_COLUMN_STAR, star);

            int rows = db.update(STAR_TABLE_NAME, values,
                    STAR_COLUMN_LEVEL + " = ? ",
                    new String[]{String.valueOf(levelID)});
            return rows > 0;
        } finally {
            db.close();
        }
    }

    public int getLevelStar(int levelID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        int star = -1;
        try {
            cursor = db.query(STAR_TABLE_NAME,
                    new String[]{STAR_COLUMN_STAR},
                    STAR_COLUMN_LEVEL + " =? ",
                    new String[]{String.valueOf(levelID)},
                    null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                star = cursor.getInt(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return star;
    }

    public ArrayList<Integer> getAllLevelStar() {
        ArrayList<Integer> stars = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + STAR_TABLE_NAME + " ORDER BY " + STAR_COLUMN_LEVEL + " ASC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    stars.add(cursor.getInt(cursor.getColumnIndexOrThrow(STAR_COLUMN_STAR)));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return stars;
    }
    //===========================================================

}
