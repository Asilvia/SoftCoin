package com.android.asilvia.softcoin.api;


import android.arch.lifecycle.LiveData;

import com.android.asilvia.softcoin.vo.Coins;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by asilvia on 26-10-2017.
 */

public interface MainApiHelper {

    @GET("all/coinlist")
    LiveData<ApiResponse<Coins>> getCoinList();
    @GET("pricemulti")
    LiveData<ApiResponse<Map<String,Map<String,String>>>> getCoinPrices(@Query("fsyms") String from,
                                                                        @Query("tsyms") String to);

}
