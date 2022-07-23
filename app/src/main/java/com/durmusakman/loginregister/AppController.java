package com.durmusakman.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

public class AppController extends Application {


    public static  final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestqueue;
    private static AppController mInstance;

    @Override
    public  void onCreate(){
        super.onCreate();
        mInstance=this;
    }

    public static  synchronized AppController getInstance(){
        return mInstance;
    }


    public RequestQueue getmRequestQueue() {
        if (mRequestqueue == null) {
            mRequestqueue = Volley.newRequestQueue(getApplicationContext());}


            return mRequestqueue;
    }


    public <T> void addToRequestQueue(Request<T> req,String tag){
        req.setTag(TextUtils.isEmpty(tag) ? TAG:tag);
        getmRequestQueue().add(req);

    }



    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getmRequestQueue().add(req);

    }

    public <T> void cancelPendingReque(Object tag ){
        if(mRequestqueue!=null){
            mRequestqueue.cancelAll(tag);}
        }
    }

