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
import android.view.View;
import android.widget.RemoteViews;

import com.android.asilvia.cryptoo.R;
import com.android.asilvia.cryptoo.api.ApiResponse;
import com.android.asilvia.cryptoo.db.LocalCoin;
import com.android.asilvia.cryptoo.repository.DataManager;
import com.android.asilvia.cryptoo.ui.start.StartAdapter;
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
                setFirstColumn(view, savedCoins.get(0));
                setSecondColumn(view, savedCoins.get(1));
                if(size >= 3) {
                    view.setViewVisibility(R.id.llThirdEmpty, View.GONE);
                    view.setViewVisibility(R.id.llThird, View.VISIBLE);
                    view.setViewVisibility(R.id.llThirdrow, View.VISIBLE);
                    view.setViewVisibility(R.id.llThirdrowEmpty, View.GONE);
                    setThirdColumn(view, savedCoins.get(2));
                }
                else
                {
                    view.setViewVisibility(R.id.llThirdEmpty, View.VISIBLE);
                    view.setViewVisibility(R.id.llThird, View.GONE);
                    view.setViewVisibility(R.id.llThirdrow, View.GONE);
                    view.setViewVisibility(R.id.llThirdrowEmpty, View.VISIBLE);
                }




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

    private void setFirstColumn(RemoteViews view, LocalCoin coin) {
        view.setTextViewText(R.id.coin_name1, coin.getKey());
        AppWidgetTarget appWidgetTarget=
        new AppWidgetTarget( getApplicationContext(), view, R.id.coin_icon1, 0 );


        Glide
                .with( getApplicationContext() ) // safer!
                .load(coin.getImageUrl())
                .asBitmap()
                .into( appWidgetTarget );

        view.setTextViewText(R.id.coin_percentage1, String.format("%.2f",coin.getIndex() ) + "%");
        view.setImageViewResource(R.id.coin_indicator1, getIndicator(coin.getIndex()));
        view.setTextViewText(R.id.coin_value1, String.format("%.2f",coin.getPrice() ));
        view.setTextViewText(R.id.coin_user_profit1, getProfit(coin));
        view.setTextViewText(R.id.coin_user_profit_indicator1, getProfitIndexText(coin));
        view.setImageViewResource(R.id.coin_user_profit_indicator_icon1, getIndicator(getProfitIndex(coin)));
    }
    private void setSecondColumn(RemoteViews view, LocalCoin coin) {
        view.setTextViewText(R.id.coin_name2, coin.getKey());
        AppWidgetTarget appWidgetTarget=
                new AppWidgetTarget( getApplicationContext(), view, R.id.coin_icon2, 0 );


        Glide
                .with( getApplicationContext() ) // safer!
                .load(coin.getImageUrl())
                .asBitmap()
                .into( appWidgetTarget );

        view.setTextViewText(R.id.coin_percentage2, String.format("%.2f",coin.getIndex() ) + "%");
        view.setImageViewResource(R.id.coin_indicator2, getIndicator(coin.getIndex()));
        view.setTextViewText(R.id.coin_value2, String.format("%.2f",coin.getPrice() ));
        view.setTextViewText(R.id.coin_user_profit2, getProfit(coin));
        view.setTextViewText(R.id.coin_user_profit_indicator2, getProfitIndexText(coin));
        view.setImageViewResource(R.id.coin_user_profit_indicator_icon2, getIndicator(getProfitIndex(coin)));
    }

    private void setThirdColumn(RemoteViews view, LocalCoin coin) {
        view.setTextViewText(R.id.coin_name3, coin.getKey());
        AppWidgetTarget appWidgetTarget=
                new AppWidgetTarget( getApplicationContext(), view, R.id.coin_icon3, 0 );


        Glide
                .with( getApplicationContext() ) // safer!
                .load(coin.getImageUrl())
                .asBitmap()
                .into( appWidgetTarget );

        view.setTextViewText(R.id.coin_percentage3, String.format("%.2f",coin.getIndex() ) + "%");
        view.setImageViewResource(R.id.coin_indicator3, getIndicator(coin.getIndex()));
        view.setTextViewText(R.id.coin_value3, String.format("%.2f",coin.getPrice() ));
        view.setTextViewText(R.id.coin_user_profit3, getProfit(coin));
        view.setTextViewText(R.id.coin_user_profit_indicator3, getProfitIndexText(coin));
        view.setImageViewResource(R.id.coin_user_profit_indicator_icon3, getIndicator(getProfitIndex(coin)));
    }

    private int  getIndicator(double percentage) {
        if(percentage > 0)
        {
           return R.drawable.ic_arrow_drop_up;
        }
        else
        {
            return R.drawable.ic_arrow_drop_down;

        }
    }
    public String getProfit(LocalCoin localCoin)
    {

        double finalPrice = localCoin.getPrice();
        double userPrice = localCoin.getUserPrice();
        return String.format("%.2f",(finalPrice - userPrice));
    }



    public String getProfitIndexText(LocalCoin localCoin)
    {

        double finalPrice = localCoin.getPrice();
        double userPrice = localCoin.getUserPrice();
        return String.format("%.2f", ((finalPrice - userPrice)/finalPrice * 100)) + "%";
    }

    public double getProfitIndex(LocalCoin localCoin)
    {

        double finalPrice = localCoin.getPrice();
        double userPrice = localCoin.getUserPrice();
        return  ((finalPrice - userPrice)/finalPrice * 100);
    }


    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }
}
