package com.asilvia.cryptoo.ui.start;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.asilvia.cryptoo.BR;
import com.asilvia.cryptoo.R;
import com.asilvia.cryptoo.databinding.ActivityStartBinding;
import com.asilvia.cryptoo.db.LocalCoin;
import com.asilvia.cryptoo.ui.base.BaseActivity;
import com.asilvia.cryptoo.ui.base.navigation.AppNavigation;
import com.asilvia.cryptoo.util.NetworkUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.schedulers.Schedulers;
import timber.log.Timber;

public class StartActivity extends BaseActivity<ActivityStartBinding, StartViewModel> implements StartNavigator{

    ActivityStartBinding mActivityStartBinding;
    private StartViewModel mStartViewModel;
    StartAdapter adapter;
    boolean isAlertShown = false;
    boolean isUndone = false;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        if(NetworkUtils.isNetworkConnected(this)) {
            if (i.hasExtra("Action")) {
                String extra = i.getStringExtra("Action");
                if (extra.equals("add")) {
                    Timber.d("Go to next activity from widget");
                    AppNavigation.goToAddCoinActivity(this);
                }

            }
        }
        else
        {
            buildDialog(StartActivity.this).show();
        }
        mActivityStartBinding = getViewDataBinding();
        mStartViewModel.setNavigator(this);
        renderView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getCoins();

    }

    private void getCoins() {
        mStartViewModel.getCoinList();
        String last_update = getString(R.string.last_update) + " "+ mStartViewModel.getLastUpdate();
       mActivityStartBinding.updateTime.setText( last_update);
       mActivityStartBinding.notifyChange();
        mStartViewModel.getObservableCoinsList().observe(this, new Observer<List<LocalCoin>>() {
            @Override
            public void onChanged(@Nullable List<LocalCoin> localCoins) {
                Timber.d("OnChange: " + "localCoins.size: " + localCoins.size());
                adapter.updateSymbolAndMarket(mStartViewModel.getCoinSymbol(), mStartViewModel.isMarket());
                adapter.setCoin(localCoins);
                mStartViewModel.setIsLoading(false);
            }
        });
    }




    private void renderView() {
        renderActionBar();

        //Empty screen
        Glide
                .with(this)
                .load("https://media.giphy.com/media/GwRBmXyEOvFtK/giphy.gif")
                .apply(new RequestOptions().circleCrop())


                .into(mActivityStartBinding.empty.pushButton)
        ;

        mStartViewModel.setIsLoading(true);
      //  mStartViewModel.RetrieveCoinList();

        mStartViewModel.getHasError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean == true && !isAlertShown)
                    buildDialog(StartActivity.this).show();
            }
        });
        renderCardList();


        mActivityStartBinding.addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("Go to next activity");
                AppNavigation.goToAddCoinActivity((Activity)v.getContext());


            }
        });


    }

    private void renderCardList() {
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
                        String last_update = getString(R.string.last_update) + " "+ mStartViewModel.getLastUpdate();
                        adapter.updateSymbolAndMarket(mStartViewModel.getCoinSymbol(), mStartViewModel.isMarket());
                        adapter.setCoin(localCoins);
                        mActivityStartBinding.swiperefresh.setRefreshing(false);
                        mActivityStartBinding.updateTime.setText(last_update);
                    }
                });
            }
        });

        adapter = new StartAdapter(getApplicationContext(), new ArrayList<LocalCoin>(),mStartViewModel.getCoinSymbol(), mStartViewModel.isMarket());
        mActivityStartBinding.coinsList.setAdapter(adapter);


    }

    private void renderActionBar() {
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_logo_cryptoo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings)
        {
            Timber.d("Go to settings Activity");
            AppNavigation.goToSettingsActivity(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public AlertDialog.Builder buildDialog(Context c) {
            isAlertShown = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle(R.string.noInternet);
            builder.setMessage(R.string.noInternet_body);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });

        return builder;
    }

}
