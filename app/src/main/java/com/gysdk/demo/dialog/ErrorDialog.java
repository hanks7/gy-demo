package com.gysdk.demo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.gysdk.demo.R;

/**
 * Created by wang on 17/4/13.
 */

public class ErrorDialog extends AppCompatDialog {

    private AppCompatTextView tvError;
    private AppCompatButton btnConfirm;
    private AppCompatActivity activity;
    private static ErrorDialog instance = null;

    String mErr;

    private ErrorDialog(Context context) {
        super(context, R.style.errorDialogStyle);
        this.activity = ((AppCompatActivity) context);

    }

    public static ErrorDialog getInstance(Context context) {
        instance = new ErrorDialog(context);
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_error);
        tvError = (AppCompatTextView) findViewById(R.id.tv_error);
        btnConfirm = (AppCompatButton) findViewById(R.id.btn_confirm);
        tvError.setText(mErr);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    public void show(String error) {
        try {
            if (activity != null && !activity.isFinishing()) {
                this.mErr = error;
                instance.show();
            }
        } catch (Exception e) {
            Log.e("error", Log.getStackTraceString(e));
        }
    }
}
