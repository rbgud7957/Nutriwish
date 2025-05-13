package com.example.nutriwish;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserFragment extends Fragment {

    private EditText etName, etBirthDate, etId, etPassword;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private Button btnEdit, btnSave, btnLogout;
    private ImageView profileImage;
    private String selectedGender;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        //파이어베이스
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // 프로필 이미지 초기화
        profileImage = view.findViewById(R.id.profileImage);

        // EditText 필드 초기화
        etName = view.findViewById(R.id.etName);
        etBirthDate = view.findViewById(R.id.etBirthDate);
        etId = view.findViewById(R.id.etId);

        // 성별 선택을 위한 RadioGroup과 RadioButton 초기화
        rgGender = view.findViewById(R.id.rgGender);
        rbMale = view.findViewById(R.id.rbMale);
        rbFemale = view.findViewById(R.id.rbFemale);

        // 버튼 초기화
        btnEdit = view.findViewById(R.id.btnEdit);
        btnSave = view.findViewById(R.id.btnSave);
        btnLogout = view.findViewById(R.id.btnLogout);

        // 성별에 따라 프로필 이미지 변경
        rgGender.setOnCheckedChangeListener((group, checkedID) -> {
            // 남자일 경우
            if (checkedID == R.id.rbMale) {
                selectedGender = "남자";
                profileImage.setImageResource(R.drawable.male_profile);
            } else if (checkedID == R.id.rbFemale) {
                selectedGender = "여자";
                profileImage.setImageResource(R.drawable.female_profile);
            }
        });

        // 사용자 정보 로드
        loadUserInfo();

        // 수정 버튼 클릭 시 필드 수정 가능하도록 함
        btnEdit.setOnClickListener(v -> {
            enableEditing(true);
            btnSave.setEnabled(true);
            btnEdit.setEnabled(false);
        });

        // 저장 버튼 클릭 시 Firestore에 업데이트
        btnSave.setOnClickListener(v -> saveUserInfo());

        // 로그아웃 버튼 클릭 시 파이어베이스 로그아웃 처리
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut(); // 파이어베이스 로그아웃 처리
            Toast.makeText(getContext(), "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

            // 로그인 화면으로 이동
            Intent intent = new Intent(getContext(), LoginActivity.class); // LoginActivity로 이동
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            // 현재 Activity 종료
            getActivity().finish();
        });

        return view;
    }

    private void loadUserInfo() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        etName.setText(documentSnapshot.getString("name"));
                        etBirthDate.setText(documentSnapshot.getString("birthdate"));
                        etId.setText(documentSnapshot.getString("email"));

                        // Firestore에서 불러온 성별에 따라 라디오 버튼 및 프로필 이미지 설정
                        String gender = documentSnapshot.getString("gender");
                        if ("남자".equals(gender)) {
                            rbMale.setChecked(true);
                            profileImage.setImageResource(R.drawable.male_profile);
                            selectedGender = "남자";
                        } else if ("여자".equals(gender)) {
                            rbFemale.setChecked(true);
                            profileImage.setImageResource(R.drawable.female_profile);
                            selectedGender = "여자";
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "사용자 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show());
    }

    private void saveUserInfo() {
        String userId = mAuth.getCurrentUser().getUid();
        String name = etName.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();

        // Firestore에 업데이트할 데이터 구성
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name);
        userInfo.put("birthdate", birthDate);
        userInfo.put("gender", selectedGender);

        db.collection("users").document(userId).update(userInfo)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "사용자 정보가 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                    enableEditing(false);
                    btnSave.setEnabled(false);
                    btnEdit.setEnabled(true);
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "정보 업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show());
    }

    private void enableEditing(boolean enabled) {
        etName.setEnabled(enabled);
        etBirthDate.setEnabled(enabled);
        rbMale.setEnabled(enabled);
        rbFemale.setEnabled(enabled);
    }
}