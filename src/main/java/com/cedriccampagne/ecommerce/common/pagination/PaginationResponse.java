package com.cedriccampagne.ecommerce.common.pagination;

import java.util.List;

public record  PaginationResponse<T> (
    List<T>items,
    int page,
    int size,
    int totalItems,
    int totalPages,
    boolean hasNext,
    boolean hasPrevious
) {}
