package com.android.asilvia.cryptoo.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleService;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.android.asilvia.cryptoo.R;
import com.android.asilvia.cryptoo.api.ApiResponse;
import com.android.asilvia.cryptoo.db.LocalCoin;
import com.android.asilvia.cryptoo.repository.DataManager;
import com.android.asilvia.cryptoo.util.AbsentLiveData;
import com.android.asilvia.cryptoo.vo.CoinsPrice;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.NotificationTarget;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by ana.medeira on 23-3-2018.
 */

public class UpdateService  extends LifecycleService{
    @Inject
    DataManager mDataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);
        ArrayList<LocalCoin> savedCoins = new ArrayList<>();
        ComponentName theWidget = new ComponentName(this, CryptooWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        savedCoins.addAll(mDataManager.getSavedCoinList());
        String from = mDataManager.getCoinsName(savedCoins);
        String to = mDataManager.getMainCoin();

        RemoteViews view = new RemoteViews(getPackageName(), R.layout.cryptoo_widget);

        int size = savedCoins.size();

        mDataManager.getCoinPrices(from, to).observe(this, new Observer<ApiResponse<CoinsPrice>>() {
            @Override
            public void onChanged(@Nullable ApiResponse<CoinsPrice> coinsPriceApiResponse) {
                String price = String.valueOf(coinsPriceApiResponse.body.getRAW().get(savedCoins.get(0).getKey()).get(to).getPRICE());
                savedCoins.get(0).setPrice(Double.valueOf(price));
                double index = coinsPriceApiResponse.body.getRAW().get(savedCoins.get(0).getKey()).get(to).getCHANGEPCT24HOUR();
                savedCoins.get(0).setIndex(index);
                Timber.d("On changeeeeeee" + price);
                view.setTextViewText(R.id.coin_name1, savedCoins.get(0).getName());

                AppWidgetTarget appWidgetTarget=
                new AppWidgetTarget( getApplicationContext(), view, R.id.coin_icon1, 0 );


                Glide
                        .with( getApplicationContext() ) // safer!
                        .load(savedCoins.get(0).getImageUrl())
                        .asBitmap()
                        .into( appWidgetTarget );





                manager.updateAppWidget(theWidget, view);
                mDataManager.updatesCoins(savedCoins).subscribeOn(Schedulers.io()).subscribe(() -> {

                    Timber.d("--> localcoin success");


                    //   AppNavigation.goToStartActivity(CoinListActivity.this);
                }, throwable -> {
                    Timber.d("--> localcoin failed" + throwable.getMessage());
                });
            }
        });


       /* LiveData<List<LocalCoin>> mObservableCoinsList = Transformations.switchMap(mDataManager.getCoinPrices(from, to), coinList -> {

            for (LocalCoin coin : savedCoins) {

                String price = String.valueOf(coinList.body.getRAW().get(coin.getKey()).get(to).getPRICE());
                coin.setPrice(Double.parseDouble(price));
                double index = coinList.body.getRAW().get(coin.getKey()).get(to).getCHANGEPCT24HOUR();
                coin.setIndex(index);
            }

            if (size == 1) {

                view.setTextViewText(R.id.coin_name1, savedCoins.get(0).getName());
            } else {

                view.setTextViewText(R.id.coin_name1, "Maria");
            }
            mDataManager.updatesCoins(savedCoins).subscribeOn(Schedulers.io()).subscribe(() -> {

                Timber.d("--> localcoin success");


                //   AppNavigation.goToStartActivity(CoinListActivity.this);
            }, throwable -> {
                Timber.d("--> localcoin failed" + throwable.getMessage());
            });
            MediatorLiveData<List<LocalCoin>> completeCoinList = new MediatorLiveData<>();
            completeCoinList.postValue(savedCoins);
            return completeCoinList;
        });




*/


        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }
}
