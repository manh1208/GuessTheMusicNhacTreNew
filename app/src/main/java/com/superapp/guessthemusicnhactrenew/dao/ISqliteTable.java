package com.superapp.guessthemusicnhactrenew.dao;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Nhattruong on 12/13/2015.
 */
public interface ISqliteTable {
    void setValue(Cursor cursor);
    ContentValues getContentValues();
    String getPrimaryValue();

}
