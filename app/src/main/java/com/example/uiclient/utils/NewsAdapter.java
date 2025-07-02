package com.example.uiclient.utils;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uiclient.R;
import com.example.uiclient.model.NewsItem;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<NewsItem> newsList;
    private OnItemClickListener listener;

    public NewsAdapter(List<NewsItem> newsList, OnItemClickListener listener) {
        this.newsList = newsList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(NewsItem item);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem currentNews = newsList.get(position);
        String time = currentNews.getTimestamp();

        if (time != null && !time.isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                Date date = inputFormat.parse(time);
                SimpleDateFormat outputFormat = new SimpleDateFormat("EEE dd, yyyy HH:mm", Locale.US);
                outputFormat.setTimeZone(TimeZone.getDefault());

                time = outputFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.newsTitle.setText(getFirstNWords(currentNews.getContent(), 2));
        holder.newsTime.setText(time);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(currentNews));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView newsTitle;
        public TextView newsTime;

        public NewsViewHolder(View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsTime = itemView.findViewById(R.id.newsTime);
        }
    }

    public void updateNewsList(List<NewsItem> newNewsList) {
        this.newsList = newNewsList;
        notifyDataSetChanged();
    }

    public static String getFirstNWords(String text, int n) {
        String[] words = text.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < Math.min(n, words.length); i++) {
            if (i > 0) result.append(" ");
            result.append(words[i]);
        }
        return result.toString();
    }

}
