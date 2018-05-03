package com.asilvia.cryptoo.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleService;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RemoteViews;

import com.asilvia.cryptoo.R;
import com.asilvia.cryptoo.api.ApiResponse;
import com.asilvia.cryptoo.db.LocalCoin;
import com.asilvia.cryptoo.repository.DataManager;
import com.asilvia.cryptoo.ui.start.StartActivity;
import com.asilvia.cryptoo.vo.CoinsPrice;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;

import java.util.ArrayList;

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

                if (size <= 0) {
                    showAllEmptyScreens(view);
                } else {
                    updateCoinPrice(coinsPriceApiResponse, size, savedCoins, to);
                    if (size == 1) {
                        showFirstElement(view, savedCoins.get(0));
                    } else if (size == 2) {
                        showFirstTwoElememts(view, savedCoins.get(0), savedCoins.get(1));

                    } else {
                        showAllElements(view, savedCoins.get(0), savedCoins.get(1), savedCoins.get(2));

                    }


                    manager.updateAppWidget(theWidget, view);
                    mDataManager.updatesCoins(savedCoins).subscribeOn(Schedulers.io()).subscribe(() -> {

                        Timber.d("--> localcoin success");


                        //   AppNavigation.goToStartActivity(CoinListActivity.this);
                    }, throwable -> {
                        Timber.d("--> localcoin failed" + throwable.getMessage());
                    });
                }
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    private PendingIntent getPendingIntent(String action)
    {
        Intent intent = new Intent(this, StartActivity.class);
        intent.putExtra("Action", action);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        return PendingIntent.getActivity(this, 0,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private void showAllElements(RemoteViews view, LocalCoin localCoin, LocalCoin localCoin1, LocalCoin localCoin2) {
        setFirstColumn(view, localCoin);
        setSecondColumn(view, localCoin1);
        setThirdColumn(view, localCoin2);

    }

    private void showFirstTwoElememts(RemoteViews view, LocalCoin localCoin, LocalCoin localCoin1) {
            setFirstColumn(view, localCoin);
            setSecondColumn(view, localCoin1);
            showThirdElementEmpty(view);

    }

    private void updateCoinPrice(@Nullable ApiResponse<CoinsPrice> coinsPriceApiResponse, int size, ArrayList<LocalCoin> savedCoins, String to) {
       if(coinsPriceApiResponse.body != null) {
           for (int i = 0; i < size; i++) {
               String price = String.valueOf(coinsPriceApiResponse.body.getRAW().get(savedCoins.get(i).getKey()).get(to).getPRICE());
               savedCoins.get(i).setPrice(Double.valueOf(price));
               double index = coinsPriceApiResponse.body.getRAW().get(savedCoins.get(i).getKey()).get(to).getCHANGEPCT24HOUR();
               savedCoins.get(i).setIndex(index);
           }
       }
       else{
           Timber.d("Error on the widget");
       }
    }

    private void showFirstElement(RemoteViews view, LocalCoin coin) {
        setFirstColumn(view, coin);
        showSecondElementEmpty(view);
        showThirdElementEmpty(view);
    }

    private void showAllEmptyScreens(RemoteViews view) {

        showFirstElementEmpty(view);
        showSecondElementEmpty(view);
        showThirdElementEmpty(view);
    }


    private void setFirstColumn(RemoteViews view, LocalCoin coin) {
        showFirstElement(view);
        view.setTextViewText(R.id.coin_name1, coin.getKey());
        AppWidgetTarget appWidgetTarget=
        new AppWidgetTarget( getApplicationContext(), view, R.id.coin_icon1, 0 );


        Glide
                .with( getApplicationContext() ) // safer!
                .load(mDataManager.getImageUriFromFile(coin.getKey()))
                .asBitmap()
                .into( appWidgetTarget );

        view.setTextViewText(R.id.coin_percentage1, String.format("%.2f",coin.getIndex() ) + "%");
        view.setImageViewResource(R.id.coin_indicator1, getIndicator(coin.getIndex()));
        view.setTextViewText(R.id.coin_value1, mDataManager.getMainCurrencySymbol() + String.format("%.2f",coin.getPrice() ));
        view.setTextViewText(R.id.coin_user_profit1, mDataManager.getMainCurrencySymbol() + coin.getUserProfit());
        view.setTextViewText(R.id.coin_user_profit_indicator1, getProfitIndexText(coin));
        view.setImageViewResource(R.id.coin_user_profit_indicator_icon1, getIndicator(getProfitIndex(coin)));
    }
    private void setSecondColumn(RemoteViews view, LocalCoin coin) {
        showSecondElement(view);
        view.setTextViewText(R.id.coin_name2, coin.getKey());
        AppWidgetTarget appWidgetTarget=
                new AppWidgetTarget( getApplicationContext(), view, R.id.coin_icon2, 0 );


        Glide
                .with( getApplicationContext() ) // safer!
                .load(mDataManager.getImageUriFromFile(coin.getKey()))
                .asBitmap()
                .into( appWidgetTarget );

        view.setTextViewText(R.id.coin_percentage2, String.format("%.2f",coin.getIndex() ) + "%");
        view.setImageViewResource(R.id.coin_indicator2, getIndicator(coin.getIndex()));
        view.setTextViewText(R.id.coin_value2, mDataManager.getMainCurrencySymbol() +String.format("%.2f",coin.getPrice() ));
        view.setTextViewText(R.id.coin_user_profit2, mDataManager.getMainCurrencySymbol() + coin.getUserProfit());
        view.setTextViewText(R.id.coin_user_profit_indicator2, getProfitIndexText(coin));
        view.setImageViewResource(R.id.coin_user_profit_indicator_icon2, getIndicator(getProfitIndex(coin)));
    }

    private void setThirdColumn(RemoteViews view, LocalCoin coin) {

        showThirdElement(view);
        view.setTextViewText(R.id.coin_name3, coin.getKey());
        AppWidgetTarget appWidgetTarget=
                new AppWidgetTarget( getApplicationContext(), view, R.id.coin_icon3, 0 );


        Glide
                .with( getApplicationContext() ) // safer!
                .load(mDataManager.getImageUriFromFile(coin.getKey()))
                .asBitmap()
                .into( appWidgetTarget );

        view.setTextViewText(R.id.coin_percentage3, String.format("%.2f",coin.getIndex() ) + "%");
        view.setImageViewResource(R.id.coin_indicator3, getIndicator(coin.getIndex()));
        view.setTextViewText(R.id.coin_value3, mDataManager.getMainCurrencySymbol() +String.format("%.2f",coin.getPrice() ));
        view.setTextViewText(R.id.coin_user_profit3, mDataManager.getMainCurrencySymbol() + coin.getUserProfit());
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

    private void showFirstElementEmpty(RemoteViews view) {
        view.setViewVisibility(R.id.llFirstEmpty, View.VISIBLE);
        view.setViewVisibility(R.id.llFirstrowEmpty, View.VISIBLE);
        view.setViewVisibility(R.id.llFirst, View.GONE);
        view.setViewVisibility(R.id.llFirstrow, View.GONE);
        view.setOnClickPendingIntent(R.id.llFirstEmpty, getPendingIntent("add"));
    }
    private void showSecondElementEmpty(RemoteViews view) {
        view.setViewVisibility(R.id.llSecondEmpty, View.VISIBLE);
        view.setViewVisibility(R.id.llSecondrowEmpty, View.VISIBLE);
        view.setViewVisibility(R.id.llSecond, View.GONE);
        view.setViewVisibility(R.id.llSecondrow, View.GONE);
        view.setOnClickPendingIntent(R.id.llSecondEmpty, getPendingIntent("add"));

    }
    private void showThirdElementEmpty(RemoteViews view) {
        view.setViewVisibility(R.id.llThirdEmpty, View.VISIBLE);
        view.setViewVisibility(R.id.llThirdrowEmpty, View.VISIBLE);
        view.setViewVisibility(R.id.llThird, View.GONE);
        view.setViewVisibility(R.id.llThirdrow, View.GONE);
        view.setOnClickPendingIntent(R.id.llThirdEmpty, getPendingIntent("add"));
    }

    private void showFirstElement(RemoteViews view) {
        view.setViewVisibility(R.id.llFirstEmpty, View.GONE);
        view.setViewVisibility(R.id.llFirstrowEmpty, View.GONE);
        view.setViewVisibility(R.id.llFirst, View.VISIBLE);
        view.setViewVisibility(R.id.llFirstrow, View.VISIBLE);
        view.setOnClickPendingIntent(R.id.llFirst, getPendingIntent("start"));
        view.setOnClickPendingIntent(R.id.llFirstrow, getPendingIntent("start"));
    }
    private void showSecondElement(RemoteViews view) {
        view.setViewVisibility(R.id.llSecondEmpty, View.GONE);
        view.setViewVisibility(R.id.llSecondrowEmpty, View.GONE);
        view.setViewVisibility(R.id.llSecond, View.VISIBLE);
        view.setViewVisibility(R.id.llSecondrow, View.VISIBLE);
        view.setOnClickPendingIntent(R.id.llSecond, getPendingIntent("start"));
        view.setOnClickPendingIntent(R.id.llSecondrow, getPendingIntent("start"));
    }
    private void showThirdElement(RemoteViews view) {
        view.setViewVisibility(R.id.llThirdEmpty, View.GONE);
        view.setViewVisibility(R.id.llThirdrowEmpty, View.GONE);
        view.setViewVisibility(R.id.llThird, View.VISIBLE);
        view.setViewVisibility(R.id.llThirdrow, View.VISIBLE);
        view.setOnClickPendingIntent(R.id.llThird, getPendingIntent("start"));
        view.setOnClickPendingIntent(R.id.llThirdrow, getPendingIntent("start"));
    }
}
