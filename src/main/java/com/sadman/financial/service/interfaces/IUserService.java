package com.sadman.financial.service.interfaces;

import com.sadman.financial.dto.UserRequest;
import com.sadman.financial.responses.UserResponse;

public interface IUserService {
    // Get user by ID
    UserResponse getUserProfile();

    // Update user profile
    UserResponse updateUserProfile(UserRequest userRequest);
}
