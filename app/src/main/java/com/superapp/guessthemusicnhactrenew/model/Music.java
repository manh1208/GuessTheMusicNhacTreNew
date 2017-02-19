package com.superapp.guessthemusicnhactrenew.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ManhNV on 2/18/17.
 */

public class Music {
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
    @SerializedName("_v")
    private String _v;

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

    public String get_v() {
        return _v;
    }

    public void set_v(String _v) {
        this._v = _v;
    }
}
