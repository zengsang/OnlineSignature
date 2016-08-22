package com.example.sign_online.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.example.sign_online.Baseclass.MyBaseActivity;
import com.example.sign_online.R;

/**
 * Created by 曾志强 on 2016/1/30.
 */
public class CompleteUsermsg  extends MyBaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutcompleteusermsg);
    }


    /*初始化一些控件、变量
*init
*/
    public void init(){
        //toolbar

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toobarback:
                if(!(CompleteUsermsg.this.getSharedPreferences("use_for_login",MODE_PRIVATE).getBoolean("loginstatus",false))) {
                    //如果为登陆的完善信息回到login界面
                    finish();
                    startActivity(new Intent(CompleteUsermsg.this, LoginActivity.class));
                }
                else{
                    //如果已经登陆的完善信息回到信息界面
                    startActivity(new Intent(CompleteUsermsg.this, RefactorUserMsgActivity.class));
                }
                finish();
                break;
            case R.id.registmoremsgbutton:
                break;
        }
    }
}
