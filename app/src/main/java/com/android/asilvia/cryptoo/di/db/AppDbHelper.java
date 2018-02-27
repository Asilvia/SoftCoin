package com.android.asilvia.softcoin.di.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.android.asilvia.softcoin.db.LocalCoin;
import com.android.asilvia.softcoin.db.dao.LocalCoinDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by ana.medeira on 20-2-2018.
 */

@Singleton
public class AppDbHelper implements DbHelper {
    private final AppDatabase mAppDatabase;
    private final LocalCoinDao mlocalCoinDao;

    @Inject
    public AppDbHelper(@DbsInfo.DbInfo AppDatabase appDatabase)
    {
        mAppDatabase = appDatabase;
        mlocalCoinDao = mAppDatabase.localCoinDao();
    }

    @Override
    public void saveCoin(LocalCoin localCoin) {
        mlocalCoinDao.insert(localCoin);
    }

    @Override
    public List<LocalCoin> getSavedCoins() {
        return mlocalCoinDao.findAllCoins();
    }

    @Override
    public void saveCoinList(List<LocalCoin> list) {
        mlocalCoinDao.saveCoinList(list);
    }
}
