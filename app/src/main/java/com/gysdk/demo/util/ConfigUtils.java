package com.gysdk.demo.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewConfiguration;

import com.g.gysdk.cta.ELoginThemeConfig;

import java.lang.reflect.Method;

/**
 * Time：2020-08-13 on 10:10.
 * Decription:.
 * Author:jimlee.
 */
public class ConfigUtils {

    public final static String TAG = ConfigUtils.class.getSimpleName();


    /**
     * 配置页面布局
     *
     * @return config
     */
    public static ELoginThemeConfig getFullScreenConfig(Context context) {
        ELoginThemeConfig.Builder builder = new ELoginThemeConfig.Builder();
        //设置背景
        builder.setAuthVideoBGPath(context.getCacheDir().getAbsolutePath() + "media.MP4")
                .setStatusBar(Color.TRANSPARENT, Color.TRANSPARENT, true)
                //设置标题栏布局
                .setAuthNavLayout(Color.TRANSPARENT, 49, true, false)
                //设置标题栏中间⽂文字相关
                .setAuthNavTextView("", Color.BLACK, 17, false, "服务条款", Color.BLACK, 17)
                .setAuthNavTextViewTypeface(Typeface.DEFAULT_BOLD, Typeface.DEFAULT_BOLD)
                //设置标题栏返回按钮相关
                .setAuthNavReturnImgView("gy_left_black", 24, 24, false, 12)
                //设置logo相关
                .setLogoImgView("ic_launcher", 71, 71, false, 125, 0, 0)
                //设置号码相关
                .setNumberView(Color.BLACK, 20, 200, 0, 0)
                //设置切换账号相关
                .setSwitchView("切换账号", 0xFF3973FF, 14, false, 249, 0, 0)
                .setSwitchViewTypeface(Typeface.DEFAULT)
                //设置登录按钮布局
                .setLogBtnLayout("login_btn_normal", 268, 36, 324, 0, 0)
                //设置登录按钮中间⽂文字相关
                .setLogBtnTextView("一键登录", Color.WHITE, 15)
                //设置Slogan相关
                .setSloganView(0xFFA8A8A8, 10, 382, 0, 0)
                .setSloganViewTypeface(Typeface.defaultFromStyle(Typeface.ITALIC))
                //设置隐私条款布局
                .setPrivacyLayout(256, 0, px2dip(context, getNavigationBarHeight(context) * 1.0f), 0)
                //设置隐私条款选择框相关
                .setPrivacyCheckBox("login_unchecked", "login_checked", true, 9, 9)
                //设置隐私条款字体相关
                .setPrivacyClauseView(0xFFA8A8A8, 0xFF3973FF, 10)
                //设置除了了隐私条款其他的字体相关
                .setPrivacyTextView("登录即认可", "和", "、", "并使⽤用本机号码登录")
                .setPrivacyClauseViewTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.DEFAULT_BOLD)
                //设置开发者隐私条款相关
                .setPrivacyClauseText("", "",
                        "自定义协议1", "https://www.getui.com/cn/index.html?f=3&p=1",
                        "自定义协议2", "https://www.getui.com/cn/company.html")
                .setPrivacyUnCheckedToastText("请同意服务条款");
        Log.i(TAG, "getNavigationBarHeight = " + getNavigationBarHeight(context));
        return builder.build();
    }


    public static ELoginThemeConfig getLandscapeConfig(Context context) {
        ELoginThemeConfig.Builder builder = new ELoginThemeConfig.Builder();
        //设置背景
        builder.setAuthBGImgPath("login_bg")
                .setStatusBar(Color.WHITE, Color.WHITE, true)
                //设置标题栏布局
                .setAuthNavLayout(Color.TRANSPARENT, 49, true, false)
                //设置标题栏中间⽂文字相关
                .setAuthNavTextView("一键登录", Color.BLACK, 17, false, "服务条款", Color.BLACK, 17)
                .setAuthNavTextViewTypeface(Typeface.DEFAULT_BOLD, Typeface.DEFAULT_BOLD)
                //设置标题栏返回按钮相关
                .setAuthNavReturnImgView("gy_left_black", 24, 24, false, 12)
                //设置logo相关
                .setLogoImgView("ic_launcher", 50, 50, false, 50, 0, 0)
                //设置号码相关
                .setNumberView(Color.BLACK, 20, 110, 0, 0)
                //设置切换账号相关
                .setSwitchView("切换账号", 0xFF3973FF, 14, false, 140, 0, 0)
                .setSwitchViewTypeface(Typeface.DEFAULT)
                //设置登录按钮布局
                .setLogBtnLayout("login_btn_normal", 268, 36, 165, 0, 0)
                //设置登录按钮中间⽂文字相关
                .setLogBtnTextView("一键登录", Color.WHITE, 15)
                //设置loading图⽚片相关
                //设置Slogan相关
                .setSloganView(0xFFA8A8A8, 10, 212, 0, 0)
                .setSloganViewTypeface(Typeface.defaultFromStyle(Typeface.ITALIC))
                //设置隐私条款布局
                .setPrivacyLayout(256, 0, 10, 0)
                //设置隐私条款选择框相关
                .setPrivacyCheckBox("login_unchecked", "login_checked", true, 9, 9)
                //设置隐私条款字体相关
                .setPrivacyClauseView(0xFFA8A8A8, 0xFF3973FF, 10)
                //设置除了了隐私条款其他的字体相关
                .setPrivacyTextView("登录即认可", "和", "、", "并使⽤用本机号码登录")
                .setPrivacyClauseViewTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.DEFAULT_BOLD)
                //设置开发者隐私条款相关
                .setPrivacyClauseText("", "",
                        "自定义协议1", "https://www.getui.com/cn/index.html?f=3&p=1",
                        "自定义协议2", "https://www.getui.com/cn/company.html")
                .setPrivacyUnCheckedToastText("请同意服务条款");
        Log.i(TAG, "getNavigationBarHeight = " + getNavigationBarHeight(context));
        return builder.build();
    }


    public static ELoginThemeConfig getDialogConfig(Context context) {
        ELoginThemeConfig.Builder builder = new ELoginThemeConfig.Builder();
        //设置背景
        builder.setAuthGifBGPath("login_bg")
                .setDialogTheme(true)
                .setStatusBar(Color.WHITE, Color.WHITE, true)
                //设置标题栏布局
                .setAuthNavLayout(Color.TRANSPARENT, 49, true, false)
                //设置标题栏中间⽂文字相关
                .setAuthNavTextView("", Color.BLACK, 17, false, "服务条款", Color.BLACK, 17)
                //设置标题栏返回按钮相关
                .setAuthNavReturnImgView("gy_left_black", 24, 24, false, 12)
                //设置logo相关
                .setLogoImgView("ic_launcher", 60, 60, false, 70, 0, 0)
                //设置号码相关
                .setNumberView(Color.BLACK, 20, 160, 0, 0)
                .setNumberViewTypeface(Typeface.defaultFromStyle(Typeface.ITALIC))
                //设置切换账号相关
                .setSwitchView("切换账号", 0xFF3973FF, 14, false, 200, 0, 0)
                .setSwitchViewTypeface(Typeface.DEFAULT)
                //设置登录按钮布局
                .setLogBtnLayout("login_btn_normal", 268, 36, 240, 0, 0)
                //设置登录按钮中间⽂文字相关
                .setLogBtnTextView("一键登录", Color.WHITE, 15)
                //设置Slogan相关
                .setSloganView(0xFFA8A8A8, 10, 310, 0, 0)
                .setSloganViewTypeface(Typeface.defaultFromStyle(Typeface.ITALIC))
                //设置隐私条款布局
                .setPrivacyLayout(256, 0, 10, 0)
                //设置隐私条款选择框相关
                .setPrivacyCheckBox("login_unchecked", "login_checked", true, 9, 9)
                //设置隐私条款字体相关
                .setPrivacyClauseView(0xFFA8A8A8, 0xFF3973FF, 10)
                //设置除了了隐私条款其他的字体相关
                .setPrivacyTextView("登录即认可", "和", "、", "并使⽤用本机号码登录")
                .setPrivacyClauseViewTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.DEFAULT_BOLD)
                //设置开发者隐私条款相关
                .setPrivacyClauseText("", "",
                        "自定义协议1", "https://www.getui.com/cn/index.html?f=3&p=1",
                        "自定义协议2", "https://www.getui.com/cn/company.html")
                .setPrivacyUnCheckedToastText("请同意服务条款");
        Log.i(TAG, "getNavigationBarHeight = " + getNavigationBarHeight(context));
        return builder.build();
    }


    public static ELoginThemeConfig getPortraitConfig(Activity context) {
        ELoginThemeConfig.Builder builder = new ELoginThemeConfig.Builder();
        //设置背景
        DisplayMetrics outMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        int heightPixels = outMetrics.heightPixels;

        builder.setAuthBGImgPath("login_bg")
                .setDialogTheme(true, px2dip(context, widthPixels), px2dip(context, heightPixels * 3 / 4), 0, 0, true, false)
                .setStatusBar(Color.WHITE, Color.TRANSPARENT, true)
                //设置标题栏布局
                .setAuthNavLayout(Color.TRANSPARENT, 49, true, false)
                //设置标题栏中间⽂文字相关
                .setAuthNavTextView("", Color.BLACK, 17, false, "服务条款", Color.BLACK, 17)
                .setAuthNavTextViewTypeface(Typeface.DEFAULT_BOLD, Typeface.DEFAULT_BOLD)
                //设置标题栏返回按钮相关
                .setAuthNavReturnImgView("gy_left_black", 24, 24, false, 12)
                //设置logo相关
                .setLogoImgView("ic_launcher", 60, 60, false, 70, 0, 0)
                //设置号码相关
                .setNumberView(Color.BLACK, 20, 160, 0, 0)
                //设置切换账号相关
                .setSwitchView("切换账号", 0xFF3973FF, 14, false, 200, 0, 0)
                .setSwitchViewTypeface(Typeface.DEFAULT)
                //设置登录按钮布局
                .setLogBtnLayout("login_btn_normal", 268, 36, 240, 0, 0)
                //设置登录按钮中间⽂文字相关
                .setLogBtnTextView("一键登录", Color.WHITE, 15)
                //设置loading图⽚片相关
                //设置Slogan相关
                .setSloganView(0xFFA8A8A8, 10, 310, 0, 0)
                .setSloganViewTypeface(Typeface.defaultFromStyle(Typeface.ITALIC))
                //设置隐私条款布局
                .setPrivacyLayout(256, 0, px2dip(context, getNavigationBarHeight(context) * 1.0f), 0)
                //设置隐私条款选择框相关
                .setPrivacyCheckBox("login_unchecked", "login_checked", true, 9, 9)
                //设置隐私条款字体相关
                .setPrivacyClauseView(0xFFA8A8A8, 0xFF3973FF, 10)
                //设置除了了隐私条款其他的字体相关
                .setPrivacyTextView("登录即认可", "和", "、", "并使⽤用本机号码登录")
                .setPrivacyClauseViewTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.DEFAULT_BOLD)
                //设置开发者隐私条款相关
                .setPrivacyClauseText("", "",
                        "自定义协议1", "https://www.getui.com/cn/index.html?f=3&p=1",
                        "自定义协议2", "https://www.getui.com/cn/company.html")
                .setPrivacyUnCheckedToastText("请同意服务条款");
        Log.i(TAG, "getNavigationBarHeight = " + getNavigationBarHeight(context));
        return builder.build();
    }


    private static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    private static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        } else {
            result = 20;
        }
        Log.d(TAG, "NavigationBarHeight = " + result);
        return result;
    }


    /**
     * 检查是否存在虚拟按键栏
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else {
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        return sNavBarOverride;
    }


}
