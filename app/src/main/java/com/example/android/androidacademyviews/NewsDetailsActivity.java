package com.example.android.androidacademyviews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class NewsDetailsActivity extends AppCompatActivity {
    public static final String KEY_PICTURE = "KEY_PICTURE";
    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_DATE = "KEY_DATE";
    public static final String KEY_TEXT = "KEY_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        Intent intent = getIntent();
        String pictureUrl = intent.getStringExtra(KEY_PICTURE);
        String title = intent.getStringExtra(KEY_TITLE);
        String date = intent.getStringExtra(KEY_DATE);
        String text = intent.getStringExtra(KEY_TEXT);

        ImageView imageView = findViewById(R.id.img_picture);
        TextView titleView = findViewById(R.id.tv_title);
        TextView dateView = findViewById(R.id.tv_date);
        TextView textView = findViewById(R.id.tv_text);
        Glide.with(this).load(pictureUrl).into(imageView);
        titleView.setText(title);
        dateView.setText(date);
        textView.setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}