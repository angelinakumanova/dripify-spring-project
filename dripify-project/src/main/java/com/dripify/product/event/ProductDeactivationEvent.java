package com.dripify.product.event;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductDeactivationEvent {

    private final UUID productId;


    public ProductDeactivationEvent(UUID productId) {
        this.productId = productId;
    }
}
