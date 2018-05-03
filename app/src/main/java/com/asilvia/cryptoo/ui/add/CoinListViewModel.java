package com.asilvia.cryptoo.ui.add;


import android.arch.lifecycle.LiveData;
import android.graphics.Bitmap;

import com.asilvia.cryptoo.api.ApiResponse;
import com.asilvia.cryptoo.db.LocalCoin;
import com.asilvia.cryptoo.repository.DataManager;
import com.asilvia.cryptoo.ui.base.BaseViewModel;
import com.asilvia.cryptoo.util.AbsentLiveData;
import com.asilvia.cryptoo.util.rx.SchedulerProvider;
import com.asilvia.cryptoo.vo.Coins;
import com.asilvia.cryptoo.vo.CoinsDetails;
import com.asilvia.cryptoo.vo.CoinsPrice;

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
                    removeLocalcoins(list);
                }


            } else {
                Timber.e(coinsApiResponse.errorMessage);
            }
        } else {
            Timber.d("Api response is null");
        }
        return list;
    }

    private void removeLocalcoins(ArrayList<CoinsDetails> list) {

        ArrayList<LocalCoin> savedCoins = new ArrayList<>();
        savedCoins.addAll(getDataManager().getSavedCoinList());
        for (int i=0; i<savedCoins.size(); i++)
        {
            for(int j=0; j<list.size(); j++)
            {
                if(savedCoins.get(i).getName().equals(list.get(j).getCoinName()))
                {
                    list.remove(j);
                    break;
                }
            }
        }
    }

    private void sortList(ArrayList<CoinsDetails> list) {
        Collections.sort(list, new Comparator<CoinsDetails>() {
            @Override
            public int compare(CoinsDetails o1, CoinsDetails o2) {
                return Integer.parseInt(o1.getSortOrder()) - Integer.parseInt(o2.getSortOrder());
            }
        });
    }
//todo change long to double
    public Completable saveItem(CoinsDetails coin, Double userprice, long amount)
    {

        LocalCoin localCoin = new LocalCoin(coin.getId(), coin.getCoinName(), coin.getName(), coin.getImageUrl(), coin.getUrl(),0d, userprice, getDataManager().getMainCoin(), amount,0);
        return getDataManager().saveCoin(localCoin);

    }

    public void saveImage(Bitmap bitmap, String name)
    {
        getDataManager().saveImageOnFile(bitmap, name);
    }


    private void completeImageUrl(ArrayList<CoinsDetails> list, String baseImageUrl) {
        for(CoinsDetails coin: list)
        {
            coin.setImageUrl(baseImageUrl + coin.getImageUrl());
        }
    }


    public ArrayList<String> getSavedSearch() {
        return getDataManager().getSavedSearched();

    }

    public void setSavedSearch(ArrayList<String> savedSearch) {
        getDataManager().setSavedSearched(savedSearch);
    }

    public LiveData<ApiResponse<CoinsPrice>> getCoinPrice(String from)
    {
        return getDataManager().getCoinPrices(from, getDataManager().getMainCoin());
    }

    public String getSymbol()
    {
        return getDataManager().getMainCurrencySymbol();
    }



}
