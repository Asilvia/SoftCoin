package com.asilvia.cryptoo.ui.add;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.asilvia.cryptoo.BR;
import com.asilvia.cryptoo.BuildConfig;
import com.asilvia.cryptoo.R;
import com.asilvia.cryptoo.api.ApiResponse;
import com.asilvia.cryptoo.databinding.ActivityCoinListBinding;
import com.asilvia.cryptoo.ui.base.BaseActivity;
import com.asilvia.cryptoo.vo.Coins;
import com.asilvia.cryptoo.vo.CoinsDetails;
import com.asilvia.cryptoo.vo.CoinsPrice;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.inject.Inject;

import rx.schedulers.Schedulers;
import timber.log.Timber;



public class CoinListActivity extends BaseActivity<ActivityCoinListBinding, CoinListViewModel> implements CoinListNavigator {

    private CoinListViewModel mCoinListViewModel;
    private ActivityCoinListBinding mActivityCoinListBinding;

    private CoinListAdapter mAdapter;

    ArrayList<CoinsDetails> list;
    ArrayList<String> savedSearch;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;


    private LinearLayout  nativeAdContainer;
    private LinearLayout adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCoinListBinding = getViewDataBinding();
        mCoinListViewModel.setNavigator(this);
        renderView();
        showNativeAd();
    }

    private void renderView()
    {
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.add_currency);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        mCoinListViewModel.setIsLoading(true);
        mCoinListViewModel.retrieveCoinList();
        savedSearch = mCoinListViewModel.getSavedSearch();

        mActivityCoinListBinding.lastsearched.setVisibility(View.VISIBLE);


        list = new ArrayList<> ();

        mAdapter = new CoinListAdapter(this, list, savedSearch);
        mActivityCoinListBinding.coinList.setAdapter(mAdapter);

        mActivityCoinListBinding.searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityCoinListBinding.search.setIconified(false);
            }
        });

        mCoinListViewModel.getObservableCoinsList().observe(this, new Observer<ApiResponse<Coins>>() {
            @Override
            public void onChanged(@Nullable ApiResponse<Coins> coinsApiResponse) {
                mCoinListViewModel.setIsLoading(false);
                list = mCoinListViewModel.getParseDataResponse(coinsApiResponse);
                mAdapter.addList(list);
                Timber.d("list: " + list.size() + " --> " + mAdapter.originalCoinList.size());
            }
        });


        mActivityCoinListBinding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mActivityCoinListBinding.lastsearched.setVisibility(View.GONE);
                    mAdapter.getFilter().filter(newText.toString());
                    return true;
                }
            });

        mActivityCoinListBinding.coinList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final CoinsDetails item = (CoinsDetails) mAdapter.getItem(position);


                mCoinListViewModel.getCoinPrice(item.getName()).observe(CoinListActivity.this, new Observer<ApiResponse<CoinsPrice>>() {
                    @Override
                    public void onChanged(@Nullable ApiResponse<CoinsPrice> coinsPriceApiResponse) {
                        Double price = -1d;
                        if(coinsPriceApiResponse.body != null && coinsPriceApiResponse.body.getRAW()!= null && !coinsPriceApiResponse.body.getRAW().isEmpty()) {
                            price = coinsPriceApiResponse.body.getRAW().get(item.getName()).get(mCoinListViewModel.getDataManager().getMainCoin()).getPRICE();
                        }
                        showDialog(view,price, item);
                    }
                });


            }
        });


    }

    private void showDialog(View view, Double price, CoinsDetails item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CoinListActivity.this);
        View dialogView = getView(price);
        builder.setTitle(item.getFullName()).setView(dialogView)

                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String strPrice = ((EditText) ((AlertDialog) dialog).findViewById(R.id.much)).getText().toString();
                        String strAmount = ((EditText) ((AlertDialog) dialog).findViewById(R.id.many)).getText().toString();
                        saveCoin(strPrice, strAmount, item);
                        storeImage(view, item);

                    }
                })
                .create()
                .show();
    }

    @NonNull
    private View getView(Double price) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_coin_dialog, null);
        if(price >0) {
            String coinSymbol = mCoinListViewModel.getSymbol();
            ((EditText) dialogView.findViewById(R.id.much)).setText(coinSymbol + String.valueOf(price));
        }
        return dialogView;
    }

    private void saveCoin(String strPrice, String strAmount, CoinsDetails item) {

        Double price = Double.parseDouble(strPrice.replace(mCoinListViewModel.getSymbol(),""));

        mCoinListViewModel.saveItem(item, price, Long.parseLong(strAmount))
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                    ArrayList<String> filteredSavedSearch = savedSearch.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
                    mCoinListViewModel.setSavedSearch(filteredSavedSearch);
                    finish();
                    //   AppNavigation.goToStartActivity(CoinListActivity.this);
                }, throwable -> {
                    Timber.d("localcoin failed" + throwable.getMessage());
                });
    }

    private void storeImage(View view, CoinsDetails item) {

        Glide.with(this)
                .load(item.getImageUrl())
                .into(new SimpleTarget<Drawable>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                        mCoinListViewModel.saveImage(bitmap,item.getName());
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

    private NativeAd nativeAd;

    private void showNativeAd() {


        nativeAd = new NativeAd(this, BuildConfig.FacebookAdUnit);
        nativeAd.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
                Timber.e(" Error receiving ads" + error.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAd != null) {
                    nativeAd.unregisterView();
                }
                mAdapter.setNativeAd(nativeAd);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }




        });

        // Request an ad
        nativeAd.loadAd();
    }


}
