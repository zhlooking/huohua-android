package com.huohua.novel;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class WebAppShowAdInterface {
    private Activity activity;
    private WebView webView;
    private int index = 1;

    public WebAppShowAdInterface(Activity activity, WebView webView) {
        this.activity = activity;
        this.webView = webView;
    }

    @JavascriptInterface
    public void showAd() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        if (index % 3 == 0) {
            int i = (int)Math.round(Math.random());
            if (i == 0) {
                Intent intent = new Intent(activity, FullScreenVideoActivity.class);
                intent.putExtra("vertical_rit","945173631");
                activity.startActivity(intent);
            } else {
                Intent intent = new Intent(activity, InteractionActivity.class);
                intent.putExtra( "code_id","945173635");
                activity.startActivity(intent);
            }
            index = 1;
        } else {
            index++;
        }
    }
}
