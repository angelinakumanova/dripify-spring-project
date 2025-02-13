package com.dripify.user.service;

import com.dripify.user.model.User;
import com.dripify.user.repository.UserRepository;
import com.dripify.web.dto.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existsByUsername(String username) {
        return this.userRepository.getUserByUsername(username).isPresent();
    }

//    public boolean existsByEmailAndPassword(String email, String password) {
//        this.userRepository.getUserByEmail(email).orElse(null);
//
//        if (user == null) {
//            return false;
//        }
//
//        return passwordEncoder.matches(password, user.getPassword());
//    }

    //TODO: implement check on uniqueness of user

    public void registerUser(RegisterRequest registerRequest) {
        if (userRepository.getUserByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already in use");
        }

        if (userRepository.getUserByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use");
        }

        User user = createNewUser(registerRequest);
        userRepository.save(user);
        log.info("User [%s] with id: %s  registered successfully".formatted(user.getUsername(), user.getId()));
    }

    private User createNewUser(RegisterRequest registerRequest) {
        return User.builder().
                firstName(registerRequest.getFirstName()).
                lastName(registerRequest.getLastName()).
                username(registerRequest.getUsername()).
                email(registerRequest.getEmail()).
                password(passwordEncoder.encode(registerRequest.getPassword())).
                build();
    }


}
