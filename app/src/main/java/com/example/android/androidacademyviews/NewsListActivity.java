package com.example.android.androidacademyviews;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.androidacademyviews.model.dto.ArticleDTO;
import com.example.android.androidacademyviews.model.dto.NewsResponse;
import com.example.android.androidacademyviews.utils.NetworkUtils;
import com.example.android.androidacademyviews.viewmodel.NewsAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsListActivity extends AppCompatActivity {
    private NewsResponse newsResponse;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        List<ArticleDTO> news = clean();
        NewsAdapter.OnNewsClickListener newsClickListener = createClickListener();
        adapter = new NewsAdapter(this, news, newsClickListener);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    private NewsAdapter.OnNewsClickListener createClickListener() {
        NewsAdapter.OnNewsClickListener newsClickListener = (item, position) -> {
            Intent intent = new Intent(NewsListActivity.this, NewsDetailsActivity.class);
            intent.putExtra(NewsDetailsActivity.KEY_URL, item.getUrl());
            startActivity(intent);
        };
        return newsClickListener;
    }

    private void getNewsFromInternet() {
        OkHttpClient client = NetworkUtils.buildOkHttp();
        Request request = NetworkUtils.buildRequest(category);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Gson gson = new Gson();
                String jsonResponse = response.body().string();
                newsResponse = gson.fromJson(jsonResponse, NewsResponse.class);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    if (adapter == null) {
                        createRecyclerView();
                    } else {
                        List<ArticleDTO> news = clean();
                        adapter.setNewsList(news);
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<ArticleDTO> clean() {
        if (newsResponse != null) {
            return newsResponse.getResults().stream()
                    .filter(a -> !a.getPreviewText().isEmpty())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
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