package com.cedriccampagne.ecommerce.cartItem.dto;

import java.math.BigDecimal;

public record CartTotalDto (
    BigDecimal Total
) {}
