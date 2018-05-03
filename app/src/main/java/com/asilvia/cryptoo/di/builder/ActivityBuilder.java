package com.asilvia.cryptoo.di.builder;


import com.asilvia.cryptoo.ui.add.CoinListActivity;
import com.asilvia.cryptoo.ui.add.CoinListActivityModule;
import com.asilvia.cryptoo.ui.details.CoinDetailsActivity;
import com.asilvia.cryptoo.ui.details.CoinDetailsActivityModule;
import com.asilvia.cryptoo.ui.start.StartActivity;
import com.asilvia.cryptoo.ui.start.StartActivityModule;
import com.asilvia.cryptoo.widget.UpdateService;

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

    @ContributesAndroidInjector(modules = {CoinDetailsActivityModule.class})
    abstract CoinDetailsActivity bindCoinDetailsActivity();

    @ContributesAndroidInjector
    abstract UpdateService bindUpdateService();

}
