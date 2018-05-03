package com.asilvia.cryptoo.di.db;

import com.asilvia.cryptoo.db.LocalCoin;
import com.asilvia.cryptoo.db.dao.LocalCoinDao;

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

    @Override
    public LocalCoin findCoinById(String id) {
        return mlocalCoinDao.findCoinById(id);
    }

    @Override
    public void updateCoins(List<LocalCoin> list) {
        mlocalCoinDao.update(list);
    }

    @Override
    public void deleteCoin(LocalCoin localCoin) {
        mlocalCoinDao.deleteCoin(localCoin);
    }

}
