package com.android.asilvia.softcoin.repository;

import android.arch.lifecycle.LiveData;

import com.android.asilvia.softcoin.api.ApiResponse;
import com.android.asilvia.softcoin.vo.Coins;
import java.util.Map;

/**
 * Created by asilvia on 26-10-2017.
 */

public interface DataManager{
    LiveData<ApiResponse<Coins>> getCoinList();
    LiveData<ApiResponse<Map<String,Map<String,String>>>> getCoinPrices(String from, String to);
    String getValueExample();
    void setValueExample(String valueExample);

}
