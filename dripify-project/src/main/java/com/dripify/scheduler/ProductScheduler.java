package com.dripify.scheduler;

import com.dripify.product.service.ProductService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProductScheduler {

    private final ProductService productService;

    public ProductScheduler(ProductService productService) {
        this.productService = productService;
    }

    //cron = "0 0 0 * * *"
    @Async
    @Scheduled(cron = "0/10 * * * * *")
    public void deleteInactiveProducts() {
        productService.deleteInactiveProducts();
    }

}
