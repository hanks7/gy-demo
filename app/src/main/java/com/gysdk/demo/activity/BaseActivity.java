package com.gysdk.demo.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gysdk.demo.R;
import com.gysdk.demo.dialog.ErrorDialog;
import com.gysdk.demo.util.ToastUtil;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

/**
 * Created by wang on 17/4/11.
 */

public class BaseActivity extends AppCompatActivity {
    private FrameLayout frameLayout = null;
    public Toolbar toolbar;
    AppCompatActivity activity;

    protected LoadingDialog dialog;
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        frameLayout = (FrameLayout) findViewById(R.id.layout_content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context = this;
        dialog = new com.xiasuhuei321.loadingdialog.view.LoadingDialog(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        frameLayout.removeAllViews();
        View.inflate(this, layoutResID, frameLayout);
        onContentChanged();
    }

    @Override
    public void setContentView(View view) {
        frameLayout.removeAllViews();
        frameLayout.addView(view);
        onContentChanged();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        frameLayout.removeAllViews();
        frameLayout.addView(view, params);
        onContentChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    public void showLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null) {
                    dialog = new com.xiasuhuei321.loadingdialog.view.LoadingDialog(context);
                }
                dialog.show();
            }
        });
    }

    public void hideLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    dialog.close();
                    dialog = null;
                }
            }
        });
    }

    public void showFailDialog(final int code, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoadingDialog();
                if (TextUtils.isEmpty(msg)) {
                    ErrorDialog.getInstance(activity).show(String.valueOf(code));
                } else {
                    ErrorDialog.getInstance(activity).show(msg);
                }
            }
        });
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.getInstance().showToast(msg);
            }
        });
    }

}
