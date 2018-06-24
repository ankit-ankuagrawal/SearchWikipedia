package com.sample.app.searchwikipedia.activity;

import android.app.ProgressDialog;
import android.net.http.HttpResponseCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.sample.app.searchwikipedia.R;
import com.sample.app.searchwikipedia.adapter.SearchAdapter;
import com.sample.app.searchwikipedia.asynctask.HttpGetAsyncTask;
import com.sample.app.searchwikipedia.model.PageItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();

    private RecyclerView rvSearch;
    private RecyclerView.Adapter searchAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<PageItem> pageItemList = new ArrayList<>();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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

        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.search_view));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchWord) {
                if (searchWord.isEmpty()) {
                    return false;
                }

                searchView.clearFocus();
                pageItemList.clear();

                ((SearchAdapter) searchAdapter).setSearchWord(searchWord);
                searchAdapter.notifyDataSetChanged();
                showProgressDialog();

                makeNetworkCall(searchWord);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void makeNetworkCall(String query) {
        HttpGetAsyncTask asyncTask = new HttpGetAsyncTask(this);
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
        progressDialog.cancel();
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
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }
}
