package com.example.sign_online.Tools.Refresh.RefreshView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by 曾志强 on 2016/4/15.
 */
public class PullableRecycleView extends RecyclerView implements Pullable{
    public PullableRecycleView(Context context)
    {
        super(context);
    }

    public PullableRecycleView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PullableRecycleView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }




    @Override
    public boolean canPullDown()
    {
        if (getScrollY() == 0)
        {
            // 没有item的时候也可以下拉刷新
            return true;
         } else
        {
            return false;
        }
    }



    @Override
    public boolean canPullUp()
    {
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
           	return true;
        		else
        			return false;
    }
}
