package com.superapp.guessthemusicnhactrenew.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.superapp.guessthemusicnhactrenew.R;
import com.superapp.guessthemusicnhactrenew.model.MusicList;
import com.superapp.guessthemusicnhactrenew.model.ResponseModel;
import com.superapp.guessthemusicnhactrenew.model.STATIC_DATA;
import com.superapp.guessthemusicnhactrenew.service.MusicService;
import com.superapp.guessthemusicnhactrenew.service.RestService;
import com.superapp.guessthemusicnhactrenew.thread.DoadloadFile;
import com.superapp.guessthemusicnhactrenew.thread.MessageHandler;
import com.superapp.guessthemusicnhactrenew.util.DbUtil;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private final String TAG = "Splash Activity";

    private MusicService musicService;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private int newVersion = 0;
    private int confirmMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = this.getSharedPreferences(STATIC_DATA.SHARE_PREFERENCE, MODE_PRIVATE);
        DbUtil dbUtil = new DbUtil(this);
        musicService = new MusicService(dbUtil);
        boolean isGranted = requestPermission();
        if (isGranted) {
            checkFirstTime();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void checkFirstTime() {
        Log.d(TAG, "Check First Time");
        newVersion = sharedPreferences.getInt(STATIC_DATA.NEWEST_VERSION, -1);

        if (newVersion > 0) {
            confirmMessage = R.string.confirm_update;
        } else {
            confirmMessage = R.string.warning_download_data;
        }
        checkData();
    }

    private void checkData() {

        RestService service = new RestService();
        String packageName;
        PackageInfo pInfo = null;

        try {
            pInfo = getPackageManager()
                    .getPackageInfo(
                            getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        packageName = pInfo.packageName;
        Log.d(TAG, "Package Name = " + packageName);
        Call<ResponseModel<MusicList>> call = service.getService().getMusicList(packageName, STATIC_DATA.PLATFORM, newVersion >= 0 ? newVersion : 0);
        call.enqueue(new Callback<ResponseModel<MusicList>>() {
            @Override
            public void onResponse(Call<ResponseModel<MusicList>> call, Response<ResponseModel<MusicList>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        MusicList musicList = response.body().getResult();
                        try {
                            if (musicList != null && musicList.getNewestVersion() > newVersion) {
                                confirmUpdate(musicList);
                            } else {
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            forwardNextActivity();
                        }
                    } else {
                        forwardNextActivity();
                    }
                } else {
                    Log.e(TAG, "Error[" + response.code() + "] =  " + response.message());
                    Toast.makeText(SplashActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    forwardNextActivity();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<MusicList>> call, Throwable t) {
                forwardNextActivity();
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(SplashActivity.this, R.string.string_cannot_connect_with_server, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void confirmUpdate(final MusicList musicList) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.confirm_update_title)
                .setMessage(confirmMessage)
                .setPositiveButton(R.string.btnOK, null)
                .setNegativeButton(R.string.btnCancel, null);
        final AlertDialog dialog = alert.create();
        dialog.show();

        Button btnOk = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                saveDataAndDownload(musicList);

            }
        });

        Button btnCancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                forwardNextActivity();
            }
        });
    }

    private void saveDataAndDownload(MusicList musicList) {
        boolean result = musicService.saveMusics(musicList);
        if (result) {
            sharedPreferences.edit()
                    .putInt(STATIC_DATA.NEWEST_VERSION, musicList.getNewestVersion())
                    .putString(STATIC_DATA.ROOT_FILE_DOWNLOAD, "")
                    .apply();
            downloadMusic(musicList);
        }
    }


    private boolean requestPermission() {
        Log.d(TAG, "Request permission");
        int permsRequestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(perms, permsRequestCode);
                return false;
            } else {
                return true;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                checkFirstTime();
            }
        }
    }

    private void downloadMusic(MusicList result) {
        MessageHandler handler = new MessageHandler(this);
        new DoadloadFile(this, handler).execute(result);
    }

    public void forwardNextActivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

}
