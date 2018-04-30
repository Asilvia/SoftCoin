package com.android.asilvia.cryptoo.ui.add;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.asilvia.cryptoo.BR;
import com.android.asilvia.cryptoo.BuildConfig;
import com.android.asilvia.cryptoo.R;
import com.android.asilvia.cryptoo.api.ApiResponse;
import com.android.asilvia.cryptoo.databinding.ActivityCoinListBinding;
import com.android.asilvia.cryptoo.ui.base.BaseActivity;
import com.android.asilvia.cryptoo.ui.base.navigation.AppNavigation;
import com.android.asilvia.cryptoo.vo.Coins;
import com.android.asilvia.cryptoo.vo.CoinsDetails;
import com.android.asilvia.cryptoo.vo.CoinsPrice;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;
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
                storeImage(view, item);

                mCoinListViewModel.getCoinPrice(item.getName()).observe(CoinListActivity.this, new Observer<ApiResponse<CoinsPrice>>() {
                    @Override
                    public void onChanged(@Nullable ApiResponse<CoinsPrice> coinsPriceApiResponse) {
                        Double price = -1d;
                        if(coinsPriceApiResponse.body != null && coinsPriceApiResponse.body.getRAW()!= null && !coinsPriceApiResponse.body.getRAW().isEmpty()) {
                            price = coinsPriceApiResponse.body.getRAW().get(item.getName()).get(mCoinListViewModel.getDataManager().getMainCoin()).getPRICE();
                        }
                        showDialog(price, item);
                    }
                });


            }
        });


    }

    private void showDialog(Double price, CoinsDetails item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CoinListActivity.this);
        View dialogView = getView(price);
        builder.setTitle(item.getFullName()).setView(dialogView)

                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String strPrice = ((EditText) ((AlertDialog) dialog).findViewById(R.id.much)).getText().toString();
                        String strAmount = ((EditText) ((AlertDialog) dialog).findViewById(R.id.many)).getText().toString();
                        saveCoin(strPrice, strAmount, item);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(CoinListActivity.this, "Cancel", Toast.LENGTH_LONG);
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
        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        final Bitmap bmp = ((BitmapDrawable)icon.getDrawable().getCurrent()).getBitmap();
       // final Bitmap bmp = ((GlideBitmapDrawable)icon.getDrawable()).getBitmap();
        mCoinListViewModel.saveImage(bmp,item.getName());
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
