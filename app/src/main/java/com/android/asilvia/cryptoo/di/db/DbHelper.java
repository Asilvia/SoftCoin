package com.android.asilvia.cryptoo.di.db;

import com.android.asilvia.cryptoo.db.LocalCoin;

import java.util.List;



/**
 * Created by ana.medeira on 20-2-2018.
 */

public interface  DbHelper {

    void saveCoin(LocalCoin localCoin);
    List<LocalCoin> getSavedCoins();
    void  saveCoinList(List<LocalCoin> list);
    LocalCoin findCoinById(String id);
    void updateCoins(List<LocalCoin> list);
    void deleteCoin(LocalCoin localCoin);
}
