package com.sadman.financial.service;

import com.sadman.financial.entity.User;
import com.sadman.financial.exceptions.CustomMessageException;
import com.sadman.financial.repository.UserRepository;
import com.sadman.financial.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


//    @Autowired
//    private UserRepository userRepository;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    // throws UsernameNotFoundException
    public UserDetails loadUserByUsername(String username)  {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new CustomMessageException("Username/Email not Found"));
        return UserPrincipal.create(user);
    }


    public UserDetails loadUserByUsernameAndPassword(String username, String rawPassword) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new CustomMessageException("Username/Email not Found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new CustomMessageException("Invalid Password");
        }

        return UserPrincipal.create(user);
    }
}
