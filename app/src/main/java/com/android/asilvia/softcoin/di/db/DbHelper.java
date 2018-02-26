package com.android.asilvia.softcoin.di.db;

import android.arch.lifecycle.LiveData;

import com.android.asilvia.softcoin.db.LocalCoin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ana.medeira on 20-2-2018.
 */

public interface  DbHelper {

    void saveCoin(LocalCoin localCoin);

    List<LocalCoin> getSavedCoins();
}
