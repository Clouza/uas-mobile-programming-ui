package com.example.uiclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uiclient.request.LoginRequest;
import com.example.uiclient.response.LoginResponse;
import com.example.uiclient.utils.ApiService;
import com.example.uiclient.utils.RetrofitClient;
import com.example.uiclient.utils.Storage;

import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText email, password;
    private Button login;
    private ApiService apiService;
    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.buttonLogin);
        storage = new Storage(this);

        try {
            apiService = RetrofitClient.client().create(ApiService.class);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
    }

    private void doLogin() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(userEmail, userPassword);
        Call<LoginResponse> call = apiService.login(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {
                        // save id di sharedPreferences
                        storage.saveUserId(loginResponse.getId());

                        Toast.makeText(MainActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Kredensial salah", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Web Service Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable error) {
                Toast.makeText(MainActivity.this, "Koneksi Error", Toast.LENGTH_SHORT).show();
                Log.wtf("MainActivity", error.getMessage());
            }
        });
    }
}