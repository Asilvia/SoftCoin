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





    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(view)
        ;
    }


}