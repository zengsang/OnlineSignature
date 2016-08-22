package com.example.sign_online.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sign_online.Baseclass.MyBaseAdapter;
import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyLoadingActivity;
import com.example.sign_online.Mywidget.MySelectUsericonDialog;
import com.example.sign_online.R;
import com.example.sign_online.Tools.DataCash.Fileoperation;
import com.example.sign_online.Tools.DataCash.Staticbean;

import java.util.ArrayList;
import java.util.List;


/**用来展示用户手机中的所有图片
 * Created by 曾志强 on 2016/1/29.
 */
public class ShowAllPictureActivity extends MyLoadingActivity implements AdapterView.OnItemClickListener{
    //手机中所有的图片
    private ArrayList<Bitmap> allpicture=null;
    //所有图片的路径
    private ArrayList<String> allpicturepath=null;
    //gridview显示所有图片
    private GridView gridview=null;
    ImageView imageView=null;
    //处理子线程的值
    private MyBaseHandler myBaseHandler=new MyBaseHandler(ShowAllPictureActivity.this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                TextView textView=new TextView(ShowAllPictureActivity.this);

            }
            else if(msg.what==1){
                //获取所有图片的路径
                allpicturepath=msg.getData().getStringArrayList("pictureurlpath");
                //加载成功,根据所有图片路径，并且调用此activity方法获取所有图片
                allpicture=Loadpictures(allpicturepath);
                //适配
                gridview.setAdapter(new settingadapter(ShowAllPictureActivity.this, allpicturepath));
                StopLoad();
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.layoutshowallpictures);
        //通过textview让用户等待，同时设置了当前view
        LoadSubview((LayoutInflater.from(this)).inflate(R.layout.layoutshowallpictures,null));
        allpicture=new ArrayList<Bitmap>();
        gridview=(GridView)findViewById(R.id.userpicturegridview);
        gridview.setOnItemClickListener(this);
        //调用方法，获取所有图片路径，注意此方法中有一个子线程
        (new Fileoperation(ShowAllPictureActivity.this,myBaseHandler)).Getallpictures();
    }

    //适配器内部类，继承自基础适配器
    class settingadapter extends MyBaseAdapter {
        //数据
        private List<String> data=new ArrayList<String>();
        //context
        private Context context=null;
        public settingadapter(Context context,List<String> data){
            super(context,data);
            this.data=data;
            this.context=context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null||imageView==null){
                convertView=(LayoutInflater.from(context)).inflate(R.layout.layoutshowallpictureitem,null);
                imageView=(ImageView)convertView.findViewById(R.id.pictureimageview);
            }
            imageView.setImageBitmap(allpicture.get(position));
            return convertView;
        }
    }


    /*根据文件路径，将图片文件加载
    *Loadpictures
    * @param pictureurlpath  手机中所有图片的路径结合
    * @return Bitmap
     */
    public ArrayList<Bitmap> Loadpictures(ArrayList<String> pictureurlpath){
        ArrayList<Bitmap>  pictures=new ArrayList<Bitmap>();
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=false;
        //图片缩小十分之一
        options.inSampleSize=10;
        for(int i=0;i<pictureurlpath.size();i++) {
            //加载图片
            Bitmap bitmap = BitmapFactory.decodeFile(pictureurlpath.get(i), options);
            pictures.add(bitmap);
        }
        return pictures;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(allpicture!=null) {
            //释放存放大量图片的allpictures
            allpicture = null;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(ShowAllPictureActivity.this,RegistActivity.class));
            if(allpicture!=null) {
                //释放存放大量图片的allpictures
                allpicture = null;
            }
        }
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //存储用户头像
        Staticbean.userMsg.setUsericon(allpicture.get(position));
        //定义对话框，用来给用户选择头像
        MySelectUsericonDialog mySelectUsericonDialog=new MySelectUsericonDialog(ShowAllPictureActivity.this,"确认作为头像吗",allpicture.get(position));
        mySelectUsericonDialog.show();
    }
}
