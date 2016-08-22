package com.example.sign_online.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyLoadingActivity;
import com.example.sign_online.Baseclass.UserMsg;
import com.example.sign_online.Mywidget.MyCircleImageView;
import com.example.sign_online.R;
import com.example.sign_online.Thread.SSelectProtocelMouldThread;
import com.example.sign_online.Thread.SSelectProtocelThread;
import com.example.sign_online.Thread.SSelectUserIconThread;
import com.example.sign_online.Thread.SSelectUsermsgThread;
import com.example.sign_online.Tools.DataCash.Fileoperation;
import com.example.sign_online.Tools.DataCash.Staticbean;

import java.util.ArrayList;

/**
 * Created by 曾志强 on 2016/1/19.
 */
public class LoginActivity extends MyLoadingActivity implements View.OnClickListener{
    //获取网络数据成功
    private final static int GETMSG_SUCCESS=1;
    //网络错误
    private final static int INTENT_ERROR=0;
    //加载网络用户头像
    private static final int SELECT_USERICON_SUCCESS=2;
    //加载网络用户头像
    private static final int SELECT_PROTOCELMOULD_SUCCESS=3;
    private static final int SELECT_PROTOCE_SUCCESS=4;
    //用户信息
    private UserMsg user_msg=null;
    //用户名
    private EditText editText_username;
    //操作文件的对象
    private Fileoperation fileoperation=null;
    //密码
    private EditText editText_userpassword=null;
    //登录按钮
    private  Button loginbutton;
    private SharedPreferences sharedPreferencesset=null;
    //handler用来处理返回信息,并且验证登录
    private MyBaseHandler mybasehandler=new MyBaseHandler(LoginActivity.this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //加载用户头像到本地成功
            if(msg.what==SELECT_USERICON_SUCCESS){
                //加载用户其他数据
                fileoperation.LoadUserIconToLocal(editText_username.getText().toString().trim(),msg.getData().getStringArrayList("data"));
                //登陆界面用户头像的显示
                ((MyCircleImageView)findViewById(R.id.usericon)).setImageBitmap(Staticbean.userMsg.getUsericon());
                //参数
                ArrayList<String> list_real_param=new ArrayList<String>();
                list_real_param.add(editText_username.getText().toString().trim());
                (new SSelectProtocelMouldThread(LoginActivity.this, mybasehandler, SELECT_PROTOCELMOULD_SUCCESS,list_real_param)).start();

            }
            //加载用户协议模板到本地成功
            else if(msg.what==SELECT_PROTOCELMOULD_SUCCESS){
                fileoperation.LoadUserProtocelMouldToLocal(editText_username.getText().toString().trim(), msg.getData().getStringArrayList("data"));
                //参数
                ArrayList<String> list_real_param=new ArrayList<String>();
                list_real_param.add(editText_username.getText().toString().trim());
                (new SSelectProtocelThread(LoginActivity.this, mybasehandler, SELECT_PROTOCE_SUCCESS,list_real_param)).start();
            }
            //加载用户协议模板到本地成功
            else if(msg.what==SELECT_PROTOCE_SUCCESS){
                fileoperation.LoadUserProtocelToLocal(editText_username.getText().toString().trim(), msg.getData().getStringArrayList("data"));
                //用户名，方便全局调用
                SharedPreferences.Editor editor=(LoginActivity.this.getSharedPreferences("use_for_login", MODE_PRIVATE)).edit();
                editor.putString("username1", user_msg.getUseremail());
                editor.commit();
               Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                LoginActivity.this.finish();
                startActivity(new Intent(LoginActivity.this, ProtocelmouldmainActivity.class));
            }
            else if(msg.what==GETMSG_SUCCESS){
                //访问网络过程中没有出错
                //用户信息获取
                Staticbean.userMsg=(UserMsg)msg.getData().getSerializable("UserMsg");
                user_msg= Staticbean.userMsg;
                if(user_msg.getUserpassword()==null){
                    StopLoad();
                    loginbutton.setClickable(false);
                    Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_LONG).show();
                    loginbutton.setClickable(true);
                }
                else{
                    if(editText_userpassword.getText().toString().trim().equals(user_msg.getUserpassword().toString().trim())){
                        //密码正确
                        //记住登录状态
                        Rememberuser();
                        //开始加载用户信息到本地
                        //参数
                        ArrayList<String> list_real_param = new ArrayList<String>();
                        list_real_param.add(editText_username.getText().toString().trim());
                        (new SSelectUserIconThread(LoginActivity.this,mybasehandler,SELECT_USERICON_SUCCESS,list_real_param)).start();
                    }
                    else{
                        //密码错误
                        StopLoad();
                        loginbutton.setClickable(false);
                        Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_LONG).show();
                        loginbutton.setClickable(true);
                    }
                }
            }
            else if(msg.what==INTENT_ERROR){
                StopLoad();
                loginbutton.setClickable(false);
                Toast.makeText(LoginActivity.this, "网络或远程服务出错", Toast.LENGTH_LONG).show();
                loginbutton.setClickable(true);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示配置信息
        sharedPreferencesset=LoginActivity.this.getSharedPreferences("use_for_login",MODE_PRIVATE);
        if(sharedPreferencesset.getBoolean("loginstatus", false)){
            //如果用户已经处于登录状态，则直接进入主界面
            startActivity(new Intent(LoginActivity.this,ProtocelmouldmainActivity.class));
            LoginActivity.this.finish();
        }
        LoadSubview(LayoutInflater.from(LoginActivity.this).inflate(R.layout.layoutlogin, null));
        init();
        fileoperation.DeleteBannerUrlFile((editText_username).getText().toString().trim());
        //如果用户没有登陆，则显示他的默认用户名,密码
        editText_username.setText(sharedPreferencesset.getString("username".toString(),""));
        editText_userpassword.setText(sharedPreferencesset.getString("userpassword".toString(),""));
    }

    /*初始化一些控件、变量
  *init
   */
    public void init(){
        //用户名
        editText_username=(EditText)findViewById(R.id.e_username);
        //用户密码
        editText_userpassword=(EditText)findViewById(R.id.e_userpassword);
        fileoperation=new Fileoperation(LoginActivity.this,mybasehandler);
        //登录按钮
        loginbutton=(Button)findViewById(R.id.loginbutton);
        //注册按钮
        Button registbutton=(Button)findViewById(R.id.registnowbuttong);
        //忘记密码按钮
        Button forgetpwdbutton=(Button)findViewById(R.id.forgetpasswordbuttong);
        //设置监听
        loginbutton.setOnClickListener(this);
        registbutton.setOnClickListener(this);
        forgetpwdbutton.setOnClickListener(this);
    }

    /*通过访问线程获取用户的信息,并通过Getusermsg类的usermsg保存,并且验证和登录
     *CheckLogIn
     */
    public void CheckLogIn(){
        //实参
        ArrayList<String> list_real_param=new ArrayList<String>();
        list_real_param.add(editText_username.getText().toString().trim());
        //访问网络线程
        (new SSelectUsermsgThread(LoginActivity.this, mybasehandler, GETMSG_SUCCESS,list_real_param)).start();
        //开始显示正在加载
        StartLoad();
    }

    /*记住用户的账户名和密码
    *Rememberuser
     */
    public void Rememberuser(){
        Staticbean.userMsg.setUseremail((editText_username).getText().toString().trim());
        //配置存储
        SharedPreferences sharedPreferencesget=LoginActivity.this.getSharedPreferences("use_for_login",MODE_PRIVATE);
        //存储数据
        SharedPreferences.Editor editor=sharedPreferencesget.edit();
        //存储用户信息
        editor.putBoolean("loginstatus", true);
        editor.putString("username",(editText_username).getText().toString().trim());
        editor.putString("userpassword",(editText_userpassword).getText().toString().trim());
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.loginbutton:
                //获取用户信息并且送给handler进行校验，进行登录
                loginbutton.setClickable(false);
                CheckLogIn();
                break;
            case R.id.registnowbuttong:
                LoginActivity.this.finish();
                startActivity(new Intent(LoginActivity.this,RegistActivity.class));
                break;
            case R.id.forgetpasswordbuttong:
                break;
        }
    }

    /*根据用户邮箱加载用户头像
    *LoadUserIcon
    * @param useremail
     */
    public void LoadUserIcon(String useremail){

    }

}
