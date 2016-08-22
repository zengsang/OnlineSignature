package com.example.sign_online.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyLoadingActivity;
import com.example.sign_online.R;
import com.example.sign_online.Thread.SGettranslateFilefromServer;
import com.example.sign_online.Thread.SGettranslatemsgfromServer;
import com.example.sign_online.Tools.DataCash.Fileoperation;
import com.example.sign_online.Tools.Adapter.MyMessageRecycleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 曾志强 on 2016/3/13.
 */
public class MyMessageActivity extends MyLoadingActivity implements View.OnClickListener{
    //获取用户需要的消息成功
    private static  final int GETTRANSLATEMSGSUCESS=1;
    //网络错误
    private static final int INTERNETERROR=0;
    //接收成功
    private static final int GETTRANSLATEFILESUCESS=2;
    //用户邮箱
    private String userEmail;
    //文件名
    private String translateFilename;
    //recycleview
    private RecyclerView recyclerView;
    //接收和处理消息的handler
    MyBaseHandler myBaseHandler=new MyBaseHandler(MyMessageActivity.this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==GETTRANSLATEMSGSUCESS){
                //传输过来的信息对象
                List<String> listmsg=msg.getData().getStringArrayList("data");
               userEmail=MyMessageActivity.this.getSharedPreferences("use_for_login", MODE_PRIVATE).getString("username1", null);
                MyMessageRecycleAdapter myMessageRecycleAdapter=new MyMessageRecycleAdapter(MyMessageActivity.this,listmsg,new MyMessageRecycleAdapter.ReceiveBtnListener() {
                    @Override
                    public void receivebtnonclick(String filename) {
                        translateFilename=filename;
                        //下载图片
                        ArrayList<String> list_real_param=new ArrayList<String>();
                        list_real_param.add(translateFilename + ".txt");
                        list_real_param.add(userEmail);
                        //向服务器请求下载资源
                        ((new SGettranslateFilefromServer(MyMessageActivity.this, myBaseHandler, GETTRANSLATEFILESUCESS, list_real_param))).start();
                    }
                });
                recyclerView.setAdapter(myMessageRecycleAdapter);
            }
            else if(msg.what==GETTRANSLATEFILESUCESS){
                String filecontent=msg.getData().getStringArrayList("data").get(0);
                System.out.println("filecontent" + filecontent);
                System.out.println("filecontent" + filecontent);
                //将接收到的信息储存到本地
                (new Fileoperation(MyMessageActivity.this,null)).SaveTranslateMsgToLocal(userEmail, translateFilename, filecontent);
            }
            else if(msg.what==INTERNETERROR){
                Toast.makeText(MyMessageActivity.this,"网络或服务错误",Toast.LENGTH_LONG).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadSubview(LayoutInflater.from(MyMessageActivity.this).inflate(R.layout.layoutmymessage, null));
        recyclerView=(RecyclerView)findViewById(R.id.listmymessage);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ((ImageButton)((Toolbar)findViewById(R.id.toolbar)).findViewById(R.id.toobarback)).setOnClickListener(this);
        LoadImMessage();
    }



    /*请求服务器，获取要接受的消息信息，然后进行显示
    *LoadImMessage
     */
    public void LoadImMessage(){
        //获取服务端用户需要接收信息
        //实参
        ArrayList<String> list_real_param=new ArrayList<String>();
        list_real_param.add(MyMessageActivity.this.getSharedPreferences("use_for_login",MODE_PRIVATE).getString("username1",null));
        ((new SGettranslatemsgfromServer(MyMessageActivity.this, myBaseHandler, GETTRANSLATEMSGSUCESS, list_real_param))).start();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(MyMessageActivity.this,ProtocelmouldmainActivity.class));
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toobarback:
                //回到设置界面
                finish();
                startActivity(new Intent(MyMessageActivity.this,ProtocelmouldmainActivity.class));
                break;
        }
    }
}
