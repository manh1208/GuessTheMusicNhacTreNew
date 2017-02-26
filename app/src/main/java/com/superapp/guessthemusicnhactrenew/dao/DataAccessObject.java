package com.superapp.guessthemusicnhactrenew.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.superapp.guessthemusicnhactrenew.util.DbUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ManhNV on 12/27/2015.
 */
public abstract class DataAccessObject<TEntity extends ISqliteTable> {
    private final DbUtil dbUtil;
    private final String tableName;
    private final String primaryField;
    private final Class<TEntity> entityClass;

    public DataAccessObject(DbUtil dbUtil, String tableName, String primaryField, Class<TEntity> entityClass) {
        this.dbUtil = dbUtil;
        this.tableName = tableName;
        this.primaryField = primaryField;
        this.entityClass = entityClass;
    }

    public String getTableName() {
        return tableName;
    }

    public DbUtil getDbUtil() {
        return dbUtil;
    }

    public TEntity getByPrimary(int id) {
        return getByPrimary(String.valueOf(id));
    }

    public TEntity getByPrimary(String id) {
        SQLiteDatabase db = getDbUtil().getReadableDatabase();
        String query = "SELECT * FROM " + this.getTableName() + " WHERE " + primaryField + " = ?";
        String[] param = new String[]{id};
        Cursor cursor = db.rawQuery(query, param);
        if (cursor.moveToFirst()) {
            TEntity entity = SqliteTableFactory.getInstance().getSqliteTableObject(entityClass);
            entity.setValue(cursor);
            return entity;
        }

        if (db.isOpen()){
            db.close();
        }
        return null;
    }

    public List<TEntity> getBy(String where, String value) {
        List<TEntity> result = new ArrayList<>();
        SQLiteDatabase db = getDbUtil().getReadableDatabase();
        String query = "SELECT * FROM " + this.getTableName() + " WHERE " + where + " = ?";
        String[] param = new String[]{value};
        Cursor cursor = db.rawQuery(query, param);
        if (cursor.moveToFirst()) {
            do {
                TEntity entity = SqliteTableFactory.getInstance().getSqliteTableObject(entityClass);
                entity.setValue(cursor);
                result.add(entity);
            } while (cursor.moveToNext());

        }
        if (db.isOpen()){
            db.close();
        }
        return result;
    }

    public boolean insert(TEntity entity) {
        SQLiteDatabase db = getDbUtil().getReadableDatabase();
        ContentValues value = entity.getContentValues();
        long executedQuery = db.insert(this.tableName, null, value);
        if (db.isOpen()){
            db.close();
        }
        return executedQuery > 0;
    }

    public boolean update(TEntity entity) {
        SQLiteDatabase db = getDbUtil().getReadableDatabase();
        ContentValues value = entity.getContentValues();
        String[] param = new String[]{entity.getPrimaryValue()};
        long executedQuery = db.update(this.tableName, value, this.primaryField + " = ?", param);
        if (db.isOpen()){
            db.close();
        }
        return executedQuery > 0;
    }

    public boolean delete(TEntity entity) {
        SQLiteDatabase db = getDbUtil().getReadableDatabase();
        ContentValues value = entity.getContentValues();
        String[] param = new String[]{entity.getPrimaryValue()};
        long executedQuery = db.delete(this.tableName, this.primaryField + " = ?", param);
        if (db.isOpen()){
            db.close();
        }
        return executedQuery > 0;
    }

    public void deleteAll(){
        SQLiteDatabase db = getDbUtil().getReadableDatabase();
        db.execSQL("delete from "+ this.getTableName());
        if (db.isOpen()){
            db.close();
        }
    }

    public List<TEntity> getAll() {
        List<TEntity> result = new ArrayList<>();
        SQLiteDatabase db = getDbUtil().getReadableDatabase();
        String query = "SELECT * FROM " + this.getTableName();
        String[] param = null;
        Cursor cursor = db.rawQuery(query, param);

        if (cursor.moveToFirst()) {
            do {
                TEntity entity = SqliteTableFactory.getInstance().getSqliteTableObject(entityClass);
                entity.setValue(cursor);
                result.add(entity);
            } while (cursor.moveToNext());

        }
        if (db.isOpen()){
            db.close();
        }
        return result;
    }
}
