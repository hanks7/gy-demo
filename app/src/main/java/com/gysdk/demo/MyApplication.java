package com.gysdk.demo;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.g.gysdk.GYManager;
import com.gysdk.demo.activity.GYMainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xz on 3/21/17.
 */

public class MyApplication extends Application {
    private List<AppCompatActivity> mList = new LinkedList<AppCompatActivity>();
    private static MyApplication instance;
    private static Context myContext;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        myContext = this.getApplicationContext();

        final File mediaFile = new File(getCacheDir().getAbsolutePath()+"media.MP4");
        Log.i("MyApplication","file path = "+mediaFile.getAbsolutePath());
        Log.i("MyApplication","is  exists = "+mediaFile.exists());
        if (!mediaFile.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    InputStream inputStream=null;
                    FileOutputStream fileOutputStream=null;
                    try {
                        inputStream = getAssets().open("media.MP4");
                        fileOutputStream=new FileOutputStream(mediaFile);
                        byte[] bytes=new byte[1024*10];
                        while (inputStream.read(bytes)!=-1){
                            fileOutputStream.write(bytes);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if (inputStream!=null){
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (fileOutputStream!=null){
                            try {
                                fileOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    public static MyApplication getInstance() {
        return instance;
    }

    // add Activity
    public void addActivity(AppCompatActivity activity) {
        try {
            mList.add(activity);
        } catch (Exception e) {
            Log.e("error", Log.getStackTraceString(e));
        }
    }

    // 关闭每一个list内的activity
    public void exit(boolean state) {
        try {
            for (AppCompatActivity activity : mList) {
                if (activity != null) {
                    if (state) {
                        if (!(activity instanceof GYMainActivity)) {
                            activity.finish();
                        }
                    } else {
                        activity.finish();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("error", Log.getStackTraceString(e));
        } finally {
            // System.exit(0);
        }
    }

    // 杀进程
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public static Context getMyApplicationContext() {
        return myContext;
    }
}
