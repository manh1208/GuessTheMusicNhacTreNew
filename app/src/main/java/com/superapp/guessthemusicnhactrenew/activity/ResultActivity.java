package com.superapp.guessthemusicnhactrenew.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.superapp.guessthemusicnhactrenew.R;
import com.superapp.guessthemusicnhactrenew.fragment.AdFragment;
import com.superapp.guessthemusicnhactrenew.model.STATIC_DATA;
import com.superapp.guessthemusicnhactrenew.util.BaseGameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ResultActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final String TAG = "ResultActivity";
    private static final int RC_SIGN_IN = 1990;
    private TextView mResult;
    private TextView mHighestScore;
    private ImageButton mPlayAgain;
    private ImageButton mHome;
    private ImageButton mShare;
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingConnectionFailure = false;
    private int highestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        bindView();
        int result = getIntent().getIntExtra("Score", 0);
        mResult.setText(String.valueOf(result));


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();


        highestScore = 0;
        Log.e(TAG,"Start to save score");
//        saveScoreToLeaderBoard(result);
        highestScore = getScoreFromFile();
        if (result > highestScore) {

            highestScore = result;
            saveScoreToLeaderBoard(highestScore);
            saveScoreToFile(highestScore);
        }
        mHighestScore.setText("" + highestScore);


        mPlayAgain.setOnClickListener(this);
        mHome.setOnClickListener(this);
        mShare.setOnClickListener(this);
        Fragment fragment = new AdFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(STATIC_DATA.HAS_FULL_SCREEN, true);
        fragment.setArguments(bundle );
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_ad, fragment)
                .commit();
    }

    private void bindView() {
        mResult = (TextView) findViewById(R.id.txt_result_score);
        mHighestScore = (TextView) findViewById(R.id.txt_best_score);
        mPlayAgain = (ImageButton) findViewById(R.id.btn_play_again);
        mHome = (ImageButton) findViewById(R.id.btn_home);
        mShare = (ImageButton) findViewById(R.id.btn_share);
    }

    private int getScoreFromFile() {
        File f = new File(String.valueOf(getFileStreamPath(STATIC_DATA.FILE_SCORE_NAME)));
        if (f.exists()) {
            FileReader fr = null;
            BufferedReader br = null;
            try {
                fr = new FileReader(getFileStreamPath(STATIC_DATA.FILE_SCORE_NAME));
                br = new BufferedReader(fr);
                String s = br.readLine();

                return Integer.parseInt(s);
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fr != null) {
                    try {
                        fr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return 0;
    }

    private void saveScoreToFile(int score) {
        try {
            File f = new File(String.valueOf(getFileStreamPath(STATIC_DATA.FILE_SCORE_NAME)));
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream fou = openFileOutput(STATIC_DATA.FILE_SCORE_NAME, MODE_WORLD_READABLE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write("" + score);
            osw.flush();
            osw.close();
            fou.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveScoreToLeaderBoard(int score) {
        Log.e(TAG,"save score to leader board");
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mWifi.isConnected() || mMobile.isConnected()) {
            if (isSignedIn()) {
                Log.e(TAG,"is signed in");
                Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_speed),
                        score);
            } else {
                Log.e(TAG,"is not signed in");
                mGoogleApiClient.connect();
            }
        }
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return Bitmap.createBitmap(rootView.getDrawingCache());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play_again:
                Intent intent = new Intent(ResultActivity.this, GamePlayTocDoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_home:
                onBackPressed();
                break;
            case R.id.btn_share:
                Bitmap bitmap = takeScreenshot();
                FacebookSdk.sdkInitialize(getApplicationContext());
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                ShareDialog shareDialog = new ShareDialog(ResultActivity.this);
                shareDialog.show(ResultActivity.this, content);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Log.e(TAG,"actitvity result");
            // mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                Log.e(TAG,"actitvity result ok");
                mGoogleApiClient.connect();
            } else {
                BaseGameUtils.showActivityResultError(this, requestCode, resultCode, R.string.signin_other_error);
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed(): attempting to resolve");
        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed(): already resolving");
            return;
        }
        if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult,
                RC_SIGN_IN, getString(R.string.signin_other_error))) {
            mResolvingConnectionFailure = false;
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(TAG,"is connected");
        Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
        Log.e(TAG,p.toString());
        if (p == null) {
            Log.e(TAG, "mGamesClient.getCurrentPlayer() is NULL!");
            // displayName = "???";
        } else {
                Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_speed),
                        highestScore);
        }
    }

    private boolean isSignedIn() {
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended(): attempting to connect");
        mGoogleApiClient.connect();
    }

}
