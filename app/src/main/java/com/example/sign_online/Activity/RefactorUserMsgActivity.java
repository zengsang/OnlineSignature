package com.example.sign_online.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sign_online.Baseclass.MyBaseActivity;
import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.UserMsg;
import com.example.sign_online.Mywidget.MyCircleImageView;
import com.example.sign_online.R;
import com.example.sign_online.Thread.SSelectUsermsgThread;
import com.example.sign_online.Thread.SUpdateAllUsermsgNormalThread;
import com.example.sign_online.Tools.DataCash.Fileoperation;

import java.util.ArrayList;

/**
 * Created by 曾志强 on 2016/1/26.
 */
public class RefactorUserMsgActivity extends MyBaseActivity implements  View.OnClickListener{
    private EditText edittextUsername=null;
    private EditText edittextUserrealname=null;
    private EditText edittextUserPhonenumber=null;
    private EditText edittextUsersex=null;
    private EditText edittextUseryearold=null;
    //编辑信息按钮
    private Button editmsgButton=null;
    //用户邮箱
    private String userEmail=null;
    //访问成功
    private final static int SELECT_MSG_SUCCESS=1;
    private final static int UPDATE_MSG_SUCCESS=2;
    private MyBaseHandler myBaseHandler=new MyBaseHandler(RefactorUserMsgActivity.this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SELECT_MSG_SUCCESS) {
                UserMsg userMsg = (UserMsg) msg.getData().getSerializable("UserMsg");
                String userName = userMsg.getUsername();
                String userRealName = userMsg.getUserreallyname();
                String userPhoneNumber = userMsg.getUserphonenumber();
                String userSex = userMsg.getUsersex();
                String userYearOld = userMsg.getUseryearold();
                System.out.println("eeee"+userName);
                System.out.println("eeee"+userName);
                System.out.println("eeee"+userName);
                if (null==userName) {
                    userName = "未填写";
                }
                if (null==userRealName) {
                    userRealName = "未填写";
                }
                if (null==userPhoneNumber) {
                    userPhoneNumber = "未填写";
                }
                if (null==userSex) {
                    userSex = "未填写";
                }
                if (null==userYearOld) {
                    userYearOld = "未填写";
                }
                edittextUsername.setText(userName);
                edittextUserrealname.setText(userRealName);
                edittextUserPhonenumber.setText(userPhoneNumber);
                edittextUsersex.setText(userSex);
                edittextUseryearold.setText(userYearOld);
            }
            if(msg.what==UPDATE_MSG_SUCCESS){
                Toast.makeText(RefactorUserMsgActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
            }
        }


    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutrefactorusermsg);
        init();
    }
    /*初始化一些控件、变量
*init
 */
    public void init() {
        ((ImageButton)((Toolbar)findViewById(R.id.toolbar)).findViewById(R.id.toobarback)).setOnClickListener(this);
        edittextUsername= (EditText) findViewById(R.id.usernameedittext);
        edittextUserrealname= (EditText) findViewById(R.id.userrealnameedittext);
        edittextUserPhonenumber= (EditText) findViewById(R.id.userphonenumberedittext);
        edittextUsersex= (EditText) findViewById(R.id.usersexedittext);
        edittextUseryearold= (EditText) findViewById(R.id.useryearoldedittext);
        edittextUsername.setEnabled(false);
        edittextUserrealname.setEnabled(false);
        edittextUserPhonenumber.setEnabled(false);
        edittextUsersex.setEnabled(false);
        edittextUseryearold.setEnabled(false);
        editmsgButton=(Button)findViewById(R.id.editbutton);
        //获取用户信息
        //参数
        ArrayList<String> list_real_param=new ArrayList<String>();
        userEmail=RefactorUserMsgActivity.this.getSharedPreferences("use_for_login",MODE_PRIVATE).getString("username1",null);
        list_real_param.add(userEmail);
       editmsgButton.setOnClickListener(this);
        ((new SSelectUsermsgThread(RefactorUserMsgActivity.this,myBaseHandler,SELECT_MSG_SUCCESS ,list_real_param))).start();
        userEmail=RefactorUserMsgActivity.this.getSharedPreferences("use_for_login", MODE_PRIVATE).getString("username1", null);
        ((MyCircleImageView)findViewById(R.id.usericon)).setImageBitmap((new Fileoperation(RefactorUserMsgActivity.this, null)).GetUserIcon(userEmail));

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toobarback:
                //回到设置界面
                finish();
                startActivity(new Intent(RefactorUserMsgActivity.this,SettingActivity.class));
                break;
            case R.id.editbutton:
                edittextUsername.setEnabled(true);
                edittextUserrealname.setEnabled(true);
                edittextUserPhonenumber.setEnabled(true);
                edittextUsersex.setEnabled(true);
                edittextUseryearold.setEnabled(true);
                editmsgButton.setText("确定");
                editmsgButton.setId(R.id.surerefactormsg);
                break;
            case R.id.surerefactormsg:
                ArrayList<String> listRealParam=new ArrayList<String>();
                listRealParam.add(userEmail);
                listRealParam.add(edittextUsername.getText().toString());
                listRealParam.add(edittextUserrealname.getText().toString());
                listRealParam.add(edittextUserPhonenumber.getText().toString());
                listRealParam.add(edittextUsersex.getText().toString());
                listRealParam.add(edittextUseryearold.getText().toString());
                ((new SUpdateAllUsermsgNormalThread(RefactorUserMsgActivity.this,myBaseHandler,UPDATE_MSG_SUCCESS,listRealParam))).start();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode,event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(RefactorUserMsgActivity.this,SettingActivity.class));
        }
        return true;
    }

}
