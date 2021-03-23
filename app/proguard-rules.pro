# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/xz/Documents/android/adt-bundle-mac-x86_64-20131030/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn com.g.gysdk.**
-keep class com.g.gysdk.**{*;}
-dontwarn com.g.elogin.**
-keep class com.g.elogin.**{*;}
-dontwarn com.cmic.sso.sdk.**
-keep class com.cmic.sso.sdk.** {*;}
-dontwarn com.geetest.onelogin.**
-keep class com.geetest.onelogin.** {*;}
-dontwarn com.geetest.onepassv2.**
-keep class com.geetest.onepassv2.** {*;}
-dontwarn com.unicom.xiaowo.login.**
-keep class com.unicom.xiaowo.login.** {*;}
-dontwarn cn.com.chinatelecom.account.api.**
-keep class cn.com.chinatelecom.account.api.** {*;}