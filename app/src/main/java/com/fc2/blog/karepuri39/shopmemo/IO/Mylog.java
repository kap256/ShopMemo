package com.fc2.blog.karepuri39.shopmemo.IO;

import android.util.Log;

import com.fc2.blog.karepuri39.shopmemo.background.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: プリ
 * Date: 2017/11/04 (004)
 * Time: 16:04
 * To change this template use File | Settings | File Templates.
 */

public class Mylog {
    private static Mylog instance = null;
    final static String TAG = "SHOP_MEMO";
    final static String FILE_NAME = "Mylog";
    static File sFile=null;

    boolean append = true;
    int old_logid=-1;

    private int getLogID(){
        Date now = new Date();
        int day = now.getDay();
        return  (day/5)%2;
    }
    private void Print(int priority,String mes){
        Log.println(priority,TAG,mes);

        int logid=getLogID();
        if(old_logid!=logid) {
            if (old_logid >=0) {
                append=false;
            }
            old_logid = logid;
        }
        Date now = new Date();

        File file = new File(App.getInstance().getFilesDir(), FILE_NAME+logid+".txt");
        try (FileWriter writer = new FileWriter(file,append)) {
            writer.write( now.toLocaleString()+" "+mes+"\n");
            append=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Init(Mylog log){
        instance = log;
        d("---------- Mylog Created ------------");
    }

    private static Mylog getInstance(){
        return instance;
    }

    public static void d(String mes){
        getInstance().Print(Log.DEBUG,mes);
    }
    public static void i(String mes){
        getInstance().Print(Log.INFO,mes);
    }
    public static String getLogs(){
        return Mylog.getInstance().getLogsP();
    }
    public String getLogsP(){
        int logid = getLogID();
        StringBuilder builder=new StringBuilder("");
        for(int i=0;i<2;i++){
            logid = (logid+1)%2;    //古い方から出すので、先に足す。

            File file = new File(App.getInstance().getFilesDir(), FILE_NAME+logid+".txt");
            try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
                while(true) {
                    String line = reader.readLine();
                    if (line == null) break;
                    builder.insert(0,"\n");
                    builder.insert(0,line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }
}
