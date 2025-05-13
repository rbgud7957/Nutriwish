package com.example.nutriwish;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SupplementDetailFragment extends Fragment {

    private String supplementName;
    private String benefits;
    private String usage;
    private String precautions;

    private int currentIndex;
    private int minIndex;
    private int maxIndex;
    private List<Supplement> supplements;
    private Set<String> favoriteSupplements;

    private int cnt = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supplement_detail, container, false);

        // 뒤로가기 버튼 설정
        Button backButton = view.findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> {
            cnt++;
            Log.d("test","뒤로가기 버튼 클릭 : " + cnt);
            getParentFragmentManager().popBackStack();
        });

        // SharedPreferences로 즐겨찾기 데이터 로드
        favoriteSupplements = new HashSet<>(requireContext()
                .getSharedPreferences("favorites", Context.MODE_PRIVATE)
                .getStringSet("favorites", new HashSet<>()));

        // Supplement 정보 받음
        Bundle bundle = getArguments();
        if (bundle != null) {
            supplements = (List<Supplement>) bundle.getSerializable("supplements");
            currentIndex = bundle.getInt("index", 0);
            minIndex = bundle.getInt("minIndex", 0);
            maxIndex = bundle.getInt("maxIndex", supplements.size() - 1);

            Supplement currentSupplement = supplements.get(currentIndex);
            supplementName = currentSupplement.getName();
            benefits = currentSupplement.getBenefits();
            usage = currentSupplement.getUsage();
            precautions = currentSupplement.getPrecautions();
        }

        // View 참조
        TextView supplementTitle = view.findViewById(R.id.supplement_title);
        ImageView supplementImage = view.findViewById(R.id.supplement_image);
        TextView textContent = view.findViewById(R.id.text_content);
        Button buttonBenefits = view.findViewById(R.id.button_benefits);
        Button buttonUsage = view.findViewById(R.id.button_usage);
        Button buttonPrecautions = view.findViewById(R.id.button_precautions);
        Button buttonPrevious = view.findViewById(R.id.button_previous);
        Button buttonNext = view.findViewById(R.id.button_next);
        Button favoriteToggleButton = view.findViewById(R.id.button_favorite_toggle);

        // 영양제 이름 설정
        supplementTitle.setText(supplementName);
        supplementImage.setImageResource(R.drawable.supplement);

        // 즐겨찾기 상태 초기화
        updateFavoriteButton(favoriteToggleButton);

        // 즐겨찾기 토글 버튼 클릭 리스너
        favoriteToggleButton.setOnClickListener(v -> {
            if (favoriteSupplements.contains(supplementName)) {
                favoriteSupplements.remove(supplementName);
            } else {
                favoriteSupplements.add(supplementName);
            }
            updateFavoriteButton(favoriteToggleButton);
            saveFavorites();
        });

        // 효능 버튼 클릭 리스너
        buttonBenefits.setOnClickListener(v -> {
            textContent.setText(benefits);
            textContent.setVisibility(View.VISIBLE);
        });

        // 복용법 버튼 클릭 리스너
        buttonUsage.setOnClickListener(v -> {
            textContent.setText(usage);
            textContent.setVisibility(View.VISIBLE);
        });

        // 주의사항 버튼 클릭 리스너
        buttonPrecautions.setOnClickListener(v -> {
            textContent.setText(precautions);
            textContent.setVisibility(View.VISIBLE);
        });

        // 이전 버튼 클릭 리스너
        buttonPrevious.setOnClickListener(v -> {
            if (currentIndex > minIndex) {
                openSupplementDetail(currentIndex - 1);
            } else {
                textContent.setText("첫 번째 영양제입니다.");
                textContent.setVisibility(View.VISIBLE);
            }
        });

        // 다음 버튼 클릭 리스너
        buttonNext.setOnClickListener(v -> {
            if (currentIndex < maxIndex) {
                openSupplementDetail(currentIndex + 1);
            } else {
                textContent.setText("마지막 영양제입니다.");
                textContent.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideCategoryButtons();
    }

    private void updateFavoriteButton(Button button) {
        if (favoriteSupplements.contains(supplementName)) {
            button.setText("즐겨찾기 해제");
            button.setBackgroundColor(Color.parseColor("#fcce75"));
            button.setTextColor(Color.BLACK);
        } else {
            button.setText("즐겨찾기 추가");
            button.setBackgroundColor(Color.LTGRAY);
            button.setTextColor(Color.BLACK);
        }
    }

    private void saveFavorites() {
        requireContext()
                .getSharedPreferences("favorites", Context.MODE_PRIVATE)
                .edit()
                .putStringSet("favorites", favoriteSupplements)
                .apply();
    }

    private void openSupplementDetail(int index) {
        SupplementDetailFragment detailFragment = new SupplementDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("supplements", (ArrayList<Supplement>) supplements);
        bundle.putInt("index", index);
        bundle.putInt("minIndex", minIndex);
        bundle.putInt("maxIndex", maxIndex);
        detailFragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void hideCategoryButtons() {
        View btnDigestion = getActivity().findViewById(R.id.btn_digestion);
        if (btnDigestion != null) btnDigestion.setVisibility(View.GONE);

        View btnJoint = getActivity().findViewById(R.id.btn_joint);
        if (btnJoint != null) btnJoint.setVisibility(View.GONE);

        View btnImmunity = getActivity().findViewById(R.id.btn_immunity);
        if (btnImmunity != null) btnImmunity.setVisibility(View.GONE);

        View btnFatigue = getActivity().findViewById(R.id.btn_fatigue);
        if (btnFatigue != null) btnFatigue.setVisibility(View.GONE);

        View btnDiet = getActivity().findViewById(R.id.btn_diet);
        if (btnDiet != null) btnDiet.setVisibility(View.GONE);

        View btnSleep = getActivity().findViewById(R.id.btn_sleep);
        if (btnSleep != null) btnSleep.setVisibility(View.GONE);

        View btnBrain = getActivity().findViewById(R.id.btn_brain);
        if (btnBrain != null) btnBrain.setVisibility(View.GONE);

        View btnSkin = getActivity().findViewById(R.id.btn_skin);
        if (btnSkin != null) btnSkin.setVisibility(View.GONE);

        View btnEye = getActivity().findViewById(R.id.btn_eye);
        if (btnEye != null) btnEye.setVisibility(View.GONE);

        View btnHeart = getActivity().findViewById(R.id.btn_heart);
        if (btnHeart != null) btnHeart.setVisibility(View.GONE);
    }
}

