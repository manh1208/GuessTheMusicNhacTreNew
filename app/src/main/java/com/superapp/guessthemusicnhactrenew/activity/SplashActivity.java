package com.superapp.guessthemusicnhactrenew.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
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

import org.json.JSONException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private final String TAG = "Splash Activity";

    private MusicService musicService;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        boolean isGranted = requestPermission();
        musicService = new MusicService();
        if (isGranted) {
            checkFirstTime();
        }

    }

    private void checkFirstTime() {
        Log.d(TAG, "Check First Time");
        if (musicService.checkFileJson()) {
            Log.d(TAG, "This is not first time");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                   forwardNextActivity();
                }
            }, STATIC_DATA.SPLASH_TIME_OUT);
        } else {
            Log.d(TAG, "This is first time. Show alert!!");
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.warning_title)
                    .setMessage(R.string.warning_download_data)
                    .setPositiveButton(R.string.btnOK, null)
                    .setNegativeButton(R.string.btnCancel, null);

            final AlertDialog dialog = alert.create();
            dialog.show();

            Button btnOk = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getData();
                    dialog.dismiss();
                }
            });
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

    private void getData() {
        getJsonResource();
    }

    private void getJsonResource() {
        Log.d(TAG, "Get Json Resource");
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang kết nối đến máy chủ");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);
        Call<ResponseModel<MusicList>> call = service.getService().getMusicList(packageName, STATIC_DATA.PLATFORM, 0);
        call.enqueue(new Callback<ResponseModel<MusicList>>() {
            @Override
            public void onResponse(Call<ResponseModel<MusicList>> call, Response<ResponseModel<MusicList>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        try {
                            musicService.saveToJsonFile(response.body().getResult());
                            downloadMusic(response.body().getResult());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.e(TAG, "Error[" + response.code() + "] =  " + response.message());
                    Toast.makeText(SplashActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<MusicList>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(SplashActivity.this, R.string.string_cannot_connect_with_server, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadMusic(MusicList result) {
        MessageHandler handler = new MessageHandler(this);
        new DoadloadFile(this,handler).execute(result);
    }

    public void forwardNextActivity(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

}
