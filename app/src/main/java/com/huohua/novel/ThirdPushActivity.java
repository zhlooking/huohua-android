package com.huohua.novel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.umeng.message.UmengNotifyClickActivity;

import org.android.agoo.common.AgooConstants;

public class ThirdPushActivity extends UmengNotifyClickActivity {

    private static String TAG = ThirdPushActivity.class.getName();
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_third_push);
    }
    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Log.i(TAG, body);
    }
}
