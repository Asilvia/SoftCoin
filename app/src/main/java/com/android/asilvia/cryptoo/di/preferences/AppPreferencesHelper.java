package com.android.asilvia.cryptoo.di.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.asilvia.cryptoo.BuildConfig;

import javax.inject.Inject;

/**
 * Created by asilvia on 26-10-2017.
 */

public class AppPreferencesHelper implements PreferencesHelper {


    private final SharedPreferences mPrefs;


    @Inject
    public AppPreferencesHelper(Context context,
                                @PreferencesInfo.PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }


    @Override
    public String getMainCoin() {
        return mPrefs.getString("MainCoin", BuildConfig.MAINDEFAULTCOIN);

    }

    @Override
    public void setMainCoin(String coin) {
        mPrefs.edit().putString("MainCoin", coin).apply();
    }
}
