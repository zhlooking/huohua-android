package com.huohua.novel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class Util {
    @SuppressLint("SetJavaScriptEnabled")
    public static void configWebView(final WebView webView, final Activity activity, CharSequence type) {
        //启用javaScript
        webView.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        }

        //全屏显示
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        webView.setWebViewClient(getWebViewClient(activity, type));
    }

    private static WebViewClient getWebViewClient(final Activity activity, final CharSequence type) {
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                Uri uri = Uri.parse(url);
                String path = uri.getEncodedPath();
                if ((path.contains("_")
                        ||  path.contains("paihangbang")
                        ||  path.contains("quanben")
                ) && !TextUtils.equals("detail", type)) {
                    Intent intent = new Intent(activity, DetailActivity.class);
                    intent.putExtra("pathName", path);
                    activity.startActivity(intent);

                    return true;
                } else if (TextUtils.equals("/home/index/bookcase", path) && !TextUtils.equals("history", type)) {    // 书架
                    NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
                    navController.navigate(R.id.navigation_history);
                    return true;
                } else if (TextUtils.equals("/xiaoshuodaquan", path) && !TextUtils.equals("category", type)) {         // 书库
                    NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
                    navController.navigate(R.id.navigation_category);
                    return true;
                } else if ((TextUtils.equals("/", path) || TextUtils.equals("", path)) && !TextUtils.equals("home", type)) {     // 书城
                    NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
                    navController.navigate(R.id.navigation_home);
                    return true;
                }

                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Log.v("---> onPageStarted", type + "");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Log.v("---> onPageFinished", type + "");
            }
        };
    }
}
