package com.dripify.product.model.enums;


public enum Color {
    BLACK("Black"),
    WHITE("White"),
    GRAY("Gray"),
    RED("Red"),
    BLUE("Blue"),
    GREEN("Green"),
    YELLOW("Yellow"),
    ORANGE("Orange"),
    PINK("Pink"),
    PURPLE("Purple"),
    BROWN("Brown"),
    BEIGE("Beige"),
    NAVY("Navy"),
    TEAL("Teal"),
    OLIVE("Olive"),
    MAROON("Maroon"),
    CYAN("Cyan"),
    OTHER("Other");

    private final String name;

    Color(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
