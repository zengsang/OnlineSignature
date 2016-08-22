package com.example.sign_online.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyLoadingActivity;
import com.example.sign_online.R;
import com.example.sign_online.Tools.Adapter.MyProtocelListViewAdapter;
import com.example.sign_online.Tools.DataCash.Fileoperation;
import com.example.sign_online.Tools.Adapter.MyProtocelsRecycleAdapter;
import com.example.sign_online.Tools.Refresh.PullToRefresh.PullToRefreshLayout;
import com.example.sign_online.Tools.Refresh.RefreshView.PullableListView;
import com.example.sign_online.Tools.Refresh.RefreshView.PullableRecycleView;

import java.util.ArrayList;

/**
 * Created by 曾志强 on 2016/2/17.
 */
public class MyProtocelsActivity extends MyLoadingActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener {
    //显示所有protocelmould
    private PullableListView listView = null;
    //所有mouldname
    private ArrayList<String> allmouldname = null;
    //加载成功
    private final static int SELECT_PROTOCELMOUDEL_FAILD = 0;
    //加载失败
    private final static int SELECT_PROTOCELMOUDEL_SECCESS = 1;
    private MyBaseHandler myBaseHandler =null;
    private  Intent intent=null;
    //用户名
    private static String userEmail=null;
    //屏幕宽高
    private int screenWidth,screenHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth=displayMetrics.widthPixels;
        screenHeight=displayMetrics.heightPixels;
        LoadSubview(LayoutInflater.from(MyProtocelsActivity.this).inflate(R.layout.layoutmyprotocel, null));
        init();
    }

    /*初始化一些控件、变量
*init
 */
    public void init() {
        ((ImageButton)findViewById(R.id.toobarback)).setOnClickListener(this);
        listView = (PullableListView) findViewById(R.id.listprotocelmould);
        allmouldname = (new Fileoperation(MyProtocelsActivity.this, null)).GetUserProtocelMouldName((MyProtocelsActivity.this.getSharedPreferences("use_for_login", MODE_PRIVATE).getString("username1", null)));
        //绑定recycleview
        listView.setAdapter(new MyProtocelListViewAdapter( MyProtocelsActivity.this,allmouldname,screenWidth,screenHeight));
        listView.setOnItemClickListener(this);
        ((PullToRefreshLayout)findViewById(R.id.pulltorefreshlayout)).setOnRefreshListener(new MyProtocelsActivity());
        userEmail=MyProtocelsActivity.this.getSharedPreferences("use_for_login",MODE_PRIVATE).getString("username1",null);
        //设置加载效果
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toobarback:
                MyProtocelsActivity.this.finish();
                intent = new Intent(MyProtocelsActivity.this, ProtocelmouldmainActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            intent=(new Intent(MyProtocelsActivity.this, ProtocelmouldmainActivity.class));
            startActivity(intent);
        }
        return true;
    }


    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

        //     pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        myBaseHandler=new MyBaseHandler(MyProtocelsActivity.this){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==SELECT_PROTOCELMOUDEL_SECCESS){
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED, R.id.listprotocelmould, null, listView, new MyProtocelListViewAdapter( MyProtocelsActivity.this,allmouldname,screenWidth,screenHeight));
                }
                else if(msg.what==SELECT_PROTOCELMOUDEL_FAILD){
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL,0,null,null,null);
                }
            }
        };
        allmouldname=(new Fileoperation(MyProtocelsActivity.this,myBaseHandler)).GetUserProtocelMouldName(userEmail);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        //       pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String protocelmouldname=((TextView)(listView.getChildAt(position-listView.getFirstVisiblePosition()).findViewById(R.id.protocelmouldtextview))).getText().toString();
        Intent intent=new Intent(MyProtocelsActivity.this,MyProtocelsDetileActivity.class);
        intent.putExtra("protocelmouldname",protocelmouldname);
        startActivity(intent);
    }
}

