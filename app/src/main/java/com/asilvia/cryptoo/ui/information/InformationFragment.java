package com.asilvia.cryptoo.ui.information;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.asilvia.cryptoo.BR;
import com.asilvia.cryptoo.R;
import com.asilvia.cryptoo.databinding.FragmentInformationBinding;
import com.asilvia.cryptoo.db.LocalCoin;
import com.asilvia.cryptoo.ui.add.AddCoinActivity;
import com.asilvia.cryptoo.ui.add.AddCoinViewModel;
import com.asilvia.cryptoo.ui.base.BaseFragment;
import com.asilvia.cryptoo.vo.CoinsDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import javax.inject.Inject;



public class InformationFragment extends BaseFragment<FragmentInformationBinding, AddCoinViewModel> {

    public static final String TAG = "InformationFragment";
    private AddCoinViewModel addCoinViewModel;
    FragmentInformationBinding fragmentInformationBinding;
    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    CoinsDetails item;
    boolean isAdded = false;

    LocalCoin localCoin;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentInformationBinding = getViewDataBinding();
        renderView();

    }

    private void renderView() {


        Glide.with(this).load(addCoinViewModel.getSelected().getImageUrl()).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher))
                .into(fragmentInformationBinding.icon);

        addCoinViewModel.getCoinPrice(addCoinViewModel.getSelected()).observe(this, new Observer<LocalCoin>() {
            @Override
            public void onChanged(@Nullable LocalCoin localCoin) {
                fragmentInformationBinding.tvPriceNow.setText(String.valueOf(localCoin.getPrice()));
                fragmentInformationBinding.tvIndex.setText(String.format("%.2f", localCoin.getIndex()) + "%");
                if(localCoin.getIndex() > 0)
                {
                    fragmentInformationBinding.imIndexIndicator.setBackgroundResource(R.drawable.ic_arrow_drop_up);
                }
                else
                {
                    fragmentInformationBinding.imIndexIndicator.setBackgroundResource(R.drawable.ic_arrow_drop_down);
                }
            }
        });




        fragmentInformationBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((AddCoinActivity)getActivity()).openDialog(addCoinViewModel.getSelected());
            }
        });
    }



    @Override
    public AddCoinViewModel getViewModel() {
        addCoinViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(AddCoinViewModel.class);
        return addCoinViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_information;
    }
}
