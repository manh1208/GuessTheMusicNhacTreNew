package com.superapp.guessthemusicnhactrenew.service;


import com.superapp.guessthemusicnhactrenew.model.MusicList;
import com.superapp.guessthemusicnhactrenew.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ManhNV on 2/11/17.
 */

public interface IService {

    @GET("/guessmusic/data")
    Call<ResponseModel<MusicList>> getMusicList(@Query("bundle") String bundle,
                                                @Query("platform") String platform,
                                                @Query("version") int version);


}
