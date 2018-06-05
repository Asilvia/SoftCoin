package com.asilvia.cryptoo.ui.add;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
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

public class AddCoinViewModel extends BaseViewModel<AddCoinNavigator> {

    private LiveData<ApiResponse<Coins>> mObservableCoinsList;
    public CoinsDetails selected;
    private LiveData<LocalCoin> localCoin;
    private LocalCoin tempCoin;


    public AddCoinViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        mObservableCoinsList = AbsentLiveData.create();
        selected = new CoinsDetails();
    }

    public void retrieveCoinList()
    {
        mObservableCoinsList = getDataManager().getCoinList();
    }

    public LiveData<ApiResponse<Coins>> getObservableCoinsList()
    {
        return mObservableCoinsList;
    }

    public ArrayList<CoinsDetails> getParseDataResponse(ApiResponse<Coins> coinsApiResponse)
    {
        ArrayList<CoinsDetails> list = new ArrayList<>();
        if (coinsApiResponse != null) {
            if (coinsApiResponse.isSuccessful()) {

                //TODO improve this  time
                Timber.d("isSuccessful" + coinsApiResponse.body.getMessage());
                if(coinsApiResponse.body.getData() != null) {
                    list = new ArrayList<CoinsDetails>(coinsApiResponse.body.getData().values());
                    sortList(list);

                    long startTime = System.currentTimeMillis();
                    completeImageUrl(list, coinsApiResponse.body.getBaseImageUrl());
                    removeLocalcoins(list);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    Timber.d("=====Time: " + elapsedTime);
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
    public Completable saveItem(Double userprice, long amount)
    {
        LocalCoin localCoin = new LocalCoin(selected.getId(), selected.getCoinName(), selected.getName(), selected.getImageUrl(), selected.getUrl(),0d, userprice, getDataManager().getMainCoin(), amount,0);
        return getDataManager().saveCoin(localCoin);
    }

    public void saveImage(Bitmap bitmap)
    {
        getDataManager().saveImageOnFile(bitmap, selected.getName());
    }


    private void completeImageUrl(ArrayList<CoinsDetails> list, String baseImageUrl) {
        for(CoinsDetails coin: list)
        {
            if(coin.getImageUrl() != null) {
                coin.setImageUrl(baseImageUrl + coin.getImageUrl());
            }
        }
    }


    public LiveData<LocalCoin> getCoinPrice(CoinsDetails coinsDetails) {
        tempCoin = new LocalCoin(coinsDetails.getId(), coinsDetails.getCoinName(), coinsDetails.getName(), coinsDetails.getImageUrl(), coinsDetails.getUrl(), 0d, 0d, getDataManager().getMainCoin(), 0, 0);

        localCoin = Transformations.switchMap(getDataManager().getCoinPrices(coinsDetails.getName(), getDataManager().getMainCoin()), response -> {
            MutableLiveData<LocalCoin> mCoin = new MutableLiveData<>();
            if (response.isSuccessful()) {
                if (response.body != null && response.body.getRAW()!= null) {
                    String price = String.valueOf(response.body.getRAW().get(tempCoin.getKey()).get(getDataManager().getMainCoin()).getPRICE());
                    tempCoin.setPrice(Double.parseDouble(price));
                    double index = response.body.getRAW().get(tempCoin.getKey()).get(getDataManager().getMainCoin()).getCHANGEPCT24HOUR();
                    tempCoin.setIndex(index);
                }
            }
            mCoin.postValue(tempCoin);
            return mCoin;
        });
        return localCoin;

    }


    public String getSymbol()
    {
        return getDataManager().getMainCurrencySymbol();
    }

    public CoinsDetails getSelected() {
        Timber.d(" ====== getSelected ======" + selected.getCoinName());
        return selected;
    }
    public void select(CoinsDetails item) {
        Timber.d(" ====== select ======" + item.getCoinName());
        selected = item;

    }



}
