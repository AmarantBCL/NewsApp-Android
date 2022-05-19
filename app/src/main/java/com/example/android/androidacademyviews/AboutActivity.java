package com.example.android.androidacademyviews;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_OPEN = 1;

    private ImageView imageView;
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        imageView = findViewById(R.id.img_avatar);
        ImageView facebook = findViewById(R.id.img_facebook);
        ImageView telegram = findViewById(R.id.img_telegram);
        ImageView instagram = findViewById(R.id.img_instagram);

        facebook.setOnClickListener(v -> {
            Uri webpage = Uri.parse("https://ms-my.facebook.com/vadim.petelsky");
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(intent);
        });
        telegram.setOnClickListener(v -> {
            Uri webpage = Uri.parse("https://t.me/VadymPetelskiy");
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(intent);
        });
        instagram.setOnClickListener(v -> {
            Uri webpage = Uri.parse("https://www.instagram.com/vadympetelskyi/");
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(intent);
        });

        imageView.setImageResource(R.drawable.vados);
        imageView.setOnClickListener(v -> selectImage());

        Button sendButton = findViewById(R.id.btn_send);
        edit = findViewById(R.id.edit_enter_message);
        sendButton.setOnClickListener(v ->
                composeEmail(new String[] {"vadamarant@gmail.com"}, edit.getText().toString()));
    }

    public void composeEmail(String[] addresses, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(intent);
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_IMAGE_OPEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            imageView.setImageURI(fullPhotoUri);
        }
    }
}