package com.example.sign_online.Baseclass;

import android.content.Context;
import android.os.Handler;

/**所有handler继承此handler
 * Created by 曾志强 on 2016/1/18.
 */
public class MyBaseHandler extends Handler{
private Context context=null;
    public MyBaseHandler(Context context){
        this.context=context;
    }
}
