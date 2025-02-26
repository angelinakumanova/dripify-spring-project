package com.dripify.web.mapper;

import com.dripify.user.model.User;
import com.dripify.web.dto.UserEditRequest;
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
}
