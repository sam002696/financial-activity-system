package com.sadman.financial.service.interfaces;


import com.sadman.financial.dto.LoginRequest;
import com.sadman.financial.dto.RegisterRequest;
import com.sadman.financial.entity.User;
import com.sadman.financial.responses.LoginResponse;

public interface IAuthService {
    User register(RegisterRequest user);

    LoginResponse login(LoginRequest loginRequest);
}
