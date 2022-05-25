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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsListActivity extends AppCompatActivity {
    private NewsResponse newsResponse;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        new Thread(() -> getNewsFromInternet()).start();
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
        NewsAdapter adapter = new NewsAdapter(this, news, newsClickListener);
        recyclerView.setAdapter(adapter);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
    }

    private NewsAdapter.OnNewsClickListener createClickListener() {
        NewsAdapter.OnNewsClickListener newsClickListener = new NewsAdapter.OnNewsClickListener() {
            @Override
            public void onStateClick(ArticleDTO item, int position) {
                Intent intent = new Intent(NewsListActivity.this, NewsDetailsActivity.class);
                intent.putExtra(NewsDetailsActivity.KEY_URL, item.getUrl());
                startActivity(intent);
            }
        };
        return newsClickListener;
    }

    private void getNewsFromInternet() {
        OkHttpClient client = NetworkUtils.buildOkHttp();
        Request request = NetworkUtils.buildRequest();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Gson gson = new Gson();
                String jsonResponse = response.body().string();
                newsResponse = gson.fromJson(jsonResponse, NewsResponse.class);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> createRecyclerView());
            }
        });
    }

    private List<ArticleDTO> clean() {
        if (newsResponse != null) {
            List<ArticleDTO> articles = new ArrayList<>();
            for (ArticleDTO article : newsResponse.getResults()) {
                if (!article.getPreviewText().isEmpty()) {
                    articles.add(article);
                }
            }
            return articles;
        }
        return new ArrayList<>();
    }
}