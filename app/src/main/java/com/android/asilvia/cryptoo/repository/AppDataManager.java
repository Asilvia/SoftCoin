package com.android.asilvia.cryptoo.repository;


import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;


import com.android.asilvia.cryptoo.BuildConfig;
import com.android.asilvia.cryptoo.api.ApiResponse;
import com.android.asilvia.cryptoo.api.MainApiHelper;
import com.android.asilvia.cryptoo.di.db.AppDbHelper;
import com.android.asilvia.cryptoo.di.file.FileHelper;
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

    private FileHelper mFileHelper;

    @Inject
    public AppDataManager(Context context, PreferencesHelper preferencesHelper, MainApiHelper mainApiHelper, AppDbHelper appDbHelper, FileHelper appFileHelper) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mMainApiHelper = mainApiHelper;
        mAppDbHelper = appDbHelper;
        mFileHelper = appFileHelper;
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
    public Completable deleteCoin(LocalCoin localCoin) {
        return Completable.fromAction(()-> mAppDbHelper.deleteCoin(localCoin));
    }

    @Override
    public String getCoinsName(List<LocalCoin> list) {
        String coinsList = "";
        for (LocalCoin item : list) {
            Timber.d("--> item: " + item.getKey());
            coinsList = coinsList + item.getKey() + ",";
        }
        if(coinsList.length() != 0) {
            coinsList = coinsList.substring(0, coinsList.length() - 1);
            Timber.d("-->" + coinsList);
        }
        return coinsList;
    }

    @Override
    public ArrayList<String> getSavedSearched() {
        return mPreferencesHelper.getSearchedCoins();
    }

    @Override
    public void setSavedSearched(ArrayList<String> list) {
        mPreferencesHelper.setSearchedCoins(list);
    }

    @Override
    public String getMainCurrencySymbol() {
        if(getMainCoin().equals("EUR"))
            return "€";
        else
            return "$";
    }

    @Override
    public boolean isMarket() {
        return mPreferencesHelper.isMarketPrice();
    }

    @Override
    public Uri getImageUriFromFile(String name) {
        return mFileHelper.getFileUri(name);
    }

    @Override
    public String saveImageOnFile(Bitmap bitmap, String fileName) {
        return mFileHelper.writeBmpToFile(bitmap,fileName);
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
