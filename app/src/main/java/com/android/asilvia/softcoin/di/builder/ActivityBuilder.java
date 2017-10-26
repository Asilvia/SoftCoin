package com.android.asilvia.softcoin.di.builder;

import com.android.asilvia.softcoin.ui.start.StartActivity;
import com.android.asilvia.softcoin.ui.start.StartActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by asilvia on 26-10-2017.
 */
@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {StartActivityModule.class})
    abstract StartActivity bindStartActivity();

}
