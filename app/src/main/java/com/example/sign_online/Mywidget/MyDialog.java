package com.example.sign_online.Mywidget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by 曾志强 on 2016/4/18.
 */
public class MyDialog extends Dialog{
    public MyDialog(Context context) {
        super(context);
    }

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


   /*构造特定的dialog
   *createDialog
   * @param Context context 上下文
   * @param int dialogStyle 对话框的风格
   * @param View dialogView 对应view
   * @param Boolean isCancleable 点击其他地方对话框是否消失
   * @parma int width 对话框的宽度
   * @param int height 对话框的高度
   * @param int animation 动画style
    */
    public static MyDialog createDialog(Context context,View dialogView,int dialogStyle,Boolean isCancleable,int width,int height,int animation){
        MyDialog myDialog=new MyDialog(context,dialogStyle);
        myDialog.setCancelable(isCancleable);
        myDialog.setContentView(dialogView);
        Window window=myDialog.getWindow();
        //对话框窗口大小布局对象
        WindowManager.LayoutParams layoutParams=window.getAttributes();
        layoutParams.width=width;
        layoutParams.height=height;
        window.setWindowAnimations(animation);
        //通过style传递动画对象
        window.setAttributes(layoutParams);
        return myDialog;
    }

}
