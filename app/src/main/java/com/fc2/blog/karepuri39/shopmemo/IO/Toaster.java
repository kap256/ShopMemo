package com.fc2.blog.karepuri39.shopmemo.IO;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.fc2.blog.karepuri39.shopmemo.R;

public class Toaster {
    /*
    あわせてぜんぶ表示
     */
    static public void Print(String text,Context context){
        Toast(text,context);
        Notify.Show(text,context);
    }
    static public void Toast(final String text, final Context context) {
        Mylog.d("Toast:"+text);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable(){
            public void run() {
                android.widget.Toast.makeText(context , text, android.widget.Toast.LENGTH_LONG).show();
            }
        });

    }
}
