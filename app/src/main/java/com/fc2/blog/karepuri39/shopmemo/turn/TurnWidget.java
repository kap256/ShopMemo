package com.fc2.blog.karepuri39.shopmemo.turn;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.RemoteViews;

import com.fc2.blog.karepuri39.shopmemo.IO.Mylog;
import com.fc2.blog.karepuri39.shopmemo.R;
import com.fc2.blog.karepuri39.shopmemo.background.App;

public class TurnWidget extends AppWidgetProvider {

    static RemoteViews mRemoteViews =null;
    static ComponentName mComponentName=null;
    static int Icon = android.R.drawable.ic_menu_rotate;

    public static void Init(Context context)
    {
        if(mRemoteViews!=null){
            Mylog.d("Init skip.");
            return;
        }
        Mylog.d("TurnWidget::Init start.");

        mRemoteViews = new RemoteViews(context.getPackageName(),R.layout.turn_widget_layout);

        PendingIntent clickIntent = PendingIntent.getService(context,0,new Intent(context, TurnService.class),PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent clickIntent = PendingIntent.getActivity(context, 0, new Intent(context, TurnReceiver.class), 0);
        mRemoteViews.setOnClickPendingIntent(R.id.TurnImage, clickIntent);

        mComponentName = new ComponentName(context, TurnWidget.class);
    }
    public static void ChangeIcon(){
        ChangeIcon(android.R.drawable.ic_menu_rotate);
    }
    public static void ChangeIcon(int icon ){
        Icon=icon;
        Update();
    }
    public static void Update(){
        Context context = App.getInstance().getApplicationContext();
        Update(context);
    }
    public static void Update(Context context){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Update(context,appWidgetManager);
    }
    public static void Update(Context context, AppWidgetManager appWidgetManager){
        Init(context);
        mRemoteViews.setInt(R.id.TurnImage, "setBackgroundResource", Icon);
        appWidgetManager.updateAppWidget(mComponentName,mRemoteViews);
    }
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        Mylog.d("TurnWidget::onUpdate");
        Update(context,appWidgetManager);
    }

}
