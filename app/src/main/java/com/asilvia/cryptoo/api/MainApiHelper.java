package com.asilvia.cryptoo.api;


import android.arch.lifecycle.LiveData;

import com.asilvia.cryptoo.vo.Coins;
import com.asilvia.cryptoo.vo.CoinsPrice;

import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by asilvia on 26-10-2017.
 */

public interface MainApiHelper {

    @GET("all/coinlist")
    LiveData<ApiResponse<Coins>> getCoinList();
    @GET("pricemultifull")
    LiveData<ApiResponse<CoinsPrice>> getCoinPrices(@Query("fsyms") String from,
                                                    @Query("tsyms") String to);

}
