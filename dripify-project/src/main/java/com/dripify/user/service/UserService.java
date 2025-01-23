package com.dripify.user.service;

import com.dripify.user.model.User;
import com.dripify.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByUsername(String username) {
        return this.userRepository.getUserByUsername(username).orElse(null);
    }

    public boolean existsByEmailAndPassword(String email, String password) {
        User user = this.userRepository.getUserByEmail(email).orElse(null);

        if (user == null) {
            return false;
        }

        return passwordEncoder.matches(password, user.getPassword());
    }
}
