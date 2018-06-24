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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<PageItem> pageItemList;
    private Context context;

    public SearchAdapter(Context context, List<PageItem> pageItemList) {
        this.context = context;
        this.pageItemList = pageItemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout linearLayout;
        public TextView tvTitle;
        public TextView tvDescription;
        public ImageView image;

        public ViewHolder(View v) {
            super(v);
            linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            tvDescription = (TextView) v.findViewById(R.id.tvDescription);
            image = (ImageView) v.findViewById(R.id.image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PageItem pageItem = pageItemList.get(position);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(SearchUtility.FULL_URL_EXTRA, pageItem.getFullUrl());
                context.startActivity(intent);
            }
        });
        holder.tvTitle.setText(pageItem.getTitle());
        if (pageItem.getTerms() != null) {
            String descriptionString = (String) pageItem.getTerms().getDescription().get(0);
            holder.tvDescription.setText(descriptionString.substring(0, 1).toUpperCase() + descriptionString.substring(1));
        }
        if (pageItem.getThumbnail() != null) {
            Picasso.get().load(pageItem.getThumbnail().getSource()).into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.icons8_picture_480);
        }
    }

    @Override
    public int getItemCount() {
        return pageItemList.size();
    }
}
