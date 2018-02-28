package com.android.asilvia.cryptoo.di.module;

import com.android.asilvia.cryptoo.BuildConfig;
import com.android.asilvia.cryptoo.api.MainApiHelper;
import com.android.asilvia.cryptoo.di.adapter.LiveDataCallAdapterFactory;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asilvia on 26-10-2017.
 */
@Module
public class NetModule {

    @Provides
    @Singleton
    Retrofit provideMainCall() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        // Customize the request
                        Request request = original.newBuilder()
                                .header("Content-Type", "application/json")
                                .removeHeader("Pragma")
                                .header("Cache-Control", String.format("max-age=%d", BuildConfig.CACHETIME))
                                .build();

                        okhttp3.Response response = chain.proceed(request);
                        response.cacheResponse();
                        // Customize or return the response
                        return response;
                    }
                })
                .build();


        return new Retrofit.Builder()
                .baseUrl(BuildConfig.MAINBASEURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public MainApiHelper providesMainWebService(
            Retrofit retrofit) {
        return retrofit.create(MainApiHelper.class);
    }



}
