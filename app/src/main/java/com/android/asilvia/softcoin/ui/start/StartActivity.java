package com.android.asilvia.softcoin.ui.start;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.android.asilvia.softcoin.BR;
import com.android.asilvia.softcoin.R;
import com.android.asilvia.softcoin.api.ApiResponse;
import com.android.asilvia.softcoin.databinding.ActivityStartBinding;

import com.android.asilvia.softcoin.db.LocalCoin;
import com.android.asilvia.softcoin.ui.base.BaseActivity;
import com.android.asilvia.softcoin.ui.base.navigation.AppNavigation;
import com.android.asilvia.softcoin.vo.Coins;
import com.android.asilvia.softcoin.vo.CoinsDetails;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

import static android.os.Build.VERSION_CODES.O;

public class StartActivity extends BaseActivity<ActivityStartBinding, StartViewModel> implements StartNavigator{

    ActivityStartBinding mActivityStartBinding;
    private StartViewModel mStartViewModel;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityStartBinding = getViewDataBinding();
        mStartViewModel.setNavigator(this);
        renderView();
    }

    private void renderView() {
        mStartViewModel.setIsLoading(true);
      //  mStartViewModel.RetrieveCoinList();
        mStartViewModel.getCoinList();
        mActivityStartBinding.coinsList.setHasFixedSize(true);
        mActivityStartBinding.coinsList.setLayoutManager(new GridLayoutManager(this, 2));
        mActivityStartBinding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Timber.d("Refresh");
               // mStartViewModel.RetrieveCoinList();
                //mActivityStartBinding.swiperefresh.setRefreshing(false);
            }
        });




        mActivityStartBinding.addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("Go to next activity");
                AppNavigation.goToCoinListActivity((Activity)v.getContext());


            }
        });
        mStartViewModel.getObservableCoinsList().observe(this, new Observer<List<LocalCoin>>() {
                    @Override
                    public void onChanged(@Nullable List<LocalCoin> localCoins) {
                        Timber.d("OnChange: " + "localCoins.size: " + localCoins.size());
                        String coinsList = getCoinsName(localCoins);


                        mStartViewModel.setIsLoading(false);
                    }
                });
    /*    mStartViewModel.getObservableCoinsList().observe(this, new Observer<ApiResponse<Coins>>() {
            @Override
            public void onChanged(@Nullable ApiResponse<Coins> coinsApiResponse) {
                Timber.d("OnChange");
                mStartViewModel.setIsLoading(false);


            }
        });*/
    }


    @Override
    public StartViewModel getViewModel() {
        mStartViewModel = ViewModelProviders.of(this, mViewModelFactory).get(StartViewModel.class);
        return mStartViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_start;
    }

    @NonNull
    private String getCoinsName(List<LocalCoin> localCoins) {
        String coinsList = "";
        for (LocalCoin item : localCoins) {
            Timber.d("--> item: " + item.getKey());
            coinsList = coinsList + item.getKey() + ",";
        }
        coinsList = coinsList.substring(0, coinsList.length() - 1);
        Timber.d("-->" +coinsList);

        return coinsList;
    }


}
