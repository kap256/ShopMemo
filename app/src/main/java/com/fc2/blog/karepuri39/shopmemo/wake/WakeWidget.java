package com.fc2.blog.karepuri39.shopmemo.wake;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.fc2.blog.karepuri39.shopmemo.IO.Mylog;
import com.fc2.blog.karepuri39.shopmemo.R;
import com.fc2.blog.karepuri39.shopmemo.turn.TurnService;

public class WakeWidget extends AppWidgetProvider {

    static RemoteViews mRemoteViews =null;
    static ComponentName mComponentName=null;

    public static void Init(Context context)
    {
        if(mRemoteViews!=null){
            Mylog.d("Init skip.");
            return;
        }
        Mylog.d("WakeWidget::Init start.");

        mRemoteViews = new RemoteViews(context.getPackageName(),R.layout.wake_widget_layout);

        PendingIntent clickIntent = PendingIntent.getService(context,0,new Intent(context, WakeService.class),PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.WakeImage, clickIntent);
        mComponentName = new ComponentName(context, WakeWidget.class);
    }
    public static void Update(Context context, AppWidgetManager appWidgetManager){
        Init(context);
        appWidgetManager.updateAppWidget(mComponentName,mRemoteViews);
    }
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        Mylog.d("WakeWidget::onUpdate");
        Update(context,appWidgetManager);
    }

}
