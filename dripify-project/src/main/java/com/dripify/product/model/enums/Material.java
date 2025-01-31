package com.dripify.product.model.enums;

public enum Material {
    COTTON("Cotton"),
    WOOL("Wool"),
    LINEN("Linen"),
    SILK("Silk"),
    POLYESTER("Polyester"),
    NYLON("Nylon"),
    SPANDEX("Spandex"),
    ACRYLIC("Acrylic"),
    LEATHER("Leather"),
    FAUX_LEATHER("Faux Leather"),
    DENIM("Denim"),
    SUEDE("Suede"),
    VELVET("Velvet"),
    SATIN("Satin"),
    CHIFFON("Chiffon"),
    LACE("Lace"),
    FLEECE("Fleece");

    private final String name;

    Material(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
