package com.huohua.novel;

import androidx.appcompat.app.ActionBar;

import android.os.Bundle;
import android.webkit.WebView;

public class DetailActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        String pathName = "/";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pathName = extras.getString("pathName");
        }

        webView = findViewById(R.id.detail_webview);
        Util.configWebView(webView, this, "detail");
        webView.loadUrl(Config.kDomain + pathName);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
