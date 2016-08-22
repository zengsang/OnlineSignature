package com.example.sign_online.Tools.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.sign_online.Activity.MyProtocelsDetileActivity;
import com.example.sign_online.R;

import java.util.ArrayList;

/**
 * Created by 曾志强 on 2016/4/6.
 */
  /*用于适配协议图片的galllery
*galleryadpter
*
 */
public class GalleryImageAdapter extends BaseAdapter implements View.OnClickListener{
    private Context context;
    //协议图片
    private ArrayList<Bitmap> protocels=null;
    //item
    private int width,height;

    @Override
    public void onClick(View v) {

    }

    //回调接口
    public interface OnImageItemClickListener{
        public void onImageItemClick();
    }

    public GalleryImageAdapter(Context context,ArrayList<Bitmap> protocels,int width,int height){
        this.context=context;
        this.protocels=protocels;
        this.width=width;
        this.height=height;
    }
    //回调接口
    interface OnGalleryImageItemClickListener{
        public void onGalleryImageItemClick(Bitmap imageItem);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return protocels.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return protocels.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    class MyViewHolder{
        private ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      MyViewHolder myViewHolder=null;
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.layoutgalleryviewitem,null);
            myViewHolder=new MyViewHolder();
            myViewHolder.imageView= (ImageView) convertView.findViewById(R.id.galleryimageview);
            Gallery.LayoutParams layoutParams=new Gallery.LayoutParams(width,height);
            myViewHolder.imageView.setLayoutParams(layoutParams);
            convertView.setTag(myViewHolder);
           // imageView.setOnClickListener(this);
        } else {
             myViewHolder= (MyViewHolder) convertView.getTag();
        }
        myViewHolder.imageView.setImageBitmap(protocels.get(position));
        return convertView;
    }


}
