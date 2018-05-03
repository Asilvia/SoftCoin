package com.asilvia.cryptoo.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.asilvia.cryptoo.db.dao.LocalCoinDao;
import com.asilvia.cryptoo.di.db.AppDatabase;
import com.asilvia.cryptoo.di.db.AppDbHelper;
import com.asilvia.cryptoo.di.db.DbHelper;
import com.asilvia.cryptoo.di.db.DbsInfo;
import com.asilvia.cryptoo.di.file.AppFileHelper;
import com.asilvia.cryptoo.di.file.FileHelper;
import com.asilvia.cryptoo.di.file.FilesInfo;
import com.asilvia.cryptoo.di.preferences.AppPreferencesHelper;
import com.asilvia.cryptoo.di.preferences.PreferencesHelper;
import com.asilvia.cryptoo.di.preferences.PreferencesInfo;
import com.asilvia.cryptoo.repository.AppDataManager;
import com.asilvia.cryptoo.repository.DataManager;
import com.asilvia.cryptoo.util.AppConstants;
import com.asilvia.cryptoo.util.rx.AppSchedulerProvider;
import com.asilvia.cryptoo.util.rx.SchedulerProvider;

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


    @Provides
    @FilesInfo.FileInfo
    String provideFileName() {
        return AppConstants.DIRECTORY_NAME;
    }

    @Provides
    @Singleton
    FileHelper provideFileHelper(AppFileHelper appFileHelper) {
        return appFileHelper;
    }




}
