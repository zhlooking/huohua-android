package com.huohua.novel.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huohua.novel.Config;
import com.huohua.novel.R;
import com.huohua.novel.Util;

public class CategoryFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category, container, false);

        WebView webView = root.findViewById(R.id.category_webview);
        Util.configWebView(webView, getActivity(), "category");
        webView.loadUrl(Config.kDomain + "/xiaoshuodaquan");

        return root;
    }
}