package com.sadman.financial.responses;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;
    private String role;
    private String accessToken;
    private String refreshToken;
    private String expirationTime;

    public static LoginResponse select(LoginResponse user) {
        if (user == null) {
            return null;
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(user.getId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setRole(user.getRole());
        loginResponse.setAccessToken((user.getAccessToken()));
        loginResponse.setRefreshToken(user.getRefreshToken());
        loginResponse.setExpirationTime(user.getExpirationTime());
        return loginResponse;
    }
}
