package com.android.asilvia.cryptoo.ui.details;



import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.asilvia.cryptoo.BR;
import com.android.asilvia.cryptoo.R;
import com.android.asilvia.cryptoo.databinding.ActivityCoinDetailsBinding;
import com.android.asilvia.cryptoo.db.LocalCoin;
import com.android.asilvia.cryptoo.ui.base.BaseActivity;
import com.android.asilvia.cryptoo.ui.start.StartViewModel;

import javax.inject.Inject;

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
