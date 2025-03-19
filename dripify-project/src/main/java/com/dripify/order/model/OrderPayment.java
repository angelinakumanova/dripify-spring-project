package com.dripify.order.model;

public enum OrderPayment {
    CASH("Payment On Delivery"), CARD("Pay Online");

    private final String displayName;

    OrderPayment(String displayName) {
        this.displayName = displayName;
    }


}
