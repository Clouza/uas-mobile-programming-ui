package com.example.uiclient.utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // todo: ganti icon android dan menu
    private static String BASE_URL = "https://42c4-2a09-bac5-d561-25d7-00-3c5-10.ngrok-free.app";

    public static Retrofit client() throws NoSuchAlgorithmException {
        OkHttpClient client = getUnsafeOkHttpClient();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static String getAssetPath() {
        return BASE_URL + "/public/files/";
    }

    // https://stackoverflow.com/questions/25509296/trusting-all-certificates-with-okhttp/25992879#25992879
    public static OkHttpClient getUnsafeOkHttpClient() throws NoSuchAlgorithmException {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[]{}; }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder.build();
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
}
