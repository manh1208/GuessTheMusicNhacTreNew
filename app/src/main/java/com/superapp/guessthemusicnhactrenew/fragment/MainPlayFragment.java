package com.superapp.guessthemusicnhactrenew.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.superapp.guessthemusicnhactrenew.R;
import com.superapp.guessthemusicnhactrenew.activity.GamePlayActivity;
import com.superapp.guessthemusicnhactrenew.activity.GamePlayTocDoActivity;

/**
 * Created by ManhNV on 2/19/17.
 */

public class MainPlayFragment extends Fragment implements View.OnClickListener{
    private Button btnPlay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_play,container,false);
        bindView(v);
        return v;
    }

    private void bindView(View v) {
        btnPlay = (Button) v.findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_play:
                Intent intent = new Intent(getActivity(), GamePlayTocDoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
