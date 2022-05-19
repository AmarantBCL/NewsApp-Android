package com.example.android.androidacademyviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<NewsItem> news;
    private final Context context;
    private final OnStateClickListener onClickListener;

    interface OnStateClickListener {
        void onStateClick(NewsItem news, int position);
    }

    public NewsAdapter(Context context, List<NewsItem> news, OnStateClickListener onClickListener) {
        this.news = news;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        NewsItem newsItem = news.get(position);
        Glide.with(context).load(newsItem.getImageUrl()).into(holder.headingView);
        holder.categoryView.setText(newsItem.getCategory().getName());
        holder.titleView.setText(newsItem.getTitle());
        holder.previewTextView.setText(newsItem.getPreviewText());
        holder.dateView.setText(newsItem.getPublishDate().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onStateClick(newsItem, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView headingView;
        final TextView categoryView, titleView, previewTextView, dateView;

        ViewHolder(View view) {
            super(view);
            headingView = view.findViewById(R.id.img_heading);
            categoryView = view.findViewById(R.id.tv_category);
            titleView = view.findViewById(R.id.tv_title);
            previewTextView = view.findViewById(R.id.tv_preview_text);
            dateView = view.findViewById(R.id.tv_date);
        }
    }
}
