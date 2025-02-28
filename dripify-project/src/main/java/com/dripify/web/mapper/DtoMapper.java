package com.dripify.web.mapper;

import com.dripify.user.model.User;
import com.dripify.web.dto.EmailUpdateRequest;
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
}
