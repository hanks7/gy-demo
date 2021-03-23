package com.gysdk.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.gysdk.demo.MyApplication;
import com.gysdk.demo.R;
import com.gysdk.demo.util.ToastUtil;
import com.gysdk.demo.view.NoDoubleClickListener;

/**
 * Created by wang on 17/4/10.
 */

public class VerifyResultActivity extends AppCompatActivity {
    AppCompatButton btnLogout;
    private long firstTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_result);
        btnLogout = (AppCompatButton) findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                MyApplication.getInstance().exit(true);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    ToastUtil.getInstance().showToast("再按一次退出程序");
                    firstTime = secondTime;
                    return true;
                } else {
                    MyApplication.getInstance().exit(false);
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            Log.e("error", Log.getStackTraceString(e));
        }
        return super.onKeyDown(keyCode, event);
    }
}
