package com.sample.app.searchwikipedia.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.app.searchwikipedia.R;
import com.sample.app.searchwikipedia.adapter.SearchAdapter;
import com.sample.app.searchwikipedia.asynctask.HttpGetAsyncTask;
import com.sample.app.searchwikipedia.model.PageItem;
import com.sample.app.searchwikipedia.util.SearchUtility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity responsible to display all available search result in recycler view
 */

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();

    private RecyclerView rvSearch;
    private RecyclerView.Adapter searchAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<PageItem> pageItemList = new ArrayList<>();

    private ProgressDialog progressDialog;
    private TextView tvInstruction;

    private SearchView searchView;

    private HttpGetAsyncTask asyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tvInstruction = (TextView) findViewById(R.id.tvInstruction);
        rvSearch = (RecyclerView) findViewById(R.id.rvSearch);
        layoutManager = new LinearLayoutManager(this);
        rvSearch.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        rvSearch.setLayoutManager(layoutManager);

        searchAdapter = new SearchAdapter(this, pageItemList);
        rvSearch.setAdapter(searchAdapter);

        try {
            File httpCacheDir = new File(getCacheDir(), "network cache");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.e(LOG_TAG, "HTTP response cache installation failed", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        final MenuItem searchMenuItem = (MenuItem) menu.findItem(R.id.searchMenu);
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                tvInstruction.setVisibility(View.GONE);
                refreshRecyclerView("");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                tvInstruction.setVisibility(View.VISIBLE);
                return true;
            }
        });
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_view));
        searchView.setOnQueryTextListener(this);

        return true;
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

    public List<PageItem> getPageItemList() {
        return pageItemList;
    }

    public void updateRecyclerView(int positionStart, int itemCount) {
        if (pageItemList.size() > 0) {
            searchAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }
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
                searchView.setQuery("", false);
                refreshRecyclerView("");
                searchView.requestFocusFromTouch();
                break;
            default:
        }
    }
}
