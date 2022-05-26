package com.example.android.androidacademyviews;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.androidacademyviews.utils.NetworkUtils;
import com.example.android.androidacademyviews.viewmodel.NewsAdapter;

import java.util.ArrayList;

public class NewsListActivity extends AppCompatActivity {
    private String category = "World";
    private TextView categoryView;
    private ProgressBar progressBar;
    private NewsAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        categoryView = findViewById(R.id.tv_category);
        progressBar = findViewById(R.id.progress_bar);
        categoryClick();
        createRecyclerView();
        new Thread(this::getNewsFromInternet).start();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_switch) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE)
                ? new GridLayoutManager(this, 2)
                : new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        NewsAdapter.OnNewsClickListener newsClickListener = createClickListener();
        adapter = new NewsAdapter(this, new ArrayList<>(), newsClickListener);
        recyclerView.setAdapter(adapter);
    }

    private NewsAdapter.OnNewsClickListener createClickListener() {
        return (item, position) -> {
            Intent intent = new Intent(NewsListActivity.this, NewsDetailsActivity.class);
            intent.putExtra(NewsDetailsActivity.KEY_URL, item.getUrl());
            startActivity(intent);
        };
    }

    private void getNewsFromInternet() {
        NetworkUtils.call(category);
        NetworkUtils.listener = news -> {
            adapter.setNewsList(news);
            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        };
    }

    private void categoryClick() {
        String[] categories = getResources().getStringArray(R.array.news_categories);
        categoryView.setText(category);
        categoryView.setOnLongClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setSingleChoiceItems(categories, 25, (dialog, which) -> category = categories[which])
                    .setPositiveButton("OK", (dialog, which) -> {
                        new Thread(this::getNewsFromInternet).start();
                        categoryView.setText(category);
                        progressBar.setVisibility(View.VISIBLE);
                        adapter.getNewsList().clear();
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            alertDialog.show();
            return true;
        });
    }
}