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
import com.example.sign_online.Tools.ViewTools.GridViewHolder;
import com.example.sign_online.Tools.ViewTools.ImageMotify;

import java.util.List;

/**
 * Created by 曾志强 on 2016/2/12.
 */
public class GridviewAdapter extends BaseAdapter {
    //协议模板标题
    private List<String> titles=null;
    //上下文
    private Context context=null;
    private Bitmap bitmap=null;
//    private ImageMotify imageMotify=null;
    //用户点击的位置
    private int selectposition;
    //屏幕的宽和高
//    private int screenWidth,screenHeight;
    public GridviewAdapter(List<String> titles,Context context,int screenWidth,int screenHeight){
        this.titles=titles;
        this.context=context;
//        this.screenWidth=screenWidth;
//        this.screenHeight=screenHeight;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelectposition(int selectposition) {
        this.selectposition = selectposition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewHolder gridViewHolder=new GridViewHolder();
        if(convertView==null){
            convertView=(LayoutInflater.from(context)).inflate(R.layout.layoutgridviewitem,parent,false);
            gridViewHolder.textView=(TextView)convertView.findViewById(R.id.tv_item);
            gridViewHolder.imageView= (ImageView) convertView.findViewById(R.id.iv_item);
            convertView.setTag(gridViewHolder);
        }
        else{
            gridViewHolder=(GridViewHolder)convertView.getTag();
        }
//        bitmap= BitmapFactory.decodeResource(context.getResources(),R.mipmap.mainbitmap);
//        if(imageMotify==null){
//          imageMotify=new ImageMotify(context,bitmap,screenWidth,screenHeight);
//        }
//        bitmap=imageMotify.motifyImageSize(0.5f,0.1f);
//        gridViewHolder.imageView.setImageBitmap(bitmap);
        gridViewHolder.textView.setText(titles.get(position));
        return convertView;
    }
}
