package com.fc2.blog.karepuri39.shopmemo.memo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.fc2.blog.karepuri39.shopmemo.IO.Mylog;
import com.fc2.blog.karepuri39.shopmemo.IO.Preference;
import com.fc2.blog.karepuri39.shopmemo.R;
import com.fc2.blog.karepuri39.shopmemo.background.CheckerService;

/**
 * Created with IntelliJ IDEA.
 * User: プリ
 * Date: 2017/11/04 (004)
 * Time: 13:41
 * To change this template use File | Settings | File Templates.
 */

public class MemoWidget extends AppWidgetProvider {

    static RemoteViews mRemoteViews =null;
    static ComponentName mComponentName=null;

    public static void Init(Context context)
    {
        if(mRemoteViews!=null){
            Mylog.d("Init skip.");
            return;
        }
        Mylog.d("MemoWidget::Init start.");
        CheckerService.StartChecker(context);

        mRemoteViews = new RemoteViews(context.getPackageName(),R.layout.memo_widget_layout);

        PendingIntent clickIntent = PendingIntent.getActivity(context, 0, new Intent(context,MemoActivity.class), 0);
        mRemoteViews.setOnClickPendingIntent(R.id.memo_text, clickIntent);

        mComponentName = new ComponentName(context, MemoWidget.class);
    }
    public static void Update(Context context, AppWidgetManager appWidgetManager){
        Init(context);
        Preference pref = new Preference(context);
        mRemoteViews.setTextViewText(R.id.memo_text, pref.getText());
        appWidgetManager.updateAppWidget(mComponentName,mRemoteViews);
    }
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        Mylog.d("onUpdate");
        Update(context,appWidgetManager);
    }

}
