package com.example.uiclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uiclient.request.DetailNewsRequest;
import com.example.uiclient.response.DetailNewsResponse;
import com.example.uiclient.response.LoginResponse;
import com.example.uiclient.utils.ApiService;
import com.example.uiclient.utils.RetrofitClient;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.noties.markwon.Markwon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private ApiService apiService;

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

        try {
            apiService = RetrofitClient.client().create(ApiService.class);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        TextView title = findViewById(R.id.detailTitle);
        TextView time = findViewById(R.id.detailTime);
        TextView content = findViewById(R.id.detailContent);
        Button macroButton = findViewById(R.id.buttonMacro);
        Button recomendationButton = findViewById(R.id.buttonRecommendation);

        Intent intent = getIntent();
        String newsTitle = intent.getStringExtra("title");
        String newsTime = intent.getStringExtra("time");
        final String[] newsContent = {intent.getStringExtra("content")};

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

        // plain old set text
        title.setText(newsTitle);
        time.setText(newsTime);

        macroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText("Sedang mendapatkan detail...");
                DetailNewsRequest detailNewsRequest = new DetailNewsRequest(newsContent[0]);
                Call<DetailNewsResponse> call = apiService.macro(detailNewsRequest);

                call.enqueue(new Callback<DetailNewsResponse>() {
                    @Override
                    public void onResponse(Call<DetailNewsResponse> call, Response<DetailNewsResponse> response) {
                        if (response.isSuccessful()) {
                            DetailNewsResponse detailNewsResponse = response.body();

                            newsContent[0] = detailNewsResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
                            Markwon markwon = Markwon.create(DetailActivity.this);
                            String markdown = newsContent[0].replace("\\n", "\n");
                            markwon.setMarkdown(content, markdown);
                        } else {
                            Toast.makeText(DetailActivity.this, "External API Error", Toast.LENGTH_SHORT).show();
                            content.setText("Gagal mendapatkan detail.");
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailNewsResponse> call, Throwable error) {
                        Toast.makeText(DetailActivity.this, "External API Error", Toast.LENGTH_SHORT).show();
                        Log.wtf("DetailActivity", error.getMessage());
                        content.setText("Gagal mendapatkan detail.");
                    }
                });
            }
        });

        recomendationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText("Sedang mendapatkan detail...");
                DetailNewsRequest detailNewsRequest = new DetailNewsRequest(newsContent[0]);
                Call<DetailNewsResponse> call = apiService.recommendation(detailNewsRequest);

                call.enqueue(new Callback<DetailNewsResponse>() {
                    @Override
                    public void onResponse(Call<DetailNewsResponse> call, Response<DetailNewsResponse> response) {
                        if (response.isSuccessful()) {
                            DetailNewsResponse detailNewsResponse = response.body();

                            newsContent[0] = detailNewsResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
                            Markwon markwon = Markwon.create(DetailActivity.this);
                            String markdown = newsContent[0].replace("\\n", "\n");
                            markwon.setMarkdown(content, markdown);
                        } else {
                            Toast.makeText(DetailActivity.this, "External API Error", Toast.LENGTH_SHORT).show();
                            content.setText("Gagal mendapatkan detail.");
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailNewsResponse> call, Throwable error) {
                        Toast.makeText(DetailActivity.this, "External API Error", Toast.LENGTH_SHORT).show();
                        Log.wtf("DetailActivity", error.getMessage());
                        content.setText("Gagal mendapatkan detail.");
                    }
                });
            }
        });
    }
}