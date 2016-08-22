package com.example.sign_online.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.sign_online.Baseclass.MyLoadingActivity;
import com.example.sign_online.R;

/**
 * Created by 曾志强 on 2016/2/21.
 */
public class MyProtocelInMouldNameActivity extends MyLoadingActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadSubview(LayoutInflater.from(MyProtocelInMouldNameActivity.this).inflate(R.layout.layoutmyprotocelinmouldname,null));
    }
}
