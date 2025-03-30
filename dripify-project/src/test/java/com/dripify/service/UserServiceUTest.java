package com.dripify.service;

import com.dripify.cart.model.ShoppingCart;
import com.dripify.cart.service.ShoppingCartService;
import com.dripify.cloudinary.service.CloudinaryService;
import com.dripify.exception.UserRegistrationException;
import com.dripify.exception.UserUpdateException;
import com.dripify.notification.service.NotificationService;
import com.dripify.product.model.Product;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.event.UserDeactivationEvent;
import com.dripify.user.model.User;
import com.dripify.user.model.UserRole;
import com.dripify.user.repository.UserRepository;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.RegisterRequest;
import com.dripify.web.dto.UserEditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserService userService;


    @Test
    void givenMissingUserFromDatabase_whenLoadByUsername_thenThrowsException() {

        // Given
        String username = "test";
        when(userRepository.findByUsernameAndIsActiveTrue(username)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.loadUserByUsername(username));

    }

    @Test
    void givenExistingActiveUser_whenLoadByUsername_thenReturnCorrectAuthenticationMetadata() {

        // Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("test")
                .password("123123")
                .isActive(true)
                .role(UserRole.ADMIN)
                .build();
        when(userRepository.findByUsernameAndIsActiveTrue("test")).thenReturn(Optional.of(user));

        // When
        UserDetails userDetails = userService.loadUserByUsername("test");

        // Then
        assertInstanceOf(AuthenticationMetadata.class, userDetails);
        AuthenticationMetadata authenticationMetadata = (AuthenticationMetadata) userDetails;

        assertEquals(user.getId(), authenticationMetadata.getUserId());
        assertEquals(user.getUsername(), authenticationMetadata.getUsername());
        assertEquals(user.getPassword(), authenticationMetadata.getPassword());
        assertEquals(user.getRole(), authenticationMetadata.getRole());
        assertEquals(user.isActive(), authenticationMetadata.isActive());
        assertThat(authenticationMetadata.getAuthorities()).hasSize(1);
        assertEquals("ROLE_ADMIN", authenticationMetadata.getAuthorities().iterator().next().getAuthority());

    }

    @Test
    void givenExistingUsername_whenRegister_thenThrowsException() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("test");
        when(userRepository.getUserByUsername(any())).thenReturn(Optional.of(new User()));

        // When && Then
        assertThrows(UserRegistrationException.class, () -> userService.register(registerRequest));
    }

    @Test
    void givenExistingEmail_whenRegister_thenThrowsException() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        when(userRepository.getUserByEmail(any())).thenReturn(Optional.of(new User()));

        // When && Then
        assertThrows(UserRegistrationException.class, () -> userService.register(registerRequest));
    }

    @Test
    void givenHappyPath_whenRegister() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("test");
        request.setPassword("123123");
        request.setEmail("test@test.com");
        request.setFirstName("firstTest");
        request.setLastName("lastTest");

        User user = User.builder()
                .id(UUID.randomUUID())
                .build();
        when(userRepository.getUserByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(userRepository.getUserByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(shoppingCartService.createNewCart(user)).thenReturn(new ShoppingCart());

        // When
        userService.register(request);

        // Then
        assertThat(user.getShoppingCart()).isNotNull();
        verify(userRepository, times(1)).save(any(User.class));
        verify(shoppingCartService, times(1)).createNewCart(any(User.class));
        verify(notificationService, times(1)).upsertNotificationPreference(user.getId(), true, user.getEmail());
        verify(notificationService, times(1)).sendWelcomeEmail(user.getId(), user.getFirstName());
    }

    @Test
    void givenUserEditRequestWithDeletePictureTrue_whenEditProfile_thenDeleteImageAndSaveUser() {
        // Given
        User user = User.builder()
                .imageUrl("www.image.com").build();
        UserEditRequest userEditRequest = UserEditRequest.builder()
                .firstName("firstName")
                .lastName("lastName")
                .description("description")
                .deletePicture(true)
                .build();

        // When
        userService.editProfile(userEditRequest, user);

        // Then
        assertNull(user.getImageUrl());
        assertEquals(userEditRequest.getFirstName(), user.getFirstName());
        assertEquals(userEditRequest.getLastName(), user.getLastName());
        assertEquals(userEditRequest.getDescription(), user.getDescription());
        verify(cloudinaryService, times(1)).deleteImage("www.image.com");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserEditRequestWithProfilePicture_whenEditProfile_thenUploadNewImageAndSaveUser() {
        // Given
        User user = User.builder()
                .imageUrl("www.image.com").build();
        MultipartFile newProfilePicture = mock(MultipartFile.class);
        UserEditRequest userEditRequest = UserEditRequest.builder()
                .firstName("firstName")
                .lastName("lastName")
                .description("description")
                .profilePicture(newProfilePicture)
                .build();

        when(cloudinaryService.uploadProfileImage(newProfilePicture)).thenReturn("new-image-url");

        // When
        userService.editProfile(userEditRequest, user);

        // Then
        assertEquals("new-image-url", user.getImageUrl());
        assertEquals(userEditRequest.getFirstName(), user.getFirstName());
        assertEquals(userEditRequest.getLastName(), user.getLastName());
        assertEquals(userEditRequest.getDescription(), user.getDescription());
        verify(cloudinaryService, times(1)).deleteImage("www.image.com");
        verify(cloudinaryService, times(1)).uploadProfileImage(any(MultipartFile.class));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserEditRequestWithNoProfilePictureChange_whenEditProfile_thenOnlyUpdateUserFields() {
        // Given
        User user = new User();
        UserEditRequest userEditRequest = UserEditRequest.builder()
                .firstName("firstName")
                .lastName("lastName")
                .description("description")
                .build();

        // When
        userService.editProfile(userEditRequest, user);

        // Then
        assertEquals(userEditRequest.getFirstName(), user.getFirstName());
        assertEquals(userEditRequest.getLastName(), user.getLastName());
        assertEquals(userEditRequest.getDescription(), user.getDescription());

        verify(cloudinaryService, never()).deleteImage(anyString());
        verify(cloudinaryService, never()).uploadProfileImage(any());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenSameNewUsernameAsUser_whenUpdateUsername_thenThrowsException() {
        // Given
        String newUsername = "angie";
        User user = User.builder().username("angie").build();

        // When & Then
        assertThrows(UserUpdateException.class, () -> userService.updateUsername(user, newUsername));

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenRecentlyChangedUsername_whenUpdateUsername_thenThrowsException() {
        // Given
        User user = User.builder()
                .username("angie")
                .lastModifiedUsername(LocalDate.now().minusDays(10))
                .build();
        String newUsername = "angie_new";

        // When & Then
        assertThrows(UserUpdateException.class, () -> userService.updateUsername(user, newUsername));

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenUsernameAlreadyTaken_whenUpdateUsername_thenThrowsException() {
        // Given
        User user = User.builder().username("angie").build();
        String newUsername = "takenUsername";

        when(userRepository.getUserByUsername(newUsername)).thenReturn(Optional.of(User.builder().username(newUsername).build()));

        // When & Then
        assertThrows(UserUpdateException.class, () -> userService.updateUsername(user, newUsername));

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenValidNewUsername_whenUpdateUsername_thenUsernameAndAuthenticationAreUpdatedSuccessfully() {
        // Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("angie")
                .password("hashedPassword")
                .role(UserRole.USER)
                .isActive(true)
                .build();
        String newUsername = "angie_new";

        when(userRepository.getUserByUsername(newUsername)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);

        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);

        // When
        userService.updateUsername(user, newUsername);

        // Then
        assertEquals(newUsername, user.getUsername());
        assertNotNull(user.getLastModifiedUsername());
        verify(userRepository, times(1)).save(user);
        verify(mockSecurityContext).setAuthentication(argThat(auth -> {
            AuthenticationMetadata metadata = (AuthenticationMetadata) auth.getPrincipal();
            return metadata.getUsername().equals(newUsername);
        }));
    }


    @Test
    void givenSameNewEmailAsUser_whenUpdateEmail_thenThrowsException() {
        // Given
        String newEmail = "test@example.com";
        User user = User.builder().email("test@example.com").build();

        // When & Then
        assertThrows(UserUpdateException.class, () -> userService.updateEmail(user, newEmail));

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenUserChangedEmailToday_whenUpdateEmail_thenThrowsException() {
        // Given
        String newEmail = "new@example.com";
        User user = User.builder()
                .email("old@example.com")
                .lastModifiedEmail(LocalDate.now())
                .build();

        // When & Then
        assertThrows(UserUpdateException.class, () -> userService.updateEmail(user, newEmail));

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenNewEmailAlreadyExists_whenUpdateEmail_thenThrowsException() {
        // Given
        String newEmail = "existing@example.com";
        User user = User.builder().email("old@example.com").build();

        when(userRepository.getUserByEmail(newEmail)).thenReturn(Optional.of(new User()));

        // When & Then
        assertThrows(UserUpdateException.class, () -> userService.updateEmail(user, newEmail));

        verify(userRepository, never()).save(any());
    }

    @Test
    void happyPath_whenUpdateEmail() {
        // Given
        String newEmail = "new@example.com";
        User user = User.builder()
                .email("old@example.com")
                .lastModifiedEmail(LocalDate.now().minusDays(1)) // Changed yesterday
                .build();

        when(userRepository.getUserByEmail(newEmail)).thenReturn(Optional.empty());

        // When
        userService.updateEmail(user, newEmail);

        // Then
        assertEquals(newEmail, user.getEmail());
        assertEquals(LocalDate.now(), user.getLastModifiedEmail());
        assertNotNull(user.getUpdatedOn());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenSamePasswordAsCurrent_whenUpdatePassword_thenThrowsException() {
        // Given
        String currentPassword = "hashedPassword";
        String newPassword = "password123";
        User user = User.builder().password(currentPassword).build();

        when(passwordEncoder.matches(newPassword, currentPassword)).thenReturn(true);

        // When & Then
        assertThrows(UserUpdateException.class, () -> userService.updatePassword(user, newPassword));

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenUserChangedPasswordToday_whenUpdatePassword_thenThrowsException() {
        // Given
        String newPassword = "newPassword123";
        User user = User.builder()
                .password("hashedOldPassword")
                .lastModifiedPassword(LocalDate.now()) // Password changed today
                .build();

        when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(false);

        // When & Then
        assertThrows(UserUpdateException.class, () -> userService.updatePassword(user, newPassword));

        verify(userRepository, never()).save(any());
    }

    @Test
    void happyPath_whenUpdatePassword() {
        // Given
        String newPassword = "newPassword123";
        String encodedPassword = "hashedNewPassword";
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("angie")
                .password("hashedPassword")
                .lastModifiedPassword(null)
                .role(UserRole.USER)
                .isActive(true)
                .build();

        when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(false);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);

        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);

        // When
        userService.updatePassword(user, newPassword);

        // Then
        assertEquals(encodedPassword, user.getPassword());
        assertEquals(LocalDate.now(), user.getLastModifiedPassword());
        assertNotNull(user.getUpdatedOn());

        verify(userRepository, times(1)).save(user);
        verify(mockSecurityContext).setAuthentication(argThat(auth -> {
            AuthenticationMetadata metadata = (AuthenticationMetadata) auth.getPrincipal();
            return metadata.getPassword().equals(user.getPassword());
        }));
    }

    @Test
    void givenExistingUserId_whenGetById_thenReturnUser() {
        // Given
        UUID id = UUID.randomUUID();
        User user = User.builder().id(id).build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // When
        User foundUser = userService.getById(id);

        // Then
        assertEquals(id, foundUser.getId());
    }

    @Test
    void givenNonExistentUserId_whenGetById_thenThrowsException() {
        // Given
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Then & When
        assertThrows(IllegalArgumentException.class, () -> userService.getById(id));
    }

    @Test
    void givenExistingUsername_whenGetByUsername_thenReturnUser() {
        // Given
        String username = "angie";
        User user = User.builder().username(username).build();
        when(userRepository.findByUsernameAndIsActiveTrue(username)).thenReturn(Optional.of(user));

        // When
        User foundUser = userService.getByUsername(username);

        // Then
        assertEquals(username, foundUser.getUsername());
    }

    @Test
    void givenNonExistentUsername_whenGetByUsername_thenThrowsException() {
        // Given
        String username = "angie";
        when(userRepository.findByUsernameAndIsActiveTrue(username)).thenReturn(Optional.empty());

        // Then & When
        assertThrows(IllegalArgumentException.class, () -> userService.getByUsername(username));
    }

    @Test
    void givenProductAlreadyFavorited_whenFavoriteProduct_thenThrowsException() {
        // Given
        User user = new User();
        Product product = new Product();
        user.getFavoriteProducts().add(product);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.favoriteProduct(product, user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenNewProduct_whenFavoriteProduct_thenProductIsAddedAndSaved() {
        // Given
        User user = new User();
        Product product = new Product();

        // When
        userService.favoriteProduct(product, user);

        // Then
        assertTrue(user.getFavoriteProducts().contains(product));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenExistingFavoriteProduct_whenRemoveFavoriteProduct_thenProductIsRemovedAndSaved() {
        // Given
        User user = new User();
        Product product = new Product();
        user.getFavoriteProducts().add(product);

        // When
        userService.removeFavoriteProduct(product, user);

        // Then
        assertFalse(user.getFavoriteProducts().contains(product));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenNonFavoriteProduct_whenRemoveFavoriteProduct_thenUserIsStillSaved() {
        // Given
        User user = new User();
        Product product = new Product();

        // When
        userService.removeFavoriteProduct(product, user);

        // Then
        assertFalse(user.getFavoriteProducts().contains(product));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenValidUserAndPageNumber_whenGetAllUsers_thenReturnsUserPage() {
        // Given
        User user = new User();
        user.setUsername("currentUser");

        int page = 0;
        Pageable pageable = PageRequest.of(page, 30);
        List<User> users = List.of(new User(), new User());
        Page<User> expectedPage = new PageImpl<>(users, pageable, users.size());

        when(userRepository.getAllByUsernameIsNot(user.getUsername(), pageable)).thenReturn(expectedPage);

        // When
        Page<User> result = userService.getAllUsers(user, page);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(userRepository, times(1)).getAllByUsernameIsNot(user.getUsername(), pageable);
    }

    @Test
    void givenAdminUser_whenSwitchRole_thenRoleBecomesUser() {
        // Given
        User user = new User();
        user.setRole(UserRole.ADMIN);

        // When
        userService.switchRole(user);

        // Then
        assertEquals(UserRole.USER, user.getRole());
        assertNotNull(user.getUpdatedOn());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenRegularUser_whenSwitchRole_thenRoleBecomesAdmin() {
        // Given
        User user = User.builder().role(UserRole.USER).build();

        // When
        userService.switchRole(user);

        // Then
        assertEquals(UserRole.ADMIN, user.getRole());
        assertNotNull(user.getUpdatedOn());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUser_whenChangeStatus_thenUserStatusChanges() {
        // Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(true)
                .shoppingCart(new ShoppingCart())
                .build();

        // When
        userService.changeStatus(user);

        // Then
        assertFalse(user.isActive());
        assertNotNull(user.getUpdatedOn());
        verify(shoppingCartService, times(1)).clearCart(user.getShoppingCart());
        verify(notificationService, times(1)).updateNotificationPreference(user.getId(), false);
        verify(userRepository, times(1)).save(user);
        verify(eventPublisher, times(1)).publishEvent(any(UserDeactivationEvent.class));

        // When again (now the user is inactive)
        userService.changeStatus(user);

        // Then
        assertTrue(user.isActive());
    }



}
