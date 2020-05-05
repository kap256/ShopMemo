package com.fc2.blog.karepuri39.shopmemo.wake;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Surface;

import com.fc2.blog.karepuri39.shopmemo.IO.Mylog;
import com.fc2.blog.karepuri39.shopmemo.IO.Toaster;

public class WakeService extends IntentService {

    static final int WAKE_MINUTE = 30;

    public WakeService(){
        super("WakeService");
    }
    protected void onHandleIntent(Intent intent) {
        Mylog.d("WakeService::onHandleIntent");

        wake(this.getApplicationContext());
    }

    static public void wake(Context context)
    {
        Toaster.Print("ランチモードを有効にします:"+WAKE_MINUTE+" 分",context);
        PowerManager pm = (PowerManager) context.getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakelock =
                pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "shop_memo:");
        wakelock.acquire(WAKE_MINUTE*60*1000);

    }

}
