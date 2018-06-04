package com.asilvia.cryptoo.ui.add;

import android.arch.lifecycle.ViewModelProvider;

import com.asilvia.cryptoo.repository.DataManager;
import com.asilvia.cryptoo.ui.base.ViewModelProviderFactory;
import com.asilvia.cryptoo.util.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by asilvia on 03/06/2018.
 */

@Module
public class AddCoinActivityModule {

    @Provides
    AddCoinViewModel provideAddCoinViewModel(DataManager dataManager,
                                            SchedulerProvider schedulerProvider) {
        return new AddCoinViewModel(dataManager, schedulerProvider);
    }

    @Provides
    ViewModelProvider.Factory addCoinViewModelProvider(AddCoinViewModel addCoinViewModel) {
        return new ViewModelProviderFactory<>(addCoinViewModel);
    }

}
