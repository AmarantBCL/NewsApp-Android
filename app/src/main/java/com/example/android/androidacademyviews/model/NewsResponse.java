package com.example.android.androidacademyviews.model;

import androidx.annotation.Nullable;

import java.util.List;

public class NewsResponse {
    private List<ArticleDTO> results;

    @Nullable
    public List<ArticleDTO> getResults() {
        return results;
    }
}
