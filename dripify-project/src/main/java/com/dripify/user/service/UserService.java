package com.dripify.user.service;

import com.dripify.exception.DomainException;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.model.UserRole;
import com.dripify.user.repository.UserRepository;
import com.dripify.web.dto.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.getUserByUsername(username).orElseThrow(() ->
                new DomainException("User with username %s does not exist!".formatted(username)));


        return new AuthenticationMetadata(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.isActive());
    }

    //TODO: implement check on uniqueness of user
    public void register(RegisterRequest registerRequest) {
        if (userRepository.getUserByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already in use");
        }

        if (userRepository.getUserByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use");
        }

        User user = createNewUser(registerRequest);
        userRepository.save(user);

        log.info("User [%s] with id: %s registered successfully".formatted(user.getUsername(), user.getId()));
    }

    private User createNewUser(RegisterRequest registerRequest) {
        return User.builder().
                firstName(registerRequest.getFirstName()).
                lastName(registerRequest.getLastName()).
                username(registerRequest.getUsername()).
                email(registerRequest.getEmail()).
                password(passwordEncoder.encode(registerRequest.getPassword())).
                role(UserRole.USER).
                isActive(true).
                build();
    }


    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new DomainException("User with id %s does not exist!".formatted(id)));
    }
}
