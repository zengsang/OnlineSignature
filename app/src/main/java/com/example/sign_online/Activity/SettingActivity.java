package com.example.sign_online.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sign_online.Baseclass.MyBaseActivity;
import com.example.sign_online.R;
import com.example.sign_online.Baseclass.MyBaseAdapter;
import java.util.ArrayList;
import java.util.List;

/**设置
 * Created by 曾志强 on 2016/1/18.
 */
public class SettingActivity  extends MyBaseActivity implements AdapterView.OnItemClickListener,View.OnClickListener{
    private ListView settinglistview=null;
    //数据
    private List<String> settingdata=new ArrayList<String>();
    //图片数据
    private int[] settingimage={R.mipmap.refactor_usermsg,R.mipmap.usercenter,R.mipmap.suggestion,R.mipmap.forus};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutsetting);
        init();
    }


    /*初始化一些控件、变量
  *init
   */
    public void init() {
        settinglistview=(ListView)findViewById(R.id.settinglistview);
        //添加设置显示项
        settingdata.add("修改信息");
        settingdata.add("退出账号");
        settingdata.add("反馈意见");
        settingdata.add("关于我们");
        settingadapter settingadapter=new settingadapter(SettingActivity.this,settingdata);
        //为listview设置适配器
        settinglistview.setAdapter(settingadapter);
        settinglistview.setOnItemClickListener(this);
        (((ImageButton)((Toolbar)findViewById(R.id.toolbar)).findViewById(R.id.toobarback))).setOnClickListener(this);
    }

    /*当点击listview后触发的方法
    *onitemclick
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                //修改信息
                startActivity(new Intent(SettingActivity.this,RefactorUserMsgActivity.class));
                finish();
                break;
            case 1:
                //退出账户
                Logoff();
                break;
            case 2:
                //反馈意见
                startActivity(new Intent(SettingActivity.this,UserSuggestionActivity.class));
                finish();
                break;
            case 3:
                //关于我们
                startActivity(new Intent(SettingActivity.this,AboutDeveloperActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toobarback:
                //回到主界面
                finish();
                startActivity(new Intent(SettingActivity.this, ProtocelmouldmainActivity.class));
                break;
        }
    }

    //适配器内部类，继承自基础适配器
    class settingadapter extends MyBaseAdapter {
        //数据
        private List<String> data=new ArrayList<String>();
        //context
        private  Context context=null;
        public settingadapter(Context context,List<String> data){
            super(context,data);
            this.data=data;
            this.context=context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=(LayoutInflater.from(context)).inflate(R.layout.layoutsettingsitem,null);
            }
            ImageView imageView=(ImageView)convertView.findViewById(R.id.i_setting_item);
            TextView textView = (TextView)convertView.findViewById(R.id.t_setting_item);
            imageView.setImageResource(settingimage[position]);
            textView.setText(data.get(position));
            return convertView;
        }
    }

    /*退出登录
    *Logoff()
     */
    public void Logoff(){
        //将登录状态改为false
        SharedPreferences sharedPreferences=SettingActivity.this.getSharedPreferences("use_for_login",MODE_PRIVATE);
        System.out.println("___________________---------------------------"+sharedPreferences.getBoolean("loginstatus",false));
        //存储数据
        SharedPreferences.Editor editor=sharedPreferences.edit();
        //存储用户信息
        editor.putBoolean("loginstatus", false);
        editor.commit();
        //回到登录界面
        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
        SettingActivity.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(SettingActivity.this,ProtocelmouldmainActivity.class));
        }
        return true;
    }
}
