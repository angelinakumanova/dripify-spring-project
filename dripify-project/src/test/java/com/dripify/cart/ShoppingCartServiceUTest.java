package com.dripify.cart;

import com.dripify.cart.model.ShoppingCart;
import com.dripify.cart.repository.ShoppingCartRepository;
import com.dripify.cart.service.ShoppingCartService;
import com.dripify.exception.ShoppingCartException;
import com.dripify.product.model.Product;
import com.dripify.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceUTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @Test
    void givenUserWithoutCart_whenCreateNewCart_thenSaveAndReturnNewCart() {
        // Given
        User user = new User();

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.empty());

        when(shoppingCartRepository.save(any())).thenReturn(new ShoppingCart());

        ArgumentCaptor<ShoppingCart> cartCaptor = ArgumentCaptor.forClass(ShoppingCart.class);

        // When
        shoppingCartService.createNewCart(user);

        // Then
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verify(shoppingCartRepository).save(cartCaptor.capture());
        ShoppingCart savedCart = cartCaptor.getValue();

        assertNotNull(savedCart);
        assertEquals(user, savedCart.getUser());
    }

    @Test
    void givenUserWithCart_whenCreateNewCart_thenThrowsException() {
        //Given
        User user = new User();

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(new ShoppingCart()));

        // When && Then
        assertThrows(IllegalArgumentException.class, () -> shoppingCartService.createNewCart(user));
        verify(shoppingCartRepository, never()).save(any());
    }

    @Test
    void givenUserWithoutCart_whenGetCart_thenCreateCartForThisUser() {
        // Given
        User user = new User();
        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.empty());

        ArgumentCaptor<ShoppingCart> cartCaptor = ArgumentCaptor.forClass(ShoppingCart.class);

        // When
        shoppingCartService.getCart(user);

        // Then
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verify(shoppingCartRepository).save(cartCaptor.capture());

        ShoppingCart savedCart = cartCaptor.getValue();
        assertEquals(user, savedCart.getUser());
    }

    @Test
    void givenUserWithCart_whenGetCart_thenReturnsCartForThisUser() {
        // Given
        User user = new User();
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        // When
        ShoppingCart fetchedCart = shoppingCartService.getCart(user);

        // Then
        assertEquals(cart, fetchedCart);
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verify(shoppingCartRepository, never()).save(any());
    }

    @Test
    void givenHappyPath_whenAddProduct() {
        // Given
        User user = new User();
        Product product = new Product();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));

        // When
        shoppingCartService.addProduct(user, product);

        // Then
        assertEquals(user, shoppingCart.getUser());
        assertTrue(shoppingCart.getProducts().contains(product));
        verify(shoppingCartRepository, times(1)).save(shoppingCart);
    }

    @Test
    void givenAlreadyExistingProductInCart_whenAddProduct_thenThrowsException() {
        User user = new User();
        Product product = new Product();
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cart.getProducts().add(product);

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        assertThrows(ShoppingCartException.class, () -> shoppingCartService.addProduct(user, product));
    }

    @Test
    void givenHappyPath_whenRemoveProduct() {
        // Given
        User user = new User();
        Product product = new Product();
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cart.getProducts().add(product);

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        // When
        boolean result = shoppingCartService.removeProduct(user, product);

        // Then
        assertTrue(result);
        assertFalse(cart.getProducts().contains(product));
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verify(shoppingCartRepository, times(1)).save(cart);

    }

    @Test
    void givenProductNotInUserCart_whenRemoveProduct_thenKeepUserCartTheSame() {
        // Given
        User user = new User();
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);

        Product product = new Product();

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        // When
        boolean result = shoppingCartService.removeProduct(user, product);

        // Then
        assertFalse(result);
        assertFalse(cart.getProducts().contains(product));
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verify(shoppingCartRepository, times(1)).save(cart);
    }

    @Test
    void givenCart_whenClearCart_thenReturnsEmptyCart() {
        ShoppingCart cart = new ShoppingCart();
        cart.getProducts().addAll(List.of(new Product(), new Product()));

        shoppingCartService.clearCart(cart);

        assertTrue(cart.getProducts().isEmpty());
        verify(shoppingCartRepository, times(1)).save(cart);
    }
}
