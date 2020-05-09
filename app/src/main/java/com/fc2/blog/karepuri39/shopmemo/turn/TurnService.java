package com.fc2.blog.karepuri39.shopmemo.turn;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.Surface;

import com.fc2.blog.karepuri39.shopmemo.IO.Mylog;
import com.fc2.blog.karepuri39.shopmemo.IO.Toaster;
import com.fc2.blog.karepuri39.shopmemo.background.App;

public class TurnService extends IntentService {
    public TurnService(){
        super("TurnService");
    }
    protected void onHandleIntent(Intent intent) {
        Mylog.d("TurnService::onHandleIntent");
        Toaster.Toast("画面回転します。",this.getApplicationContext());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        turn(this.getApplicationContext(),null);

    }

    static public void turn(Context context,Integer rotate)
    {
        Mylog.d("TurnService::turn");
        if(Settings.System.canWrite(context)){
            Mylog.d("TurnService::Settings.System.canWrite=true");
            ContentResolver contentresolver = context.getContentResolver();
            try {
                if (rotate == null) {
                    switch(android.provider.Settings.System.getInt(contentresolver, Settings.System.USER_ROTATION)){
                        case Surface.ROTATION_0:
                        case Surface.ROTATION_180:
                            rotate = Surface.ROTATION_90;
                            TurnWidget.ChangeIcon(android.R.drawable.ic_media_play);
                            break;
                        case Surface.ROTATION_90:
                        case Surface.ROTATION_270:
                            rotate = Surface.ROTATION_0;
                            TurnWidget.ChangeIcon();
                            break;
                    }
                }
                Mylog.d("TurnService::rotate to  "+rotate);

                android.provider.Settings.System.putInt(contentresolver, Settings.System.USER_ROTATION,rotate);

            }catch(Exception e){
                Mylog.d("TurnService::exception: "+e.getMessage());

            }

        }else{
            try {
                Mylog.d("TurnService::Settings.System.canWrite=false");
                Intent setting_intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                setting_intent.setData(Uri.parse("package:" + context.getPackageName()));
                setting_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(setting_intent);
            }catch(Exception e){
                Mylog.d("TurnService::exception: "+e.getMessage());

            }
        }
    }

}
