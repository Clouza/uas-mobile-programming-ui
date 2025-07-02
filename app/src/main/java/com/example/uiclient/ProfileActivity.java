package com.example.uiclient;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uiclient.request.ProfileRequest;
import com.example.uiclient.response.ProfileResponse;
import com.example.uiclient.utils.ApiService;
import com.example.uiclient.utils.NavigationBar;
import com.example.uiclient.utils.RetrofitClient;
import com.example.uiclient.utils.Storage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    EditText email, password, apiKey;
    Button changeImage, update, logout, news, profile;
    ImageView image;

    private ApiService apiService;
    private Storage storage;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        NavigationBar.bottomNavigation(this, bottomNavigationView);

        storage = new Storage(this);
        String userId = storage.getUserId();
        apiService = RetrofitClient.client().create(ApiService.class);

        email = findViewById(R.id.editTextNewEmail);
        password = findViewById(R.id.editTextNewPassword);
        apiKey = findViewById(R.id.editTextAPIKey);

        changeImage = findViewById(R.id.buttonChangePhoto);
        image = findViewById(R.id.imageViewProfileImage);

        // get user info
        getUserInfo(userId);

        update = findViewById(R.id.buttonUpdateProfile);
        logout = findViewById(R.id.buttonLogout);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileRequest profileRequest = new ProfileRequest(userId, email.getText().toString(), password.getText().toString(), apiKey.getText().toString());
                updateUserInfo(profileRequest);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage.removeUserId();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(ProfileActivity.this, "Logout berhasil!", Toast.LENGTH_SHORT).show();
            }
        });

        // read storage
        readExternalStorage();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        image.setImageURI(uri);
                        Log.i("ProfileActivity", "Selected image: " + uri.toString());
                    }
                }
            }
        });

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String path = Environment.getExternalStorageDirectory() + "/Pictures/";
                // Uri uri = Uri.parse(path);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // intent.setDataAndType(uri, "*/*");
                activityResultLauncher.launch(intent);
            }
        });
    }

    private ProfileResponse getUserInfo(String id) {
        Call<ProfileResponse> call = apiService.userInfo(id);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    ProfileResponse profileResponse = response.body();

                    if (profileResponse.isSuccess()) {
                        email.setText(profileResponse.getEmail());
                        // todo: image url show
                    } else {
                        Toast.makeText(ProfileActivity.this, "Id tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Web Service Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable error) {
                Toast.makeText(ProfileActivity.this, "Koneksi Error", Toast.LENGTH_SHORT).show();
                Log.wtf("ProfileActivity", error.getMessage());
            }
        });
        return new ProfileResponse();
    }

    private void updateUserInfo(ProfileRequest profileRequest) {
        Call<ProfileResponse> call = apiService.updateUserInfo(profileRequest);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    ProfileResponse profileResponse = response.body();

                    if (profileResponse.isSuccess()) {
                        email.setText(profileResponse.getEmail());

                        if (profileResponse.getApiKey() == null) {
                            Toast.makeText(ProfileActivity.this, "Invalid API Key", Toast.LENGTH_SHORT).show();
                        }

                        if (profileResponse.getApiKey() != null && !apiKey.getText().toString().isEmpty()){
                            storage.saveApiKey(profileResponse.getApiKey());
                            apiKey.setText(profileResponse.getApiKey());
                            Toast.makeText(ProfileActivity.this, "API Key Valid", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Id tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Web Service Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable error) {
                Toast.makeText(ProfileActivity.this, "Koneksi Error", Toast.LENGTH_SHORT).show();
                Log.wtf("ProfileActivity", error.getMessage());
            }
        });
    }

    /**
     * /////////////////////////////////////
     *     Image File Operations Section
     * /////////////////////////////////////
     */

    // request permission files
    private void requestPermission(String permissionName) {
        ActivityCompat.requestPermissions(this, new String[]{permissionName}, PERMISSION_REQUEST_CODE);
    }

    // dialog popup
    private void showExplanation(String title, String message, String permission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission(permission);
                    }
                });
    }

    // permission check
    private void readExternalStorage() {
        String permissionName = "";
        if (Build.VERSION.SDK_INT >= 33) {
            permissionName = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permissionName = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this, permissionName);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
                showExplanation("Permission required", "Application need a permission", permissionName);
            } else {
                requestPermission(permissionName);
            }
        } else {
            Toast.makeText(this, "Permission sudah dibuat", Toast.LENGTH_SHORT).show();
        }
    }

    // result of user options
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Access enabled", Toast.LENGTH_SHORT).show();
            }

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Access disabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // get image path, return as string
    private String getImagePath(Context context, Uri uri) {
        Cursor cursor = null;

        try {
            String[] dataMedia = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, dataMedia, null, null, null);

            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(index);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}