package com.android.asilvia.cryptoo.ui.base;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import com.android.asilvia.cryptoo.BR;
import com.android.asilvia.cryptoo.repository.DataManager;
import com.android.asilvia.cryptoo.util.rx.SchedulerProvider;


/**
 * Created by asilvia on 26-10-2017.
 */

public abstract class BaseViewModel<N> extends ViewModel {
    private N mNavigator;
    private final DataManager mDataManager;
    private final SchedulerProvider mSchedulerProvider;
    private final ObservableBoolean mIsLoading = new ObservableBoolean(false);

    public BaseViewModel(DataManager dataManager,
                         SchedulerProvider schedulerProvider) {
        this.mDataManager = dataManager;
        this.mSchedulerProvider = schedulerProvider;
    }


    public void onViewCreated() {

    }

    public void onDestroyView() {

    }

    public void setNavigator(N navigator) {
        this.mNavigator = navigator;
    }


    public N getNavigator() {
        return mNavigator;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }


    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean isLoading) {
        mIsLoading.set(isLoading);
    }
}
