package com.android.asilvia.softcoin.api;


import android.arch.lifecycle.LiveData;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by asilvia on 26-10-2017.
 */

public interface DetailsApiHelper {

    @GET("pricemulti")
    LiveData<ApiResponse<Map<String,Map<String,String>>>> getCoinPrices(@Query("fsyms") String from,
                                                                        @Query("tsyms") String to);

}
