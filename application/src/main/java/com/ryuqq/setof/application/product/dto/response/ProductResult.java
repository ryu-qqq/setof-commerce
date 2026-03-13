package com.ryuqq.setof.application.product.dto.response;

import java.time.Instant;
import java.util.List;

/** 상품(SKU) 조회 결과 DTO. */
public record ProductResult(
        Long id,
        Long productGroupId,
        String skuCode,
        int regularPrice,
        int currentPrice,
        Integer salePrice,
        int discountRate,
        int stockQuantity,
        String status,
        int sortOrder,
        List<ProductOptionMappingResult> optionMappings,
        Instant createdAt,
        Instant updatedAt) {}
