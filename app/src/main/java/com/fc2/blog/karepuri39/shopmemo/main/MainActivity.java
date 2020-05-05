package com.fc2.blog.karepuri39.shopmemo.main;

import android.app.Activity;
import android.os.PowerManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fc2.blog.karepuri39.shopmemo.IO.Mylog;
import com.fc2.blog.karepuri39.shopmemo.IO.Preference;
import com.fc2.blog.karepuri39.shopmemo.R;
import com.fc2.blog.karepuri39.shopmemo.background.CheckerService;
import com.fc2.blog.karepuri39.shopmemo.background.State;

/**
 * Created with IntelliJ IDEA.
 * User: プリ
 * Date: 2017/11/04 (004)
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 */

public class MainActivity extends Activity {

    @Override
    protected void onStart() {

        Mylog.d("MainActivity::onStart");

        try {

            setContentView(R.layout.main_layout);

            Preference pref = new Preference(this);
            updateEditIP(pref);

            findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Mylog.d("onClick");
                    MainActivity.this.Update();
                }
            });

            super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        }catch (Exception e){
            Mylog.i("Exception : "+e.getMessage());
            throw e;
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        updateLog();
    }

    void updateEditIP(Preference pref)
    {
        Mylog.d("updateEditIP");
        EditText edit = (EditText)(findViewById(R.id.Edit_IP));
        String ip =pref.getIP();
        if(ip.isEmpty()){
            ip="192.168.1.10:8080";
            Mylog.d("updateEditIP::IP not found");
        }
        edit.setText(ip);
    }
    void updateLog()
    {
        EditText edit = (EditText)(findViewById(R.id.Edit_LOG));
        edit.setMovementMethod(ScrollingMovementMethod.getInstance());
        edit.setText(Mylog.getLogs());
        edit.setKeyListener(null);
    }


    void Update() {
        Preference pref = new Preference(this);
        EditText edit = (EditText)(findViewById(R.id.Edit_IP));
        pref.setIP(edit.getText().toString());

        CheckerService.StartChecker(MainActivity.this);
        State.OutputState(this.getBaseContext(),true);

        updateLog();
    }

}
