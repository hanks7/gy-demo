package com.gysdk.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.g.gysdk.GYManager;
import com.g.gysdk.GYResponse;
import com.g.gysdk.PicCallBack;
import com.gysdk.demo.R;
import com.gysdk.demo.util.StringUtils;
import com.gysdk.demo.util.ToastUtil;
import com.gysdk.demo.view.NoDoubleClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2018/9/21
 */
public class PicVerifyActivity extends BaseActivity {

    private static final String TAG = "PicVerifyActivity";

    public static final int TYPE_PIC = 0;
    public static final int TYPE_NOSENSE = 1;

    @BindView(R.id.tv_get_verify_code)
    TextView tvGetVerifyCode;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.tv_introduce)
    AppCompatTextView tvIntroduce;
    @BindView(R.id.ll_introduce)
    LinearLayout llIntroduce;
    @BindView(R.id.ll_result)
    LinearLayout llResult;

    private final String BUSINESS_ID = "20180620a";
    @BindView(R.id.tv_success)
    TextView tvSuccess;
    private boolean reset;
    private int type;

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, PicVerifyActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("动画验证码");

        setContentView(R.layout.activity_pic_verify);
        ButterKnife.bind(this);

        type = getIntent().getIntExtra("type", TYPE_PIC);
        if (type == TYPE_NOSENSE) {
            setTitle("智能无感");
            tvIntroduce.setText(R.string.nosense_verify_introduction);
            tvSuccess.setText("智能无感验证通过\n短信已发送");
        }

        final PicCallBack callBack = new PicCallBack() {
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
                            showSuccessLay();
                        } else if (GYManager.MSG.NONSENSE_VERIFY_SUCCESS == gyResponse.getCode()) {
                            showToast("智能无感验证成功");
                            showSuccessLay();
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
        };

        tvGetVerifyCode.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (type == TYPE_PIC) {
                    GYManager.getInstance().picVerify(PicVerifyActivity.this, BUSINESS_ID, true, callBack);
                } else {
                    GYManager.getInstance().nonSenseVerify(StringUtils.getMD5Str("1"), "accountId", BUSINESS_ID, PicVerifyActivity.this, true, callBack);
                }
            }
        });
    }

    @OnClick(R.id.btn_login)
    public void onBtnLoginClicked() {
        if (reset) {
            llIntroduce.setVisibility(View.VISIBLE);
            llResult.setVisibility(View.GONE);
            reset = false;
            btnLogin.setText("登录");
        } else {
            ToastUtil.getInstance().showToast("登录按钮只供展示，请点击“获取验证码”进行体验。");
        }
    }

    private void showSuccessLay() {
        llIntroduce.setVisibility(View.GONE);
        llResult.setVisibility(View.VISIBLE);
        reset = true;
        btnLogin.setText("再次体验");
    }

}
