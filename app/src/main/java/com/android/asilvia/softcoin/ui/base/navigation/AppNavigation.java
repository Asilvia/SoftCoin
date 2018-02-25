package com.android.asilvia.softcoin.ui.base.navigation;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.asilvia.softcoin.ui.add.CoinListActivity;
import com.android.asilvia.softcoin.ui.details.CoinDetailsActivity;
import com.android.asilvia.softcoin.ui.start.StartActivity;

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

    public static void goToStartActivity(Activity activity) {
        Intent intent = new Intent(activity, StartActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void goToDetailsActivity(Context context, String id) {

        Intent intent = new Intent(context, CoinDetailsActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }




}
