package com.example.nutriwish;

import android.os.Bundle;
import android.view.MenuItem;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // BottomNavigationView 참조
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        Log.e("test", String.valueOf(findViewById(R.id.fragment_container)));

        // 기본으로 로드될 프래그먼트 설정
        loadFragment(new CategorySlideFragment());

        // 아이템 선택 리스너 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.tab_category) {
                    selectedFragment = new CategorySlideFragment();
                } else if (item.getItemId() == R.id.tab_search) {
                    selectedFragment = new SearchFragment();
                } else if (item.getItemId() == R.id.tab_calendar) {
                    selectedFragment = new CalendarFragment();
                } else if (item.getItemId() == R.id.tab_user) {
                    selectedFragment = new UserFragment();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }

                return true;
            }
        });

    }

    // 프래그먼트 로드 함수
    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        } else {
            Log.e("test", "Fragment가 없음. 로드되지 않음.");
        }
    }
}