package com.sample.app.searchwikipedia.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;

import com.sample.app.searchwikipedia.R;
import com.sample.app.searchwikipedia.adapter.SearchAdapter;
import com.sample.app.searchwikipedia.asynctask.HttpGetAsyncTask;
import com.sample.app.searchwikipedia.model.PageItem;

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

        // searchAdapter = new SearchAdapter(this, searchResult);
        searchAdapter = new SearchAdapter(this, pageItemList);
        rvSearch.setAdapter(searchAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();

                showProgressDialog();

                pageItemList.clear();
                searchAdapter.notifyDataSetChanged();

                makeNetworkCall(query);

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
            progressDialog.setMessage("Loading Data...");
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
        searchAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }
}
