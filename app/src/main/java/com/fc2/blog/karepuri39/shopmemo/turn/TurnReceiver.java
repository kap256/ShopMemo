package com.fc2.blog.karepuri39.shopmemo.turn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Surface;

import com.fc2.blog.karepuri39.shopmemo.IO.Mylog;

public class TurnReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Mylog.d("TurnReceiver::onReceive");
        TurnService.turn(context,Surface.ROTATION_0);
        TurnWidget.ChangeIcon();
    }
}
