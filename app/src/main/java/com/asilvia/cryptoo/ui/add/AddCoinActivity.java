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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.asilvia.cryptoo.BR;
import com.asilvia.cryptoo.R;
import com.asilvia.cryptoo.databinding.ActivityAddCoinBinding;
import com.asilvia.cryptoo.db.LocalCoin;
import com.asilvia.cryptoo.ui.base.BaseActivity;
import com.asilvia.cryptoo.ui.coinList.CoinListFragment;
import com.asilvia.cryptoo.ui.information.InformationFragment;
import com.asilvia.cryptoo.util.AppUtils;
import com.asilvia.cryptoo.vo.CoinsDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import javax.inject.Inject;


import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by asilvia on 03/06/2018.
 */

public class AddCoinActivity   extends BaseActivity<ActivityAddCoinBinding, AddCoinViewModel> {


    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    ActivityAddCoinBinding activityAddCoinBinding;
    AddCoinViewModel addCoinViewModel;
    boolean isCoinsInformationShown = false;


    @Override
    public void onBackPressed() {

        if(isCoinsInformationShown == true)
        {
            showCoinList();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddCoinBinding = getViewDataBinding();
        addCoinViewModel.retrieveCoinList();


        if (savedInstanceState == null) {
            showCoinList();
        }

    }

    private void showCoinList() {
        isCoinsInformationShown = false;
        setActionBar(getResources().getString(R.string.add_currency));
        CoinListFragment fragment = new CoinListFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment, CoinListFragment.TAG).commit();
    }




    private void setActionBar(String resource) {
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(resource);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }


    @Override
    public AddCoinViewModel getViewModel() {
        addCoinViewModel = ViewModelProviders.of(this, mViewModelFactory).get(AddCoinViewModel.class);
        return addCoinViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_coin;
    }


    public void openDialog(CoinsDetails item) {

        addCoinViewModel.select(item);
        addCoinViewModel.getCoinPrice(item).observe(this, new Observer<LocalCoin>() {
            @Override
            public void onChanged(@Nullable LocalCoin localCoin) {
                showDialog(localCoin.getPrice());
            }
        });


    }



    private void showDialog(Double price) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getView(price);
        builder.setTitle(addCoinViewModel.getSelected().getFullName())
                .setView(dialogView)
                .setPositiveButton(R.string.add, null);


        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final EditText editTextMuch = alertDialog.findViewById(R.id.much);
                final EditText editTextMany = alertDialog.findViewById(R.id.many);
                alertDialog
                        .getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String strPrice = editTextMuch.getText().toString();
                        String strAmount = editTextMany.getText().toString();
                        if(AppUtils.isInteger(strAmount)) {
                            saveCoin(strPrice, strAmount);
                            storeImage();
                            alertDialog.dismiss();
                        }
                        else
                        {
                            editTextMany.setHint("Please add a number");

                        }
                    }
                });
            }
        });


        alertDialog.show();

    }

    @NonNull
    private View getView(Double price) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_coin_dialog, null);
        if(price >0) {
            String coinSymbol = addCoinViewModel.getSymbol();
            ((EditText) dialogView.findViewById(R.id.much)).setText(coinSymbol + String.valueOf(price));
        }
        return dialogView;
    }

    private void saveCoin(String strPrice, String strAmount) {

        Double price = Double.parseDouble(strPrice.replace(addCoinViewModel.getSymbol(),""));

        addCoinViewModel.saveItem(price, Long.parseLong(strAmount))
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                    finish();
                }, throwable -> {
                    Timber.d("localcoin failed" + throwable.getMessage());
                });
    }

    private void storeImage() {

        Glide.with(this)
                .load(addCoinViewModel.getSelected().getImageUrl())
                .into(new SimpleTarget<Drawable>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                        addCoinViewModel.saveImage(bitmap);
                    }
                });
    }

    public void openInformation(CoinsDetails item)
    {
        isCoinsInformationShown = true;
        addCoinViewModel.select(item);
        setActionBar(item.getFullName());
        InformationFragment fragment = new InformationFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment, InformationFragment.TAG).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
