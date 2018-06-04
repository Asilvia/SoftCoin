package com.asilvia.cryptoo.di.builder;

import com.asilvia.cryptoo.ui.add.AddCoinActivityModule;
import com.asilvia.cryptoo.ui.coinList.CoinListFragment;
import com.asilvia.cryptoo.ui.information.InformationFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by asilvia on 03/06/2018.
 */



@Module
public abstract class FragmentBuilder{
    @ContributesAndroidInjector(modules = {AddCoinActivityModule.class})
    abstract CoinListFragment contributeCoinListFragment();

    @ContributesAndroidInjector(modules = {AddCoinActivityModule.class})
    abstract InformationFragment contributeInformationFragment();
}
