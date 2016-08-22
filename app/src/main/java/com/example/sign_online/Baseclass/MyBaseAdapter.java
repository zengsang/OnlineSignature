package com.example.sign_online.Baseclass;

/**所有关于adapter继承自此adapter
 * Created by 曾志强 on 2016/1/18.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 曾志强 on 2015/12/26.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter{
    private Context context=null;
    //数据原
    private List<T> data=null;
    public MyBaseAdapter(){}

    public MyBaseAdapter(Context context, List<T> data){
        this.context=context;
        this.data=data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
}
