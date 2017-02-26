package com.superapp.guessthemusicnhactrenew.activity;


import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.superapp.guessthemusicnhactrenew.R;
import com.superapp.guessthemusicnhactrenew.model.Music;
import com.superapp.guessthemusicnhactrenew.service.MusicService;
import com.superapp.guessthemusicnhactrenew.util.DbUtil;
import com.superapp.guessthemusicnhactrenew.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GamePlayTocDoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnAnswer1;
    private Button btnAnswer2;
    private Button btnAnswer3;
    private final int MAXTIME = 15000;
    private List<Music> musics;
    MediaPlayer player;
    private TextView mTxtScore;
    private int mScore;
    private int mCountdown;
    private int mCurrentPosition;
    private HashMap<Integer, Boolean> mListButton;
    private CountDownTimer countDownGame;
    private CountDownTimer barCountdown;
    private MediaPlayer mp;
    private SeekBar bar;
    private boolean isFinish;
    private TextView txtCountdown;
    private MusicService musicService;
    private DbUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play_toc_do);
        init();
        getQuestion();
        reset();
        refreshQuestion();
        startTime();
    }

    private void getQuestion() {
        musics = musicService.getAllMusics();
    }

    private void reset() {
        Collections.shuffle(musics);
        mScore = 0;
        mTxtScore.setText("" + mScore);
        mCountdown = 15;
        mCurrentPosition = 0;
        refreshQuestion();
    }

    private void startSound(String filename) throws IOException {
//        AssetFileDescriptor afd = getAssets().openFd(filename);
        if (player != null && player.isPlaying()) {
            player.stop();
            player.reset();
        }
        Log.e("Game Play", "File name: "+filename);

        player.setDataSource(getFileStreamPath(filename).getAbsolutePath());
        player.prepare();
        player.start();

    }

    private void init() {
//        mListLogo = DataUtils.getINSTANCE(this).getMlistPresident();
        btnAnswer1 = (Button) findViewById(R.id.btnAnswer1);
        btnAnswer2 = (Button) findViewById(R.id.btnAnswer2);
        btnAnswer3 = (Button) findViewById(R.id.btnAnswer3);
        txtCountdown = (TextView) findViewById(R.id.txt_countdown);
        btnAnswer1.setOnClickListener(this);
        btnAnswer2.setOnClickListener(this);
        btnAnswer3.setOnClickListener(this);
        mTxtScore = (TextView) findViewById(R.id.txtScore);
        mListButton = new HashMap<Integer, Boolean>();
        mListButton.put(R.id.btnAnswer1, false);
        mListButton.put(R.id.btnAnswer2, false);
        mListButton.put(R.id.btnAnswer3, false);
        bar = (SeekBar) findViewById(R.id.pbTimer);
        bar.setProgress(MAXTIME);
        bar.setEnabled(false);
        player = new MediaPlayer();
        ImageView imageView = (ImageView) findViewById(R.id.image_disk);
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        rotateAnimation.setDuration(5000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        imageView.setAnimation(rotateAnimation);
        dbUtil = new DbUtil(this);
        musicService = new MusicService(dbUtil);
    }


    private void finishGame() {
        countDownGame.cancel();
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
        if (player != null && player.isPlaying()) {
            player.stop();
            player.reset();
        }
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra("Score", mScore);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (mListButton.containsKey(id)) {
            Button button = (Button) findViewById(id);
            button.setBackgroundColor(Color.parseColor("#ce1720"));
            if (mListButton.get(id)) {
                mCurrentPosition++;
                mScore += mCountdown;
                mTxtScore.setText("" + mScore);
                if (mCurrentPosition >= musics.size()) {
                    countDownGame.onFinish();
                } else {
                    refreshQuestion();
                    startTime();
                }
            } else {
//                DataUtils.playSound(getApplicationContext(), R.raw.sairoinha);
                countDownGame.onFinish();
            }
        }
    }

    private void refreshQuestion() {

        Music question = musics.get(mCurrentPosition);
        try {
            startSound(question.getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Integer> mListButtonText = new ArrayList<Integer>(mListButton.keySet());
        int rightAnswerPosition = Util.random(1, mListButtonText.size());
        int firstWrongPosition = -1;
        for (int i = 0; i < mListButtonText.size(); i++) {
            Button button = (Button) findViewById(mListButtonText.get(i));
            button.setBackgroundColor(getResources().getColor(R.color.button_answer_2));
            if (i + 1 == rightAnswerPosition) {
                mListButton.put(mListButtonText.get(i), true);
                ((Button) findViewById(mListButtonText.get(i))).setText(question.getTitle());
            } else {
                mListButton.put(mListButtonText.get(i), false);
                int wrongAnswer;
                do {
                    wrongAnswer = Util.random(1, musics.size());
                } while (wrongAnswer - 1 == mCurrentPosition || wrongAnswer == firstWrongPosition);
                firstWrongPosition = wrongAnswer;
                ((Button) findViewById(mListButtonText.get(i))).setText(musics.get(wrongAnswer - 1).getTitle());
            }
        }
        mCountdown = 15;
        txtCountdown.setText(mCountdown + "");
    }

    private void startTime() {
        boolean timeToEnd = false;
        if (countDownGame != null) {
            countDownGame.cancel();
        }
        countDownGame = new CountDownTimer(MAXTIME + 50, 999) {
            @Override
            public void onTick(long millisUntilFinished) {
                String s = "";
                mCountdown--;
                txtCountdown.setText(mCountdown + "");

            }

            @Override
            public void onFinish() {
                finishGame();

            }
        };
        countDownGame.start();
        if (barCountdown != null) {
            barCountdown.cancel();
        }
        barCountdown = new CountDownTimer(MAXTIME, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                bar.setProgress((int) (millisUntilFinished / 10));
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownGame.cancel();
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
        if (player != null && player.isPlaying()) {
            player.stop();
            player.reset();
        }
    }
}

