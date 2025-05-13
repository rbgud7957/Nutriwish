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

public class OtherCategoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_category, container, false);

        Button btnBrain = view.findViewById(R.id.btn_brain);
        Button btnSkin = view.findViewById(R.id.btn_skin);
        Button btnEye = view.findViewById(R.id.btn_eye);
        Button btnHeart = view.findViewById(R.id.btn_heart);

        btnBrain.setOnClickListener(v -> openCategoryDetailFragment("두뇌 & 집중력 강화"));
        btnSkin.setOnClickListener(v -> openCategoryDetailFragment("피부 & 모발 건강"));
        btnEye.setOnClickListener(v -> openCategoryDetailFragment("눈 건강"));
        btnHeart.setOnClickListener(v -> openCategoryDetailFragment("심장 & 혈관 건강"));

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