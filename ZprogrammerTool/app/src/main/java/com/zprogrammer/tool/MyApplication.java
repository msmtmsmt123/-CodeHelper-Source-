package com.zprogrammer.tool;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;
import com.zprogrammer.tool.bean.Data;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.DeleteListener;

public class MyApplication extends Application {
    public static final int QUERY_LIMIT = 10;
    private SharedPreferences setting;
    //这个为bmob初始化的唯一appid
    private static final String BMOB_APPID = "这个填你的bmobID";
    private static final String BUGLY_APPID = "这个填你的buglyID";
    private static MyApplication instance;
    public static String CACHE_PATH, FILES_PATH;

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, BMOB_APPID);
        CrashReport.initCrashReport(this, BUGLY_APPID, false);
        setting = PreferenceManager.getDefaultSharedPreferences(this);
        instance = this;
        CACHE_PATH = getCacheDir().getPath();
        FILES_PATH = getFilesDir().getPath();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    //代码助手里面有。。。
    public void editString(String s, String message) {
        setting.edit().putString(s, message).apply();
    }

    public void editBoolean(String s, boolean message) {
        setting.edit().putBoolean(s, message).apply();
    }

    public void editint(String s, int message) {
        setting.edit().putInt(s, message).apply();
    }

    public int getint(String s, int message) {
        return setting.getInt(s, message);
    }

    public boolean getBoolean(String s, boolean message) {
        return setting.getBoolean(s, message);
    }

    public String getString(String s, String message) {
        return setting.getString(s, message);
    }

    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    //缩放动画from开始的大小to缩放的大小，duration时间，v哪个view的动画
    public void scanim(float from, float to, long duration, View v) {
        Animation anim = new ScaleAnimation(from, to, from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(duration);
        anim.setFillAfter(true);
        v.setAnimation(anim);
        anim.startNow();
        v.startAnimation(anim);
    }

    /*位移动画
     fromx开始的x轴的坐标
     fromy开始的y轴的坐标
     tox结束的x轴的坐标
     toy结束的y轴的坐标
     duration播放时长
     v为哪个View要播放的动画。
     */
    public void tranim(float fromx, float fromy, float tox, float toy, long duration, View v) {
        Animation translate = new TranslateAnimation(fromx, tox, fromy, toy);
        translate.setDuration(duration);
        translate.setFillAfter(true);
        v.setAnimation(translate);
        translate.startNow();
        v.startAnimation(translate);
    }

    //透明度动画
    public void alanim(float from, float to, long duration, View v) {
        Animation alpha = new AlphaAnimation(from, to);
        alpha.setDuration(duration);
        alpha.setFillAfter(true);
        v.setAnimation(alpha);
        alpha.startNow();
        v.startAnimation(alpha);
    }

    //ListView的动画
    public LayoutAnimationController getListAnim() {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        /*animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                                           Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
										   -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);*/
        animation = new ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        return controller;
    }

    //判断网络状态
    public static boolean detect(Activity act) {
        ConnectivityManager manager = (ConnectivityManager) act
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        return !(networkinfo == null || !networkinfo.isAvailable());
    }

    //删除数据
    public void delete(String id) {
        Data gameScore = new Data();
        gameScore.setObjectId(id);
        gameScore.delete(this, new DeleteListener() {
            @Override
            public void onSuccess() {
                showToast("删除成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                showToast("删除失败：\n" + msg);
            }
        });
    }

}
