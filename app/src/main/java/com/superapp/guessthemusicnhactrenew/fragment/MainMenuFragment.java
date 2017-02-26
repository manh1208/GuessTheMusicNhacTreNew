package com.superapp.guessthemusicnhactrenew.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.superapp.guessthemusicnhactrenew.R;

/**
 * Created by ManhNV on 2/19/17.
 */

public class MainMenuFragment extends Fragment implements View.OnClickListener {
    private Button btnRate;
    private Button btnLikeUs;
    private Button btnAboutUs;
    private Button btnHotGame;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);
        bindView(v);
        return v;
    }

    private void bindView(View v) {
        btnLikeUs = (Button) v.findViewById(R.id.btn_like_us);
        btnAboutUs = (Button) v.findViewById(R.id.btn_about_us);
        btnRate = (Button) v.findViewById(R.id.btn_rate);
        btnHotGame = (Button) v.findViewById(R.id.btn_hot_game);
        btnLikeUs.setOnClickListener(this);
        btnAboutUs.setOnClickListener(this);
        btnRate.setOnClickListener(this);
        btnHotGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String packageName;
        PackageInfo pInfo = null;

        try {
            pInfo = getActivity().getPackageManager()
                    .getPackageInfo(
                            getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        packageName = pInfo.packageName;
        switch (view.getId()) {
            case R.id.btn_like_us:
                Uri uri;
                Intent intent;
                uri = Uri.parse("https://www.facebook.com/supercoolappteam");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                this.startActivity(intent);
                break;
            case R.id.btn_about_us:
                uri = Uri.parse("http://www.bestappsforphone.com");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                this.startActivity(intent);
                break;
            case R.id.btn_rate:
//              uri = Uri.parse("http://www.amazon.com/gp/mas/dl/android?p="+packageName);
//              uri=Uri.parse("http://appvn.com/android/details?id="+packageName);
//              uri = Uri.parse("http://play.google.com/store/apps/details?id="+packageName);
                uri = Uri.parse("http://www.bestappsforphone.com");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                this.startActivity(intent);
                break;
            case R.id.btn_hot_game:
//              uri=Uri.parse("http://www.bestappsforphone.com/appotagameofthemonth");
//              uri =Uri.parse("http://www.bestappsforphone.com/kindlegameofthemonth");
//              uri = Uri.parse("http://www.bestappsforphone.com/gameofthemonth");
                uri = Uri.parse("http://www.bestappsforphone.com");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                this.startActivity(intent);
                break;
        }
    }
}