package com.example.sign_online.Tools.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sign_online.R;

import java.util.ArrayList;

/**
 * Created by 曾志强 on 2016/2/27.
 */
public class MyProtocelDetileRecycleAdapter extends RecyclerView.Adapter<MyProtocelDetileRecycleAdapter.MyViewHolder>{
    private  ArrayList<String> data=null;
    private ArrayList<Bitmap> bitmaps=null;
    private Context context=null;
    private MyProtocelDetileRecycleListener myProtocelDetileRecycleListener=null;
    //协议图片显示的宽度
    private static int width;
    //协议图片显示的高度
    private static int height;
    //回调接口
    public interface  MyProtocelDetileRecycleListener{
        public void myprotocelDetileRecycleItemClick(Bitmap bitmap,ImageView imageView);
    }
    public MyProtocelDetileRecycleAdapter(Context context,ArrayList<String> data,ArrayList<Bitmap> bitmaps,MyProtocelDetileRecycleListener myProtocelDetileRecycleListener,int width,int height){
        this.context=context;
        this.data=data;
        this.bitmaps=bitmaps;
        this.width=width;
        this.height=height;
        this.myProtocelDetileRecycleListener=myProtocelDetileRecycleListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layoutmyprotoceldetileitem, null),myProtocelDetileRecycleListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        /*SetProtocelBitmapWidthAndHeight(bitmaps.get(position))*/
        holder.imageView.setImageBitmap(bitmaps.get(position));
        holder.nametextview.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //图标
        private ImageView imageView=null;
        //回调接口对象
        MyProtocelDetileRecycleListener myProtocelDetileRecycleListener=null;
        //模板名
        private TextView nametextview=null;
        public MyViewHolder(View view,MyProtocelDetileRecycleListener myProtocelDetileRecycleListener){
            super(view);
            this.myProtocelDetileRecycleListener=myProtocelDetileRecycleListener;
            imageView=(ImageView)view.findViewById(R.id.protocelimageview);
            nametextview=(TextView)view.findViewById(R.id.protocelnametextview);
            //协议名字要缩小为指定大小
           // nametextview.setWidth(width);
            //nametextview.setHeight(height);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myProtocelDetileRecycleListener.myprotocelDetileRecycleItemClick(bitmaps.get(getPosition()),imageView);
        }
    }

    /*将给定的图片设置一定的高度和宽度
      *SetProtocelBitmapWidthAndHeight
     *@Bitmap protocelbitmap
     * @return Bitmap
     */
    public Bitmap SetProtocelBitmapWidthAndHeight(Bitmap protocelbitmap){
               Bitmap NewBitmap = Bitmap.createBitmap(protocelbitmap, width / 2, height / 4, width, height);
               return NewBitmap;
           }

}

