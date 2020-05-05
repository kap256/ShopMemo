package com.fc2.blog.karepuri39.shopmemo.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SystemReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        CheckerService.StartChecker(context);
    }
}
