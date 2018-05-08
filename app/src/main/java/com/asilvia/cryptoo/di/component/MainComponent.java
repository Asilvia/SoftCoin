package com.asilvia.cryptoo.di.component;

import android.app.Application;


import com.asilvia.cryptoo.CryptooApp;

import com.asilvia.cryptoo.di.builder.ActivityBuilder;
import com.asilvia.cryptoo.di.module.AppModule;
import com.asilvia.cryptoo.di.module.NetModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by asilvia on 26-10-2017.
 */
@Singleton
@Component (modules = {AndroidInjectionModule.class,  NetModule.class, AppModule.class, ActivityBuilder.class})
public interface MainComponent
{
    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);


        MainComponent build();
    }
    void inject(CryptooApp app);
}