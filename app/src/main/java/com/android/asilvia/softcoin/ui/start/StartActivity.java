package com.android.asilvia.softcoin.ui.start;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.android.asilvia.softcoin.BR;
import com.android.asilvia.softcoin.R;
import com.android.asilvia.softcoin.api.ApiResponse;
import com.android.asilvia.softcoin.databinding.ActivityStartBinding;
import com.android.asilvia.softcoin.ui.base.BaseActivity;
import com.android.asilvia.softcoin.vo.Coins;
import com.android.asilvia.softcoin.vo.CoinsDetails;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
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
        mStartViewModel.RetrieveCoinList();
        mActivityStartBinding.coinsList.setHasFixedSize(true);
        mActivityStartBinding.coinsList.setLayoutManager(new GridLayoutManager(this, 2));
        mActivityStartBinding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Timber.d("Refresh");
                mStartViewModel.RetrieveCoinList();
                mActivityStartBinding.swiperefresh.setRefreshing(false);
            }
        });

        mActivityStartBinding.addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("Go to next activity");
            }
        });

        mStartViewModel.getObservableCoinsList().observe(this, new Observer<ApiResponse<Coins>>() {
            @Override
            public void onChanged(@Nullable ApiResponse<Coins> coinsApiResponse) {
                Timber.d("OnChange");
                mStartViewModel.setIsLoading(false);

                if (coinsApiResponse != null) {
                    if (coinsApiResponse.isSuccessful()) {
                        Timber.d("isSuccessful" + coinsApiResponse.body.getMessage());
                        Map<String, CoinsDetails> temporaryList = coinsApiResponse.body.getData();
                        for (Map.Entry<String, CoinsDetails> entry : temporaryList.entrySet())
                        {
                            System.out.println(entry.getKey() + "/" + entry.getValue().getCoinName());
                        }

                       // coinsApiResponse.body.getData()
                    } else {
                        Timber.e(coinsApiResponse.errorMessage);
                    }
                } else {
                    Timber.d("Api response is null");
                }
            }
        });
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
