package com.example.sign_online.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sign_online.Baseclass.MyBaseActivity;
import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.R;
import com.example.sign_online.Thread.SSaveUserSuggestionToServer;
import com.example.sign_online.Tools.DataCash.Staticbean;

import java.util.ArrayList;

/**
 * Created by 曾志强 on 2016/1/26.
 */
public class UserSuggestionActivity extends MyBaseActivity implements View.OnClickListener{
    //访问网络成功
    private static final int SAVEUSERSUGGESTION_SUCCESS=1;
    //用户的建议edittext
    private EditText UsersuggestionEdittext=null;
    //用户邮箱
    private String userEmail;
    private MyBaseHandler myBaseHandler=new MyBaseHandler(UserSuggestionActivity.this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                Toast.makeText(UserSuggestionActivity.this,"网络或服务器错误",Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==1){
                Toast.makeText(UserSuggestionActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutusersuggestion);
        init();
    }

    /*初始化一些控件、变量
        *init
        */
    public void init() {
        userEmail=UserSuggestionActivity.this.getSharedPreferences("use_for_login", MODE_PRIVATE).getString("username1",null);
        ((ImageButton)((Toolbar)findViewById(R.id.toolbar)).findViewById(R.id.toobarback)).setOnClickListener(this);
        ((ImageButton)((Toolbar)findViewById(R.id.toolbar)).findViewById(R.id.sendusersuggestionbutton)).setOnClickListener(this);
        UsersuggestionEdittext=(EditText)findViewById(R.id.usersuggestioncontent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toobarback:
                //回到主界面
                finish();
                startActivity(new Intent(UserSuggestionActivity.this,SettingActivity.class));
                break;
            case R.id.sendusersuggestionbutton:
                //将用户的建议保存到服务器
                //服务器所需要的参数
                ArrayList<String> list_real_param=new ArrayList<String>();
                list_real_param.add(userEmail);
                list_real_param.add(UsersuggestionEdittext.getText().toString());
                ((new SSaveUserSuggestionToServer(UserSuggestionActivity.this, myBaseHandler, SAVEUSERSUGGESTION_SUCCESS,list_real_param))).start();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode,event);
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(UserSuggestionActivity.this,SettingActivity.class));
        }
        return true;
    }
}
