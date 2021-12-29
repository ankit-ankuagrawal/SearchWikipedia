package com.appsnesst.searchwikipedia.view.activity;

import static com.appsnesst.searchwikipedia.util.AppConstants.SEARCH_ACTIVITY_ADS_ITEMS_PER_AD;
import static com.appsnesst.searchwikipedia.util.AppConstants.SEARCH_ACTIVITY_AD_POSITION_AMONG_ITEMS_PER_AD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sample.app.searchwikipedia.R;
import com.appsnesst.searchwikipedia.data.model.SearchResult;
import com.appsnesst.searchwikipedia.view.adapter.SearchAdapter;
import com.appsnesst.searchwikipedia.view.listener.OnSearchItemClickListener;
import com.appsnesst.searchwikipedia.viewmodel.SearchViewModel;
import com.sample.app.searchwikipedia.databinding.ActivityNewSearchBinding;
import com.appsnesst.searchwikipedia.listener.NavigationDrawerListener;
import com.appsnesst.searchwikipedia.util.AppUtility;
import com.appsnesst.searchwikipedia.util.NavigationDrawerUtil;
import com.appsnesst.searchwikipedia.util.NetworkUtil;
import com.appsnesst.searchwikipedia.util.RateMeUtility;
import com.appsnesst.searchwikipedia.util.SearchUtility;
import com.appsnesst.searchwikipedia.util.ShareUtility;
import com.appsnesst.searchwikipedia.util.ToastUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class NewSearchActivity extends AppCompatActivity implements TextView.OnEditorActionListener, Observer<SearchResult>, OnSearchItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String LOG_TAG = NewSearchActivity.class.getSimpleName();

    private ActivityNewSearchBinding binding;

    private ProgressDialog progressDialog;

    private SearchViewModel searchViewModel;

    private List<Object> pageItemList = new ArrayList<>();
    private int totalPageItemCount = 0;

    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        searchAdapter = new SearchAdapter(pageItemList, this);
        binding.rvSearch.setAdapter(searchAdapter);
        binding.rvSearch.setLayoutManager(new LinearLayoutManager(this));

        binding.etSearch.setOnEditorActionListener(this);
        searchViewModel.getSearchResultMutableLiveData().observe(this, this);

        binding.navigationView.setNavigationItemSelectedListener(new NavigationDrawerListener(this));
        NavigationDrawerUtil.updateNavigationMenu(binding.navigationView.getMenu());
        binding.searchActivityBottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            initiateNewSearch(v.getText().toString());
            return true;
        }
        return false;
    }

    @Override
    public void onChanged(SearchResult searchResult) {
        Log.i(LOG_TAG, "onChanged searchResult: " + searchResult);
        cancelProgressDialog();

        if (searchResult != null && searchResult.getQuery() != null && searchResult.getQuery().getPages().size() > 0) {
            binding.tvInstruction.setVisibility(View.GONE);
            binding.rvSearch.setVisibility(View.VISIBLE);

            pageItemList.addAll(searchResult.getQuery().getPages());

            int requestItemCount = searchResult.getQuery().getPages().size();
            totalPageItemCount += requestItemCount;

            addBannerAds(requestItemCount);
            loadBannerAds();

            searchAdapter.notifyItemRangeInserted(totalPageItemCount, requestItemCount);
        } else if (totalPageItemCount == 0) {
            ToastUtil.showToast(this, R.drawable.cross, getString(R.string.search_result_empty), R.color.error_red, R.color.white,R.color.white);
            binding.tvInstruction.setText(R.string.search_result_empty);
            binding.tvInstruction.setVisibility(View.VISIBLE);
            binding.rvSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSearchItemClicked(String url, String name) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(SearchUtility.FULL_URL_EXTRA, url);
        intent.putExtra(SearchUtility.PAGE_TITLE_EXTRA, name);
        startActivityForResult(intent, 0);
    }

    private void initiateNewSearch(String searchQuery) {
        String trimmedSearchQuery = searchQuery.trim();
        if (TextUtils.isEmpty(trimmedSearchQuery)) {
            ToastUtil.showToast(this, R.drawable.cross, getString(R.string.search_query_empty_error), R.color.error_red, R.color.white, R.color.white);
        } else {
            binding.etSearch.clearFocus();
            binding.llParent.requestFocus();
            AppUtility.hideKeyboard(this);
            refreshRecyclerView(trimmedSearchQuery);
            makeNetworkCall(trimmedSearchQuery);
            searchViewModel.saveSearchQuery(trimmedSearchQuery);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(LOG_TAG, "result code: " + resultCode);
        switch (resultCode) {
            case SearchUtility.ACTIVITY_CLOSED:
                break;
            case SearchUtility.SEARCH_NEW_ARTICLE:
                binding.etSearch.setText(null);
                binding.etSearch.requestFocus();
                AppUtility.showKeyboard(this);
                refreshRecyclerView(null);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.progress_dialog));
            progressDialog.setCancelable(false);
        }
        progressDialog.cancel();
        progressDialog.show();
    }

    public void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    private void refreshRecyclerView(String searchQuery) {
        pageItemList.clear();
        totalPageItemCount = 0;
        searchAdapter.setSearchWord(searchQuery);
        searchAdapter.notifyDataSetChanged();
    }

    private void makeNetworkCall(String searchQuery) {
        if (NetworkUtil.isInternetAvailable(this)) {
            showProgressDialog();
            searchViewModel.searchWikipedia(searchQuery, new LinkedHashMap<>());
        }
    }

    /**
     * Adds banner ads to the items list.
     */
    private void addBannerAds(int itemCount) {
        // Loop through the items array and place a new banner ad in every ith position in
        // the items List.
        for (int i = pageItemList.size() - itemCount; i <= pageItemList.size(); i++) {
            if (i % SEARCH_ACTIVITY_ADS_ITEMS_PER_AD == SEARCH_ACTIVITY_AD_POSITION_AMONG_ITEMS_PER_AD) {
                final AdView adView = new AdView(this);
                adView.setAdSize(AdSize.BANNER);
                adView.setAdUnitId(getString(R.string.search_activity_ad_unit_id));
                pageItemList.add(i, adView);
            }
        }
    }
    private void loadBannerAds() {
        // Load the first banner ad in the items list (subsequent ads will be loaded automatically
        // in sequence).
        loadBannerAd(SEARCH_ACTIVITY_AD_POSITION_AMONG_ITEMS_PER_AD);
    }

    /**
     * Loads the banner ads in the items list.
     */
    private void loadBannerAd(final int index) {

        if (index >= pageItemList.size()) {
            return;
        }

        Object item = pageItemList.get(index);
        if (!(item instanceof AdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a banner ad"
                    + " ad.");
        }

        final AdView adView = (AdView) item;

        // Set an AdListener on the AdView to wait for the previous banner ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(
                new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        // The previous banner ad loaded successfully, call this method again to
                        // load the next ad in the items list.
                        loadBannerAd(index + SEARCH_ACTIVITY_ADS_ITEMS_PER_AD);
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // The previous banner ad failed to load. Call this method again to load
                        // the next ad in the items list.
                        String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        Log.e(LOG_TAG,
                                "The previous banner ad failed to load with error: "
                                        + error
                                        + ". Attempting to"
                                        + " load the next banner ad in the items list.");
                        loadBannerAd(index + SEARCH_ACTIVITY_ADS_ITEMS_PER_AD);
                    }
                });

        // Load the banner ad.
        adView.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @android.support.annotation.NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigationDrawer:
                if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    binding.drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    binding.drawerLayout.openDrawer(Gravity.LEFT);
                }
                return true;
            case R.id.exitApp:
                finish();
                return true;
            case R.id.rateApp:
                RateMeUtility.rateMe(this);
                return true;
            case R.id.shareApp:
                ShareUtility.shareTheApp(this);
                return true;
        }
        return false;
    }
}