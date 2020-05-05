package com.fc2.blog.karepuri39.shopmemo.IO;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.fc2.blog.karepuri39.shopmemo.R;
import com.fc2.blog.karepuri39.shopmemo.background.App;

public class Notify {
    static final String channelId = "com.fc2.blog.karepuri39.shopmemo";
    static final String channelName = "ShopmemoService";

    static public void Init(Context context){

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, channelName,
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.createNotificationChannel(channel);
        }

    }
    static public void Show(String text, Context context) {

        Mylog.d("Notify:"+text);
        Notification n = GetBuilder(context)
                .setContentTitle("お買い物メモ")
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        Mylog.d("n="+n);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, n); // 設定したNotificationを通知する
    }
    static public Notification.Builder GetBuilder(Context context) {

        if (Build.VERSION.SDK_INT >= 26) {
            return new Notification.Builder(context, channelId);
        }else{
            return new Notification.Builder(context);
        }
    }
}
