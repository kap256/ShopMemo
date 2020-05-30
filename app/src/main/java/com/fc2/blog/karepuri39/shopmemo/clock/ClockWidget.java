package com.fc2.blog.karepuri39.shopmemo.clock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.fc2.blog.karepuri39.shopmemo.IO.Mylog;
import com.fc2.blog.karepuri39.shopmemo.R;
import com.fc2.blog.karepuri39.shopmemo.wake.WakeService;

public class ClockWidget extends AppWidgetProvider {
    static RemoteViews mRemoteViews = null;
    static ComponentName mComponentName = null;

    public static void Init(Context context) {
        if (mRemoteViews != null) {
            Mylog.d("Init skip.");
            return;
        }
        Mylog.d("ClockWidget::Init start.");

        mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.clock_widget_layout);
        mComponentName = new ComponentName(context, com.fc2.blog.karepuri39.shopmemo.clock.ClockWidget.class);
    }

    public static void Update(Context context, AppWidgetManager appWidgetManager) {
        Init(context);
        appWidgetManager.updateAppWidget(mComponentName, mRemoteViews);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Mylog.d("ClockWidget::onUpdate");
        Update(context, appWidgetManager);
    }

}

