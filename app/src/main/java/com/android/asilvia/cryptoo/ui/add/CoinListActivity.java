package com.android.asilvia.cryptoo.ui.add;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.asilvia.cryptoo.BR;
import com.android.asilvia.cryptoo.R;
import com.android.asilvia.cryptoo.api.ApiResponse;
import com.android.asilvia.cryptoo.databinding.ActivityCoinListBinding;
import com.android.asilvia.cryptoo.ui.base.BaseActivity;
import com.android.asilvia.cryptoo.ui.base.navigation.AppNavigation;
import com.android.asilvia.cryptoo.vo.Coins;
import com.android.asilvia.cryptoo.vo.CoinsDetails;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.schedulers.Schedulers;
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

    private void renderView()
    {
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.add_currency);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        mCoinListViewModel.setIsLoading(true);
        mCoinListViewModel.retrieveCoinList();
        list = new ArrayList<> ();

        mAdapter = new CoinListAdapter(this, list);
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
                    mAdapter.getFilter().filter(newText.toString());
                    return true;
                }
            });

        mActivityCoinListBinding.coinList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final CoinsDetails item = (CoinsDetails) mAdapter.getItem(position);
                final String[] realCoinsArray = getResources().getStringArray(R.array.realmoney_coins);
                AlertDialog.Builder builder = new AlertDialog.Builder(CoinListActivity.this);
                builder.setTitle(item.getFullName())
                        .setSingleChoiceItems(R.array.realmoney_coins, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                //todo change for my scheduler
                                mCoinListViewModel.saveItem(item,realCoinsArray[selectedPosition]).subscribeOn(Schedulers.io()).subscribe(() -> {
                                    Timber.d("localcoin success");
                                    AppNavigation.goToStartActivity(CoinListActivity.this);
                                }, throwable -> {
                                            Timber.d("localcoin failed" + throwable.getMessage());
                                });

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(CoinListActivity.this,"Cancel", Toast.LENGTH_LONG);
                            }
                        })
                        .create()
                        .show();

            }
        });

      //  mCoinListViewModel.getmObservableSave().observe(this, new Observer)
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
