package com.android.asilvia.softcoin.ui.start;


import android.arch.lifecycle.LiveData;
import android.databinding.Bindable;

import com.android.asilvia.softcoin.BR;
import com.android.asilvia.softcoin.api.ApiResponse;
import com.android.asilvia.softcoin.repository.DataManager;
import com.android.asilvia.softcoin.ui.base.BaseViewModel;
import com.android.asilvia.softcoin.util.AbsentLiveData;
import com.android.asilvia.softcoin.util.rx.SchedulerProvider;
import com.android.asilvia.softcoin.vo.Coins;

/**
 * Created by asilvia on 26-10-2017.
 */

public class StartViewModel extends BaseViewModel<StartNavigator> {

    private LiveData<ApiResponse<Coins>> mObservableCoinsList;


    public StartViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        mObservableCoinsList = AbsentLiveData.create();
    }

    void RetrieveCoinList()
    {
        mObservableCoinsList = getDataManager().getCoinList();
    }

    LiveData<ApiResponse<Coins>> getObservableCoinsList()
    {
        return mObservableCoinsList;
    }

    



}
