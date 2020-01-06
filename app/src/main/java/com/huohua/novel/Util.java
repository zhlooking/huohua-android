package com.huohua.novel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;

import java.util.Arrays;
import java.util.List;

public class Util {
    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public static void configWebView(final WebView webView, final Activity activity, CharSequence type) {
        //启用javaScript
        webView.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

//            webView.getSettings().setAppCacheEnabled(false);
//            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        //全屏显示
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle(message)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        })
                        .create();
                dialog.show();
                result.confirm();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle(message)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .create();
                dialog.show();
                return true;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        webView.setWebViewClient(getWebViewClient(activity, type));
        webView.addJavascriptInterface(new WebAppGoBackInterface(activity, webView), "Android");
    }

    private static WebViewClient getWebViewClient(final Activity activity, final CharSequence type) {
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                Uri uri = Uri.parse(url);

                if (url.startsWith("mailto:") || url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    activity.startActivity(intent);
                    return true;
                }

                String path = uri.getEncodedPath();

                List<String> list = Arrays.asList(Config.kCategoryNames);

                if (list.contains(path.substring(1))) {
                    Intent intent = new Intent(activity, DetailActivity.class);
                    intent.putExtra("pathName", path);
                    activity.startActivity(intent);

                    return true;
                }

                if ((path.contains("_")
                        ||  path.contains("paihangbang")
                        ||  path.contains("quanben")
                ) && !TextUtils.equals("detail", type)) {
                    Intent intent = new Intent(activity, DetailActivity.class);
                    intent.putExtra("pathName", path);
                    activity.startActivity(intent);

                    return true;
                } else if (TextUtils.equals("/home/index/bookcase", path) && !TextUtils.equals("history", type)) {    // 书架
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("selectedTab", R.id.navigation_history);
                    activity.startActivity(intent);

                    return true;
                } else if (TextUtils.equals("/xiaoshuodaquan", path) && !TextUtils.equals("category", type)) {         // 分类
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("selectedTab", R.id.navigation_category);
                    activity.startActivity(intent);

                    return true;
                } else if ((TextUtils.equals("/", path) || TextUtils.equals("", path)) && !TextUtils.equals("home", type)) {     // 书城
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("selectedTab", R.id.navigation_home);
                    activity.startActivity(intent);

                    return true;
                }

                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

//                Log.v("---> onPageStarted", type + "");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

//                Log.v("---> onPageFinished", type + "");
            }
        };
    }
}
