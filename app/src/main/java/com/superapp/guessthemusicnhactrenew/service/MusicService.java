package com.superapp.guessthemusicnhactrenew.service;

import com.superapp.guessthemusicnhactrenew.dao.MusicDao;
import com.superapp.guessthemusicnhactrenew.model.Music;
import com.superapp.guessthemusicnhactrenew.model.MusicList;
import com.superapp.guessthemusicnhactrenew.util.DbUtil;

import java.util.List;


/**
 * Created by ManhNV on 2/18/17.
 */

public class MusicService {

    private MusicDao dao;

    public MusicService(DbUtil dbUtil) {
        dao = new MusicDao(dbUtil);
    }

    public boolean saveMusics(MusicList musicList) {
        try {
            for (Music music : musicList.getMusics()
                    ) {
                dao.insert(music);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Music> getAllMusics(){
        return dao.getAll();
    }

    public void  delete(Music music){
        dao.delete(music);
    }

//    public void saveToJsonFile(MusicList musicList) throws JSONException, IOException {
//        JSONObject jsonObject = makeJsonOject(musicList);
//        Util.saveToFile(jsonObject.toString(),Util.getFile(Environment.getExternalStorageDirectory(),
//                STATIC_DATA.FILE_JSON_NAME,STATIC_DATA.FOLDER_GUESS_THE_MUSIC));
//
//    }
//
//    private JSONObject makeJsonOject(MusicList musicList) throws JSONException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("rootURL",musicList.getRootURL());
//        JSONArray jsonArray = new JSONArray();
//        for (Music music : musicList.getMusics()){
//            JSONObject object = new JSONObject();
//            object.put("_id",music.get_id());
//            object.put("fileName",music.getFileName());
//            object.put("create_at",music.getCreate_at());
//            object.put("title",music.getTitle());
//            object.put("key",music.getKey());
//            object.put("version",music.getVersion());
//            object.put("category",music.getCategory());
//            object.put("__v",music.get_v());
//            jsonArray.put(object);
//        }
//        jsonObject.put("musicList",jsonArray);
//        return jsonObject;
//    }
//
//    public boolean checkFileJson() {
//        File file = new File(Environment.getExternalStorageDirectory()
//                + File.separator
//                + STATIC_DATA.FOLDER_GUESS_THE_MUSIC
//                + File.separator
//                + STATIC_DATA.FILE_JSON_NAME
//        );
//
//        if (file.exists()) {
//            return true;
//        } else {
//            return false;
//        }
////    }
//
//    public void downloadMusic(MusicList result) {
//
//    }
}
