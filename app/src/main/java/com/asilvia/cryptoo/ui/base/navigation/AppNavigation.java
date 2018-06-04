package com.asilvia.cryptoo.ui.base.navigation;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.asilvia.cryptoo.ui.add.AddCoinActivity;
import com.asilvia.cryptoo.ui.details.CoinDetailsActivity;
import com.asilvia.cryptoo.ui.settings.SettingsActivity;
import com.asilvia.cryptoo.ui.start.StartActivity;

/**
 * This class will contain all the navigation in the app
 * Created by asilvia on 26-10-2017.
 */

public class AppNavigation {


    public static void goToAddCoinActivity(Activity activity) {
        Intent intent = new Intent(activity, AddCoinActivity.class);
        activity.startActivity(intent);
    }

    public static void goToStartActivity(Activity activity) {
        Intent intent = new Intent(activity, StartActivity.class);
        activity.startActivity(intent);
    }

    public static void goToDetailsActivity(Context context, String id, String title) {

        Intent intent = new Intent(context, CoinDetailsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    public static void goToSettingsActivity(Activity activity)
    {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }


}
