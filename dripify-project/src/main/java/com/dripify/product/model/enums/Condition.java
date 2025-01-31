package com.dripify.product.model.enums;

public enum Condition {
    NEW("New"),
    VERY_GOOD("Very Good"),
    ACCEPTABLE("Acceptable"),
    WORN("Worn");

    private final String name;

    Condition(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
