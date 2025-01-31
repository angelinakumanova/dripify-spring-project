package com.dripify.product.model.enums;

public enum Size {
    XXS("Extra Extra Small"),
    XS("Extra Small"),
    S("Small"),
    M("Medium"),
    L("Large"),
    XL("Extra Large"),
    XXL("Extra Extra Large"),
    XXXL("Triple Extra Large");

    private final String name;

    Size(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
