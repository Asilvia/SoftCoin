package com.android.asilvia.softcoin.api;


import android.arch.lifecycle.LiveData;

import com.android.asilvia.softcoin.vo.Coins;

import retrofit2.http.GET;



/**
 * Created by asilvia on 26-10-2017.
 */

public interface MainApiHelper {

    @GET("all/coinlist")
    LiveData<ApiResponse<Coins>> getCoinList();

}
