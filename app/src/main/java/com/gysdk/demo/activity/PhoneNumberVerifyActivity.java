package com.gysdk.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.g.gysdk.GYManager;
import com.g.gysdk.GYResponse;
import com.g.gysdk.GyCallBack;
import com.gysdk.demo.R;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: fengchaoqun.
 * date: 2019/5/21 11:35 AM.
 */
public class PhoneNumberVerifyActivity extends BaseActivity {

    String accessCode;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.btn_next)
    AppCompatButton btnNext;

    public static void start(Context context, String accessCode) {
        Intent intent = new Intent(context, PhoneNumberVerifyActivity.class);
        intent.putExtra("accessCode", accessCode);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_verify);
        ButterKnife.bind(this);
        setTitle("本机号码验证");

        accessCode = getIntent().getStringExtra("accessCode");
        btnNext.setEnabled(true);
        btnNext.setText("校验");
        tvNotice.setText("请输入手机号用以验证");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭本机号码校验
//        GYManager.getInstance().cancelGetVerifyToken();
    }

    @OnClick(R.id.btn_next)
    public void onBtnNextClicked() {
        final String phone = etInput.getText().toString();
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            showToast("请输入完整的待检测号码");
            return;
        }
        showLoadingDialog();
        //先获取本机号码校验通行证
        GYManager.getInstance().getVerifyToken(phone, 5000, new GyCallBack() {
            @Override
            public void onSuccess(final GYResponse response) {
                Log.d("getVerifyToken", "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response.getMsg());
                    String accessCode = jsonObject.getString("accessCode");
                    String processId = jsonObject.getString("process_id");
                    int operatorType = jsonObject.getInt("operatorType");
                    String phone = jsonObject.getString("phone");
                    //本机号码校验
                    verifyPhone(accessCode, processId, phone, operatorType);
                } catch (Throwable t) {
                    t.printStackTrace();
                    hideLoadingDialog();
                }
            }

            @Override
            public void onFailed(GYResponse response) {
                Log.d("getVerifyToken", "response:" + response);
                hideLoadingDialog();
                showFailDialog(response.getCode(), response.getMsg());
            }
        });

    }

    private void verifyPhone(String accessCode, String processId, String phone, int type) {
        GYManager.getInstance().verifyPhoneNumber(accessCode, processId, phone, type, new GyCallBack() {
            @Override
            public void onSuccess(GYResponse response) {
                Log.d("PhoneNumberVerifyActivi", "response:" + response);
                showToast("校验成功");
                hideLoadingDialog();
            }

            @Override
            public void onFailed(GYResponse response) {
                Log.d("PhoneNumberVerifyActivi", "response:" + response);
                showToast("校验失败:" + response.getMsg());
                hideLoadingDialog();
            }
        });
    }

}
