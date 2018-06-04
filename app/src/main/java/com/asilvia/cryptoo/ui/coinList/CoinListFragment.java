package com.asilvia.cryptoo.ui.coinList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.LinearLayout;

import com.asilvia.cryptoo.BR;
import com.asilvia.cryptoo.BuildConfig;
import com.asilvia.cryptoo.R;
import com.asilvia.cryptoo.api.ApiResponse;
import com.asilvia.cryptoo.databinding.FragmentCoinListBinding;
import com.asilvia.cryptoo.ui.add.AddCoinViewModel;
import com.asilvia.cryptoo.ui.base.BaseFragment;
import com.asilvia.cryptoo.vo.Coins;
import com.asilvia.cryptoo.vo.CoinsDetails;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdsManager;

import java.util.ArrayList;

import javax.inject.Inject;

import timber.log.Timber;



public class CoinListFragment extends BaseFragment<FragmentCoinListBinding, AddCoinViewModel> implements NativeAdsManager.Listener {

    public static final String TAG = "CoinListFragment";
    private AddCoinViewModel addCoinViewModel;
    private FragmentCoinListBinding mFragmentCoinListBinding;

    private CoinListAdapter mAdapter;

    ArrayList<CoinsDetails> list;


    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private NativeAdsManager mNativeAdsManager;

    private LinearLayout  nativeAdContainer;
    private LinearLayout adView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFragmentCoinListBinding = getViewDataBinding();
        setupNativeAds();
        renderView();


    }

    private void setupNativeAds() {
        mNativeAdsManager = new NativeAdsManager(getActivity(), BuildConfig.FacebookAdUnit, 5);
        mNativeAdsManager.loadAds();
        mNativeAdsManager.setListener(this);
    }

    private void renderView()
    {

        addCoinViewModel.setIsLoading(true);
        addCoinViewModel.retrieveCoinList();

        mFragmentCoinListBinding.lastsearched.setVisibility(View.VISIBLE);

        list = new ArrayList<> ();


        mFragmentCoinListBinding.coinList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CoinListAdapter(getActivity(), list,mNativeAdsManager);
        mFragmentCoinListBinding.coinList.setAdapter(mAdapter);



        mFragmentCoinListBinding.searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentCoinListBinding.search.setIconified(false);
            }
        });

        addCoinViewModel.getObservableCoinsList().observe(this, new Observer<ApiResponse<Coins>>() {
            @Override
            public void onChanged(@Nullable ApiResponse<Coins> coinsApiResponse) {
                addCoinViewModel.setIsLoading(false);
                list = addCoinViewModel.getParseDataResponse(coinsApiResponse);
                mAdapter.addList(list);

                Timber.d("list: " + list.size() + " --> " + mAdapter.originalCoinList.size());
            }
        });


        mFragmentCoinListBinding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mFragmentCoinListBinding.lastsearched.setVisibility(View.GONE);
                    mAdapter.getFilter().filter(newText.toString());
                    return true;
                }
            });

    }


    @Override
    public AddCoinViewModel getViewModel() {
        addCoinViewModel = ViewModelProviders.of(this, mViewModelFactory).get(AddCoinViewModel.class);
        return addCoinViewModel;
    }

    @Override
    public int getBindingVariable() {
        return com.asilvia.cryptoo.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_coin_list;
    }


    @Override
    public void onAdsLoaded() {
        Timber.d("On ad loaded");
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onAdError(AdError adError) {
        Timber.e(" Error receiving ads" + adError.getErrorMessage());
    }

    public void showDialog(CoinsDetails coinDetails)
    {
        Timber.d(" ====== show dialog ======");
        addCoinViewModel.select(coinDetails);
    }





}
