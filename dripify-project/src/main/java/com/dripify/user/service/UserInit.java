package com.dripify.user.service;

import com.dripify.web.dto.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInit implements CommandLineRunner {

    private final UserService userService;

    public UserInit(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
//        RegisterRequest registerRequest = new RegisterRequest();
//
//
//
//        userService.register(registerRequest);
    }
}
