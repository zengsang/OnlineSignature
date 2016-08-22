package com.example.sign_online.Baseclass;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sign_online.R;

/**表示具有加载动画的Activity，凡是具有加载动画的activity均继承与这个activity
 * Created by 曾志强 on 2016/2/5.
 */
public class MyLoadingActivity extends MyBaseActivity{
    //整体的布局
    private FrameLayout mainlayout=null;
    //固有的显示进度的view
    private View subviewtop=null;
    //设置个性化进度
    ProgressBar progressBar=null;
    //屏幕的宽度和高度
    private int screenwidth,screenheight;

    public int getScreenwidth() {
        return screenwidth;
    }

    public void setScreenwidth(int screenwidth) {
        this.screenwidth = screenwidth;
    }

    public int getScreenheight() {
        return screenheight;
    }

    public void setScreenheight(int screenheight) {
        this.screenheight = screenheight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainlayout=new FrameLayout(this);

        Display display=((WindowManager) MyLoadingActivity.this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        display.getMetrics(displayMetrics);
        screenwidth=displayMetrics.widthPixels;
        screenheight=displayMetrics.heightPixels;
        setContentView(mainlayout);
        //加载主view
        subviewtop=(LayoutInflater.from(this)).inflate(R.layout.layoutloadresource, null);
        progressBar=(ProgressBar)subviewtop.findViewById(R.id.showprogress);
    }

    /*加入子activity的特有view（除了表层的那个textview进度）
    *LoadSubview
    * @param subviewbuttom 底层的view
    * @param String log 顶层view 显示的文字
     */
    public void LoadSubview(View subviewbuttom){
        //加入子view，主要是每个activity不同的部分
        mainlayout.addView(subviewbuttom);
    }

    /*加入子activity的特有view（除了表层的那个textview进度）
  *LoadSubview
  * @param subviewbuttom 底层的view
  * @param String log 顶层view 显示的文字
   */
    public void LoadSubview(View subviewbuttom,FrameLayout.LayoutParams layoutParams){
        //加入子view，主要是每个activity不同的部分
        mainlayout.addView(subviewbuttom,layoutParams);
    }

    /*开始显示进度信息
    *StartLoad
    * @param String log
     */
    public void StartLoad(){
        mainlayout.removeView(progressBar);
        mainlayout.removeView(subviewtop);
    //    mainlayout.removeAllViews();
        mainlayout.addView(subviewtop);
    }

    /*停止显示进度信息
    *StopLoading
     */
    public void StopLoad(){
        progressBar.setVisibility(View.INVISIBLE);
        //((LinearLayout)findViewById(R.id.linear)).setVisibility(View.INVISIBLE);
    }

}
