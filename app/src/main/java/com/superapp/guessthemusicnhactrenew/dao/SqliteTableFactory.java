package com.superapp.guessthemusicnhactrenew.dao;


import com.superapp.guessthemusicnhactrenew.model.Music;

/**
 * Created by ManhNV on 12/27/2015.
 */
public class SqliteTableFactory {
    private SqliteTableFactory() {

    }
    private static SqliteTableFactory instance;

    public static SqliteTableFactory getInstance() {
        if (instance==null){
            instance= new SqliteTableFactory();
        }
        return instance;
    }

    public <TEntity extends ISqliteTable> TEntity getSqliteTableObject(Class<TEntity> c) {
         if (c ==  Music.class) {
            return (TEntity) new Music();
        }
        return null;
    }
}