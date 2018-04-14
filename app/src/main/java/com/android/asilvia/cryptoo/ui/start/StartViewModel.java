package com.android.asilvia.cryptoo.ui.start;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
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

import rx.schedulers.Schedulers;
import timber.log.Timber;



/**
 * Created by asilvia on 26-10-2017.
 */

public class StartViewModel extends BaseViewModel<StartNavigator> {

    private LiveData<List<LocalCoin>> mObservableCoinsList;
    private MediatorLiveData<List<LocalCoin>> completeCoinList;


    public StartViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        mObservableCoinsList = AbsentLiveData.create();
    }

    void getCoinList()
    {
        Timber.d("Refresh -->" + "teste");
        mObservableCoinsList = AbsentLiveData.create();
        setCompleteCoinList();

        ArrayList<LocalCoin> savedCoins = new ArrayList<>();
        savedCoins.addAll(getDataManager().getSavedCoinList());
        String from = getDataManager().getCoinsName(savedCoins);
        String to = getDataManager().getMainCoin();
        Timber.d("Refresh -->" + "teste111");


        mObservableCoinsList =  Transformations.switchMap(getDataManager().getCoinPrices(from, to), coinList ->{

            for(LocalCoin coin: savedCoins)
            {

                String price = String.valueOf(coinList.body.getRAW().get(coin.getKey()).get(to).getPRICE());
                coin.setPrice(Double.parseDouble(price));
                double index = coinList.body.getRAW().get(coin.getKey()).get(to).getCHANGEPCT24HOUR();
                coin.setIndex(index);
            }
            getDataManager().updatesCoins(savedCoins).subscribeOn(Schedulers.io()).subscribe(() -> {

                Timber.d("--> localcoin success");

                //   AppNavigation.goToStartActivity(CoinListActivity.this);
            }, throwable -> {
                Timber.d("--> localcoin failed" + throwable.getMessage());
            });
            completeCoinList.postValue(savedCoins);
            return completeCoinList;
        });



    }

    public String getCoinSymbol(){
        return getDataManager().getMainCurrencySymbol();
    }

    public boolean isMarket()
    {
        return getDataManager().isMarket();
    }


    LiveData<List<LocalCoin>> getObservableCoinsList()
    {
        return mObservableCoinsList;
    }

    public void setCompleteCoinList() {
        completeCoinList =  new MediatorLiveData<>();
    }



}
