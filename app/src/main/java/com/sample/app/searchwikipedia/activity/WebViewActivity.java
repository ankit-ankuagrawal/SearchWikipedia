package com.sample.app.searchwikipedia.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sample.app.searchwikipedia.R;
import com.sample.app.searchwikipedia.util.SearchUtility;

public class WebViewActivity extends AppCompatActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        String fullUrl = getIntent().getStringExtra(SearchUtility.FULL_URL_EXTRA);
        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest webResourceRequest) {
                return false;
            }
        });

        webview.loadUrl(fullUrl);
    }
}
