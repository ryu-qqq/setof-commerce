package com.ryuqq.setof.application.productgroupdescription.dto.response;

import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;
import java.util.List;

/** 상품 그룹 상세설명 조회 결과 DTO. */
public record ProductGroupDescriptionResult(
        Long id, String content, String cdnPath, List<DescriptionImageResult> images) {

    public ProductGroupDescriptionResult {
        images = images != null ? List.copyOf(images) : List.of();
    }

    public static ProductGroupDescriptionResult from(ProductGroupDescription description) {
        List<DescriptionImageResult> imageResults =
                description.images().stream().map(DescriptionImageResult::from).toList();
        return new ProductGroupDescriptionResult(
                description.idValue(),
                description.contentValue(),
                description.cdnPath(),
                imageResults);
    }
}
