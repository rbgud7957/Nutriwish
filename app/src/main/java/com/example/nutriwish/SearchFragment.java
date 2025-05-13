package com.example.nutriwish;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchFragment extends Fragment {

    private EditText searchInput;
    private Button searchButton;
    private Button favoriteButton;
    private ListView resultListView;

    private Map<String, Supplement> supplementData;
    private List<String> suggestions;
    private SearchAdapter searchAdapter;
    private Set<String> favoriteSupplements; // 즐겨찾기 데이터 저장

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // View 요소 초기화
        searchInput = view.findViewById(R.id.searchInput);
        searchButton = view.findViewById(R.id.searchButton);
        favoriteButton = view.findViewById(R.id.favoriteButton);
        resultListView = view.findViewById(R.id.resultListView);

        // 영양제 데이터를 SupplementData에서 가져옴
        supplementData = loadAllSupplements();
        suggestions = new ArrayList<>(supplementData.keySet());

        // SharedPreferences로부터 즐겨찾기 데이터 불러오기
        favoriteSupplements = loadFavoriteSupplements();

        // 어댑터 초기화 및 클릭 리스너 설정
        searchAdapter = new SearchAdapter(getContext(), new ArrayList<>(), this::openSupplementDetail);
        searchAdapter.setFavoriteSupplements(favoriteSupplements);
        resultListView.setAdapter(searchAdapter);

        // 검색 버튼 클릭 이벤트 처리
        searchButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString();
            filterSearchResults(query); // 검색어에 맞는 결과 필터링
        });

        // 즐겨찾기 버튼 클릭 이벤트 처리
        favoriteButton.setOnClickListener(v -> {
            List<String> favoriteList = new ArrayList<>(favoriteSupplements);
            searchAdapter.clear();
            searchAdapter.addAll(favoriteList);
            searchAdapter.notifyDataSetChanged();
        });

        return view;
    }

    // 모든 카테고리의 영양제를 한 번에 불러오는 메서드
    private Map<String, Supplement> loadAllSupplements() {
        Map<String, Supplement> allSupplements = new HashMap<>();
        Map<String, List<Supplement>> categorySupplements = SupplementData.getCategorySupplements();

        for (List<Supplement> supplementList : categorySupplements.values()) {
            for (Supplement supplement : supplementList) {
                allSupplements.put(supplement.getName(), supplement);
            }
        }

        return allSupplements;
    }

    // SharedPreferences에서 즐겨찾기 데이터를 로드하는 메서드
    private Set<String> loadFavoriteSupplements() {
        return new HashSet<>(requireContext()
                .getSharedPreferences("favorites", Context.MODE_PRIVATE)
                .getStringSet("favorites", new HashSet<>()));
    }

    // SharedPreferences에 즐겨찾기 데이터를 저장하는 메서드
    private void saveFavoriteSupplements() {
        requireContext()
                .getSharedPreferences("favorites", Context.MODE_PRIVATE)
                .edit()
                .putStringSet("favorites", favoriteSupplements)
                .apply();
    }

    // 검색어에 맞는 결과를 필터링하는 메서드
    private void filterSearchResults(String query) {
        List<String> filteredResults = new ArrayList<>();

        if (!query.isEmpty()) {
            for (String suggestion : suggestions) {
                if (suggestion.toLowerCase().contains(query.toLowerCase())) {
                    filteredResults.add(suggestion);
                }
            }
        }

        // 어댑터의 데이터를 갱신 (필터링된 검색어 리스트로 갱신)
        searchAdapter.clear();
        searchAdapter.addAll(filteredResults);
        searchAdapter.notifyDataSetChanged();
    }

    // 검색 결과 클릭 시 영양제 상세 정보를 표시하는 메서드
    private void openSupplementDetail(String supplementName) {
        Supplement supplement = supplementData.get(supplementName);
        if (supplement != null) {
            SupplementDetailFragment detailFragment = new SupplementDetailFragment();
            Bundle bundle = new Bundle();

            // 선택한 영양제 정보를 번들에 저장
            List<Supplement> supplements = new ArrayList<>();
            supplements.add(supplement);
            bundle.putSerializable("supplements", (ArrayList<Supplement>) supplements);
            bundle.putInt("index", 0); // 현재는 검색에서 하나의 영양제만 선택하므로 index는 0
            bundle.putInt("minIndex", 0);
            bundle.putInt("maxIndex", 0);

            detailFragment.setArguments(bundle);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveFavoriteSupplements(); // 프래그먼트가 멈출 때 즐겨찾기 저장
    }
}
