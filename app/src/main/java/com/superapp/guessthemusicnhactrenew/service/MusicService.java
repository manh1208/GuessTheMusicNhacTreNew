package com.superapp.guessthemusicnhactrenew.service;

import android.os.Environment;

import com.superapp.guessthemusicnhactrenew.model.Music;
import com.superapp.guessthemusicnhactrenew.model.MusicList;
import com.superapp.guessthemusicnhactrenew.model.STATIC_DATA;
import com.superapp.guessthemusicnhactrenew.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;


/**
 * Created by ManhNV on 2/18/17.
 */

public class MusicService {


    public void saveToJsonFile(MusicList musicList) throws JSONException, IOException {
        JSONObject jsonObject = makeJsonOject(musicList);
        Util.saveToFile(jsonObject.toString(),Util.getFile(Environment.getExternalStorageDirectory(),
                STATIC_DATA.FILE_JSON_NAME,STATIC_DATA.FOLDER_GUESS_THE_MUSIC));

    }

    private JSONObject makeJsonOject(MusicList musicList) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rootURL",musicList.getRootURL());
        JSONArray jsonArray = new JSONArray();
        for (Music music : musicList.getMusics()){
            JSONObject object = new JSONObject();
            object.put("_id",music.get_id());
            object.put("fileName",music.getFileName());
            object.put("create_at",music.getCreate_at());
            object.put("title",music.getTitle());
            object.put("key",music.getKey());
            object.put("version",music.getVersion());
            object.put("category",music.getCategory());
            object.put("__v",music.get_v());
            jsonArray.put(object);
        }
        jsonObject.put("musicList",jsonArray);
        return jsonObject;
    }

    public boolean checkFileJson() {
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator
                + STATIC_DATA.FOLDER_GUESS_THE_MUSIC
                + File.separator
                + STATIC_DATA.FILE_JSON_NAME
        );
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public void downloadMusic(MusicList result) {

    }
}
