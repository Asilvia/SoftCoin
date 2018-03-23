package com.android.asilvia.cryptoo.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleService;
import android.arch.lifecycle.LiveData;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.android.asilvia.cryptoo.R;
import com.android.asilvia.cryptoo.db.LocalCoin;
import com.android.asilvia.cryptoo.repository.DataManager;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

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
        savedCoins.addAll(mDataManager.getSavedCoinList());

        int size = savedCoins.size();
        



        RemoteViews view = new RemoteViews(getPackageName(), R.layout.cryptoo_widget);
        view.setTextViewText(R.id.coin_name1, "Ana");


        ComponentName theWidget = new ComponentName(this, CryptooWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(theWidget, view);

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }
}
