package com.quangduong.countdown.utils;

import android.content.Context;
import android.webkit.WebView;

/**
 * Created by QuangDuong on 12/15/2015.
 */
public class GifWebView extends WebView {

    public GifWebView(Context context, String path) {
        super(context);

        loadUrl(path);
    }
}
