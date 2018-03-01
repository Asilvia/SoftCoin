package com.android.asilvia.cryptoo.ui.start;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.android.asilvia.cryptoo.db.LocalCoin;
import com.android.asilvia.cryptoo.repository.DataManager;
import com.android.asilvia.cryptoo.ui.base.BaseViewModel;
import com.android.asilvia.cryptoo.util.AbsentLiveData;
import com.android.asilvia.cryptoo.util.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;



/**
 * Created by asilvia on 26-10-2017.
 */

public class StartViewModel extends BaseViewModel<StartNavigator> {

    private LiveData<List<LocalCoin>> mObservableCoinsList;


    public StartViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        mObservableCoinsList = AbsentLiveData.create();
    }

    void getCoinList()
    {
        ArrayList<LocalCoin> savedCoins = new ArrayList<>();
        savedCoins.addAll(getDataManager().getSavedCoinList());
        String from = getCoinsName(savedCoins);
        String to = getDataManager().getMainCoin();
        mObservableCoinsList =  Transformations.switchMap(getDataManager().getCoinPrices(from, to), coinList ->{
            MutableLiveData<List<LocalCoin>> completeCoinList = new MutableLiveData<>();
            for(LocalCoin coin: savedCoins)
            {
                String result = coinList.body.get(coin.getKey()).get(to);
                coin.setPrice(Double.parseDouble(result));
                Timber.d("-->" + result);
            }
            completeCoinList.postValue(savedCoins);
            return completeCoinList;
        });



    }

    LiveData<List<LocalCoin>> getObservableCoinsList()
    {
        return mObservableCoinsList;
    }


    @NonNull
    private String getCoinsName(List<LocalCoin> localCoins) {
        String coinsList = "";
        for (LocalCoin item : localCoins) {
            Timber.d("--> item: " + item.getKey());
            coinsList = coinsList + item.getKey() + ",";
        }
        if(coinsList.length() != 0) {
            coinsList = coinsList.substring(0, coinsList.length() - 1);
            Timber.d("-->" + coinsList);
        }
        return coinsList;
    }


}
