package com.android.asilvia.softcoin.di.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by asilvia on 26-10-2017.
 */

public class DbsInfo {
    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DbInfo {

    }
}
