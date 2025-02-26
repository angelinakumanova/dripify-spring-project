package com.dripify.user.service;

import com.dripify.cloudinary.service.CloudinaryService;
import com.dripify.exception.DomainException;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.model.UserRole;
import com.dripify.user.repository.UserRepository;
import com.dripify.web.dto.RegisterRequest;
import com.dripify.web.dto.UserEditRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.PublicKey;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.getUserByUsername(username).orElseThrow(() ->
                new DomainException("User with username %s does not exist!".formatted(username)));


        return new AuthenticationMetadata(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.isActive());
    }

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


    public void editUserProfile(UserEditRequest userEditRequest, User user) {

        if (Boolean.TRUE.equals(userEditRequest.getDeletePicture())) {
            if (user.getImageUrl() != null) {
                cloudinaryService.deleteImage(user.getImageUrl());

            }
            user.setImageUrl(null);
        } else if (userEditRequest.getProfilePicture() != null && !userEditRequest.getProfilePicture().isEmpty()) {
            if (user.getImageUrl() != null) {
                cloudinaryService.deleteImage(user.getImageUrl());

            }

            String imageUrl = cloudinaryService.uploadImage(userEditRequest.getProfilePicture());
            user.setImageUrl(imageUrl);
        }

        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());
        user.setDescription(userEditRequest.getDescription());

        userRepository.save(user);
    }


    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new DomainException("User with id %s does not exist!".formatted(id)));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new DomainException("User with username %s does not exist".formatted(username)));
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
}
