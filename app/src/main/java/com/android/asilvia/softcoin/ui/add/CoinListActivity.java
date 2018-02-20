package com.android.asilvia.softcoin.ui.add;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.asilvia.softcoin.BR;
import com.android.asilvia.softcoin.R;
import com.android.asilvia.softcoin.api.ApiResponse;
import com.android.asilvia.softcoin.databinding.ActivityCoinListBinding;
import com.android.asilvia.softcoin.ui.base.BaseActivity;
import com.android.asilvia.softcoin.vo.Coins;
import com.android.asilvia.softcoin.vo.CoinsDetails;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class CoinListActivity extends BaseActivity<ActivityCoinListBinding, CoinListViewModel> implements CoinListNavigator {

    private CoinListViewModel mCoinListViewModel;
    private ActivityCoinListBinding mActivityCoinListBinding;

    private CoinListAdapter mAdapter;

    ArrayList<CoinsDetails> list;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCoinListBinding = getViewDataBinding();
        mCoinListViewModel.setNavigator(this);
        renderView();
    }

    private void renderView() {
    mCoinListViewModel.setIsLoading(true);
    mCoinListViewModel.retrieveCoinList();

    list = new ArrayList<> ();

    mAdapter = new CoinListAdapter(this, list);
    mActivityCoinListBinding.coinList.setAdapter(mAdapter);

    mCoinListViewModel.getObservableCoinsList().observe(this, new Observer<ApiResponse<Coins>>() {
        @Override
        public void onChanged(@Nullable ApiResponse<Coins> coinsApiResponse) {
            mCoinListViewModel.setIsLoading(false);
            list = mCoinListViewModel.getParseDataResponse(coinsApiResponse);
            mAdapter.coinList.addAll(list);
            Timber.d("list: " + list.size() + " --> " + mAdapter.coinList.size());
            mAdapter.notifyDataSetChanged();
        }
    });

    }



    @Override
    public CoinListViewModel getViewModel() {
        mCoinListViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CoinListViewModel.class);
        return mCoinListViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_coin_list;
    }
}
