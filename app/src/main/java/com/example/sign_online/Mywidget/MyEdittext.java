package com.example.sign_online.Mywidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by 曾志强 on 2016/2/15.
 */
public class MyEdittext extends EditText{
    public  MyEdittext(Context context){
        super(context);
    }
    public MyEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyEdittext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
