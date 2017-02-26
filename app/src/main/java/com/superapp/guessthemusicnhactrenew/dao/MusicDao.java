package com.superapp.guessthemusicnhactrenew.dao;

import com.superapp.guessthemusicnhactrenew.model.Music;
import com.superapp.guessthemusicnhactrenew.util.DbUtil;

/**
 * Created by ManhNV on 2/26/17.
 */

public class MusicDao extends DataAccessObject<Music> {
    public MusicDao(DbUtil dbUtil) {
        super(dbUtil, Music.TABLENAME, Music.ID, Music.class);
    }
}
