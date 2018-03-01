package com.android.asilvia.cryptoo.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.android.asilvia.cryptoo.db.dao.LocalCoinDao;
import com.android.asilvia.cryptoo.di.db.AppDatabase;
import com.android.asilvia.cryptoo.di.db.AppDbHelper;
import com.android.asilvia.cryptoo.di.db.DbHelper;
import com.android.asilvia.cryptoo.di.db.DbsInfo;
import com.android.asilvia.cryptoo.di.preferences.AppPreferencesHelper;
import com.android.asilvia.cryptoo.di.preferences.PreferencesHelper;
import com.android.asilvia.cryptoo.di.preferences.PreferencesInfo;
import com.android.asilvia.cryptoo.repository.AppDataManager;
import com.android.asilvia.cryptoo.repository.DataManager;
import com.android.asilvia.cryptoo.util.AppConstants;
import com.android.asilvia.cryptoo.util.rx.AppSchedulerProvider;
import com.android.asilvia.cryptoo.util.rx.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Created by asilvia on 26-10-2017.
 */
@Module
public class AppModule {


    @Provides
    @PreferencesInfo.PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    //Database declaration
    @Provides
    @Singleton
    @DbsInfo.DbInfo
    AppDatabase provideAppDatabase(Application app) {
        return Room.databaseBuilder(app, AppDatabase.class,"cryptoo.db").allowMainThreadQueries().build();
    }
    @Singleton
    @Provides
    LocalCoinDao provideLocalCoinDao(AppDatabase db) {
        return db.localCoinDao();
    }

    @Provides
    @Singleton
    DbHelper provideDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }




}
