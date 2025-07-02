package com.example.uiclient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView title = findViewById(R.id.detailTitle);
        TextView time = findViewById(R.id.detailTime);
        TextView content = findViewById(R.id.detailContent);

        Intent intent = getIntent();
        String newsTitle = intent.getStringExtra("title");
        String newsTime = intent.getStringExtra("time");
        String newsContent = intent.getStringExtra("content");

        if (newsTime != null && !newsTime.isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                Date date = inputFormat.parse(newsTime);
                SimpleDateFormat outputFormat = new SimpleDateFormat("EEE dd, yyyy HH:mm", Locale.US);
                outputFormat.setTimeZone(TimeZone.getDefault());

                newsTime = outputFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        title.setText(newsTitle);
        time.setText(newsTime);
        content.setText(newsContent);
    }
}