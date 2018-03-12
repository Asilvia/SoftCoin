package com.android.asilvia.cryptoo.repository;


import android.arch.lifecycle.LiveData;
import android.content.Context;


import com.android.asilvia.cryptoo.BuildConfig;
import com.android.asilvia.cryptoo.api.ApiResponse;
import com.android.asilvia.cryptoo.api.MainApiHelper;
import com.android.asilvia.cryptoo.di.db.AppDbHelper;
import com.android.asilvia.cryptoo.di.preferences.PreferencesHelper;
import com.android.asilvia.cryptoo.vo.Coins;
import com.android.asilvia.cryptoo.db.LocalCoin;
import com.android.asilvia.cryptoo.vo.CoinsPrice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Completable;
import timber.log.Timber;

/**
 * Created by asilvia on 26-10-2017.
 */
@Singleton
public class AppDataManager implements DataManager {
    public static final String TAG = "AppDataManager";
    private final Context mContext;

    private PreferencesHelper mPreferencesHelper;

    private MainApiHelper mMainApiHelper;


    private AppDbHelper mAppDbHelper;

    @Inject
    public AppDataManager(Context context, PreferencesHelper preferencesHelper, MainApiHelper mainApiHelper, AppDbHelper appDbHelper) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mMainApiHelper = mainApiHelper;
        mAppDbHelper = appDbHelper;
    }


    @Override
    public LiveData<ApiResponse<Coins>> getCoinList() {
        Timber.d("AppDataManager getCoinList");
        return mMainApiHelper.getCoinList();
    }

    @Override
    public LiveData<ApiResponse<CoinsPrice>> getCoinPrices(String from, String to) {
        return mMainApiHelper.getCoinPrices(from, to);
    }

    @Override
    public Completable saveCoin(LocalCoin coin)
    {
        return Completable.fromAction(() -> mAppDbHelper.saveCoin(coin));
    }

    @Override
    public Completable updatesCoins(List<LocalCoin> coins)
    {
        return Completable.fromAction(() -> mAppDbHelper.updateCoins(coins));
    }

    @Override
    public String getMainCoin() {
        return mPreferencesHelper.getMainCoin();

    }

    @Override
    public void setMainCoin(String coin) {
        mPreferencesHelper.setMainCoin(coin);
    }


    @Override
    public List<LocalCoin> getSavedCoinList() {
        return mAppDbHelper.getSavedCoins();
    }

    @Override
    public Completable saveCoinList(List<LocalCoin> list) {
        return Completable.fromAction(() -> mAppDbHelper.saveCoinList(list));
    }

    @Override
    public LocalCoin findCoindById(String id) {
        return mAppDbHelper.findCoinById(id);
    }
}
