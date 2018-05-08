package com.asilvia.cryptoo.ui.details;

import android.arch.lifecycle.ViewModelProvider;

import com.asilvia.cryptoo.repository.DataManager;
import com.asilvia.cryptoo.ui.base.ViewModelProviderFactory;
import com.asilvia.cryptoo.util.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by asilvia on 05-03-2018.
 */


    @Module
    public class CoinDetailsActivityModule {

        @Provides
        CoinDetailsViewModel provideCoinDetailsViewModel(DataManager dataManager,
                                                      SchedulerProvider schedulerProvider) {
            return new CoinDetailsViewModel(dataManager, schedulerProvider);
        }

        @Provides
        ViewModelProvider.Factory mainViewModelProvider(CoinDetailsViewModel coinDetailsViewModel) {
            return new ViewModelProviderFactory<>(coinDetailsViewModel);
        }

    }