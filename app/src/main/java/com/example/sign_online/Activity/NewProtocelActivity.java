package com.example.sign_online.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.sign_online.Baseclass.MyLoadingActivity;
import com.example.sign_online.Mywidget.MyDrawProtocelView;
import com.example.sign_online.R;
import com.example.sign_online.Tools.DataCash.Fileoperation;
import com.example.sign_online.Tools.DataCash.Staticbean;

/**
 * Created by 曾志强 on 2016/2/15.
 */
public class NewProtocelActivity extends MyLoadingActivity implements View.OnClickListener{
    //显示模板
    private EditText protocel_edittext=null;
    //协议的图片
    private Bitmap bitmap=null;
    //画协议的view
    private MyDrawProtocelView myDrawProtocelView=null;
    //屏幕的宽和高
    private int screenwidth,screenheight=0;
    //协议模板体
    String protocelmouldcontent;
    //当前日期
    String date=null;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display=((WindowManager) NewProtocelActivity.this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        display.getMetrics(displayMetrics);
        screenwidth=displayMetrics.widthPixels;
        screenheight=displayMetrics.heightPixels;
        LoadSubview(LayoutInflater.from((NewProtocelActivity.this)).inflate(R.layout.layoutnewprotocel, null));
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newprotocelmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                protocel_edittext.setEnabled(true);
                //获得有标记的string
                protocelmouldcontent=(new NewprotocelmouldActivity()).GetFormEdittextContent(protocel_edittext);
                break;
            case R.id.sign_name:
                Dialog dialog=new Dialog(NewProtocelActivity.this);
                View view=LayoutInflater.from(NewProtocelActivity.this).inflate(R.layout.layoutnewprotocelselectdialogview,null);
                dialog.setContentView(view);
                Window dialogwindow=dialog.getWindow();
                WindowManager.LayoutParams layoutParams=dialogwindow.getAttributes();
                dialog.setTitle("签字方式");
                layoutParams.width=screenwidth/2;
                layoutParams.height=screenheight/3;
                dialogwindow.setAttributes(layoutParams);
                ((Button)view.findViewById(R.id.signfacetoface)).setOnClickListener(this);
                ((Button)view.findViewById(R.id.signlongdistance)).setOnClickListener(this);
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    /*初始化视图,完成控件的加载
     *init
       */
    public void init(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //对应的协议模板名
        //加载用户协议模板
        protocelmouldcontent=(new Fileoperation(NewProtocelActivity.this,null)).GetMouldContent((NewProtocelActivity.this.getSharedPreferences("use_for_login",MODE_PRIVATE).getString("username1",null)),Staticbean.protocelmouldname);
        protocel_edittext=(EditText)findViewById(R.id.protocel);
        //按用户的格式显示模板
        ShowProtocelMouldInForm(protocel_edittext, protocelmouldcontent);
        //设置模板不可编辑
        protocel_edittext.setEnabled(false);
        ((ImageButton)findViewById(R.id.toobarback)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toobarback:
                NewProtocelActivity.this.finish();
                startActivity(new Intent(NewProtocelActivity.this,ProtocelmouldmainActivity.class));
                break;
            case R.id.signfacetoface:
                SignFaceToFace();
                break;
            case R.id.signlongdistance:
                SignTheTranslateProtocel();
                break;
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(NewProtocelActivity.this, ProtocelmouldmainActivity.class));
        }
        return true;
    }

    /*edittext显示具有标志符号的string文本，加载模板
    *ShowProtocelMouldInForm
    * @param Editext edittext
    * @param String protocelmouldformcontent
     */
    public void ShowProtocelMouldInForm(EditText editText,String protocelmouldformcontent){
        //一行的数据
        char[] chars=protocelmouldformcontent.toCharArray();
        for(char c:chars){
            if(c=='{'){
                //换行
                editText.append("\n");
            }
            else if(c=='}'){
                //添加文字
                editText.append(" ");
            }
            else{
                editText.append(c+"");
            }
        }

    }


    /*使用canvas书写指定的内容，并且保存为bitmap格式，这里主要用来书写协议模板
    *DrawProtocelMould
    * @param String protocelmould
    * @param int width
    * @param int height
    * @return Bitmap bitmap
     */
    public Bitmap DrawProtocelMould(String protocelmould,int width,int height){
        Bitmap bitmap=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        String formatprotocelmould=null;
        Canvas canvas=new Canvas(bitmap);
        TextPaint paint=new TextPaint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setStrokeWidth(1.4f);
        paint.setTextSize(16f);
        canvas.drawColor(Color.WHITE);
        char[] chars=protocelmould.toCharArray();
        for(int i=0;i<protocelmould.length();i++){

            if('{'==chars[i]){
                   //换行
                   formatprotocelmould+="\n";
               }
            else if('}'==chars[i]){
                   //空格
                   formatprotocelmould+=" ";
               }
            else{
                   formatprotocelmould+=chars[i]+"";
               }
        }
        formatprotocelmould=formatprotocelmould.substring(4);
        StaticLayout staticLayout=new StaticLayout(formatprotocelmould,paint,screenwidth, Layout.Alignment.ALIGN_NORMAL,0.8F,1.0F,true);
       canvas.translate(0f,100f);
        canvas.drawText(formatprotocelmould, 0, 0, paint);
        //staticLayout.draw(canvas);
        return bitmap;
    }


    /*用户点击单方签字的时候
    *SignFaceToFace
     */
    public void  SignTheTranslateProtocel(){
        //画出协议内容bitmap
        Staticbean.protocelbitmap=DrawProtocelMould(protocelmouldcontent,screenwidth*2,screenheight/2);
        startActivity(new Intent(NewProtocelActivity.this, SignLongDistanceActivity.class));
        finish();
    }

    /*用户点击当面签字的时候
    *SignFaceToFace
     */
    public void  SignFaceToFace(){
        //画出协议内容bitmap
        Staticbean.protocelbitmap=DrawProtocelMould(protocelmouldcontent,screenwidth*2,screenheight/2);
        startActivity(new Intent(NewProtocelActivity.this, SignNameFaceToFaceActivity.class));
        finish();
    }
}
