package com.example.android.androidacademyviews.utils;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.android.androidacademyviews.model.dto.ArticleDTO;
import com.example.android.androidacademyviews.model.dto.NewsResponse;
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
import okhttp3.logging.HttpLoggingInterceptor;

public class NetworkUtils {
    private final static String URL = "https://api.nytimes.com/svc/topstories/v2/";
    private final static String ENDPOINT = ".json?api-key=";
    private final static String API_KEY = "H5dt18jNBJ00ROUYrntG1r94Ix3uh82t";

    public static OnCallListener listener;

    public interface OnCallListener {
        void finish(List<ArticleDTO> news);
    }

    public static void call(String category) {
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
                NewsResponse newsResponse = gson.fromJson(jsonResponse, NewsResponse.class);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> listener.finish(cleanResponse(newsResponse)));
            }
        });
    }

    private static OkHttpClient buildOkHttp() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    private static Request buildRequest(String category) {
        String url = URL + category.toLowerCase() + ENDPOINT + API_KEY;
        return new Request.Builder()
                .get()
                .url(url)
                .build();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static List<ArticleDTO> cleanResponse(NewsResponse newsResponse) {
        if (newsResponse != null) {
            return newsResponse.getResults().stream()
                    .filter(a -> !a.getPreviewText().isEmpty())
                    .sorted((a1, a2) -> a2.getPublishDate().compareTo(a1.getPublishDate()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
