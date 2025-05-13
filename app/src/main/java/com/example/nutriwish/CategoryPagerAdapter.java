package com.example.nutriwish;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CategoryPagerAdapter extends FragmentStateAdapter {


    public CategoryPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HealthCategoryFragment(); // 건강
            case 1:
                return new BodyCategoryFragment(); // 신체 관리
            case 2:
            default:
                return new OtherCategoryFragment(); // 기타
        }
    }

    @Override
    public int getItemCount() {
        return 3; // 3개의 슬라이드 페이지
    }
}
