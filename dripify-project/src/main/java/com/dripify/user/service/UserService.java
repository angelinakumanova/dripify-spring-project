package com.dripify.user.service;

import com.dripify.cloudinary.service.CloudinaryService;
import com.dripify.exception.*;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.model.UserRole;
import com.dripify.user.repository.UserRepository;
import com.dripify.web.dto.RegisterRequest;
import com.dripify.web.dto.UserEditRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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


    public void editProfile(UserEditRequest userEditRequest, User user) {

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
        user.setDescription(userEditRequest.getDescription().isEmpty() ? null : userEditRequest.getDescription());

        userRepository.save(user);
    }

    public void updateUsername(User user, String newUsername) {
        if (user.getUsername().equals(newUsername)) {
            throw new UserUpdateException("username", "This is already your username.");
        }

        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        if (user.getLastModifiedUsername() != null && user.getLastModifiedUsername().isAfter(thirtyDaysAgo)) {
            long remainingDays = ChronoUnit.DAYS.between(thirtyDaysAgo, user.getLastModifiedUsername());

            throw new UserUpdateException("username", "You have recently changed your username." +
                    " You can change it again in " + remainingDays + " days.");
        }


        if (userRepository.getUserByUsername(newUsername).isPresent()) {
            throw new UserUpdateException("username", "Username is already taken.");
        }

        user.setUsername(newUsername);
        user.setLastModifiedUsername(LocalDate.now());
        userRepository.save(user);

        updateAuthentication(user);
    }

    public void updateEmail(User user, String newEmail) {
        if (user.getEmail().equals(newEmail)) {
            throw new UserUpdateException("email", "You are already using this email.");
        }

        LocalDate today = LocalDate.now();
        if (user.getLastModifiedEmail() != null && user.getLastModifiedEmail().isEqual(today)) {

            throw new UserUpdateException("email", "You have already changed your email today. Try again tomorrow.");
        }

        if (userRepository.getUserByEmail(newEmail).isPresent()) {
            throw new UserUpdateException("email", "Email is already in use.");
        }

        user.setEmail(newEmail);
        user.setLastModifiedEmail(LocalDate.now());
        userRepository.save(user);
    }

    public void updatePassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new UserUpdateException("password", "You cannot use your current password.");
        }

        LocalDate today = LocalDate.now();

        if (user.getLastModifiedPassword() != null && user.getLastModifiedPassword().isEqual(today)) {
            throw new UserUpdateException("password", "You have already changed your password today. Try again tomorrow.");
        }

        String newEncodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newEncodedPassword);
        user.setLastModifiedPassword(LocalDate.now());
        userRepository.save(user);

        updateAuthentication(user);
    }


    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new DomainException("User with id %s does not exist!".formatted(id)));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new DomainException("User with username %s does not exist".formatted(username)));
    }

    private void updateAuthentication(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AuthenticationMetadata authenticationMetadata = new AuthenticationMetadata(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.isActive());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(authenticationMetadata, authentication.getCredentials(), authenticationMetadata.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
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

    public void deactivateUser(User user) {
        user.setActive(false);
        userRepository.save(user);

        SecurityContextHolder.getContext().setAuthentication(null);

    }
}
