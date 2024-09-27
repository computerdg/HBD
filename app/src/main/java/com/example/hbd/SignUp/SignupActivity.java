package com.example.hbd.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hbd.Login.LoginActivity;
import com.example.hbd.MainActivity;
import com.example.hbd.R;
import com.example.hbd.ApiService;
import com.example.hbd.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextNickname, editTextUsername, editTextPassword, editTextPasswordConfirm, editTextYear, editTextMonth, editTextDay;
    private RadioGroup radioGroupGender;
    private Spinner spinnerLocation;
    private String[] locations = {"대구 북구", "대구 서구", "대구 동구", "대구 중구", "대구 달서구", "대구 수성구", "대구 남구", "대구 달성군", "대구 군위군"};
    private String gender = null; // 초기값을 null로 설정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // UI 요소 초기화
        editTextNickname = findViewById(R.id.editTextNickname);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        editTextYear = findViewById(R.id.editTextYear);
        editTextMonth = findViewById(R.id.editTextMonth);
        editTextDay = findViewById(R.id.editTextDay);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        spinnerLocation = findViewById(R.id.spinnerLocation);

        // 지역 선택을 위한 Spinner 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(adapter);

        // RadioGroup의 선택 상태가 변경될 때 호출되는 리스너 설정
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioMale) {
                    gender = "MAN";
                } else if (checkedId == R.id.radioFemale) {
                    gender = "WOMAN";
                }
            }
        });

        // 비밀번호 확인 버튼 클릭 리스너
        findViewById(R.id.buttonVerifyPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPassword();
            }
        });

        // 회원가입 버튼 클릭 리스너
        findViewById(R.id.buttonSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignup();
            }
        });
    }

    private void verifyPassword() {
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextPasswordConfirm.getText().toString().trim();

        if (password.equals(confirmPassword)) {
            Toast.makeText(SignupActivity.this, "비밀번호가 일치합니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void performSignup() {
        String nickname = editTextNickname.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextPasswordConfirm.getText().toString().trim();
        String year = editTextYear.getText().toString().trim();
        String month = editTextMonth.getText().toString().trim();
        String day = editTextDay.getText().toString().trim();
        String location = spinnerLocation.getSelectedItem().toString();
        String birthDate = year + "-" + month + "-" + day + "T00:00:00";

        // 입력값 검증
        if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(year) || TextUtils.isEmpty(month) ||
                TextUtils.isEmpty(day) || TextUtils.isEmpty(location)) {
            Toast.makeText(this, "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (gender == null) {
            Toast.makeText(this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a SignUpRequest object
        SignUpRequest signUpRequest = new SignUpRequest(username, password, confirmPassword, nickname, birthDate, gender, location);

        // Send a sign-up request to the server
        ApiService apiService = RetrofitClient.getApiService("http://165.229.89.154:8080/"); // 실제 서버 URL로 변경
        Call<SignUpResponse> call = apiService.SignUp(signUpRequest);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(SignupActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    // 회원가입 성공 후 추가 로직 구현
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
