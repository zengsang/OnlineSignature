package com.example.sign_online.Mywidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.sign_online.Tools.DataCash.Staticbean;

/**
 * Created by 曾志强 on 2016/2/15.
 */
public class MyDrawProtocelView extends View{
    //绘制的内容
    private String protocelcontent=null;
    //绘制图的宽
    private float width;
    private Canvas canvas1;
    //绘制图的高
    private float height;
    //待保存的模板图
    private Bitmap bitmap=null;
    public MyDrawProtocelView(String protocelcontent,float width,float height,Context context,AttributeSet attrs){
        super(context, attrs);
        setWillNotDraw(false);
        this.protocelcontent=protocelcontent;
        this.width=width;
        this.height=height;
        bitmap=Bitmap.createBitmap((int)width,(int)height, Bitmap.Config.ARGB_8888);
        canvas1=new Canvas(bitmap);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景色为白色
        canvas1.drawColor(Color.WHITE);
        //画笔
        Paint paint=new Paint();
        //画笔即字的颜色为黑色
        paint.setColor(Color.BLACK);
        //写字
        canvas1.drawText(protocelcontent, 0, protocelcontent.length(),paint);
        Staticbean.protocelbitmap=bitmap;
    }

}
