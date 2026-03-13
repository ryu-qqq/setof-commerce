package com.ryuqq.setof.application.productgroupdescription.dto.response;

import com.ryuqq.setof.domain.productdescription.aggregate.DescriptionImage;

/** 상세설명 이미지 조회 결과 DTO. */
public record DescriptionImageResult(Long id, String imageUrl, int sortOrder) {

    public static DescriptionImageResult from(DescriptionImage image) {
        return new DescriptionImageResult(image.idValue(), image.imageUrl(), image.sortOrder());
    }
}
