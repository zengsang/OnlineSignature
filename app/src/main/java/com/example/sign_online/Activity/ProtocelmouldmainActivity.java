
package com.example.sign_online.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyLoadingActivity;
import com.example.sign_online.Mywidget.MyCircleImageView;
import com.example.sign_online.Mywidget.MyGridview;
import com.example.sign_online.R;
import com.example.sign_online.Tools.Adapter.MyProtocelsRecycleAdapter;
import com.example.sign_online.Tools.Refresh.PullToRefresh.PullToRefreshLayout;
import com.example.sign_online.Tools.ViewTools.Banner;
import com.example.sign_online.Tools.DataCash.Fileoperation;
import com.example.sign_online.Tools.Adapter.GridviewAdapter;
import com.example.sign_online.Tools.DataCash.Staticbean;

import java.util.ArrayList;

/**总主界面
 * Created by 曾志强 on 2016/1/18.
 */
public class ProtocelmouldmainActivity extends MyLoadingActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,AdapterView.OnItemClickListener,PullToRefreshLayout.OnRefreshListener {
    //广告滑动
    private Banner banner = null;
    private DrawerLayout drawer = null;
    private View view = null;
    //文件操作对象
    private static Fileoperation fileoperation = null;
    //用户头像
    private MyCircleImageView myCircleImageView = null;
    //Gridview
    private  MyGridview gridView = null;
    //用户名
    private static String userEmail=null;
    //所有的协议名称
    private ArrayList<String> listMoudelName=null;
    //gridview的适配器
    private GridviewAdapter gridviewAdapter=null;
    //屏幕的高度
    private int screenHeight,screenWidth;
    //加载成功
    private final static int SELECT_PROTOCELMOUDEL_FAILD=0;
    //加载失败
    private final static int SELECT_PROTOCELMOUDEL_SECCESS=1;
    private ActionBarDrawerToggle toggle=null;
    private NavigationView navigationView=null;
    private MyBaseHandler myBaseHandler=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取手机屏幕高度
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight=displayMetrics.heightPixels;
        screenWidth=displayMetrics.widthPixels;
        view = LayoutInflater.from(ProtocelmouldmainActivity.this).inflate(R.layout.activity_main, null);
        setContentView(view);
        initView();
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(banner!=null){
            banner.start();
        }
    }

    /*初始化view
            *initView
             */
    public void initView(){
        gridView=(MyGridview)findViewById(R.id.maingridview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        userEmail=ProtocelmouldmainActivity.this.getSharedPreferences("use_for_login", MODE_PRIVATE).getString("username1",null);
        fileoperation = new Fileoperation(ProtocelmouldmainActivity.this, null);
        //广告图片轮转
        banner = new Banner(ProtocelmouldmainActivity.this, view);
         //头像按钮
        myCircleImageView = (MyCircleImageView) findViewById(R.id.maiinusericon);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        listMoudelName=new ArrayList<String>();
    }
    /*初始化一些控件、变量
  *initData
   */
    public void initData() {
        //从配置信息中获取用户名
        ((PullToRefreshLayout)findViewById(R.id.refresh_view)).setOnRefreshListener(new ProtocelmouldmainActivity());
        banner.start();
        drawer.setDrawerListener(toggle);
        //加载用户头像
        //myCircleImageView.setImageBitmap(Staticbean.UserMsg.getUsericon());
        //加载用户头像
        Bitmap usericon = fileoperation.GetUserIcon(userEmail);
        if (usericon != null) {
            myCircleImageView.setImageBitmap(usericon);
        }
        myCircleImageView.setOnClickListener(this);
        //侧滑部分用户的头像
        if (usericon != null) {
            myCircleImageView = null;
            myCircleImageView = (MyCircleImageView) ((navigationView.getHeaderView(0)).findViewById(R.id.sliding_usericon));
            myCircleImageView.setImageBitmap(usericon);
        }
        navigationView.setNavigationItemSelectedListener(this);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.sliding_username)).setText(userEmail);
        //gridview点击事件，主要用于添加新的协议
        gridView.setOnItemClickListener(this);
        //不能获取焦点
        gridView.setFocusable(false);
        //获取协议模板
        listMoudelName= fileoperation.GetUserProtocelMouldName(userEmail);
        if(listMoudelName!=null&&listMoudelName.size()!=0) {
            gridviewAdapter = new GridviewAdapter(listMoudelName, ProtocelmouldmainActivity.this,screenWidth,screenHeight);
            gridView.setAdapter(gridviewAdapter);
        }
        //点击添加用户协议模板
        ((ImageButton) findViewById(R.id.addprotocelmouldbutton)).setOnClickListener(this);
    }




    /*获取协议模板名字数据并且显示（主要用于上拉加载）
*refreshProtocelMoudelForFooter
* @param userEmail
* @param void
 */
    public void refreshProtocelMoudelForFooter(String userEmail){
        if(listMoudelName!=null&&listMoudelName.size()!=0) {
//            gridviewAdapter = new GridviewAdapter(listMoudelName, ProtocelmouldmainActivity.this);
            gridView.setAdapter(gridviewAdapter);
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (banner != null) {
            banner.Stop();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_addprocel) {
            // 添加用户协议

        } else if (id == R.id.nav_myprocetol) {
            //结束当前界面
            finish();
            startActivity(new Intent(ProtocelmouldmainActivity.this,MyProtocelsActivity.class));
        }
        else if (id == R.id.nav_setting) {
            //结束当前界面
            finish();
            startActivity(new Intent(ProtocelmouldmainActivity.this, SettingActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maiinusericon:
                drawer.openDrawer(Gravity.LEFT);
                break;
            case R.id.addprotocelmouldbutton:
                ProtocelmouldmainActivity.this.finish();
                startActivity(new Intent(ProtocelmouldmainActivity.this, NewprotocelmouldActivity.class));
                break;
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String mouldname=((TextView) gridView.getChildAt(position).findViewById(R.id.tv_item)).getText().toString();
        //新建协议
        Intent intent=new Intent(ProtocelmouldmainActivity.this,NewProtocelActivity.class);
        Staticbean.protocelmouldname=mouldname;
        ProtocelmouldmainActivity.this.finish();
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }
        return true;
    }


    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout ){

        myBaseHandler=new MyBaseHandler(ProtocelmouldmainActivity.this){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==SELECT_PROTOCELMOUDEL_SECCESS){
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED,R.id.maingridview,gridView,null,new GridviewAdapter(listMoudelName,ProtocelmouldmainActivity.this,screenWidth,screenHeight));
                }
                else if(msg.what==SELECT_PROTOCELMOUDEL_FAILD){
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL,0,null,null,null);
                }
            }
        };
        fileoperation=new Fileoperation(ProtocelmouldmainActivity.this,myBaseHandler);
        listMoudelName= fileoperation.GetUserProtocelMouldName(userEmail);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED,0,null,null,null);
    }
}
