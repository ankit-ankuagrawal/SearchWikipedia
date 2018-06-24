package com.sample.app.searchwikipedia.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.sample.app.searchwikipedia.R;
import com.sample.app.searchwikipedia.util.SearchUtility;

/**
 * Activity responsible to show wikipedia click article in webview
 */

public class WebViewActivity extends AppCompatActivity {

    private ProgressBar progressBarCyclic;
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        String fullUrl = getIntent().getStringExtra(SearchUtility.FULL_URL_EXTRA);
        String pageTitle = getIntent().getStringExtra(SearchUtility.PAGE_TITLE_EXTRA);
        setTitle(pageTitle);

        progressBarCyclic = (ProgressBar) findViewById(R.id.progressBarCyclic);
        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBarCyclic.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest webResourceRequest) {
                return false;
            }
        });

        webview.loadUrl(fullUrl);
    }
}
