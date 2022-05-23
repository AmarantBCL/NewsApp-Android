package com.example.android.androidacademyviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.androidacademyviews.model.ArticleDTO;
import com.example.android.androidacademyviews.model.NewsResponse;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<ArticleDTO> newsList;
    private final Context context;
    private final OnStateClickListener onClickListener;

    interface OnStateClickListener {
        void onStateClick(ArticleDTO news, int position);
    }

    public NewsAdapter(Context context, List<ArticleDTO> newsList, OnStateClickListener onClickListener) {
        this.newsList = newsList;
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
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ArticleDTO newsItem = newsList.get(position);
        if (newsItem.getImages().get(1) == null) {
            holder.headingView.setImageResource(R.drawable.ic_baseline_image_24);
        } else {
            Glide.with(context).load(newsItem.getImages().get(1).getUrl()).into(holder.headingView);
        }
        if (newsItem.getCategory().isEmpty()) {
            holder.categoryView.setText(newsItem.getSection());
        } else {
            holder.categoryView.setText(newsItem.getCategory());
        }
        holder.titleView.setText(newsItem.getTitle());
        holder.previewTextView.setText(newsItem.getPreviewText());
        holder.dateView.setText(newsItem.getPublishDate().toString());

        holder.itemView.setOnClickListener(v -> onClickListener.onStateClick(newsItem, position));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
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
