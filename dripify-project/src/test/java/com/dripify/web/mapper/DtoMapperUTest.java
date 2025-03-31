package com.dripify.web.mapper;

import com.dripify.product.model.Product;
import com.dripify.product.model.enums.*;
import com.dripify.user.model.User;
import com.dripify.web.dto.EmailUpdateRequest;
import com.dripify.web.dto.ProductEditRequest;
import com.dripify.web.dto.UserEditRequest;
import com.dripify.web.dto.UsernameUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DtoMapperUTest {

    @Test
    void givenHappyPath_whenMappingUserToEditRequest() {

        //Given
        User user = User.builder()
                .firstName("Angie")
                .lastName("K")
                .description("Random stuff")
                .build();

        //When
        UserEditRequest userEditRequest = DtoMapper.mapUserToEditRequest(user);

        // Then
        assertEquals(userEditRequest.getFirstName(), user.getFirstName());
        assertEquals(userEditRequest.getLastName(), user.getLastName());
        assertEquals(userEditRequest.getDescription(), user.getDescription());
    }

    @Test
    void givenHappyPath_whenMappingUserToUsernameUpdateRequest() {
        // Given
        User user = User.builder().username("angiek").build();

        // When
        UsernameUpdateRequest usernameUpdateRequest = DtoMapper.mapToUsernameUpdateRequest(user);

        // Then
        assertEquals(usernameUpdateRequest.getUsername(), user.getUsername());
    }

    @Test
    void givenHappyPath_whenMappingUserToEmailUpdateRequest() {
        // Given
        User user = User.builder().email("angiek@gmail.com").build();

        // When
        EmailUpdateRequest emailUpdateRequest = DtoMapper.mapToEmailUpdateRequest(user);

        // Then
        assertEquals(emailUpdateRequest.getEmail(), user.getEmail());
    }

    @Test
    void givenHappyPath_whenMappingProductToProductEditRequest() {

        // Given
        Product product = Product.builder()
                .name("Product Name")
                .description("Product Description")
                .price(BigDecimal.valueOf(33))
                .size(Size.M)
                .brand(Brand.LOUIS_VUITTON)
                .color(Color.WHITE)
                .condition(Condition.NEW)
                .material(Material.LEATHER)
                .build();

        // When
        ProductEditRequest productEditRequest = DtoMapper.mapToProductEditRequest(product);

        // Then
        assertEquals(productEditRequest.getTitle(), product.getName());
        assertEquals(productEditRequest.getDescription(), product.getDescription());
        assertEquals(productEditRequest.getPrice(), product.getPrice());
        assertEquals(productEditRequest.getSize(), product.getSize());
        assertEquals(productEditRequest.getBrand(), product.getBrand());
        assertEquals(productEditRequest.getColor(), product.getColor());
        assertEquals(productEditRequest.getCondition(), product.getCondition());
        assertEquals(productEditRequest.getMaterial(), product.getMaterial());
    }
 }
