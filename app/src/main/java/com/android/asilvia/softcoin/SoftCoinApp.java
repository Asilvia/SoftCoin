package com.android.asilvia.softcoin;

import android.app.Activity;
import android.app.Application;
import android.arch.persistence.room.Room;


import com.android.asilvia.softcoin.di.component.DaggerMainComponent;
import com.android.asilvia.softcoin.di.db.AppDatabase;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

/**
 * Created by asilvia on 26-10-2017.
 */

public class SoftCoinApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    private AppDatabase mDatabase;


    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        DaggerMainComponent.builder().application(this).build().inject(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }
}
