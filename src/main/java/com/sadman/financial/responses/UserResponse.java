package com.sadman.financial.responses;



import com.sadman.financial.entity.User;
import lombok.Data;

@Data
public class UserResponse {


    private Long id;
    private String name;
    private String email;
    private Double balance;

    public static UserResponse select(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setBalance(user.getBalance());
        return response;
    }


}
