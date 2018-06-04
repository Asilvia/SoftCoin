package com.asilvia.cryptoo.di.builder;


import com.asilvia.cryptoo.ui.add.AddCoinActivity;
import com.asilvia.cryptoo.ui.add.AddCoinActivityModule;
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

    @ContributesAndroidInjector(modules = {AddCoinActivityModule.class})
    abstract AddCoinActivity bindCoinListActivity();

    @ContributesAndroidInjector(modules = {CoinDetailsActivityModule.class})
    abstract CoinDetailsActivity bindCoinDetailsActivity();

    @ContributesAndroidInjector
    abstract UpdateService bindUpdateService();

}
