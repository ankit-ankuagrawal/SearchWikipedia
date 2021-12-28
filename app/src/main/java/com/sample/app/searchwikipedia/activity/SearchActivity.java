package com.sample.app.searchwikipedia.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.sample.app.searchwikipedia.R;
import com.sample.app.searchwikipedia.adapter.SearchAdapter;
import com.sample.app.searchwikipedia.asynctask.HttpGetAsyncTask;
import com.sample.app.searchwikipedia.listener.NavigationDrawerListener;
import com.sample.app.searchwikipedia.model.PageItem;
import com.sample.app.searchwikipedia.util.NavigationDrawerUtil;
import com.sample.app.searchwikipedia.util.RateMeUtility;
import com.sample.app.searchwikipedia.util.SearchUtility;
import com.sample.app.searchwikipedia.util.ShareUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity responsible to display all available search result in recycler view
 */

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();

    // A banner ad is placed in every 5th position in the RecyclerView.
    public static final int ITEMS_PER_AD = 5;
    public static final int AD_POSITION_AMONG_ITEMS_PER_AD = 4;

    private static final String AD_UNIT_ID = "ca-app-pub-7157770574029952/7207231202";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private RecyclerView rvSearch;
    private RecyclerView.Adapter searchAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Object> pageItemList = new ArrayList<>();

    private ProgressDialog progressDialog;
    private TextView tvInstruction;

    private SearchView searchView;

    private HttpGetAsyncTask asyncTask;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        tvInstruction = (TextView) findViewById(R.id.tvInstruction);
        rvSearch = (RecyclerView) findViewById(R.id.rvSearch);
        layoutManager = new LinearLayoutManager(this);
        rvSearch.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        rvSearch.setLayoutManager(layoutManager);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.search_activity_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        searchAdapter = new SearchAdapter(this, pageItemList);
        rvSearch.setAdapter(searchAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.dev_navigation_icon);
        navigationView.setNavigationItemSelectedListener(new NavigationDrawerListener(this));
        NavigationDrawerUtil.updateNavigationMenu(navigationView.getMenu());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(LOG_TAG, "onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        final MenuItem searchMenuItem = (MenuItem) menu.findItem(R.id.searchMenu);
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                tvInstruction.setVisibility(View.GONE);
                rvSearch.setVisibility(View.VISIBLE);
                refreshRecyclerView("");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                tvInstruction.setText(R.string.instruction);
                tvInstruction.setVisibility(View.VISIBLE);
                rvSearch.setVisibility(View.GONE);
                return true;
            }
        });
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_view));
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makeNetworkCall(String query) {
        asyncTask = new HttpGetAsyncTask(this);
        asyncTask.execute(query);
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

    public List<Object> getPageItemList() {
        return pageItemList;
    }

    public void updateRecyclerView(int positionStart, int itemCount) {
        if (pageItemList.size() > 0 && positionStart>0) {
            if(itemCount>0)
            {
                tvInstruction.setVisibility(View.GONE);
                rvSearch.setVisibility(View.VISIBLE);
                addBannerAds(positionStart, itemCount);
                loadBannerAds();
                searchAdapter.notifyItemRangeInserted(positionStart, itemCount);
            }
        }
        else
        {
            tvInstruction.setText(R.string.search_result_empty);
            tvInstruction.setVisibility(View.VISIBLE);
            rvSearch.setVisibility(View.GONE);
        }
    }

    /**
     * Adds banner ads to the items list.
     */
    private void addBannerAds(int positionStart, int itemCount) {
        // Loop through the items array and place a new banner ad in every ith position in
        // the items List.
        for (int i = pageItemList.size() - itemCount; i <= pageItemList.size(); i++) {
            if (i % ITEMS_PER_AD == AD_POSITION_AMONG_ITEMS_PER_AD) {
                final AdView adView = new AdView(this);
                adView.setAdSize(AdSize.BANNER);
                adView.setAdUnitId(AD_UNIT_ID);
                pageItemList.add(i, adView);
            }
        }
    }

    private void loadBannerAds() {
        // Load the first banner ad in the items list (subsequent ads will be loaded automatically
        // in sequence).
        loadBannerAd(AD_POSITION_AMONG_ITEMS_PER_AD);
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
                        loadBannerAd(index + ITEMS_PER_AD);
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
                        loadBannerAd(index + ITEMS_PER_AD);
                    }
                });

        // Load the banner ad.
        adView.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (asyncTask != null) {
            asyncTask.setSearchActivity(null);
        }
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        refreshRecyclerView(query);
        makeNetworkCall(query);
        showProgressDialog();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void refreshRecyclerView(String query) {
        pageItemList.clear();
        ((SearchAdapter) searchAdapter).setSearchWord(query);
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (asyncTask != null) {
            asyncTask.setSearchActivity(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(LOG_TAG, "result code: " + resultCode);
        switch (resultCode) {
            case SearchUtility.ACTIVITY_CLOSED:
                break;
            case SearchUtility.SEARCH_NEW_ARTICLE:
                // null check, crashes coming
                if (searchView != null) {
                    searchView.setQuery("", false);
                    refreshRecyclerView("");
                    searchView.requestFocusFromTouch();
                }
                break;
            default:
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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
