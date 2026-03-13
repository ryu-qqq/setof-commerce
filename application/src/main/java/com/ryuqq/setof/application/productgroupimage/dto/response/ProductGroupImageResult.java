package com.ryuqq.setof.application.productgroupimage.dto.response;

import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;

/** 상품 그룹 이미지 조회 결과 DTO. */
public record ProductGroupImageResult(Long id, String imageUrl, String imageType, int sortOrder) {

    public static ProductGroupImageResult from(ProductGroupImage image) {
        return new ProductGroupImageResult(
                image.idValue(),
                image.imageUrlValue(),
                image.imageType().name(),
                image.sortOrder());
    }
}
