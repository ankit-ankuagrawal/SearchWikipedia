
package com.appsnesst.searchwikipedia.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sample.app.searchwikipedia.R;
import com.appsnesst.searchwikipedia.util.SearchUtility;

/**
 * Activity responsible to show wikipedia click article in webview
 */

public class WebViewActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ProgressBar progressBarCyclic;
    private WebView webview;
    private AdView mAdView;
    private BottomNavigationView bottomNavigationView;

    private String pageTitle;
    private String fullUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        fullUrl = getIntent().getStringExtra(SearchUtility.FULL_URL_EXTRA);
        pageTitle = getIntent().getStringExtra(SearchUtility.PAGE_TITLE_EXTRA);
        setTitle(pageTitle);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                finishActivityWithResult(SearchUtility.ACTIVITY_CLOSED);
                break;
            case R.id.newSearch:
                finishActivityWithResult(SearchUtility.SEARCH_NEW_ARTICLE);
                break;
            case R.id.share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.article) + pageTitle + "\n" + fullUrl);
                startActivity(Intent.createChooser(intent, getResources().getText(R.string.share_article)));
                break;
        }
        return false;
    }

    private void finishActivityWithResult(int successCode) {
        Intent intent = new Intent();
        setResult(successCode, intent);
        finish();
    }
}