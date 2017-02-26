package com.superapp.guessthemusicnhactrenew.fragment;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.superapp.guessthemusicnhactrenew.R;
import com.superapp.guessthemusicnhactrenew.util.BaseGameUtils;

/**
 * Created by ManhNV on 2/26/17.
 */

public class LeaderBoardFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final String TAG = "LeaderBoardFragment";
    private static final int RC_SIGN_IN = 1994;
    private static final int RC_UNUSED = 1998;
    private ConnectivityManager connManager;
    private NetworkInfo mMobile;
    private NetworkInfo mWifi;
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingConnectionFailure = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connManager = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addApi(AppIndex.API).build();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        Button btnLeaderBoard = (Button) v.findViewById(R.id.btn_leader_board);
        btnLeaderBoard.setOnClickListener(this);
        return v;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
        if (p == null) {
            Log.w(TAG, "mGamesClient.getCurrentPlayer() is NULL!");
            // displayName = "???";
        } else {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient),
                    RC_UNUSED);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended(): attempting to connect");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed(): attempting to resolve");
        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed(): already resolving");
            return;
        }
        if (!BaseGameUtils.resolveConnectionFailure(getActivity(), mGoogleApiClient, connectionResult,
                RC_SIGN_IN, getString(R.string.signin_other_error))) {
            mResolvingConnectionFailure = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == getActivity().RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                BaseGameUtils.showActivityResultError(getActivity(), requestCode, resultCode, R.string.signin_other_error);
            }
        }
    }

    private boolean isSignedIn() {
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_leader_board:
                if (mWifi.isConnected() || mMobile.isConnected()) {
//                    Toast.makeText(getActivity(), "is Signed In:  " + isSignedIn(), Toast.LENGTH_SHORT).show();
                    if (isSignedIn()) {
                        startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient),
                                RC_UNUSED);
                    } else {
                        mGoogleApiClient.connect();
                    }
                } else {
                    Toast.makeText(getActivity(), "Không có kết nối internet. Xin hãy bật wifi hoặc dữ liệu di động", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
