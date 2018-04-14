package com.android.asilvia.cryptoo.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;

import com.android.asilvia.cryptoo.R;

/**
 * Implementation of App Widget functionality.
 */
public class CryptooWidget extends AppWidgetProvider {
    private static PendingIntent pendingIntent;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
               final Intent i = new Intent(context, UpdateService.class);

                       if (pendingIntent == null) {
                       pendingIntent = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
                   }
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 60000, pendingIntent);


//        CharSequence widgetText = CryptooWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
     //   RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cryptoo_widget);
     //   views.setTextViewText(R.id.appwidget_text, "lallala");

        // Instruct the widget manager to update the widget
     //   appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
