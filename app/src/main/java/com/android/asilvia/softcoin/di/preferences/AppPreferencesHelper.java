package com.android.asilvia.softcoin.di.preferences;

import android.content.Context;
import android.content.SharedPreferences;

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
    public String getValueExample() {
        return  mPrefs.getString("someValue", null);
    }

    @Override
    public void setValueExample(String valueExample) {
        mPrefs.edit().putString("someValue", valueExample).apply();
    }
}
