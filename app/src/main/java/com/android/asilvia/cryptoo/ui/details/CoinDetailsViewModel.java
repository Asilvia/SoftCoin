package com.android.asilvia.cryptoo.ui.details;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.android.asilvia.cryptoo.R;
import com.android.asilvia.cryptoo.db.LocalCoin;
import com.android.asilvia.cryptoo.repository.DataManager;
import com.android.asilvia.cryptoo.ui.base.BaseViewModel;
import com.android.asilvia.cryptoo.util.AbsentLiveData;
import com.android.asilvia.cryptoo.util.rx.SchedulerProvider;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import rx.Completable;
import timber.log.Timber;


/**
 * Created by asilvia on 26-10-2017.
 */

public class CoinDetailsViewModel extends BaseViewModel<CoinDetailsNavigator> {

    private LocalCoin mCoin;


    public CoinDetailsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    void getCoinById(String id)
    {
        mCoin = getDataManager().findCoindById(id);
    }

    public String getImageUrl()
    {
        return mCoin.getImageUrl();
    }

    public String getTotalAmount()
    {
        return getDataManager().getMainCurrencySymbol() + String.valueOf(mCoin.getAmount() * mCoin.getUserPrice());
    }

    public String getPrice()
    {
        return getDataManager().getMainCurrencySymbol() + String.valueOf(mCoin.getPrice());
    }

    public String getIndex()
    {
        return String.format("%.2f", mCoin.getIndex()) + "%";
    }
    public String getAmount()
    {
        return String.valueOf(mCoin.getAmount());
    }

    public String getInitialPrice()
    {
        return getDataManager().getMainCurrencySymbol() + String.valueOf(mCoin.getUserPrice());
    }

    public String getProfit()
    {
        return getDataManager().getMainCurrencySymbol() + mCoin.getUserProfit();
    }



    public String getProfitIndex()
    {

        double finalPrice = mCoin.getPrice();
        double userPrice = mCoin.getUserPrice();
        return String.format("%.2f", ((finalPrice - userPrice)/finalPrice * 100)) + "%";
    }



    @BindingAdapter("android:src")
    public static void setImageViewResource(ImageView view, String profit) {

        double result = Double.valueOf(profit.substring(0, profit.lastIndexOf("%")));
        if(result> 0)
        {
            view.setBackgroundResource(R.drawable.ic_arrow_drop_up);
        }
        else
        {
            view.setBackgroundResource(R.drawable.ic_arrow_drop_down);
        }

    }


    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(view)
        ;
    }

    public Completable deleteCoin() {
        return getDataManager().deleteCoin(mCoin);
    }


}
