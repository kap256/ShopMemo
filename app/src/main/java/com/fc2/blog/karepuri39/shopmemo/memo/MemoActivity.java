package com.fc2.blog.karepuri39.shopmemo.memo;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.widget.EditText;

import com.fc2.blog.karepuri39.shopmemo.IO.Preference;
import com.fc2.blog.karepuri39.shopmemo.IO.Mylog;
import com.fc2.blog.karepuri39.shopmemo.R;

/**
 * Created with IntelliJ IDEA.
 * User: プリ
 * Date: 2017/11/04 (004)
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 */

public class MemoActivity extends Activity{

    @Override
    protected void onStart() {
        Mylog.d("onStart");
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.text_edit_layout);

        Preference pref = new Preference(this);
        EditText edit = (EditText)(findViewById(R.id.memo_editor));
        edit.setText(pref.getText());

    }

    @Override
    protected void onPause() {
        Mylog.d("onPause");
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.

        Preference pref = new Preference(this);
        EditText edit = (EditText)(findViewById(R.id.memo_editor));
        pref.setText(edit.getText().toString());

        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        MemoWidget.Update(this,manager);

        finishAndRemoveTask();
    }
}
