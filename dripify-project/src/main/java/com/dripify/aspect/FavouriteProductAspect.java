package com.dripify.aspect;

import com.dripify.product.model.Product;
import com.dripify.user.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FavouriteProductAspect {

    private final UserService userService;

    public FavouriteProductAspect(UserService userService) {
        this.userService = userService;
    }

    @After("execution(public void com.dripify.product..deactivateProduct(..))")
    public void removeInactiveProduct(JoinPoint joinPoint) {
        Product product = (Product) joinPoint.getArgs()[0];

        userService.removeInactiveProducts(product);
    }
}
