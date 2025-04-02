package com.dripify.web;

import com.dripify.category.service.CategoryService;
import com.dripify.order.service.OrderService;
import com.dripify.user.service.UserService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class BaseApiTest {

    @MockitoBean
    protected UserService userService;

    @MockitoBean
    protected CategoryService categoryService;

    @MockitoBean
    protected OrderService orderService;
}
