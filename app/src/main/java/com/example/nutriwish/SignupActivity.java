package com.example.nutriwish;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends AppCompatActivity {

    private TextView back;
    private EditText name, pw, pw2, email, birthdate;
    private RadioButton radio_men, radio_women;
    private RadioGroup radioGroup;
    private Button pwcheck, submit;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Firebase 초기화
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //회원가입 요소 초기화
        radio_men = findViewById(R.id.radio_men);
        radio_women = findViewById(R.id.radio_women);
        radioGroup = findViewById(R.id.radio_group);
        name = findViewById(R.id.signName);
        pw = findViewById(R.id.signPW);
        pw2 = findViewById(R.id.signPW2);
        email = findViewById(R.id.signEmail);
        birthdate = findViewById(R.id.signbirthdate);
        pwcheck = findViewById(R.id.pwcheckbutton);
        submit = findViewById(R.id.signupbutton);

        //뒤로 가기 버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());



        //비밀번호 확인 버튼
        pwcheck.setOnClickListener(v -> {
            if (pw.getText().toString().equals(pw2.getText().toString())) {
                Toast.makeText(SignupActivity.this, "비밀번호가 일치합니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SignupActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
            }
        });

        //회원가입 완료 버튼
        submit.setOnClickListener(v -> registerUser());
    }

    private void registerUser(){
        String emailInput = email.getText().toString().trim();
        String passwordInput = pw.getText().toString().trim();
        String confirmPasswordInput = pw2.getText().toString().trim();
        String nameInput = name.getText().toString().trim();
        String birthdateInput = birthdate.getText().toString().trim();

        // 성별
        int selectedGenderId = radioGroup.getCheckedRadioButtonId();
        String genderInput;
        if(selectedGenderId == R.id.radio_men) genderInput = "남자";
        else if(selectedGenderId == R.id.radio_women) genderInput = "여자";
        else {
            genderInput = "";
        }

        // 이메일을 잘 입력했는지 확인
        if(emailInput.isEmpty() || passwordInput.isEmpty() || confirmPasswordInput.isEmpty() ||
           nameInput.isEmpty() || genderInput.isEmpty() || birthdateInput.isEmpty()) {
            Toast.makeText(this, "모든 사항은 필수 입력해야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!passwordInput.equals(confirmPasswordInput)){
            Toast.makeText(this,"비밀번호가 일치자힞 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase 인증으로 이메일/비밀번호 회원가입
        mAuth.createUserWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        //회원가입 성공 시 정보 저장
                        saveUserInfoToFirestore(nameInput, genderInput, birthdateInput);
                    } else Toast.makeText(this, "회원가입 실패: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void saveUserInfoToFirestore(String name, String gender, String birthdate){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            String userId = user.getUid();

            //파이어베이스에 저장할 사용자 데이터들
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("name", name);
            userInfo.put("gender", gender);
            userInfo.put("birthdate", birthdate);
            userInfo.put("email", user.getEmail());

            //파이어베이스에 사용자 정보 저장하기
            db.collection("users").document(userId).set(userInfo)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                        // 회원가입 성공 시 로그인 화면으로 이동하기
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "사용자 정보 저장 실패", Toast.LENGTH_SHORT).show());
        }
    }
}