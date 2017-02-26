package com.superapp.guessthemusicnhactrenew.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;
import com.superapp.guessthemusicnhactrenew.dao.ISqliteTable;

/**
 * Created by ManhNV on 2/18/17.
 */

public class Music implements ISqliteTable {
    public final static String TABLENAME = "Music";
    public final static String ID = "_id";
    public final static String FILENAME = "fileName";
    public final static String CREATEAT = "create_at";
    public final static String TITLE = "title";
    public final static String KEY = "key";
    public final static String VERSION = "version";
    public final static String CATEGORY = "category";


    @SerializedName("_id")
    private String _id;
    @SerializedName("fileName")
    private String fileName;
    @SerializedName("create_at")
    private String create_at;
    @SerializedName("title")
    private String title;
    @SerializedName("key")
    private String key;
    @SerializedName("version")
    private String version;
    @SerializedName("category")
    private String category;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Override
    public void setValue(Cursor cursor) {

        this._id = cursor.getString(cursor.getColumnIndex(ID));
        this.fileName = cursor.getString(cursor.getColumnIndex(FILENAME));
        this.create_at = cursor.getString(cursor.getColumnIndex(CREATEAT));
        this.title = cursor.getString(cursor.getColumnIndex(TITLE));
        this.key = cursor.getString(cursor.getColumnIndex(KEY));
        this.version = cursor.getString(cursor.getColumnIndex(VERSION));


    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(ID, this._id);
        values.put(FILENAME, this.fileName);
        values.put(CREATEAT, this.create_at);
        values.put(TITLE, this.title);
        values.put(KEY, this.key);
        values.put(VERSION, this.version);
        return values;
    }

    @Override
    public String getPrimaryValue() {
        return this._id;
    }
}
