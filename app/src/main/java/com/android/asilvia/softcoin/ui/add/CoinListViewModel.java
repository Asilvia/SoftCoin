package com.android.asilvia.softcoin.ui.add;


import android.arch.lifecycle.LiveData;

import com.android.asilvia.softcoin.api.ApiResponse;
import com.android.asilvia.softcoin.repository.DataManager;
import com.android.asilvia.softcoin.ui.base.BaseViewModel;
import com.android.asilvia.softcoin.ui.start.StartNavigator;
import com.android.asilvia.softcoin.util.AbsentLiveData;
import com.android.asilvia.softcoin.util.rx.SchedulerProvider;
import com.android.asilvia.softcoin.vo.Coins;
import com.android.asilvia.softcoin.vo.CoinsDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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


    private void completeImageUrl(ArrayList<CoinsDetails> list, String baseImageUrl) {
        for(CoinsDetails coin: list)
        {
            coin.setImageUrl(baseImageUrl + coin.getImageUrl());
        }
    }


}
