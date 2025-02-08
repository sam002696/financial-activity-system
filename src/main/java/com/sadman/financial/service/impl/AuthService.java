package com.sadman.financial.service.impl;


import com.sadman.financial.dto.LoginRequest;
import com.sadman.financial.dto.RegisterRequest;
import com.sadman.financial.entity.User;
import com.sadman.financial.enums.RoleName;
import com.sadman.financial.exceptions.CustomMessageException;
import com.sadman.financial.repository.UserRepository;
import com.sadman.financial.responses.LoginResponse;
import com.sadman.financial.security.UserPrincipal;
import com.sadman.financial.service.CustomUserDetailsService;
import com.sadman.financial.service.interfaces.IAuthService;
import com.sadman.financial.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    

    @Override
    public User register(RegisterRequest reqUser) {
        User user = new User();
        if (reqUser.getRole() == null || reqUser.getRole().isEmpty()) {
            reqUser.setRole("USER");  // Defaulting to "USER" if role is null or empty
        }

        // Validating role to ensure it's a valid enum
        try {
            user.setRole(RoleName.valueOf(reqUser.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new CustomMessageException("Invalid role: " + reqUser.getRole());
        }

        if (userRepository.existsByEmail(reqUser.getEmail())) {
            throw new CustomMessageException(reqUser.getEmail() + " already exists");
        }

        reqUser.setPassword(passwordEncoder.encode(reqUser.getPassword()));
        user.setEmail(reqUser.getEmail());
        user.setName(reqUser.getName());
        user.setPassword(reqUser.getPassword());

        return userRepository.save(user);
    }


    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        var user = userDetailsService.loadUserByUsernameAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        var token = jwtUtils.generateToken(user);

        UserPrincipal userPrincipal = (UserPrincipal) user;

        String role = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

            loginResponse.setId(userPrincipal.getId());
            loginResponse.setUsername(userPrincipal.getName());
            loginResponse.setEmail(userPrincipal.getEmail());
            loginResponse.setRole(role);
            loginResponse.setAccessToken(token);
            loginResponse.setExpirationTime("6 days");

            return loginResponse;

    }




    
}
