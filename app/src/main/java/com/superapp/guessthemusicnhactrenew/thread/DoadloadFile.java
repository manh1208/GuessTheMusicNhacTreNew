package com.superapp.guessthemusicnhactrenew.thread;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.superapp.guessthemusicnhactrenew.R;
import com.superapp.guessthemusicnhactrenew.model.Music;
import com.superapp.guessthemusicnhactrenew.model.MusicList;
import com.superapp.guessthemusicnhactrenew.model.STATIC_DATA;
import com.superapp.guessthemusicnhactrenew.service.MusicService;
import com.superapp.guessthemusicnhactrenew.util.DbUtil;
import com.superapp.guessthemusicnhactrenew.util.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ManhNV on 2/18/17.
 */

public class DoadloadFile extends AsyncTask<MusicList, Integer, Boolean> {
    private Context mContext;
    private ProgressDialog pDialog;
    private MessageHandler messageHandler;
    private MusicService musicService;
    private DbUtil dbUtil;

    public DoadloadFile(Context mContext, MessageHandler handler) {
        this.mContext = mContext;
        this.messageHandler = handler;
        dbUtil = new DbUtil(mContext);
        musicService = new MusicService(dbUtil);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Đang tải bài hát");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        pDialog.dismiss();
        messageHandler.obtainMessage(MessageHandler.DOWNLOAD_MUSIC_SUCCESS).sendToTarget();
    }

    @Override
    protected Boolean doInBackground(MusicList... musicLists) {
        MusicList musicList = musicLists[0];
        int i = 1;
        int total = musicList.getMusics().size();
        if (total > 0) {
            for (Music music : musicList.getMusics()) {
                publishProgress(i++, total);
                downloadMusic(music, musicList.getRootURL());
                Log.e("Download file", "Processing...: " + music.getFileName());
            }
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        pDialog.setMessage("Đang tải " + values[0] + "/" + values[1] + " bài hát");
    }

    private void downloadMusic(Music music, String path) {
        try {

            java.net.URL url = new URL(path + music.getFileName().trim().replace(" ", "%20") + "?alt=media");
            URLConnection connection = url.openConnection();
            connection.connect();
            int lenghOfFile = connection.getContentLength();
            InputStream inputStream = new BufferedInputStream(url.openStream());
            OutputStream outputStream = new FileOutputStream(new File(String.valueOf(mContext.getFileStreamPath(music.getFileName()))));
            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = inputStream.read(data)) != -1) {
                total += count;
//                publishProgress((int) (total * 100 / lenghOfFile));
                outputStream.write(data, 0, count);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            musicService.delete(music);
            Log.e("Download file", "List in DB size: "+ musicService.getAllMusics().size());
        }
    }

}
