package com.example.android.androidacademyviews.model.dto;

import androidx.annotation.Nullable;

import com.example.android.androidacademyviews.model.dto.ArticleDTO;

import java.util.List;

public class NewsResponse {
    private List<ArticleDTO> results;

    @Nullable
    public List<ArticleDTO> getResults() {
        return results;
    }
}
