package com.fc2.blog.karepuri39.shopmemo.background;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;

import com.fc2.blog.karepuri39.shopmemo.IO.Http;
import com.fc2.blog.karepuri39.shopmemo.IO.Mylog;
import com.fc2.blog.karepuri39.shopmemo.IO.Notify;
import com.fc2.blog.karepuri39.shopmemo.turn.TurnReceiver;

public class App extends Application {
    private static App instance;
    private Mylog log;
    SystemReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;

        log = new Mylog();
        Mylog.Init(log);
        Notify.Init(this);
        Http.Init(this);

        //ブロードキャスト レシーバーの登録はここですればよいのかな。
        receiver = new SystemReceiver();
        IntentFilter intentFilter;

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(new TurnReceiver(), intentFilter);





        Mylog.d("startService");
        CheckerService.StartChecker(this);
    }
    public static App getInstance(){
        return instance;
    }
}
