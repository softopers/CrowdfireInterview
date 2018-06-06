package com.interview.crowdfireinterview.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.interview.crowdfireinterview.Cloth;

import java.util.ArrayList;

import static com.interview.crowdfireinterview.db.Contract.FavEntry.TABLE_NAME;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "crowdfire.db";

    private static final String SQL_CREATE_CLOTH_TABLE =
            "CREATE TABLE " + Contract.ShirtPantEntry.TABLE_NAME + " (" +
                    Contract.ShirtPantEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Contract.ShirtPantEntry.COLUMN_NAME_CLOTH_TYPE + " INTEGER," +
                    Contract.ShirtPantEntry.COLUMN_NAME_CLOTH_IMAGE_URL + " TEXT," +
                    Contract.ShirtPantEntry.COLUMN_NAME_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    private static final String SQL_CREATE_FAV_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    Contract.FavEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Contract.FavEntry.COLUMN_NAME_SHIRT_ID + " INTEGER," +
                    Contract.FavEntry.COLUMN_NAME_PANT_ID + " INTEGER," +
                    Contract.FavEntry.COLUMN_NAME_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    private static final String SQL_DELETE_CLOTH_TABLE =
            "DROP TABLE IF EXISTS " + Contract.ShirtPantEntry.TABLE_NAME;

    private static final String SQL_DELETE_FAV_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CLOTH_TABLE);
        db.execSQL(SQL_CREATE_FAV_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CLOTH_TABLE);
        db.execSQL(SQL_DELETE_FAV_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<Cloth> getCloths() {
        ArrayList<Cloth> cloths = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + Contract.ShirtPantEntry.TABLE_NAME + " ORDER BY " +
                Contract.ShirtPantEntry.COLUMN_NAME_TIMESTAMP + " ASC";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Cloth cloth = new Cloth();
                    cloth.setId(cursor.getInt(cursor.getColumnIndex(Contract.ShirtPantEntry._ID)));
                    cloth.setClothType(cursor.getInt(cursor.getColumnIndex(Contract.ShirtPantEntry.COLUMN_NAME_CLOTH_TYPE)));
                    cloth.setClothImageUrl(cursor.getString(cursor.getColumnIndex(Contract.ShirtPantEntry.COLUMN_NAME_CLOTH_IMAGE_URL)));
                    cloth.setTimeStamp(cursor.getString(cursor.getColumnIndex(Contract.ShirtPantEntry.COLUMN_NAME_TIMESTAMP)));

                    cloths.add(cloth);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }

        db.close();
        return cloths;
    }

    public long insertCloth(int clothType, String clothImageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.ShirtPantEntry.COLUMN_NAME_CLOTH_TYPE, clothType);
        values.put(Contract.ShirtPantEntry.COLUMN_NAME_CLOTH_IMAGE_URL, clothImageUrl);

        long id = db.insert(Contract.ShirtPantEntry.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public long insertFav(int shirtId, int pantId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.FavEntry.COLUMN_NAME_SHIRT_ID, shirtId);
        values.put(Contract.FavEntry.COLUMN_NAME_PANT_ID, pantId);

        long id = db.insert(TABLE_NAME, null, values);

        db.close();

        return id;
    }


    public int checkFav(int shirtId, int pantId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT  * FROM " + TABLE_NAME + " WHERE " + Contract.FavEntry.COLUMN_NAME_SHIRT_ID + " = " + shirtId + " AND " + Contract.FavEntry.COLUMN_NAME_PANT_ID + " = " + pantId + "";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return -1;
        }

        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex(Contract.FavEntry._ID));
        cursor.close();
        return id;
    }

    public void deleteFav(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + Contract.FavEntry._ID + " = " + id;
        sqLiteDatabase.execSQL(deleteQuery);
        sqLiteDatabase.close();
    }

    public Cloth getCloth(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Contract.ShirtPantEntry.TABLE_NAME,
                new String[]{Contract.ShirtPantEntry._ID, Contract.ShirtPantEntry.COLUMN_NAME_CLOTH_TYPE, Contract.ShirtPantEntry.COLUMN_NAME_CLOTH_IMAGE_URL, Contract.ShirtPantEntry.COLUMN_NAME_TIMESTAMP},
                Contract.ShirtPantEntry._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Cloth cloth = null;
        if (cursor != null) {
            cursor.moveToFirst();
            cloth = new Cloth(
                    cursor.getInt(cursor.getColumnIndex(Contract.ShirtPantEntry._ID)),
                    cursor.getInt(cursor.getColumnIndex(Contract.ShirtPantEntry.COLUMN_NAME_CLOTH_TYPE)),
                    cursor.getString(cursor.getColumnIndex(Contract.ShirtPantEntry.COLUMN_NAME_CLOTH_IMAGE_URL)),
                    cursor.getString(cursor.getColumnIndex(Contract.ShirtPantEntry.COLUMN_NAME_TIMESTAMP)));

            cursor.close();
        }
        db.close();
        return cloth;
    }
}