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

public class HealthCategoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health_category, container, false);

        Button btnImmunity = view.findViewById(R.id.btn_immunity);
        Button btnFatigue = view.findViewById(R.id.btn_fatigue);
        Button btnDigestion = view.findViewById(R.id.btn_digestion);


        btnImmunity.setOnClickListener(v -> openCategoryDetailFragment("면역 강화"));
        btnFatigue.setOnClickListener(v -> openCategoryDetailFragment("피로 회복"));
        btnDigestion.setOnClickListener(v -> openCategoryDetailFragment("소화 개선"));

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
