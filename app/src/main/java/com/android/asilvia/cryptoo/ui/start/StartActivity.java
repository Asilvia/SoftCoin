package com.android.asilvia.cryptoo.ui.start;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.android.asilvia.cryptoo.BR;
import com.android.asilvia.cryptoo.R;
import com.android.asilvia.cryptoo.databinding.ActivityStartBinding;
import com.android.asilvia.cryptoo.db.LocalCoin;
import com.android.asilvia.cryptoo.ui.base.BaseActivity;
import com.android.asilvia.cryptoo.ui.base.navigation.AppNavigation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class StartActivity extends BaseActivity<ActivityStartBinding, StartViewModel> implements StartNavigator{

    ActivityStartBinding mActivityStartBinding;
    private StartViewModel mStartViewModel;
    StartAdapter adapter;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityStartBinding = getViewDataBinding();
        mStartViewModel.setNavigator(this);
        getSupportActionBar().setElevation(0);
        renderView();
    }

    private void renderView() {

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_logo_cryptoo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mStartViewModel.setIsLoading(true);
      //  mStartViewModel.RetrieveCoinList();
        mStartViewModel.getCoinList();
        mActivityStartBinding.coinsList.setHasFixedSize(true);
        mActivityStartBinding.coinsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mActivityStartBinding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Timber.d("Refresh");
                mStartViewModel.getCoinList();
                mStartViewModel.getObservableCoinsList().observe(StartActivity.this, new Observer<List<LocalCoin>>() {
                    @Override
                    public void onChanged(@Nullable List<LocalCoin> localCoins) {
                        adapter.setCoin(localCoins);
                        mActivityStartBinding.swiperefresh.setRefreshing(false);
                    }
                });
            }
        });

        adapter = new StartAdapter(getApplicationContext(), new ArrayList<LocalCoin>());
        mActivityStartBinding.coinsList.setAdapter(adapter);


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
                        adapter.setCoin(localCoins);
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




}
