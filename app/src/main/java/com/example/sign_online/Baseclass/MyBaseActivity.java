package com.example.sign_online.Baseclass;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

/**父的activity，抽象了子类的所有特性，并且子activity继承自改父类
 * Created by 曾志强 on 2016/1/18.
 */
public class MyBaseActivity extends AppCompatActivity{
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
