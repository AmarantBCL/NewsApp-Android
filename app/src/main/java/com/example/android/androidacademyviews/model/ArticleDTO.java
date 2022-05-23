package com.example.android.androidacademyviews.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class ArticleDTO {
    @SerializedName("section")
    private String section;
    @SerializedName("subsection")
    private String category;
    @SerializedName("title")
    private String title;
    @SerializedName("abstract")
    private String previewText;
    @SerializedName("published_date")
    private Date publishDate;
    @SerializedName("multimedia")
    private List<ImagesDTO> images;

    public String getSection() {
        return section;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getPreviewText() {
        return previewText;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public List<ImagesDTO> getImages() {
        return images;
    }
}
