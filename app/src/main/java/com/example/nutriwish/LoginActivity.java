package com.example.nutriwish;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextView sign;
    private TextView login;
    private EditText loginEmail, loginPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //파이어베이스 Auth 초기화
        mAuth = FirebaseAuth.getInstance();

        //로그인 UI 초기화
        login = findViewById(R.id.btn_login);
        sign = findViewById(R.id.btn_signup);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);

        //로그인 버튼
        login = findViewById(R.id.btn_login);

        //회원가입 버튼
        sign = findViewById(R.id.btn_signup);

        //로그인 버튼 클릭시, DB의 정보와 동일하면 메인화면으로 이동
        login.setOnClickListener(v -> loginUser());

        //회원가입 버튼 클릭시, 회원가입 페이지로 이동
        sign.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser(){
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        //이메일과 비밀번호를 입력 안 했을 겯우에 입력 요구하는 메시지
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        //파이어베이스로 로그인 처리하기
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        //로그인 성공
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show();

                        //메인 화면으로 이동
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //로그인 실패
                        Toast.makeText(this, "로그인 실패: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}