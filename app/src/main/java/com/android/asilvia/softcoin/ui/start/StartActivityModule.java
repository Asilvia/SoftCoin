package com.android.asilvia.softcoin.ui.start;

import android.arch.lifecycle.ViewModelProvider;


import com.android.asilvia.softcoin.repository.DataManager;
import com.android.asilvia.softcoin.ui.base.ViewModelProviderFactory;
import com.android.asilvia.softcoin.util.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by asilvia on 26-10-2017.
 */


    @Module
    public class StartActivityModule {

        @Provides
        StartViewModel provideStartViewModel(DataManager dataManager,
                                            SchedulerProvider schedulerProvider) {
            return new StartViewModel(dataManager, schedulerProvider);
        }

        @Provides
        ViewModelProvider.Factory mainViewModelProvider(StartViewModel startViewModel) {
            return new ViewModelProviderFactory<>(startViewModel);
        }

    }
