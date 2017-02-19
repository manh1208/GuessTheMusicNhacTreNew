package com.superapp.guessthemusicnhactrenew.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ManhNV on 2/18/17.
 */

public class MusicList {
    @SerializedName("musicList")
    private List<Music> musics;

    @SerializedName("rootURL")
    private String rootURL;


    public List<Music> getMusics() {
        return musics;
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }

    public String getRootURL() {
        return rootURL;
    }

    public void setRootURL(String rootURL) {
        this.rootURL = rootURL;
    }
}
