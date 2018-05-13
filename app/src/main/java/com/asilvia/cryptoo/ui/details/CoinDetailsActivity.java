package com.asilvia.cryptoo.ui.details;



import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.asilvia.cryptoo.BR;
import com.asilvia.cryptoo.R;
import com.asilvia.cryptoo.databinding.ActivityCoinDetailsBinding;
import com.asilvia.cryptoo.ui.base.BaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

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
                showConfirmationDialog();
            }
        });
    }


    private void showConfirmationDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                this);
        alert.setTitle(R.string.dialog_delete_title);
        alert.setMessage(R.string.dialog_delete_body);
        alert.setPositiveButton(R.string.dialog_delete_confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Dialog dialogAnimation=new Dialog(CoinDetailsActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialogAnimation.setContentView(R.layout.remove_coin_dialog);
                ImageView dialogImage = dialogAnimation.findViewById(R.id.removeButton);
                Glide
                        .with(CoinDetailsActivity.this)
                        .load("https://media.giphy.com/media/l0HFkA6omUyjVYqw8/giphy.gif")
                        .apply(new RequestOptions().circleCrop())
                        .into(dialogImage);


                dialogAnimation.show();

                ImageView dialogCloseButton = dialogAnimation.findViewById(R.id.closeDialog);
                dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();

                    }
                });

                mCoinDetailsViewModel.deleteCoin().subscribeOn(Schedulers.io()).subscribe(() -> {

                    Timber.d("localcoin delete success");
                    dialog.dismiss();
                    //   AppNavigation.goToStartActivity(CoinListActivity.this);
                }, throwable -> {
                    Timber.d("localcoin failed" + throwable.getMessage());
                    dialog.dismiss();
                });
            }
        });
        alert.setNegativeButton(R.string.dialog_delete_skip, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();
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
