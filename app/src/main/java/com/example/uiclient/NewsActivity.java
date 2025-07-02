package com.example.uiclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uiclient.model.NewsItem;
import com.example.uiclient.request.ApiRequest;
import com.example.uiclient.request.NewsRequest;
import com.example.uiclient.response.ApiResponse;
import com.example.uiclient.response.LoginResponse;
import com.example.uiclient.response.NewsResponse;
import com.example.uiclient.utils.ApiService;
import com.example.uiclient.utils.NavigationBar;
import com.example.uiclient.utils.NewsAdapter;
import com.example.uiclient.utils.RetrofitClient;
import com.example.uiclient.utils.Storage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private List<NewsItem> newsList = new ArrayList<>();
    private Storage storage;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        NavigationBar.bottomNavigation(this, bottomNavigationView);
        storage = new Storage(this);
        apiService = RetrofitClient.client().create(ApiService.class);

        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(newsList, item -> {
            Intent intent = new Intent(NewsActivity.this, DetailActivity.class);
            intent.putExtra("title", item.getContent());
            intent.putExtra("time", item.getTimestamp());
            intent.putExtra("content", item.getContent());
            startActivity(intent);
        });
        newsRecyclerView.setAdapter(newsAdapter);

        getNewsData();
    }

    private void getNewsData() {
        // check api key
        String apiKey = storage.getApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            setApiKeyFirstDialog();
        }

        ApiRequest apiRequest = new ApiRequest(apiKey);
        Call<ApiResponse> callApi = apiService.apiKey(apiRequest);

        callApi.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();

                    if (apiResponse.isSuccess()) {
                        storage.saveApiKey(apiResponse.getApiKey());
                        Toast.makeText(NewsActivity.this, "API Key Valid", Toast.LENGTH_SHORT).show();
                    } else {
                        setApiKeyFirstDialog();
                    }
                } else {
                    Toast.makeText(NewsActivity.this, "Web Service Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable error) {
                Toast.makeText(NewsActivity.this, "Koneksi Error", Toast.LENGTH_SHORT).show();
                Log.wtf("NewsActivity", error.getMessage());
            }
        });

        // get news
        NewsRequest newsRequest = new NewsRequest(apiKey);
        Call<NewsResponse> callNews = apiService.news(newsRequest);

        callNews.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse newsResponse = response.body();

                    if (newsResponse.isSuccess()) {
                        newsAdapter.updateNewsList(newsResponse.getNews());
                        Log.d("NewsActivity", "Data berita berhasil dimuat: " + newsResponse.getNews().size() + " item");
                    } else {
                        setApiKeyFirstDialog();
                    }
                } else {
                    Toast.makeText(NewsActivity.this, "Web Service Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable error) {
                Toast.makeText(NewsActivity.this, "Koneksi Error", Toast.LENGTH_SHORT).show();
                Log.wtf("NewsActivity", error.getMessage());
            }
        });
    }

    private void setApiKeyFirstDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("API Key Kosong/Kadaluarsa");
        builder.setMessage("Silakan isi API Key terlebih dahulu.");

        builder.setPositiveButton("Isi API Key", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(NewsActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}