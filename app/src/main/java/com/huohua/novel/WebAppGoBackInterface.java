package com.huohua.novel;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class WebAppGoBackInterface {
    private Activity activity;
    private WebView webView;

    public WebAppGoBackInterface(Activity activity, WebView webView) {
        this.activity = activity;
        this.webView = webView;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void goBack() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Log.i(getClass().getSimpleName(), "go back");

        activity.finish();
    }
}
