package com.example.sign_online.Tools.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sign_online.R;
import com.example.sign_online.Tools.ViewTools.ImageMotify;

import java.util.List;

/**
 * Created by 曾志强 on 2016/4/21.
 */
public class MyProtocelListViewAdapter extends BaseAdapter{
    //textview数据
    private List<String> data=null;
    //上下文
    private Context context=null;
    //屏幕的宽和高
    private int screenWidth,screenHeight;

    public MyProtocelListViewAdapter(Context context,List<String> data,int screenWidth,int screenHeight){
        this.context=context;
        this.data=data;
        this.screenWidth=screenWidth;
        this.screenHeight=screenHeight;
    }
    //viewholder
    class ViewHolder{
        private ImageView imageView;
        private TextView textView;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.layoutmyprotocelsitem,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.protocelmouldimageview);
            viewHolder.textView= (TextView) convertView.findViewById(R.id.protocelmouldtextview);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(R.mipmap.mouldicon);
        viewHolder.textView.setText(data.get(position));
        return convertView;

    }

}
