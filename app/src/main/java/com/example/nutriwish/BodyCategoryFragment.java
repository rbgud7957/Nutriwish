package com.example.nutriwish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class BodyCategoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_body_category, container, false);

        Button btnDiet = view.findViewById(R.id.btn_diet);
        Button btnSleep = view.findViewById(R.id.btn_sleep);
        Button btnJoint = view.findViewById(R.id.btn_joint);

        btnDiet.setOnClickListener(v -> openCategoryDetailFragment("다이어트 & 체중 관리"));
        btnSleep.setOnClickListener(v -> openCategoryDetailFragment("숙면 & 스트레스 완화"));
        btnJoint.setOnClickListener(v -> openCategoryDetailFragment("관절 건강"));


        return view;
    }

    private void openCategoryDetailFragment(String categoryName) {
        CategoryDetailFragment detailFragment = new CategoryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category", categoryName);
        detailFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}