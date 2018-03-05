package com.android.asilvia.cryptoo.repository;

import android.arch.lifecycle.LiveData;

import com.android.asilvia.cryptoo.api.ApiResponse;
import com.android.asilvia.cryptoo.vo.Coins;
import com.android.asilvia.cryptoo.db.LocalCoin;

import java.util.List;
import java.util.Map;

import rx.Completable;

/**
 * Created by asilvia on 26-10-2017.
 */

public interface DataManager{
    LiveData<ApiResponse<Coins>> getCoinList();
    LiveData<ApiResponse<Map<String,Map<String,String>>>> getCoinPrices(String from, String to);
    Completable saveCoin(LocalCoin coin);
    String getMainCoin();
    void setMainCoin(String coin);
    List<LocalCoin> getSavedCoinList();
    Completable saveCoinList(List<LocalCoin> list);
    LocalCoin findCoindById(String id);


}
