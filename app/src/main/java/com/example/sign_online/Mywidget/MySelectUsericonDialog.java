package com.example.sign_online.Mywidget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.R;
import com.example.sign_online.Thread.SUploadUsericonToServerThread;
import com.example.sign_online.Tools.DataCash.Fileoperation;
import com.example.sign_online.Tools.DataCash.Staticbean;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**用于用户注册选择头像的时候确认所选头像
 * Created by 曾志强 on 2016/2/2.
 */
public class MySelectUsericonDialog extends Dialog implements View.OnClickListener{
    //对话框标题
    private String title=null;
    //要扩大显示的预览图片
    private Bitmap bitmap=null;
    //上下文
    private Context context=null;
    //上传头像成功
    private final static int UPLOADICON_SUCESS=1;
    //接收访问服务器上传头像的线程结果
    private MyBaseHandler myBaseHandler=new MyBaseHandler(context){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                Toast.makeText(context,"上传照片失败",Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==UPLOADICON_SUCESS){
                Toast.makeText(context,"上传照片成功",Toast.LENGTH_SHORT).show();
                //保存图片到本地缓存
                (new Fileoperation(context,myBaseHandler)).SaveUserIcon(Staticbean.userMsg.getUseremail().toString().trim(),Staticbean.userMsg.getUsericon());

            }
        }
    };
    public MySelectUsericonDialog(Context context,String title,Bitmap bitmap){
        super(context);
        this.context=context;
        this.title=title;
        this.bitmap=bitmap;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutmyprotocelmaxpicture);
        //用户点击其他地方对话框不消失
        setCancelable(false);
        init();
    }

    /*包括一些初始化操作，包括监听等操作
    *init
     */
    public void init(){
        //设置对话框标题
        setTitle(title);
        //加载各组件并设置相关值
        ImageView imageView=(ImageView)findViewById(R.id.userpictureimageview);
        Button selectButton=(Button)findViewById(R.id.selectbutton);
        Button unselectButton=(Button)findViewById(R.id.noselectbutton);
        //对话框按钮监听
        selectButton.setOnClickListener(this);
        unselectButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selectbutton:
                ArrayList<String> list_real_param=new ArrayList<String>();
                //获取头像的字节流
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                Staticbean.userMsg.getUsericon().compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] bytesicon=byteArrayOutputStream.toByteArray();
                //将byte转为string
                String usericonstring=new String(bytesicon);
                list_real_param.add(Staticbean.userMsg.getUseremail().toString().trim());
                list_real_param.add(usericonstring);
                //将图片存入远程数据库以及本地
                (new SUploadUsericonToServerThread(context, myBaseHandler, UPLOADICON_SUCESS,list_real_param )).start();
                break;
            case R.id.noselectbutton:
                dismiss();
                break;
        }
    }
}
