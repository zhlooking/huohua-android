package com.huohua.novel.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.huohua.novel.Config;
import com.huohua.novel.R;
import com.huohua.novel.Util;

public class HistoryFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        WebView webView = root.findViewById(R.id.history_webview);
        Util.configWebView(webView, getActivity(), "history");
        webView.loadUrl(Config.kDomain + "/home/index/bookcase");

        return root;
    }
}