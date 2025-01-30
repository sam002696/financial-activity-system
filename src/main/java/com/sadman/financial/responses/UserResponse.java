package com.sadman.financial.responses;


import com.sami.plant_ecom.entity.User;
import lombok.Data;

@Data
public class UserResponse {


    private Long userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String profileImage;

    public static UserResponse selectUser(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        return response;
    }

    public static UserResponse selectUserInfoChange(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setProfileImage(user.getProfileImage());
        return response;
    }


}
