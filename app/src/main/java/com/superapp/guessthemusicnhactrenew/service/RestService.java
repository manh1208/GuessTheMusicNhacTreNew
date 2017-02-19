package com.superapp.guessthemusicnhactrenew.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.superapp.guessthemusicnhactrenew.model.STATIC_DATA;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestService {
    private final String url = STATIC_DATA.API_URL;
    private Retrofit retrofit;
    private IService service;

    public RestService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        service = retrofit.create(IService.class);

    }

    public IService getService() {
        return service;
    }
}
