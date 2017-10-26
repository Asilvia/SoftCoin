package com.android.asilvia.softcoin.repository;


import android.arch.lifecycle.LiveData;
import android.content.Context;


import com.android.asilvia.softcoin.api.ApiResponse;
import com.android.asilvia.softcoin.api.DetailsApiHelper;
import com.android.asilvia.softcoin.api.MainApiHelper;
import com.android.asilvia.softcoin.di.preferences.PreferencesHelper;
import com.android.asilvia.softcoin.vo.Coins;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

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

    private DetailsApiHelper mDetailsApiHelper;

    @Inject
    public AppDataManager(Context context, PreferencesHelper preferencesHelper, MainApiHelper mainApiHelper, DetailsApiHelper detailsApiHelper) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mMainApiHelper = mainApiHelper;
        mDetailsApiHelper = detailsApiHelper;
    }


    @Override
    public LiveData<ApiResponse<Coins>> getCoinList() {
        Timber.d("AppDataManager getCoinList");
        return mMainApiHelper.getCoinList();
    }

    @Override
    public LiveData<ApiResponse<Map<String, Map<String, String>>>> getCoinPrices(String from, String to) {
        return mDetailsApiHelper.getCoinPrices(from, to);
    }

    @Override
    public String getValueExample() {
        return mPreferencesHelper.getValueExample();
    }

    @Override
    public void setValueExample(String valueExample) {
        mPreferencesHelper.setValueExample(valueExample);
    }
}
