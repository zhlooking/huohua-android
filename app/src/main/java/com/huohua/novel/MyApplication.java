package com.huohua.novel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.mezu.MeizuRegister;
import org.android.agoo.oppo.OppoRegister;
import org.android.agoo.vivo.VivoRegister;
import org.android.agoo.xiaomi.MiPushRegistar;
import org.json.JSONException;
import org.json.JSONObject;

public class MyApplication extends Application {
    private static Context mContext;

    public static Context getInstance() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        // 三方推送注册
        // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
        HuaWeiRegister.register(this);

        // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
        MiPushRegistar.register(this, "2882303761518308633", "5261830817633");

        // OPPO通道注册
        OppoRegister.register(this, "d1c75708b12f474ebdaad9b4d1d9b1f9", "b9e387c9dceb4c6696b1e0a98112b747");

        // 魅族通道注册
        MeizuRegister.register(this, "127677", "8fec7f0cd86744cf9b92fa0648bb7244");

        // VIVO通道注册
        VivoRegister.register(this);

        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "174a887e9dfff90b37e6890a749546bf");
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.e("ApplicationHuohua","注册成功: deviceToken_success -------->  " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
//                Log.e("Application","注册失败: deviceToken_failed -------->  " + "s:" + s + ",s1:" + s1);
            }
        });

        mPushAgent.setNotificationClickHandler(new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                try {
                    JSONObject custom = new JSONObject(msg.custom);
                    String type = custom.getString("type");
                    int bookId = custom.getInt("book_id");
                    String bookPathName = "/" + (int)Math.floor(bookId / 1000) + "_" + bookId + ".html";

                    if (TextUtils.equals(type, "book_open")) {
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("pathName", bookPathName);
                        intent.putExtra("from", "notification");
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        //穿山甲SDK初始化
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        TTAdManagerHolder.init(this);
        //如果明确某个进程不会使用到广告SDK，可以只针对特定进程初始化广告SDK的content
        //if (PROCESS_NAME_XXXX.equals(processName)) {
        //   TTAdManagerHolder.init(this)
        //}
    }
}
