package com.example.sign_online.Mywidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by 曾志强 on 2016/2/15.
 */
public class MyScrollView extends ScrollView{
public MyScrollView(Context context){
    super(context);
}
 public   MyScrollView(Context context,AttributeSet attributeSet){
     super(context,attributeSet);
 }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
}
