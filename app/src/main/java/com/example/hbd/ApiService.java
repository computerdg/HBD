package com.example.hbd;

import com.example.hbd.Login.LoginRequest;
import com.example.hbd.Login.LoginResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    // 로그인
    @POST("/api/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // 회원가입
    @POST("/api/join") // 실제 서버의 회원가입 엔드포인트로 수정
    Call<com.example.hbd.SignUp.SignUpResponse> SignUp(@Body com.example.hbd.SignUp.SignUpRequest signUpRequest);




}
