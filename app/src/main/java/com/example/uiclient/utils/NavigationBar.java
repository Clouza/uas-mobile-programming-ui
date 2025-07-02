package com.example.uiclient.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import com.example.uiclient.NewsActivity;
import com.example.uiclient.ProfileActivity;
import com.example.uiclient.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationBar {
    public static void bottomNavigation(Context context, BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();

                if (context instanceof Activity) {
                    Activity currentActivity = (Activity) context;

                    if (itemId == R.id.menuNews) {
                        if (!(currentActivity instanceof NewsActivity)) {
                            Intent intent = new Intent(context, NewsActivity.class);
                            context.startActivity(intent);
                        }
                        return true;
                    } else if (itemId == R.id.menuProfile) {
                        if (!(currentActivity instanceof ProfileActivity)) {
                            Intent intent = new Intent(context, ProfileActivity.class);
                            context.startActivity(intent);
                        }
                        return true;
                    }
                }

                Log.i("NavigationBar", String.valueOf(context instanceof Activity));
                return false;
            }
        });
    }
}
