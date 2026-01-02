package com.connectly.partnerAdmin.module.external.dto;


import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;

public record ProductGroupImageRequestDto(
        ProductGroupImageType productImageType,
        String imageUrl
){}
