package com.dripify.product.model.enums;

public enum Brand {
    NIKE("Nike"),
    ADIDAS("Adidas"),
    PUMA("Puma"),
    REEBOK("Reebok"),
    NEW_BALANCE("New Balance"),
    CONVERSE("Converse"),
    VANS("Vans"),
    FILA("FILA"),
    LEVI_S("Levi's"),
    TOMMY_HILFIGER("Tommy Hilfiger"),
    CALVIN_KLEIN("Calvin Klein"),
    ZARA("Zara"),
    BERSHKA("Bershka"),
    H_M("H&M"),
    GUCCI("Gucci"),
    PRADA("Prada"),
    LOUIS_VUITTON("Louis Vuitton"),
    BALENCIAGA("Balenciaga"),
    OTHER("Other"),
    NO_LABEL("No Label");

    private final String name;

    Brand(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
