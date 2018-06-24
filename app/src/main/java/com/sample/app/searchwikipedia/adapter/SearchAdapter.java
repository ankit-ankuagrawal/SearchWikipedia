package com.sample.app.searchwikipedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sample.app.searchwikipedia.R;
import com.sample.app.searchwikipedia.activity.WebViewActivity;
import com.sample.app.searchwikipedia.model.PageItem;
import com.sample.app.searchwikipedia.util.SearchUtility;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = SearchAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<PageItem> pageItemList;
    private Context context;

    private String searchWord = "";

    public SearchAdapter(Context context, List<PageItem> pageItemList) {
        this.context = context;
        this.pageItemList = pageItemList;
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {

        public LinearLayout linearLayout;
        public TextView tvTitle;
        public TextView tvDescription;
        public ImageView image;

        public ViewHolderItem(View v) {
            super(v);
            linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            tvDescription = (TextView) v.findViewById(R.id.tvDescription);
            image = (ImageView) v.findViewById(R.id.image);
        }
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {
        public TextView tvSearchTitle;

        public ViewHolderHeader(View v) {
            super(v);
            tvSearchTitle = (TextView) v.findViewById(R.id.tvSearchTitle);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = null;
        if (viewType == TYPE_HEADER) {
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_list_header, parent, false);

            return new ViewHolderHeader(rowView);
        } else if (viewType == TYPE_ITEM) {
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_list_row, parent, false);
            return new ViewHolderItem(rowView);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolderItem) {
            ViewHolderItem viewHolderItem = (ViewHolderItem) holder;
            final PageItem pageItem = pageItemList.get(position - 1);
            viewHolderItem.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(SearchUtility.FULL_URL_EXTRA, pageItem.getFullUrl());
                    context.startActivity(intent);
                }
            });
            viewHolderItem.tvTitle.setText(pageItem.getTitle());
            if (pageItem.getTerms() != null) {
                String descriptionString = (String) pageItem.getTerms().getDescription().get(0);
                viewHolderItem.tvDescription.setText(descriptionString.substring(0, 1).toUpperCase() + descriptionString.substring(1));
            }
            if (pageItem.getThumbnail() != null) {
                Picasso.get().load(pageItem.getThumbnail().getSource()).into(viewHolderItem.image);
            } else {
                viewHolderItem.image.setImageResource(R.drawable.icons8_picture_480);
            }
        } else if (holder instanceof ViewHolderHeader) {
            ViewHolderHeader viewHolderHeader = (ViewHolderHeader) holder;
            if (!searchWord.isEmpty()) {

                viewHolderHeader.tvSearchTitle.setText(context.getString(R.string.search_result) + " " + "\"" + searchWord + "\"");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        if (searchWord.isEmpty()) {
            return 0;
        }
        return pageItemList.size() + 1;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }
}
