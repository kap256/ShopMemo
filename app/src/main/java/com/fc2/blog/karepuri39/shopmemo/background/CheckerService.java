package com.fc2.blog.karepuri39.shopmemo.background;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.view.Surface;

import com.fc2.blog.karepuri39.shopmemo.IO.Http;
import com.fc2.blog.karepuri39.shopmemo.IO.Mylog;
import com.fc2.blog.karepuri39.shopmemo.IO.Notify;
import com.fc2.blog.karepuri39.shopmemo.IO.Preference;
import com.fc2.blog.karepuri39.shopmemo.R;
import com.fc2.blog.karepuri39.shopmemo.memo.MemoWidget;
import com.fc2.blog.karepuri39.shopmemo.turn.TurnService;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.net.NetworkInfo.State.CONNECTED;

public class CheckerService extends Service {
    private final int Home_Minute = 2;
    private final int Out_Minute = 60;

    static final String URL = "/ComicCheckWebserver/shop_memo";

    static boolean isForeground = false;

    public CheckerService(){

        Mylog.d("CheckerService created..");
    }
    public static void StartChecker(Context context){
        stopAlarmService(context);
        Intent intent = new Intent(context, CheckerService.class);
        if(Build.VERSION.SDK_INT<26){
            Mylog.d("Ver < 26. startService().");
            context.startService(intent);
        }else {
            Mylog.d("Ver >= 26. startForegroundService().");
            context.startForegroundService(intent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Mylog.d("CheckerService start.");
        try {
            SetForground();
            MainJob();
            setNextAlarmService(getApplicationContext());
        }catch (Exception e){
            Mylog.i("Exception : "+e.getMessage());
            throw e;
        }
        return START_STICKY;
    }
    private void MainJob() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        //現状判定
        if( info!=null &&
                info.getType() == ConnectivityManager.TYPE_WIFI &&
                info.getState() == CONNECTED
                ) {

            State.SetHome(true);
            HttpCheck();
        }else{
            State.SetHome(false);
        }
        State.OutputState(this.getBaseContext(),false);
        ActionForState(getBaseContext());
    }

    public boolean HttpCheck(){
        final Context context = this.getBaseContext();
        Preference pref = new Preference(context);
        //URL取得
        String url ="http://"+pref.getIP()+URL;
        //送信パラメータ取得
        Date date = pref.getDate();
        String date_str = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(date);
        String param = date_str+ "\n" + pref.getText();
        //送信
        Http.Post(
                url,
                param,
                new Http.Listener() {
                    @Override
                    public void Response(String response){
                        State.SetSleep(false);
                        Preference pref = new Preference(context);
                        String old = pref.getText();
                        if(!old.equals(response)) {
                            Mylog.d("HTTP更新："+response);
                            pref.setText(response);
                            AppWidgetManager manager = AppWidgetManager.getInstance(context);
                            MemoWidget.Update(context, manager);
                        }
                    }
                    @Override
                    public void Error(){
                        State.SetSleep(true);
                    }
                }
        );
        return true;
    }

    static public void ActionForState(Context context){
        ChangeVolume(context);
        ChangeTurn(context);
    }
    static private void ChangeVolume(Context context){
        AudioManager audio_man = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        AudioDeviceInfo[] infos = audio_man.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
        for(AudioDeviceInfo info:infos){
            int type = info.getType();
            Mylog.d("AudioDeviceType = "+type);
            switch(type){
                case AudioDeviceInfo.TYPE_WIRED_HEADPHONES:
                case AudioDeviceInfo.TYPE_WIRED_HEADSET:
                case AudioDeviceInfo.TYPE_BLUETOOTH_A2DP:
                case AudioDeviceInfo.TYPE_BLUETOOTH_SCO:
                    Mylog.d("内蔵スピーカー以外を検出");
                    return;
            }
        }
        ChangeVolume(audio_man,AudioManager.STREAM_MUSIC);
        //ChangeVolume(audio_man,AudioManager.STREAM_NOTIFICATION); おもいのほかうっとおしい。
    }
    static private void ChangeVolume(AudioManager audio_man,int type){
        int max = audio_man.getStreamMaxVolume(type);
        int volume;

        if(State.IsHome()){
            volume=max/2;
        }else{
            volume=0;
        }
        Mylog.d("set volume = "+volume);

        audio_man.setStreamVolume(type,volume,0);
    }
    static private void ChangeTurn(Context context) {
        if (!State.IsHome()) {
            TurnService.turn(context,Surface.ROTATION_0);
        }
    }


    private void SetForground(){
        if(isForeground)    return;
        // ForegroundにするためNotificationが必要、Contextを設定
        Mylog.d("SetForground");
        NotificationManager notificationManager =
                (NotificationManager)getApplicationContext().
                        getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationManager != null){
            Notification.Builder builder = Notify.GetBuilder(getApplicationContext());
            builder.setContentTitle("お買い物サービス");
            builder.setContentText("Ⓒ 2018 Yellow Factory");
            builder.setSmallIcon(R.mipmap.ic_launcher);

            Notification n =builder.build();

            // startForeground
            startForeground(2, n);
            isForeground=true;
            Mylog.d("StartForeground complete");
        }
    }

    private static PendingIntent getPendingIntent(Context context){
        Intent intent = new Intent(context, CheckerService.class);
        return  PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    // 次のアラームの設定
    private void setNextAlarmService(Context context){

        long repeatPeriod;
        if(State.IsHome()){
            repeatPeriod = Home_Minute*60*1000;
        }else{
            repeatPeriod = Out_Minute*60*1000;
        }
        long startMillis = System.currentTimeMillis() + repeatPeriod;

        PendingIntent pendingIntent = getPendingIntent(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(alarmManager != null){
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startMillis, pendingIntent);
        }
    }

    private static void stopAlarmService(Context context){
        PendingIntent pendingIntent = getPendingIntent(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Mylog.d("TimerService::Destroy.");

        stopAlarmService(getApplicationContext());
        stopForeground(true);
        stopSelf();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
