package com.sadman.financial.service.impl;

import com.sadman.financial.dto.UserRequest;
import com.sadman.financial.entity.User;
import com.sadman.financial.repository.UserRepository;
import com.sadman.financial.responses.UserResponse;
import com.sadman.financial.exceptions.CustomMessageException;
import com.sadman.financial.security.UserPrincipal;
import com.sadman.financial.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    // Getting user by ID
    @Override
    public UserResponse getUserProfile() {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrincipal.getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomMessageException("User not found with id: " + userId));

        return UserResponse.select(user);
    }

    // Updating user profile
    @Override
    public UserResponse updateUserProfile(UserRequest userRequest) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrincipal.getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomMessageException("User not found with id: " + userId));

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setBalance(userRequest.getBalance());

        userRepository.save(user);

        return UserResponse.select(user);
    }
}
