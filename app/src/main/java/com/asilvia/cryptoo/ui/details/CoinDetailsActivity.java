package com.asilvia.cryptoo.ui.details;



import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.asilvia.cryptoo.BR;
import com.asilvia.cryptoo.R;
import com.asilvia.cryptoo.databinding.ActivityCoinDetailsBinding;
import com.asilvia.cryptoo.ui.base.BaseActivity;

import javax.inject.Inject;

import rx.schedulers.Schedulers;
import timber.log.Timber;

public class CoinDetailsActivity extends BaseActivity<ActivityCoinDetailsBinding, CoinDetailsViewModel> implements CoinDetailsNavigator {

    public static final String TAG = "CoinDetailsActivity";
    private CoinDetailsViewModel mCoinDetailsViewModel;
    ActivityCoinDetailsBinding mActivityCoinDetailsBinding;
    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    String id;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getParameters();
        super.onCreate(savedInstanceState);
        mActivityCoinDetailsBinding = getViewDataBinding();
        mCoinDetailsViewModel.setNavigator(this);
        renderView();

    }

    private void renderView() {
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowTitleEnabled(true);



        mActivityCoinDetailsBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCoinDetailsViewModel.deleteCoin().subscribeOn(Schedulers.io()).subscribe(() -> {

                    Timber.d("localcoin delete success");
                    finish();
                    //   AppNavigation.goToStartActivity(CoinListActivity.this);
                }, throwable -> {
                    Timber.d("localcoin failed" + throwable.getMessage());
                });
            }
        });
    }


    private void getParameters() {
        Intent intent = getIntent();
        if(intent!= null)
        {
            id = intent.getStringExtra("id");
            Timber.d(TAG, id);
            title = intent.getStringExtra("title");
            Timber.d(TAG, title);
        }
    }


    @Override
    public CoinDetailsViewModel getViewModel() {
        mCoinDetailsViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CoinDetailsViewModel.class);
        mCoinDetailsViewModel.getCoinById(id);
        return mCoinDetailsViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_coin_details;
    }
}
