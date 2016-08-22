package com.example.sign_online.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sign_online.Baseclass.MyBaseActivity;
import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyLoadingActivity;
import com.example.sign_online.Mywidget.CustomGallery;
import com.example.sign_online.Mywidget.MyDialog;
import com.example.sign_online.R;
import com.example.sign_online.Thread.SDeleteMyProtocel;
import com.example.sign_online.Tools.Adapter.GalleryImageAdapter;
import com.example.sign_online.Tools.DataCash.Fileoperation;

import java.util.ArrayList;

/**
 * Created by 曾志强 on 2016/2/17.
 */
public class MyProtocelsDetileActivity extends MyLoadingActivity implements AdapterView.OnItemClickListener{
    private RecyclerView recyclerView=null;
    //协议模板名
    private String protocelMoudelName=null;
    //协议图片名
    private ArrayList<String>  protocelname=null;
    //协议图片
    private ArrayList<Bitmap> protocels=null;
    //当前协议图片名称
    private String currentProtocelName=null;
    //屏幕的宽度
    private int screenwidth=0;
    //屏幕的高度
    private int screenheight=0;
    private   CustomGallery customGallery=null;
    //用户邮箱
    private String userEmail=null;
    private Dialog myDeleteSureDialog=null;
    //具体展示用户的协议全屏对话框
    private Dialog dialog=null;
    //handler用来加载用户协议图片
    private MyBaseHandler myBaseHandler=new MyBaseHandler(MyProtocelsDetileActivity.this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                //成功加载本地协议图片
                //  recyclerView=(RecyclerView)findViewById(R.id.listmyprotocelimagerecycle);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MyProtocelsDetileActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                customGallery=(CustomGallery)findViewById(R.id.customgallery);
                System.out.println("bitmap"+protocels);
                GalleryImageAdapter imageAdapter=new GalleryImageAdapter(MyProtocelsDetileActivity.this,protocels,screenwidth*3/4,screenheight*3/4);
                customGallery.setAdapter(imageAdapter);
                customGallery.setOnItemClickListener(MyProtocelsDetileActivity.this);
                StopLoad();
            }
            else if(msg.what==2){
                //成功删除远程协议图片后，删除本地图片
                ((new Fileoperation(MyProtocelsDetileActivity.this, null))).DeleteMyProtocel(userEmail, getIntent().getStringExtra("protocelmouldname"),currentProtocelName);
            }
            else if(msg.what==3){
                //本地删除成功
                myDeleteSureDialog.dismiss();
                Toast.makeText(MyProtocelsDetileActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==0){
                myDeleteSureDialog.dismiss();
                Toast.makeText(MyProtocelsDetileActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenwidth=displayMetrics.widthPixels;
        screenheight=displayMetrics.heightPixels;
        LoadSubview(LayoutInflater.from(MyProtocelsDetileActivity.this).inflate(R.layout.layoutmyprotoceldetile, null));
        init();
    }


    /*初始化一些控件、变量
   *init
    */
    public void init() {
        userEmail=MyProtocelsDetileActivity.this.getSharedPreferences("use_for_login", MODE_PRIVATE).getString("username1", null);
        protocelname=new ArrayList<String>();
        protocels=new ArrayList<Bitmap>();
        //开辟一个子线程访问本地图片
        new Thread(){
            @Override
            public void run() {
                super.run();
                SetProtocelnameAndProtocels((MyProtocelsDetileActivity.this.getSharedPreferences("use_for_login",MODE_PRIVATE).getString("username1",null)),getIntent().getStringExtra("protocelmouldname"));
                myBaseHandler.sendEmptyMessage(1);
            }
        }.start();
        StartLoad();
    }

    /*通过对本地文件的操作获取协议名和协议的图片
     *SetProtocelnameAndProtocels
     * @ String useremail
     * @return void
     */
    public void SetProtocelnameAndProtocels(String useremail,String protocelmouldname){
        ArrayList<String> protocelnameandprotocels=((new Fileoperation(MyProtocelsDetileActivity.this,null)).GetUserProtocel(useremail,protocelmouldname));
        //协议图片
        Bitmap protocelbitmap=null;
        if(protocelnameandprotocels!=null){
            for(int i=0;i<protocelnameandprotocels.size();i=i+2){
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inPreferredConfig=Bitmap.Config.RGB_565;
                options.inPurgeable=true;
                options.inInputShareable=true;
                protocelname.add(protocelnameandprotocels.get(i));
                byte[] bytes= Base64.decode(protocelnameandprotocels.get(i+1),Base64.DEFAULT);
                protocelbitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
                protocels.add(protocelbitmap);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(MyProtocelsDetileActivity.this,MyProtocelsActivity.class));
        }
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        View dialogView=LayoutInflater.from(MyProtocelsDetileActivity.this).inflate(R.layout.layoutmyprotocelmaxpicture,null);
        //初始化view
        final Dialog myDialog= MyDialog.createDialog(MyProtocelsDetileActivity.this,dialogView,R.style.Transparent,false,screenwidth,screenheight,R.style.dialog_anima_style);

        ImageButton deleteButton= (ImageButton) dialogView.findViewById(R.id.deletebutton);
        ((ImageButton)dialogView.findViewById(R.id.toobarback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProtocelsDetileActivity.this, MyProtocelsActivity.class));
                finish();
            }
        });
        //显示图片
        ((ImageView)myDialog.findViewById(R.id.userpictureimageview)).setImageBitmap(((BitmapDrawable)((((ImageView)((customGallery.getChildAt(position-customGallery.getFirstVisiblePosition())).findViewById(R.id.galleryimageview))).getDrawable()))).getBitmap());
        myDialog.show();
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除该协议
                View myDeleteSureDialogView=LayoutInflater.from(MyProtocelsDetileActivity.this).inflate(R.layout.layoutsimpledialogview, null);
                myDeleteSureDialog= MyDialog.createDialog(MyProtocelsDetileActivity.this,myDeleteSureDialogView,R.style.Transparent,false,screenwidth/2,screenheight/3,R.style.dialog_anima_style);
                myDeleteSureDialog.show();

                ((Button)myDeleteSureDialogView.findViewById(R.id.surebutton)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        protocelMoudelName = getIntent().getStringExtra("protocelmouldname");
                        currentProtocelName = protocelname.get(position).toString();
                        ArrayList<String> listRealParam = new ArrayList<String>();
                        listRealParam.add(userEmail);
                        listRealParam.add(protocelMoudelName);
                        listRealParam.add(currentProtocelName);
                        (new SDeleteMyProtocel(MyProtocelsDetileActivity.this, myBaseHandler, 2, listRealParam)).start();
                    }
                });



            }
        });
    }



}

