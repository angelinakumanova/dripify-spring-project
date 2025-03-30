package com.dripify.user.service;

import com.dripify.cart.model.ShoppingCart;
import com.dripify.cart.service.ShoppingCartService;
import com.dripify.cloudinary.service.CloudinaryService;
import com.dripify.exception.*;
import com.dripify.notification.service.NotificationService;
import com.dripify.product.event.ProductDeactivationEvent;
import com.dripify.product.model.Product;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.event.UserDeactivationEvent;
import com.dripify.user.model.User;
import com.dripify.user.model.UserRole;
import com.dripify.user.repository.UserRepository;
import com.dripify.web.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private static final int DEFAULT_PAGE_SIZE = 30;

    private final UserRepository userRepository;

    private final NotificationService notificationService;
    private final ShoppingCartService shoppingCartService;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher eventPublisher;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CloudinaryService cloudinaryService,
                       NotificationService notificationService, ShoppingCartService shoppingCartService, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
        this.notificationService = notificationService;
        this.shoppingCartService = shoppingCartService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameAndIsActiveTrue(username).orElseThrow(() ->
                new IllegalArgumentException("User with username %s does not exist!".formatted(username)));


        return new AuthenticationMetadata(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.isActive());
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userRepository.getUserByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UserRegistrationException("username", "Username is already in use");
        }

        if (userRepository.getUserByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserRegistrationException("email", "Email is already in use");
        }

        User user = userRepository.save(createNewUser(registerRequest));


        ShoppingCart shoppingCart = shoppingCartService.createNewCart(user);
        user.setShoppingCart(shoppingCart);

        notificationService.upsertNotificationPreference(user.getId(), true, user.getEmail());
        notificationService.sendWelcomeEmail(user.getId(), user.getFirstName());

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

            String imageUrl = cloudinaryService.uploadProfileImage(userEditRequest.getProfilePicture());
            user.setImageUrl(imageUrl);
        }

        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());
        user.setDescription(userEditRequest.getDescription().isEmpty() ? null : userEditRequest.getDescription());
        user.setUpdatedOn(LocalDateTime.now());

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
        user.setUpdatedOn(LocalDateTime.now());
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
        user.setUpdatedOn(LocalDateTime.now());
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
        user.setUpdatedOn(LocalDateTime.now());
        userRepository.save(user);

        updateAuthentication(user);
    }


    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User with id %s does not exist!".formatted(id)));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsernameAndIsActiveTrue(username).orElseThrow(() -> new IllegalArgumentException("User with username %s does not exist".formatted(username)));
    }

    public void favoriteProduct(Product product, User user) {
        if (user.getFavoriteProducts().contains(product)) {
            throw new IllegalArgumentException("You are already have this product as favourite.");
        }

        user.getFavoriteProducts().add(product);
        userRepository.save(user);
    }

    public void removeFavoriteProduct(Product product, User user) {
        user.getFavoriteProducts().remove(product);
        userRepository.save(user);
    }




    public Page<User> getAllUsers(User user, int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        return userRepository.getAllByUsernameIsNot(user.getUsername(), pageable);
    }

    @EventListener
    @Transactional
    public void unlinkProductFromFavourites(ProductDeactivationEvent event) {
        userRepository.removeFavouriteProduct(event.getProductId());
    }

    @Transactional
    public void changeStatus(User target) {
        if (target.isActive()) {
            deactivateUser(target);
        } else {
            activateUser(target);
        }
    }

    public void switchRole(User target) {

        if (target.getRole().equals(UserRole.ADMIN)) {
            target.setRole(UserRole.USER);
        } else {
            target.setRole(UserRole.ADMIN);
        }

        target.setUpdatedOn(LocalDateTime.now());
        userRepository.save(target);
    }

    private void deactivateUser(User user) {
        user.setActive(false);
        user.setUpdatedOn(LocalDateTime.now());
        shoppingCartService.clearCart(user.getShoppingCart());
        notificationService.updateNotificationPreference(user.getId(), false);
        userRepository.save(user);

        eventPublisher.publishEvent(new UserDeactivationEvent(user));
    }

    private void activateUser(User user) {
        user.setActive(true);
        user.setUpdatedOn(LocalDateTime.now());
        userRepository.save(user);
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
                createdOn(LocalDateTime.now()).
                updatedOn(LocalDateTime.now()).
                build();
    }


}
