package com.example.sign_online.Tools.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sign_online.Activity.MyProtocelsDetileActivity;
import com.example.sign_online.R;

import java.util.ArrayList;

/**
 * Created by 曾志强 on 2016/2/26.
 */
public class MyProtocelsRecycleAdapter  extends RecyclerView.Adapter<MyProtocelsRecycleAdapter.MyViewHolder> {
    private Context context = null;
    private ArrayList<String> data = null;
    private static int position;
    //接口，类似于listview的onitemclicklistener
    private interface onRecycleItemClickListener {
        void onItemClick(String protocelmouldname);
    }
    public MyProtocelsRecycleAdapter(ArrayList<String> data,Context context) {
        this.data = data;
        this.context=context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.layoutmyprotocelsitem, null);
        return new MyViewHolder(view, new onRecycleItemClickListener() {
            @Override
            public void onItemClick(String protocelmouldname) {
                Intent intent=new Intent(context,MyProtocelsDetileActivity.class);
                intent.putExtra("protocelmouldname",protocelmouldname);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.imageView.setImageResource(R.mipmap.mouldicon);
        holder.mouldnametextview.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        //图标
        private ImageView imageView = null;
        //模板名
        private TextView mouldnametextview=null;

        private onRecycleItemClickListener RecycleItemClickListener;

        public MyViewHolder(View view,onRecycleItemClickListener recycleItemClickListener) {
            super(view);
            this.RecycleItemClickListener=recycleItemClickListener;
            imageView = (ImageView) view.findViewById(R.id.protocelmouldimageview);
            mouldnametextview = (TextView) view.findViewById(R.id.protocelmouldtextview);
//            ((CardView)view.findViewById(R.id.cardview)).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(RecycleItemClickListener!=null){
                RecycleItemClickListener.onItemClick(mouldnametextview.getText().toString());
            }

        }

    }
}


