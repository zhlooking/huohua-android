package com.huohua.novel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.umeng.message.UmengNotifyClickActivity;

import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class ThirdPushActivity extends UmengNotifyClickActivity {

    private static String TAG = ThirdPushActivity.class.getName();
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);

        Log.i(TAG, body);

        this.gotoBookDetailActivity(body);
    }

    private void gotoBookDetailActivity(String customString) {
        try {
            JSONObject customMessage = new JSONObject(customString);
            JSONObject body = customMessage.getJSONObject("body");
            JSONObject custom = new JSONObject(body.getString("custom"));

            String type = custom.getString("type");
            int bookId = custom.getInt("book_id");
            Log.e(TAG, type);
            Log.e(TAG, bookId + "----->");
            String bookPathName = "/" + (int)Math.floor(bookId / 1000) + "_" + bookId + ".html";

            if (TextUtils.equals(type, "book_open")) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("from", "third");
                intent.putExtra("pathName", bookPathName);
                startActivity(intent);
                ThirdPushActivity.this.finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
