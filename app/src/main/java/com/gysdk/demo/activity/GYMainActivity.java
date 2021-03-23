package com.gysdk.demo.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.g.gysdk.CheckBuilder;
import com.g.gysdk.GYManager;
import com.g.gysdk.GYResponse;
import com.g.gysdk.GyCallBack;
import com.g.gysdk.PicCallBack;
import com.gysdk.demo.R;
import com.gysdk.demo.util.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Time：2020-07-30 on 16:00.
 * Decription:.
 * Author:jimlee.
 */
public class GYMainActivity extends BaseActivity {
    private static final int CODE_REQUEST_PERMISSION = 0x10;
    private static final String BUSINESS_ID = "20180620a";

    @BindView(R.id.tv_version)
    TextView tvVersion;

    private final String TAG = getClass().getSimpleName();
    ArrayList<String> lists = new ArrayList<String>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gy_main);
        ButterKnife.bind(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            lists.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            lists.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            lists.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (lists != null && !lists.isEmpty()) {
            ActivityCompat.requestPermissions(this, (String[]) lists.toArray(new String[0]), 0);
        }

        if (lists != null && !lists.isEmpty()) {
            ActivityCompat.requestPermissions(this, (String[]) lists.toArray(new String[0]), CODE_REQUEST_PERMISSION);
        } else {
            GYManager.getInstance().setDebug(true);
            GYManager.getInstance().init(this.getApplicationContext());
        }

        tvVersion.setText(String.format("版本：%s", GYManager.getInstance().getVersion()));
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephonyManager.getSimState();
        Log.i("TAG", "simstate = " + simState);
    }


    @OnClick({R.id.fr_elogin, R.id.fr_protect, R.id.fr_pic_verify, R.id.fr_verify})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.fr_elogin:
                startActivity(new Intent(this, ChooseEloginActivity.class));
                break;
            case R.id.fr_protect:
                checkRegister();
                break;
            case R.id.fr_pic_verify:
                picVerify();
                break;
            case R.id.fr_verify:
                startActivity(new Intent(this, PhoneNumberVerifyActivity.class));
                break;

        }

    }


    private void checkRegister() {
        String gtAppId = "";
        try {
            gtAppId = getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData.getString("GETUI_APPID");
        } catch (Throwable ignore) {
        }
        final CheckBuilder.Builder builder = new CheckBuilder.Builder(gtAppId);
        builder.setAccount("account");
        showLoadingDialog();
        GYManager.getInstance().checkRegister(builder.build(), new GyCallBack() {
            @Override
            public void onSuccess(final GYResponse gyResponse) {
                tvVersion.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "onSuccess:" + gyResponse);
                        try {
                            JSONObject jsonObject = new JSONObject(gyResponse.getMsg());
                            //风险等级
                            final String level = jsonObject.getString("level");
                            Log.d(TAG, "level:" + level);
                            String riskTypes = jsonObject.getString("riskType");
                            Log.d(TAG, "riskTypes:" + riskTypes);
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(GYMainActivity.this);
                            builder1.setNegativeButton("确定", null);
                            builder1.setMessage("验证成功，当前风险等级：" + level).create().show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        hideLoadingDialog();
                    }
                });
            }

            @Override
            public void onFailed(final GYResponse gyResponse) {
                tvVersion.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.getInstance().showToast("验证失败:" + gyResponse);
                        Log.d("MainActivity", "onFailed:" + gyResponse);
                        hideLoadingDialog();
                    }
                });
            }
        });
    }

    private void picVerify() {
        GYManager.getInstance().picVerify(this, BUSINESS_ID, true, new PicCallBack() {
            @Override
            public void onPicReady(GYResponse gyResponse) {
                Log.d(TAG, "动画验证码准备完成，即将展示:" + gyResponse);
            }

            @Override
            public void onSuccess(final GYResponse gyResponse) {
                Log.d(TAG, "验证成功:" + gyResponse);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (GYManager.MSG.PIC_VERIFY_SUCCESS == gyResponse.getCode()) {
                            showToast("动画验证码验证成功");
                        } else if (GYManager.MSG.NONSENSE_VERIFY_SUCCESS == gyResponse.getCode()) {
                            showToast("智能无感验证成功");
                        }
                    }
                });
            }

            @Override
            public void onFailed(final GYResponse gyResponse) {
                Log.d(TAG, "验证失败:" + gyResponse);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (GYManager.MSG.PIC_VERIFY_CANCEL == gyResponse.getCode()) {
                            showToast("动画验证码取消");
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_REQUEST_PERMISSION) {
            GYManager.getInstance().setDebug(true);
            GYManager.getInstance().init(this.getApplicationContext());
        }

    }
}
