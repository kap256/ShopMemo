package com.fc2.blog.karepuri39.shopmemo.background;

import android.content.Context;

import com.fc2.blog.karepuri39.shopmemo.IO.Notify;
import com.fc2.blog.karepuri39.shopmemo.IO.Toaster;

public class State {
    static boolean mIsHome=false;
    static boolean mIsSleep=false;
    static boolean mChanged=false;

    public static boolean IsHome(){
        return mIsHome;
    }
    public static boolean IsSleep(){
        return mIsHome && mIsSleep;
    }

    public static void SetHome(boolean home){
        if(mIsHome == home) return;
        mIsHome=home;
        mChanged=true;


    }
    public static void SetSleep(boolean sleep){
        if(!mIsHome)    return;
        if(mIsSleep == sleep) return;
        mIsSleep=sleep;
        mChanged=true;


    }
    public static void OutputState(Context context, boolean force){
        if(!(mChanged || force))   return;
        mChanged=false;

        if(IsSleep()){
            Notify.Show("おやすみモードに切り替えました。", context);
        }else if(IsHome()){
            Notify.Show("おはようモードに切り替えました。", context);
        }else {
            Notify.Show("外出モードに切り替えました。", context);
        }
    }
}
