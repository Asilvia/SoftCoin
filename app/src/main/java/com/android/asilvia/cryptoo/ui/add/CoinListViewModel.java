package com.android.asilvia.cryptoo.ui.add;


import android.arch.lifecycle.LiveData;

import com.android.asilvia.cryptoo.util.AbsentLiveData;
import com.android.asilvia.cryptoo.api.ApiResponse;
import com.android.asilvia.cryptoo.repository.DataManager;
import com.android.asilvia.cryptoo.ui.base.BaseViewModel;
import com.android.asilvia.cryptoo.util.rx.SchedulerProvider;
import com.android.asilvia.cryptoo.vo.Coins;
import com.android.asilvia.cryptoo.vo.CoinsDetails;
import com.android.asilvia.cryptoo.db.LocalCoin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import rx.Completable;
import timber.log.Timber;

/**
 * Created by asilvia on 26-10-2017.
 */

public class CoinListViewModel extends BaseViewModel<CoinListNavigator> {

    private LiveData<ApiResponse<Coins>> mObservableCoinsList;



    public CoinListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        mObservableCoinsList = AbsentLiveData.create();
    }

    void retrieveCoinList()
    {
        mObservableCoinsList = getDataManager().getCoinList();
    }

    LiveData<ApiResponse<Coins>> getObservableCoinsList()
    {
        return mObservableCoinsList;
    }

    ArrayList<CoinsDetails> getParseDataResponse(ApiResponse<Coins> coinsApiResponse)
    {
        ArrayList<CoinsDetails> list = new ArrayList<>();
        if (coinsApiResponse != null) {
            if (coinsApiResponse.isSuccessful()) {
                Timber.d("isSuccessful" + coinsApiResponse.body.getMessage());
                if(coinsApiResponse.body.getData() != null) {

                    list = new ArrayList<CoinsDetails>(coinsApiResponse.body.getData().values());
                    sortList(list);
                    completeImageUrl(list, coinsApiResponse.body.getBaseImageUrl());
                }


            } else {
                Timber.e(coinsApiResponse.errorMessage);
            }
        } else {
            Timber.d("Api response is null");
        }
        return list;
    }

    private void sortList(ArrayList<CoinsDetails> list) {
        Collections.sort(list, new Comparator<CoinsDetails>() {
            @Override
            public int compare(CoinsDetails o1, CoinsDetails o2) {
                return Integer.parseInt(o1.getSortOrder()) - Integer.parseInt(o2.getSortOrder());
            }
        });
    }

    public Completable saveItem(CoinsDetails coin, String realCoin)
    {

        LocalCoin localCoin = new LocalCoin(coin.getId(), coin.getCoinName(), coin.getName(), coin.getImageUrl(), coin.getUrl(),0d, 0d, realCoin, 0);
        return getDataManager().saveCoin(localCoin);

    }


    private void completeImageUrl(ArrayList<CoinsDetails> list, String baseImageUrl) {
        for(CoinsDetails coin: list)
        {
            coin.setImageUrl(baseImageUrl + coin.getImageUrl());
        }
    }

/*
    public LiveData<Boolean> getmObservableSave() {
        return mObservableSave;
    }*/
}
