package com.android.asilvia.cryptoo.di.builder;

import com.android.asilvia.cryptoo.ui.add.CoinListActivityModule;
import com.android.asilvia.cryptoo.ui.start.StartActivityModule;
import com.android.asilvia.cryptoo.ui.add.CoinListActivity;
import com.android.asilvia.cryptoo.ui.start.StartActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by asilvia on 26-10-2017.
 */
@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {StartActivityModule.class})
    abstract StartActivity bindStartActivity();

    @ContributesAndroidInjector(modules = {CoinListActivityModule.class})
    abstract CoinListActivity bindCoinListActivity();

}
