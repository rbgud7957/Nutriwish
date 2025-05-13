package com.example.nutriwish;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryDetailFragment extends Fragment {

    private String selectedCategory;
    private Map<String, List<Supplement>> categorySupplements;

    private int cnt = 0;
    public CategoryDetailFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_detail, container, false);
        // 뒤로가기 버튼 설정
        Button backButton = view.findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> {
            cnt++;
            // Log.d("test","뒤로가기 버튼 클릭 : " + cnt);
            getParentFragmentManager().popBackStack();
        });

        // 선택된 카테고리 정보를 받아옴
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedCategory = bundle.getString("category");
        }

        // 영양제 데이터를 가져옴
        categorySupplements = SupplementData.getCategorySupplements();

        // 선택한 카테고리에 따라 영양제 버튼을 동적으로 생성
        LinearLayout buttonContainer = view.findViewById(R.id.button_container);

        if (selectedCategory != null && categorySupplements.containsKey(selectedCategory)) {
            List<Supplement> supplements = categorySupplements.get(selectedCategory);
            addButtons(buttonContainer, supplements);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 모든 카테고리 버튼 숨기기
        hideCategoryButtons();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 모든 카테고리 버튼 다시 표시
        showCategoryButtons();
    }

    private void addButtons(LinearLayout container, List<Supplement> supplements) {
        for (int i = 0; i < supplements.size(); i++) {
            Supplement supplement = supplements.get(i);
            Button button = new Button(getContext());
            button.setText(supplement.getName());

            button.setBackgroundColor(Color.parseColor("#f3fbf3"));

            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.gamtan);
            button.setTypeface(typeface);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 20, 20, 0);
            button.setLayoutParams(params);

            int finalIndex = i;
            button.setOnClickListener(v -> openSupplementDetailFragment(supplements, finalIndex));

            container.addView(button);
        }
    }

    private void openSupplementDetailFragment(List<Supplement> supplements, int index) {
        SupplementDetailFragment detailFragment = new SupplementDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("supplements", (ArrayList<Supplement>) supplements);
        bundle.putInt("index", index);
        detailFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void hideCategoryButtons() {
        // 소화 개선 버튼 숨기기
        View btnDigestion = getActivity().findViewById(R.id.btn_digestion);
        if (btnDigestion != null) {
            btnDigestion.setVisibility(View.GONE);
        }

        // 관절 건강 버튼 숨기기
        View btnJoint = getActivity().findViewById(R.id.btn_joint);
        if (btnJoint != null) {
            btnJoint.setVisibility(View.GONE);
        }

        // 면역 강화 버튼 숨기기
        View btnImmunity = getActivity().findViewById(R.id.btn_immunity);
        if (btnImmunity != null) {
            btnImmunity.setVisibility(View.GONE);
        }

        // 피로 회복 버튼 숨기기
        View btnFatigue = getActivity().findViewById(R.id.btn_fatigue);
        if (btnFatigue != null) {
            btnFatigue.setVisibility(View.GONE);
        }

        // 다이어트 & 체중 관리 버튼 숨기기
        View btnDiet = getActivity().findViewById(R.id.btn_diet);
        if (btnDiet != null) {
            btnDiet.setVisibility(View.GONE);
        }

        // 숙면 & 스트레스 완화 버튼 숨기기
        View btnSleep = getActivity().findViewById(R.id.btn_sleep);
        if (btnSleep != null) {
            btnSleep.setVisibility(View.GONE);
        }

        // 두뇌 & 집중력 강화 버튼 숨기기
        View btnBrain = getActivity().findViewById(R.id.btn_brain);
        if (btnBrain != null) {
            btnBrain.setVisibility(View.GONE);
        }

        // 피부 & 모발 건강 버튼 숨기기
        View btnSkin = getActivity().findViewById(R.id.btn_skin);
        if (btnSkin != null) {
            btnSkin.setVisibility(View.GONE);
        }

        // 눈 건강 버튼 숨기기
        View btnEye = getActivity().findViewById(R.id.btn_eye);
        if (btnEye != null) {
            btnEye.setVisibility(View.GONE);
        }

        // 심장 & 혈관 건강 버튼 숨기기
        View btnHeart = getActivity().findViewById(R.id.btn_heart);
        if (btnHeart != null) {
            btnHeart.setVisibility(View.GONE);
        }
    }

    private void showCategoryButtons() {
        // 소화 개선 버튼 표시
        View btnDigestion = getActivity().findViewById(R.id.btn_digestion);
        if (btnDigestion != null) {
            btnDigestion.setVisibility(View.VISIBLE);
        }

        // 관절 건강 버튼 표시
        View btnJoint = getActivity().findViewById(R.id.btn_joint);
        if (btnJoint != null) {
            btnJoint.setVisibility(View.VISIBLE);
        }

        // 면역 강화 버튼 표시
        View btnImmunity = getActivity().findViewById(R.id.btn_immunity);
        if (btnImmunity != null) {
            btnImmunity.setVisibility(View.VISIBLE);
        }

        // 피로 회복 버튼 표시
        View btnFatigue = getActivity().findViewById(R.id.btn_fatigue);
        if (btnFatigue != null) {
            btnFatigue.setVisibility(View.VISIBLE);
        }

        // 다이어트 & 체중 관리 버튼 표시
        View btnDiet = getActivity().findViewById(R.id.btn_diet);
        if (btnDiet != null) {
            btnDiet.setVisibility(View.VISIBLE);
        }

        // 숙면 & 스트레스 완화 버튼 표시
        View btnSleep = getActivity().findViewById(R.id.btn_sleep);
        if (btnSleep != null) {
            btnSleep.setVisibility(View.VISIBLE);
        }

        // 두뇌 & 집중력 강화 버튼 표시
        View btnBrain = getActivity().findViewById(R.id.btn_brain);
        if (btnBrain != null) {
            btnBrain.setVisibility(View.VISIBLE);
        }

        // 피부 & 모발 건강 버튼 표시
        View btnSkin = getActivity().findViewById(R.id.btn_skin);
        if (btnSkin != null) {
            btnSkin.setVisibility(View.VISIBLE);
        }

        // 눈 건강 버튼 표시
        View btnEye = getActivity().findViewById(R.id.btn_eye);
        if (btnEye != null) {
            btnEye.setVisibility(View.VISIBLE);
        }

        // 심장 & 혈관 건강 버튼 표시
        View btnHeart = getActivity().findViewById(R.id.btn_heart);
        if (btnHeart != null) {
            btnHeart.setVisibility(View.VISIBLE);
        }
    }
}
