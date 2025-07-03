package com.example.uiclient.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import okhttp3.Call;

@GlideModule
public class CustomGlide extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        try {
            registry.replace(
                    GlideUrl.class,
                    InputStream.class,
                    new OkHttpUrlLoader.Factory((Call.Factory) RetrofitClient.getUnsafeOkHttpClient())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}