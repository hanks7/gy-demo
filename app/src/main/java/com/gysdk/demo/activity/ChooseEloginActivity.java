package com.gysdk.demo.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmic.sso.sdk.AuthRegisterViewConfig;
import com.cmic.sso.sdk.utils.rglistener.CustomInterface;
import com.g.gysdk.GYManager;
import com.g.gysdk.GYResponse;
import com.g.gysdk.GyCallBack;
import com.g.gysdk.cta.AuthPageListener;
import com.g.gysdk.cta.ELoginThemeConfig;
import com.gysdk.demo.R;
import com.gysdk.demo.util.ConfigUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.widget.LinearLayout.HORIZONTAL;
import static com.xiasuhuei321.loadingdialog.view.SizeUtils.dip2px;

/**
 * Time：2020-07-31 on 09:50.
 * Decription:.
 * Author:jimlee.
 */
public class ChooseEloginActivity extends BaseActivity implements AuthPageListener {

    private final String TAG = this.getClass().getSimpleName();
    private boolean isLandscape;
    private Activity authActivity;
    private String phoneNum;
    private final int REQUEST_SETTING_CODE = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_elogin);
        toolbar.setVisibility(View.GONE);

        Toolbar toolBar = (Toolbar) findViewById(R.id.tools_bar);
        toolBar.setTitle("Elogin");
        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //提前进行预登录 这样可以提高一键登录的速度
        GYManager.getInstance().ePreLogin(3000, new GyCallBack() {
            @Override
            public void onSuccess(GYResponse gyResponse) {
                Log.d(TAG, "提前预登录成功:" + gyResponse);
                setPhoneNum(gyResponse);
            }

            @Override
            public void onFailed(GYResponse gyResponse) {
                Log.d(TAG, "提前预登录失败:" + gyResponse);
            }
        });

        ButterKnife.bind(this);
        GYManager.getInstance().setAuthPageListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private ELoginThemeConfig config = null;

    @OnClick({R.id.ll_full_screen, R.id.ll_landscape, R.id.ll_portrait, R.id.ll_dialog})
    public void onClicked(View view) {
        isLandscape = false;
        switch (view.getId()) {
            case R.id.ll_full_screen:
                setThirdPartLoginBtn(420);
                //设置自定义view
                setCustomConfig(270);
                config = ConfigUtils.getFullScreenConfig(this);
                break;
            case R.id.ll_landscape:
                isLandscape = true;
                setThirdPartLoginBtn(235);
                //设置自定义view
                setCustomConfig(160);
                config = ConfigUtils.getLandscapeConfig(this);
                break;
            case R.id.ll_portrait:
                setThirdPartLoginBtn(350);
                setCustomConfig(270);
                config =  ConfigUtils.getPortraitConfig(this);
                break;
            case R.id.ll_dialog:
                setThirdPartLoginBtn(350);
                setCustomConfig(270);
                config = ConfigUtils.getDialogConfig(this);
                break;
            default:
                config = null;
                break;

        }

        if (config != null) {
            if (GYManager.getInstance().isPreLoginResultValid()) {
                if (GYManager.getInstance().getSimCount(this)>1){
                    showSimDialog(phoneNum);
                }else {
                    eLogin(config);
                }
            } else {
                GYManager.getInstance().ePreLogin(5000, new GyCallBack() {
                    @Override
                    public void onSuccess(GYResponse response) {
                        if (GYManager.getInstance().getSimCount(ChooseEloginActivity.this) > 1) {
                            setPhoneNum(response);
                            showSimDialog(phoneNum);
                        } else {
                            eLogin(config);
                        }
                    }

                    //预登录失败 请切换到其他验证方式
                    @Override
                    public void onFailed(GYResponse response) {
                        showToast("预登录失败:" + response);
                        hideLoadingDialog();
                    }
                });
            }
        }

    }


    private void showSimDialog(String phoneNum) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.sim_dialog_title)
                .setMessage(String.format(getResources().getString(R.string.sim_dialog_content), phoneNum))
                .setNegativeButton(R.string.sim_dialog_setting_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS), REQUEST_SETTING_CODE);
                    }
                })
                .setPositiveButton(R.string.sim_dialog_entry_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eLogin(config);
                    }
                })
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        hideLoadingDialog();
                    }
                })
                .show();
    }


    /**
     * 预登录后，保存脱敏的手机号
     *
     * @param response
     */
    private void setPhoneNum(GYResponse response) {
        try {
            JSONObject jsonObject = new JSONObject(response.getMsg());
            phoneNum = jsonObject.getString("number");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void eLogin(ELoginThemeConfig config) {
        if (config == null) {
            return;
        }
        GYManager.getInstance().eAccountLogin(config, new GyCallBack() {
            @Override
            public void onSuccess(GYResponse response) {
                hideLoadingDialog();
                Log.d(TAG, "response:" + response);
                showToast("登录成功");
                //关闭一键登录界面
                GYManager.getInstance().finishAuthActivity();

                try {
                    JSONObject jsonObject = new JSONObject(response.getMsg());
                    JSONObject data = jsonObject.getJSONObject("data");
                    String token = data.getString("token");
                    long expiredTime = data.getLong("expiredTime");
                    Log.d(TAG, "token:" + token + "  expiredTime:" + expiredTime);
                    //将token汇报服务端进行取号
                    // ....由开发者自行实现
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(GYResponse response) {
                hideLoadingDialog();
                Log.d(TAG, "response:" + response);
                showToast("一键登录失败:" + response);
                //关闭一键登录界面
                GYManager.getInstance().finishAuthActivity();
            }
        });
    }


    private void setThirdPartLoginBtn(int marginTop) {

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authActivity != null) {
                    authActivity.finish();
                }
            }
        };

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dip2px(this, 45), dip2px(this, 45));
        layoutParams.weight = 1;
        ImageView qqImageView = new ImageView(this);
        qqImageView.setImageResource(R.mipmap.qq);
        qqImageView.setLayoutParams(layoutParams);

        qqImageView.setOnClickListener(onClickListener);


        ImageView wechatImg = new ImageView(this);
        wechatImg.setImageResource(R.mipmap.wechat);
        wechatImg.setLayoutParams(layoutParams);

        wechatImg.setOnClickListener(onClickListener);

        ImageView weiboImg = new ImageView(this);
        weiboImg.setImageResource(R.mipmap.weibo);
        weiboImg.setLayoutParams(layoutParams);
        weiboImg.setOnClickListener(onClickListener);

        LinearLayout thirdLogin = new LinearLayout(this);
        thirdLogin.setOrientation(HORIZONTAL);
        thirdLogin.addView(qqImageView);
        thirdLogin.addView(wechatImg);
        thirdLogin.addView(weiboImg);

        RelativeLayout.LayoutParams thirdLayoutParams = new RelativeLayout.LayoutParams(dip2px(this, 250), RelativeLayout.LayoutParams.WRAP_CONTENT);
        thirdLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        thirdLayoutParams.setMargins(0, dip2px(context, marginTop), 0, 0);
        thirdLogin.setLayoutParams(thirdLayoutParams);

        GYManager.getInstance().addRegisterViewConfig("third_button", new AuthRegisterViewConfig.Builder()
                .setView(thirdLogin)
                .setRootViewId(AuthRegisterViewConfig.RootViewId.ROOT_VIEW_ID_BODY)
                .build()
        );
    }


    /**
     * 添加自定义view.
     */
    private void setCustomConfig(int marginTop) {

        Button mBtn = new Button(context);
        mBtn.setText("其他方式登录");
        mBtn.setTextColor(Color.parseColor("#3973FF"));
        mBtn.setBackgroundColor(Color.TRANSPARENT);
        mBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        RelativeLayout.LayoutParams mLayoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        mLayoutParams1.setMargins(0, dip2px(context, marginTop), 0, 0);
        mBtn.setLayoutParams(mLayoutParams1);

        GYManager.getInstance().addRegisterViewConfig("other_button", new AuthRegisterViewConfig.Builder()
                .setView(mBtn)
                .setRootViewId(AuthRegisterViewConfig.RootViewId.ROOT_VIEW_ID_BODY)
                .setCustomInterface(new CustomInterface() {
                    @Override
                    public void onClick(Context context) {
                        showToast("其他登录方式");
                    }
                })
                .build()
        );

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SETTING_CODE:
                GYManager.getInstance().ePreLogin(5000, new GyCallBack() {
                    @Override
                    public void onSuccess(GYResponse response) {
                        setPhoneNum(response);
                        eLogin(config);
                    }

                    //预登录失败 请切换到其他验证方式
                    @Override
                    public void onFailed(GYResponse response) {
                        showToast("预登录失败:" + response);
                        hideLoadingDialog();
                    }
                });
                break;
            default:
                break;
        }
    }



    @Override
    public void onPrivacyClick(String s, String s1) {
        Log.d(TAG, "隐私条款点击回调:" + s + ":" + s1);
    }

    @Override
    public void onPrivacyCheckBoxClick(boolean b) {
        Log.d(TAG, "隐私条款复选框点击回调:" + b);
    }

    @Override
    public void onLoginButtonClick() {
        Log.d(TAG, "一键登录按钮点击回调");
    }

    @Override
    public void onAuthActivityCreate(Activity activity) {
        Log.d(TAG, "授权页面启动回调");
        authActivity = activity;
        if (isLandscape) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }
    }

    @Override
    public void onAuthWebActivityCreate(Activity activity) {
        Log.d(TAG, "隐私条款页面启动回调");
    }
}
