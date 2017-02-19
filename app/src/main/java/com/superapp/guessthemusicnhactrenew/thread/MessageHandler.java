package com.superapp.guessthemusicnhactrenew.thread;

import android.os.Handler;
import android.os.Message;

import com.superapp.guessthemusicnhactrenew.activity.SplashActivity;

/**
 * Created by ManhNV on 2/18/17.
 */

public class MessageHandler extends Handler {


    public static final int DOWNLOAD_MUSIC_SUCCESS = 1;
    SplashActivity splashActivity;

    public MessageHandler(SplashActivity activity) {
        splashActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case DOWNLOAD_MUSIC_SUCCESS:
                splashActivity.forwardNextActivity();
                break;
        }
    }
}
