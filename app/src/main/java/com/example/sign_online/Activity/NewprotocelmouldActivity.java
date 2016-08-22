package com.example.sign_online.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyLoadingActivity;
import com.example.sign_online.R;
import com.example.sign_online.Thread.SUploadUserprotocelMouldToServerThread;
import com.example.sign_online.Tools.DataCash.Fileoperation;
import com.example.sign_online.Tools.DataCash.Staticbean;

import java.util.ArrayList;

/**用来添加一个新的协议模板
 * Created by 曾志强 on 2016/1/24.
 */
public class NewprotocelmouldActivity extends MyLoadingActivity implements View.OnClickListener {
    //用户模板标题
    private EditText editTextmouldtitle = null;
    //用户模板正文
    private EditText editTextmouldcontent = null;
    //确认添加模板按钮
    private Button imageButtonsure = null;
    //模板姓名
    String mouldtitle = null;
    //模板体
    String mouldcontent = null;
    //已经加入换行符的protocelcontent
    String formedmouldcontent = null;
    //模板传到服务器成功
    private final static int UPDATE_TO_SERVER_SUCCESS = 1;
    //handler
    private MyBaseHandler myBaseHandler = new MyBaseHandler(NewprotocelmouldActivity.this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_TO_SERVER_SUCCESS) {
                //添加到本地
                (new Fileoperation(NewprotocelmouldActivity.this, null)).SaveUserProtocelMould((NewprotocelmouldActivity.this.getSharedPreferences("use_for_login",MODE_PRIVATE).getString("username1",null)), mouldtitle, formedmouldcontent);
                StopLoad();
                Toast.makeText(NewprotocelmouldActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 0) {
                Toast.makeText(NewprotocelmouldActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadSubview(LayoutInflater.from((NewprotocelmouldActivity.this)).inflate(R.layout.layoutnewuserprotocelmould, null));
        init();
        //用户未处于登陆状态
    }


    /*初始化视图,完成控件的加载
       *init
       */
    public void init(){
        editTextmouldtitle=(EditText)findViewById(R.id.userprotoceltitle);
        editTextmouldcontent=(EditText)findViewById(R.id.newprotocelcontent);
        imageButtonsure=(Button)findViewById(R.id.addprotocelmouldsurebutton);
        imageButtonsure.setOnClickListener(this);
        ((ImageButton)findViewById(R.id.toobarback)).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addprotocelmouldsurebutton:
                //模板姓名
                mouldtitle=editTextmouldtitle.getText().toString();
                //模板体
                mouldcontent=editTextmouldcontent.getText().toString();

                formedmouldcontent=GetFormEdittextContent(editTextmouldcontent);
                //  mouldcontent=GetFormEdittextContent(editTextmouldcontent);
                //提交到网络服务器
                //模板不能为空
                if ("".equals(formedmouldcontent)) {
                Toast.makeText(NewprotocelmouldActivity.this,"模板无效",Toast.LENGTH_SHORT).show();
                }

                else {
                    //实参
                    ArrayList<String> list_real_param = new ArrayList<String>();
                    list_real_param.add(Staticbean.userMsg.getUseremail());
                    list_real_param.add(mouldtitle);
                    list_real_param.add(formedmouldcontent);
                    StartLoad();
                    (new SUploadUserprotocelMouldToServerThread(NewprotocelmouldActivity.this, myBaseHandler, UPDATE_TO_SERVER_SUCCESS, list_real_param)).start();
                }
                break;
            case R.id.toobarback:
                finish();
                startActivity(new Intent(NewprotocelmouldActivity.this,ProtocelmouldmainActivity.class));
                break;
        }
    }


    /*这个方法用来将一个edittext中的内容转为有空格标记的普通文本，便于加载格式
    *GetFormEdittextContent
    * @param EditText edittext
     */
    public String GetFormEdittextContent(EditText editText){
        String edittextcontent=editText.getText().toString();
//        //换行符的前一段
//        String prestring=null;
//        //换行符的后一段
//        String laststring=null;
        char[] chars=edittextcontent.toCharArray();
        for(int i=0;i<edittextcontent.length();i++){
                if('\n'==chars[i]) {
                    chars[i]='{';
                }
            else if(' '==chars[i]){
                    chars[i]='}';
                }
        }
        return new String(chars);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(NewprotocelmouldActivity.this,ProtocelmouldmainActivity.class));
        }
        return true;
    }
}
