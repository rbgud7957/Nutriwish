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

public class CategorySlideFragment extends Fragment {

    private Button selectedButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_slide, container, false);

        // 버튼 참조
        Button btnHealth = view.findViewById(R.id.btn_health);
        Button btnBody = view.findViewById(R.id.btn_body);
        Button btnOther = view.findViewById(R.id.btn_other);

        // 초기 화면과 버튼 설정
        setSelectedButton(btnHealth);
        replaceFragment(new HealthCategoryFragment());

        // 버튼 클릭 이벤트 설정
        btnHealth.setOnClickListener(v -> {
            setSelectedButton(btnHealth);
            replaceFragment(new HealthCategoryFragment());
        });

        btnBody.setOnClickListener(v -> {
            setSelectedButton(btnBody);
            replaceFragment(new BodyCategoryFragment());
        });

        btnOther.setOnClickListener(v -> {
            setSelectedButton(btnOther);
            replaceFragment(new OtherCategoryFragment());
        });

        return view;
    }

    // 선택된 버튼 상태 설정
    private void setSelectedButton(Button button) {
        if (selectedButton != null) {
            selectedButton.setSelected(false); // 이전 버튼 선택 해제
        }
        button.setSelected(true); // 현재 버튼 선택
        selectedButton = button;
    }

    // 프래그먼트를 교체하는 메서드
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
