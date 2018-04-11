package com.android.asilvia.cryptoo.di.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.asilvia.cryptoo.BuildConfig;

import java.util.ArrayList;
import java.util.StringTokenizer;

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

    @Override
    public ArrayList<String> getSearchedCoins() {
        String savedString = mPrefs.getString("most_searched", "");
        ArrayList<String> savedList = new ArrayList<>();
        if(!savedString.isEmpty()) {
            String[] items = savedString.split(",");


            for (int i = 0; i < items.length; i++) {
                savedList.add(items[i]);
            }
        }
        return savedList;
    }

    @Override
    public void setSearchedCoins(ArrayList<String> list) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            str.append(list.get(i)).append(",");
        }
        mPrefs.edit().putString("most_searched", str.toString()).apply();

    }
}
