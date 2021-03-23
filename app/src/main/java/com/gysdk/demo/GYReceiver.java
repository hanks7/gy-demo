package com.gysdk.demo;

import android.content.Context;
import android.util.Log;

import com.g.gysdk.GYResponse;
import com.g.gysdk.GyMessageReceiver;

/**
 * Created by cyq on 2017/3/27.
 */

public class GYReceiver extends GyMessageReceiver {

    private static final String TAG = "GYReceiver";

    @Override
    public void onInit(Context context, boolean result) {
        Log.d(TAG, "onInit:" + result);
        if (result) {
            Log.d(TAG, "onReceiverInitSuccess");
        }
    }

    @Override
    public void onError(Context context, GYResponse response) {
        Log.e(TAG, "response:" + response.toString());
    }

    @Override
    public void onGyUidReceived(Context context, String gyUid) {
        Log.d(TAG, "gyUid:" + gyUid);
    }

}
