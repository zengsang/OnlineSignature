package com.example.sign_online.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyLoadingActivity;
import com.example.sign_online.Baseclass.UserMsg;
import com.example.sign_online.R;
import com.example.sign_online.Thread.SInitUserFileInServerThread;
import com.example.sign_online.Thread.SInsertNewuserNormalThread;
import com.example.sign_online.Thread.SSelectUsermsgThread;
import com.example.sign_online.Tools.DataCash.Fileoperation;

import java.util.ArrayList;

/**
 * Created by 曾志强 on 2016/1/19.
 */
public class RegistActivity extends MyLoadingActivity implements View.OnClickListener{
    private final static int REGIST_SUCCESS=1;
    private final static int INTENT_ERROR=0;
    private final static int SELECT_SUCCESS=2;
    private final static int INIT_FILE_SUCCESS=3;
    private String useremail=null;
    private String userpassword=null;
    //用户输入的第二次密码
    private String userpasswordagain=null;
    //接受访问网络的反馈消息
    private MyBaseHandler mybasehandler=new MyBaseHandler(RegistActivity.this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==SELECT_SUCCESS){

                //查询成功，判断用户是否已经存在
                if(((UserMsg)msg.getData().getSerializable("UserMsg")).getUseremail()!=null){
                    //用户已经存在
                    StopLoad();
                    Toast.makeText(RegistActivity.this,"邮箱已被注册",Toast.LENGTH_SHORT).show();
                }
                else{
                    //茶如用户信息到服务器
                    //在本地创建用户文件
                    (new Fileoperation(RegistActivity.this,mybasehandler)).InitFile(useremail);
                    //在服务端创建用户相应的文件
                    //远程方法的实参
                    ArrayList<String> list_real_param=new ArrayList<String>();
                    list_real_param.add(useremail);
                    (new SInitUserFileInServerThread(RegistActivity.this,mybasehandler,INIT_FILE_SUCCESS,list_real_param)).start();
                }
            }
            else if(msg.what==INTENT_ERROR){
                //网络出错
                StopLoad();
                Toast.makeText(RegistActivity.this,"网络或远程服务出错",Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==INIT_FILE_SUCCESS){
                //符合重要信息注册要求进行注册
                //远程方法的实参
                ArrayList<String> list_real_param=new ArrayList<String>();
                list_real_param.add(useremail);
                list_real_param.add(userpassword);
                (new SInsertNewuserNormalThread(RegistActivity.this,mybasehandler,REGIST_SUCCESS,list_real_param)).start();
            }
            else if(msg.what==REGIST_SUCCESS){
                //成功注册
                StopLoad();
                Toast.makeText(RegistActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                RegistActivity.this.finish();
                startActivity(new Intent(RegistActivity.this,LoginActivity.class));
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadSubview(LayoutInflater.from(RegistActivity.this).inflate(R.layout.layoutregister,null));
        init();
    }

    /*初始化一些控件、变量
    *init
     */
    public void init(){
        ((ImageButton)findViewById(R.id.toobarback1)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.registbutton)).setOnClickListener(this);
    }


    /*将用户所有信息存入到数据库注册
     *Regist
     */
    public void Regist(){
        useremail=(((EditText)findViewById(R.id.e_useremail))).getText().toString().trim();
        userpassword=(((EditText)findViewById(R.id.e_userpassword))).getText().toString().trim();
        userpasswordagain=(((EditText)findViewById(R.id.e_userpasswordagain))).getText().toString().trim();
        if(("".equals(useremail))||("".equals(userpassword))||"".equals(userpasswordagain)){
            //信息未填写完整
            Toast.makeText(RegistActivity.this,"信息不全",Toast.LENGTH_SHORT).show();
        }
        else{
            //信息填写完整的情况
            if(!userpassword.equals(userpasswordagain)){
                //两次密码不一致
                StopLoad();
                Toast.makeText(RegistActivity.this,"两次密码不同",Toast.LENGTH_SHORT).show();
            }
            else{
                //远程方法的实参
                ArrayList<String> list_real_param=new ArrayList<String>();
                //符合重要信息注册要求进行注册
                list_real_param.add(useremail);
                //查询数据库中是否已经有了该用户
                (new SSelectUsermsgThread(RegistActivity.this,mybasehandler,SELECT_SUCCESS,list_real_param)).start();
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registbutton:
                //用户点击注册
                StartLoad();
                Regist();
                break;
            case R.id.toobarback1:
                //回到登录界面
                finish();
                startActivity(new Intent(RegistActivity.this, LoginActivity.class));
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode,event);
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(RegistActivity.this,LoginActivity.class));
            finish();
        }
        return true;
    }
}
