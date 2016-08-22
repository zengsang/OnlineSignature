package com.example.sign_online.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.example.sign_online.Baseclass.MyBaseActivity;
import com.example.sign_online.R;

/**
 * Created by 曾志强 on 2016/1/26.
 */
public class AboutDeveloperActivity extends MyBaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutaboutdeveloper);
        ((ImageButton)((Toolbar)findViewById(R.id.toolbar)).findViewById(R.id.toobarback)).setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(AboutDeveloperActivity.this,SettingActivity.class));
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toobarback:
                //回到主界面
                finish();
                startActivity(new Intent(AboutDeveloperActivity.this,SettingActivity.class));
                break;
        }
    }
}
