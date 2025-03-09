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
    EU36(SizeCategory.SHOE),
    EU37(SizeCategory.SHOE),
    EU38(SizeCategory.SHOE),
    EU39(SizeCategory.SHOE),
    EU40(SizeCategory.SHOE),
    EU41(SizeCategory.SHOE),
    EU42(SizeCategory.SHOE),
    EU43(SizeCategory.SHOE),
    EU44(SizeCategory.SHOE);

    private final SizeCategory category;

    Size(SizeCategory category) {
        this.category = category;
    }

    public SizeCategory getCategory() {
        return category;
    }

}
