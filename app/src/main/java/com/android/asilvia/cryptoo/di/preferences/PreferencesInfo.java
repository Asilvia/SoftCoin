package com.android.asilvia.softcoin.di.preferences;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by asilvia on 26-10-2017.
 */

public class PreferencesInfo {
    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PreferenceInfo {

    }
}
