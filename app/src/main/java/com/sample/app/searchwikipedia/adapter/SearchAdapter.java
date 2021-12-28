package com.sample.app.searchwikipedia.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.sample.app.searchwikipedia.R;
import com.sample.app.searchwikipedia.activity.SearchActivity;
import com.sample.app.searchwikipedia.activity.WebViewActivity;
import com.sample.app.searchwikipedia.model.PageItem;
import com.sample.app.searchwikipedia.util.SearchUtility;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter class for article search results
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = SearchAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_BANNER_ID = 2;

    private List<Object> pageItemList;
    private Context context;

    private String searchWord = "";

    public SearchAdapter(Context context, List<Object> pageItemList) {
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

    public class ViewHolderAdMob extends RecyclerView.ViewHolder {

        ViewHolderAdMob(View view) {
            super(view);
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
        else if(viewType == TYPE_BANNER_ID)
        {
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_ad_container,
                    parent, false);
            return new ViewHolderAdMob(rowView);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i(LOG_TAG, position+", "+holder);

        if (holder instanceof ViewHolderItem) {
            ViewHolderItem viewHolderItem = (ViewHolderItem) holder;
            final PageItem pageItem = (PageItem) pageItemList.get(position - 1);
            viewHolderItem.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(SearchUtility.FULL_URL_EXTRA, pageItem.getFullUrl());
                    intent.putExtra(SearchUtility.PAGE_TITLE_EXTRA, pageItem.getTitle());
                    ((Activity) context).startActivityForResult(intent, 0);
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
        else if(holder instanceof ViewHolderAdMob)
        {
            ViewHolderAdMob bannerHolder = (ViewHolderAdMob) holder;
            AdView adView = (AdView) pageItemList.get(position-1);
            ViewGroup adCardView = (ViewGroup) bannerHolder.itemView;

            if (adCardView.getChildCount() > 0) {
                adCardView.removeAllViews();
            }
            if (adView.getParent() != null) {
                ((ViewGroup) adView.getParent()).removeView(adView);
            }
            // Add the banner ad to the ad view.
            adCardView.addView(adView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return ((position-1) % SearchActivity.ITEMS_PER_AD == SearchActivity.AD_POSITION_AMONG_ITEMS_PER_AD) ? TYPE_BANNER_ID
                : TYPE_ITEM;
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
