package com.example.sign_online.Tools.ViewTools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.sign_online.Activity.SignLongDistanceActivity;
import com.example.sign_online.Baseclass.MyLoadingActivity;
import com.example.sign_online.Tools.DataCash.Staticbean;

/**
 * Created by 曾志强 on 2016/2/20.
 */
public class MyCanvasforPartb extends View{
    // 定义绘制路径
    private Path path;
    // 定义画笔
    private Paint paint;
    private float prex, prey; // 前一个点
    private Bitmap bt; // 缓冲区
    private Canvas canvas1;
    private int width = 380;
    private int height = 325;
    private long pretime; // 前一个时间
    private long curtime; // 当前时间
    // 笔顺采样起点
    private static float midx;
    private static float midy;
    //判断是甲方还是乙方
    private boolean isparta=true;
    //存储笔顺值
    private ArrayList<Integer> list = null;
    // 计数器，用于存点
    private int count = 0;
    // view传到Login
    private Intent intent = null;
    private Context context;
    // 是否为第一次触摸点
    private static boolean is_first_time = true;
    private static boolean flag;
    @SuppressWarnings("deprecation")
    public MyCanvasforPartb(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //获取屏幕的宽高
        MyLoadingActivity signLongDistanceActivity=(MyLoadingActivity)context;
        width=signLongDistanceActivity.getScreenwidth();
        height=signLongDistanceActivity.getScreenheight()*3/11;
        bt = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        canvas1 = new Canvas(bt);
        canvas1.setBitmap(bt); // 设置缓冲区
        // 设置画笔属性
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Style.STROKE); // 设置画笔绘画样式
        paint.setStrokeWidth(4); // 设置画笔宽度
        // 反锯齿
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        path = new Path();
        list=new ArrayList<Integer>();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 当用户写字的时候
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 当用户移动手指时
                path.quadTo(prex, prey, x, y);
                // 递增count
                count++;
           /*     if (count >= 4) {
                    // 当经过4个单位点后，进行取样
                    get_order_result(get_order(midx, midy, x, y));
                    midx = x;
                    midy = y;
                    // 将count值为0
                    count = 0;
                }*/
                prex = x;
                prey = y;

                break;

            case MotionEvent.ACTION_DOWN:
                // 当用户手指点下时
                path.moveTo(x, y);
                prex = x;
                prey = y;
                is_first_time = true;
                break;
            case MotionEvent.ACTION_UP:
                canvas1.drawPath(path, paint);
                path.reset(); // 重置路径
                // 设为第一次
                is_first_time = true;
                break;
        }
        // 通知重绘
        invalidate();
        return true;
    }

    //重绘方法
    public void rewrite(){
        flag=true;
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        // 定义实际的
        Paint bmpaint = new Paint();
        bmpaint.setAntiAlias(true);
        bmpaint.setFilterBitmap(true);
        if(flag){
            bt = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            path=new Path();
            canvas1.setBitmap(bt);
            flag=false;
            invalidate();
        }
        canvas.drawBitmap(bt, 0, 0, bmpaint); // 将缓冲区的东西显示出来
        canvas.drawPath(path, paint);
        Staticbean.partbbitmap=bt;
    }


	/*
	 * 主要用于笔顺的获取
	 * get_order和get_order_result
	 */

    /*
     * name:get_order
     * paramter  prex，prey上一个触摸点x，y坐标  x，y当前触摸点的x，y坐标
     */
    public int get_order(float prex, float prey, float x, float y) {
        // 笔顺标记
        int order1 = 0;
        // 用户写字时直线的斜率
        float f_value = Math.abs((x - prex) / (y - prey));
        if (!is_first_time) {
            // 非第一次输入
            // 第一种情况，沿x轴的右半区
            if ((x - prex > 0) && (f_value > 1)) {
                order1 = 0;
            }
            // 第二种情况，沿x轴的上半区
            else if ((y - prey < 0) && (f_value <= 1)) {
                order1 = 1;
            }
            // 第三种情况，沿x轴的左半区
            else if ((x - prex < 0) && (f_value > 1)) {
                order1 = 2;
            }
            // 第四种情况，沿x轴的上半区
            else if ((y - prey > 0) && (f_value <= 1)) {
                order1 = 3;
            }
        }
        return order1;
    }
    /*
     * 获取最终的笔顺
     * name：get_order_result
     * parameter order_item:每一笔的笔顺值
     */
    public List<Integer> get_order_result(int order_item){
        //当前的序列号
        int curone=0;
        //上一个序列号
        int lastone=1;
        // 用来接收广播
        curone=order_item;
        if ((curone != lastone)) {
            // 前后两次笔顺不一样，或者是第一次接收广播
            list.add(intent.getIntExtra("number", 0));
            System.out.println(intent.getIntExtra("number", 0)+"条件输出");
        }
        System.out.println("\n"+"curone的值" + curone);
        System.out.println("\n"+"lastone的值（上一个curone值）" + lastone);
        lastone = curone;
        return list;
    }

}
