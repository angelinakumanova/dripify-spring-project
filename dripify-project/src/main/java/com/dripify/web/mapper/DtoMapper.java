package com.dripify.web.mapper;

import com.dripify.product.model.Product;
import com.dripify.user.model.User;
import com.dripify.web.dto.EmailUpdateRequest;
import com.dripify.web.dto.ProductEditRequest;
import com.dripify.web.dto.UserEditRequest;
import com.dripify.web.dto.UsernameUpdateRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static UserEditRequest mapUserToEditRequest(User user) {
        return UserEditRequest.builder().
                firstName(user.getFirstName()).
                lastName(user.getLastName()).
                description(user.getDescription()).
                build();
    }

    public static UsernameUpdateRequest mapToUsernameUpdateRequest(User user) {
        return UsernameUpdateRequest.builder().username(user.getUsername()).build();
    }

    public static EmailUpdateRequest mapToEmailUpdateRequest(User user) {
        return EmailUpdateRequest.builder().email(user.getEmail()).build();
    }

    public static ProductEditRequest mapToProductEditRequest(Product product) {

        return ProductEditRequest.builder()
                .title(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .size(product.getSize())
                .brand(product.getBrand())
                .color(product.getColor())
                .condition(product.getCondition())
                .material(product.getMaterial())
                .build();
    }
}
