package com.superapp.guessthemusicnhactrenew.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.superapp.guessthemusicnhactrenew.R;
import com.superapp.guessthemusicnhactrenew.fragment.AdFragment;
import com.superapp.guessthemusicnhactrenew.fragment.LeaderBoardFragment;
import com.superapp.guessthemusicnhactrenew.fragment.MainMenuFragment;
import com.superapp.guessthemusicnhactrenew.fragment.MainPlayFragment;
import com.superapp.guessthemusicnhactrenew.model.STATIC_DATA;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
//        List<Music> musicList = DataUtils.getINSTANCE(this).getMusics();
//        String rootURL = DataUtils.getINSTANCE(this).getRootURL();
//        Log.i(TAG, "Musics size: " + musicList.size());
//        Log.i(TAG,"Root URL: "+rootURL);

    }

    private void bindView() {
        Fragment playFragment = new MainPlayFragment();
        Fragment menuFragment = new MainMenuFragment();
        Fragment adFragment = new AdFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(STATIC_DATA.HAS_FULL_SCREEN, false);
        adFragment.setArguments(bundle );
        Fragment leaderBoardFragment = new LeaderBoardFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_play, playFragment)
                .replace(R.id.main_fragment_setting, menuFragment)
                .replace(R.id.main_fragment_ad, adFragment)
                .replace(R.id.main_fragment_leaderboard, leaderBoardFragment)
                .commit();

    }
}
