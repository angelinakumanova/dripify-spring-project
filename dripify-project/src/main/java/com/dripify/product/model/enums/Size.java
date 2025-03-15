package com.dripify.product.model.enums;


public enum Size {
    // Clothing Sizes
    XS(SizeCategory.CLOTHING),
    S(SizeCategory.CLOTHING),
    M(SizeCategory.CLOTHING),
    L(SizeCategory.CLOTHING),
    XL(SizeCategory.CLOTHING),
    XXL(SizeCategory.CLOTHING),
    XXXL(SizeCategory.CLOTHING),

    // Shoe Sizes
    EU36(SizeCategory.SHOES),
    EU37(SizeCategory.SHOES),
    EU38(SizeCategory.SHOES),
    EU39(SizeCategory.SHOES),
    EU40(SizeCategory.SHOES),
    EU41(SizeCategory.SHOES),
    EU42(SizeCategory.SHOES),
    EU43(SizeCategory.SHOES),
    EU44(SizeCategory.SHOES);

    private final SizeCategory category;

    Size(SizeCategory category) {
        this.category = category;
    }

    public SizeCategory getCategory() {
        return category;
    }

}
