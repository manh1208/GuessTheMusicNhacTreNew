package com.superapp.guessthemusicnhactrenew.util;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.superapp.guessthemusicnhactrenew.model.Music;

/**
 * Created by Nhattruong on 12/13/2015.
 */
public class DbUtil extends SQLiteOpenHelper {
    public static final String DB_NAME = "GTM.db";
    public static final int DATA_VERSION = 1;
    private static final String CREATE_MUSIC_TABLE_SCRIPT =
            "CREATE TABLE " + Music.TABLENAME + "(" +
                    Music.ID + " TEXT Primary Key ," +
                    Music.FILENAME + " TEXT," +
                    Music.CREATEAT + " TEXT," +
                    Music.TITLE + " TEXT," +
                    Music.KEY + " TEXT," +
                    Music.VERSION + " TEXT," +
                    Music.CATEGORY + " TEXT" +
                    ")";

    private SQLiteDatabase db;

    public DbUtil(Context context) {
        super(context, DB_NAME, null, DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MUSIC_TABLE_SCRIPT);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
//                db.execSQL("ALTER TABLE " + DATABASE_TABLE + " ADD COLUMN " + NEW_COLUMN_NAME + TYPE);
        }
    }

    /**
     * open database
     */
    public void open() {
        try {
            db = getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * close database
     */
    public void close() {
        if (db != null && db.isOpen()) {
            try {
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
