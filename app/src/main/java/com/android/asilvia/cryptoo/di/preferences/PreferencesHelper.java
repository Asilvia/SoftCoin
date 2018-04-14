package com.android.asilvia.cryptoo.di.preferences;

import java.util.ArrayList;

/**
 * Created by asilvia on 26-10-2017.
 */

public interface PreferencesHelper {
    String getMainCoin();
    void setMainCoin(String valueExample);
    ArrayList<String> getSearchedCoins();
    void setSearchedCoins(ArrayList<String> list);

    boolean isMarketPercentage();
    boolean isMarketPrice();
    String getMainPeriod();






}
