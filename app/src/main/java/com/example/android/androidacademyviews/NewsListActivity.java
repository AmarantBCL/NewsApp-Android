package com.example.android.androidacademyviews;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

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

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> createRecyclerView(), 2000);
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

    private void createRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE)
                ? new GridLayoutManager(this, 2)
                : new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        List<NewsItem> news = DataUtils.generateNews();
        NewsAdapter.OnStateClickListener newsClickListener = createClickListener();
        NewsAdapter adapter = new NewsAdapter(this, news, newsClickListener);
        recyclerView.setAdapter(adapter);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
    }

    private NewsAdapter.OnStateClickListener createClickListener() {
        NewsAdapter.OnStateClickListener newsClickListener = new NewsAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(NewsItem item, int position) {
                Intent intent = new Intent(NewsListActivity.this, NewsDetailsActivity.class);
                intent.putExtra(NewsDetailsActivity.KEY_PICTURE, item.getImageUrl());
                intent.putExtra(NewsDetailsActivity.KEY_TITLE, item.getTitle());
                intent.putExtra(NewsDetailsActivity.KEY_DATE, item.getPublishDate().toString());
                intent.putExtra(NewsDetailsActivity.KEY_TEXT, item.getFullText());
                startActivity(intent);
            }
        };
        return newsClickListener;
    }
}