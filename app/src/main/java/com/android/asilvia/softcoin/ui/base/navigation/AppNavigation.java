package com.android.asilvia.softcoin.ui.base.navigation;


import android.app.Activity;
import android.content.Intent;

import com.android.asilvia.softcoin.ui.add.CoinListActivity;

/**
 * This class will contain all the navigation in the app
 * Created by asilvia on 26-10-2017.
 */

public class AppNavigation {


    public static void goToCoinListActivity(Activity activity) {
        Intent intent = new Intent(activity, CoinListActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }




}
