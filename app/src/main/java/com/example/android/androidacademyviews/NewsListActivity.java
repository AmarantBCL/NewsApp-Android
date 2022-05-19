package com.example.android.androidacademyviews;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        List<NewsItem> news = DataUtils.generateNews();

        NewsAdapter.OnStateClickListener newsClickListener = new NewsAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(NewsItem news, int position) {
                Intent intent = new Intent(NewsListActivity.this, NewsDetailsActivity.class);
                intent.putExtra(NewsDetailsActivity.KEY_PICTURE, news.getImageUrl());
                intent.putExtra(NewsDetailsActivity.KEY_TITLE, news.getTitle());
                intent.putExtra(NewsDetailsActivity.KEY_DATE, news.getPublishDate().toString());
                intent.putExtra(NewsDetailsActivity.KEY_TEXT, news.getFullText());
                startActivity(intent);
            }
        };

        NewsAdapter adapter = new NewsAdapter(this, news, newsClickListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_switch:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}