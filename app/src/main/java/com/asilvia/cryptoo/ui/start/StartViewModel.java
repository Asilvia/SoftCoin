package com.asilvia.cryptoo.ui.start;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.asilvia.cryptoo.db.LocalCoin;
import com.asilvia.cryptoo.repository.DataManager;
import com.asilvia.cryptoo.ui.base.BaseViewModel;
import com.asilvia.cryptoo.util.AbsentLiveData;
import com.asilvia.cryptoo.util.rx.SchedulerProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Completable;
import rx.schedulers.Schedulers;
import timber.log.Timber;



/**
 * Created by asilvia on 26-10-2017.
 */

public class StartViewModel extends BaseViewModel<StartNavigator> {

    private LiveData<List<LocalCoin>> mObservableCoinsList;
    private MutableLiveData<List<LocalCoin>> completeCoinList;



    private MutableLiveData<Boolean> hasError ;


    public StartViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        mObservableCoinsList = AbsentLiveData.create();
        hasError = new MutableLiveData<>();
    }

    void getCoinList()
    {
        Timber.d("Refresh -->" + "test");
        mObservableCoinsList = AbsentLiveData.create();
        setCompleteCoinList();

        ArrayList<LocalCoin> savedCoins = new ArrayList<>();
        savedCoins.addAll(getDataManager().getSavedCoinList());
        String from = getDataManager().getCoinsName(savedCoins);
        String to = getDataManager().getMainCoin();



        mObservableCoinsList =  Transformations.switchMap(getDataManager().getCoinPrices(from, to), coinList ->{

            if(!coinList.isSuccessful())
            {
                hasError.postValue(true);
                Timber.e("Error: " + coinList.errorMessage);
            }

            for(LocalCoin coin: savedCoins)
            {
                if(coinList.body != null) {
                    String price = String.valueOf(coinList.body.getRAW().get(coin.getKey()).get(to).getPRICE());
                    coin.setPrice(Double.parseDouble(price));
                    double index = coinList.body.getRAW().get(coin.getKey()).get(to).getCHANGEPCT24HOUR();
                    coin.setIndex(index);
                }
                else
                {
                    hasError.postValue(true);
                    //hasError.notify();
                    Timber.e("Error: " + coinList.errorMessage);
                    break;
                }

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

    public MutableLiveData<Boolean> getHasError() {
        return hasError;
    }

    public void setHasError(MutableLiveData<Boolean> hasError) {
        this.hasError = hasError;
    }
    public Completable deleteCoin(LocalCoin coin) {
        return getDataManager().deleteCoin(coin);
    }
    public Completable saveItem(LocalCoin localCoin) {return getDataManager().saveCoin(localCoin);}

    public String getLastUpdate()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return  sdf.format(c.getTime());
    }

}
