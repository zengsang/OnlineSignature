package com.example.sign_online.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyLoadingActivity;
import com.example.sign_online.Mywidget.MyDialog;
import com.example.sign_online.R;
import com.example.sign_online.Thread.SUploadUserprotocelToServerThread;
import com.example.sign_online.Tools.DataCash.Fileoperation;
import com.example.sign_online.Tools.ViewTools.MyCanvasforParta;
import com.example.sign_online.Tools.DataCash.Staticbean;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by 曾志强 on 2016/2/20.
 */
public class SignLongDistanceActivity extends MyLoadingActivity implements View.OnClickListener{
    private Button surebutton,canclebutton=null;
    //屏幕的宽度和高度
    private int screenwidth,screenheight;
    //上传协议到服务器成功
    private final static  int UPLOAD_USERPROTOCEL_SUCCESS=1;
    //日期
    private String date=null;
    //最终的合同图片
    private Bitmap bitmap=null;
    //玉兰合同对话框

    public int getScreenheight() {
        return screenheight;
    }

    public void setScreenheight(int screenheight) {
        this.screenheight = screenheight;
    }

    public int getScreenwidth() {
        return screenwidth;
    }

    public void setScreenwidth(int screenwidth) {
        this.screenwidth = screenwidth;
    }

    private Dialog dialog=null;
    //handler
    private MyBaseHandler myBaseHandler=new MyBaseHandler(SignLongDistanceActivity.this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                Toast.makeText(SignLongDistanceActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                StopLoad();
            }
            else if(msg.what==1){
                ((new Fileoperation(SignLongDistanceActivity.this,null))).SaveUserProtocel((SignLongDistanceActivity.this.getSharedPreferences("use_for_login",MODE_PRIVATE).getString("username1",null)), Staticbean.protocelmouldname, date, bitmap);
                StopLoad();
                Toast.makeText(SignLongDistanceActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                SignLongDistanceActivity.this.finish();
                startActivity(new Intent(SignLongDistanceActivity.this,ProtocelmouldmainActivity.class));
                //释放存储签字bitmap
                Staticbean.partaandpartbbitmap=null;
                Staticbean.partbbitmap=null;
                Staticbean.partbbitmap=null;
                Staticbean.protocelbitmap=null;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display=((WindowManager) SignLongDistanceActivity.this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        display.getMetrics(displayMetrics);
        screenwidth=displayMetrics.widthPixels;
        screenheight=displayMetrics.heightPixels;
        LoadSubview(LayoutInflater.from(SignLongDistanceActivity.this).inflate(R.layout.layoutsignnamelongdistance, null));
        init();
    }
    /*初始化视图,完成控件的加载
             *init
             */
    public void init(){
        surebutton=(Button)findViewById(R.id.surebutton);
        canclebutton=(Button)findViewById(R.id.canclebutton);
        surebutton.setOnClickListener(this);
        canclebutton.setOnClickListener(this);
        ((ImageButton)findViewById(R.id.rewrite)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.surebutton:
                //合并三张图
                //     bitmap = GetUserSignProtocelBitmap(Staticbean.protocelbitmap,GetUserSignBitmap(Staticbean.partabitmap, Staticbean.partbbitmap,  screenwidth, screenheight / 4),  screenwidth*2,screenheight+screenheight/4);
                bitmap=GetUserSignProtocelBitmap(Staticbean.protocelbitmap,Staticbean.partabitmap,screenwidth, screenheight);
                // bitmap=Staticbean.protocelbitmap;
                View dialogview= LayoutInflater.from(SignLongDistanceActivity.this).inflate(R.layout.layoutdialogview,null);
                //用户预览合同对话框
                dialog=MyDialog.createDialog(SignLongDistanceActivity.this,dialogview,R.style.Transparent,false,screenwidth*12/13,screenheight*12/13,R.style.dialog_anima_style);
                ((ImageButton) dialogview.findViewById(R.id.dialogsurebutton)).setOnClickListener(this);
                ((ImageButton)dialogview.findViewById(R.id.dialogcanclebutton)).setOnClickListener(this);
                //预览合同mageview
                ((ImageView)dialogview.findViewById(R.id.dialogimageview)).setImageBitmap(bitmap);
                dialog.show();
                break;
            case R.id.canclebutton:
                Intent intent1=new Intent(SignLongDistanceActivity.this,NewProtocelActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.dialogsurebutton:
                StartLoad();
                SaveProtocelToServer(bitmap);
                break;
            case R.id.dialogcanclebutton:
                dialog.dismiss();
                break;
            case R.id.rewrite:
                //甲方重写
                ((MyCanvasforParta)findViewById(R.id.mycanvasforparta)).rewrite();
        }
    }


    /*访问网络，保存相应的用户协议
 *
  */
    public void SaveProtocelToServer(Bitmap bitmap){
        //已签
        //显示用户签名
        ImageView imageView = new ImageView(SignLongDistanceActivity.this);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        //获取当前日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        date = simpleDateFormat.format(System.currentTimeMillis());
        ArrayList<String> list_real_param = new ArrayList<String>();
        list_real_param.add(Staticbean.userMsg.getUseremail());
        list_real_param.add(Staticbean.protocelmouldname);
        list_real_param.add(date);
        // list_real_param.add(Base64.encodeToString(bytes, Base64.DEFAULT));
        list_real_param.add(Base64.encodeToString(bytes, Base64.DEFAULT));
        ((new SUploadUserprotocelToServerThread(SignLongDistanceActivity.this, myBaseHandler, UPLOAD_USERPROTOCEL_SUCCESS, list_real_param))).start();
    }

    /*拼凑两张bitmap，这里主要用来拼凑用户的签字bitmap和编辑后的模板bitmap
*GetUserSignBitmap
* @param bitmap bitmap1
* @param bitmap bitmap2
* @param int width 单个bitmap宽度
* @param int height 单个bitmap高度
* @return bitmap
 */
    public Bitmap GetUserSignProtocelBitmap(Bitmap bitmap1,Bitmap bitmap2,int width,int height){
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        Matrix matrix=new Matrix();
        Bitmap bit1=Bitmap.createBitmap(bitmap1,0,0,bitmap1.getWidth(),bitmap1.getHeight(),matrix,true);
        matrix.postScale(0.3f,0.3f);
        Bitmap bit2=Bitmap.createBitmap(bitmap2,0,0,bitmap2.getWidth(),bitmap2.getHeight(),matrix,true);
        Bitmap resultbitmap=Bitmap.createBitmap(width/2, height, Bitmap.Config.ARGB_8888);
        // 使用空白图片生成canvas
        Canvas canvas=new Canvas(resultbitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bit1, 0, 0, paint);
        canvas.translate(4/5*width,8/9*height);
        canvas.drawBitmap(bit2, 0, resultbitmap.getHeight() / 2, paint);
        return resultbitmap;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(SignLongDistanceActivity.this,NewProtocelActivity.class));
        }
        return true;
    }

}
