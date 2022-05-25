package com.example.android.androidacademyviews.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class NetworkUtils {
    private final static String URL = "https://api.nytimes.com/svc/topstories/v2/";
    private final static String ENDPOINT = ".json?api-key=";
    private final static String API_KEY = "H5dt18jNBJ00ROUYrntG1r94Ix3uh82t";

    public static OkHttpClient buildOkHttp() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    public static Request buildRequest(String category) {
        String url = URL + category.toLowerCase() + ENDPOINT + API_KEY;
        return new Request.Builder()
                .get()
                .url(url)
                .build();
    }
}
