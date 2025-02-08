package com.sadman.financial.service.interfaces;

import com.sadman.financial.dto.UserRequest;
import com.sadman.financial.responses.UserResponse;

public interface IUserService {
    // Getting user by ID
    UserResponse getUserProfile();

    // Updating user profile
    UserResponse updateUserProfile(UserRequest userRequest);
}
