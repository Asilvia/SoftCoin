package com.android.asilvia.cryptoo;

import android.app.Activity;
import android.app.Application;
import android.app.Service;


import com.android.asilvia.cryptoo.di.db.AppDatabase;
import com.android.asilvia.cryptoo.di.component.DaggerMainComponent;


import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import timber.log.Timber;

/**
 * Created by asilvia on 26-10-2017.
 */

public class CryptooApp extends Application implements HasActivityInjector, HasServiceInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Inject
    DispatchingAndroidInjector<Service> serviceDispatchingAndroidInjector;

    private AppDatabase mDatabase;


    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceDispatchingAndroidInjector;
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
