package com.android.asilvia.cryptoo.ui.add;

import android.arch.lifecycle.ViewModelProvider;

import com.android.asilvia.cryptoo.repository.DataManager;
import com.android.asilvia.cryptoo.ui.base.ViewModelProviderFactory;
import com.android.asilvia.cryptoo.util.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by asilvia on 26-10-2017.
 */


    @Module
    public class CoinListActivityModule {

        @Provides
        CoinListViewModel provideStartViewModel(DataManager dataManager,
                                            SchedulerProvider schedulerProvider) {
            return new CoinListViewModel(dataManager, schedulerProvider);
        }

        @Provides
        ViewModelProvider.Factory mainViewModelProvider(CoinListViewModel coinListViewModel) {
            return new ViewModelProviderFactory<>(coinListViewModel);
        }

    }
