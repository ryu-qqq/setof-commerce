package com.ryuqq.setof.application.productdescription.dto.command;

import java.util.List;

/**
 * 상품그룹 상세설명 등록 커맨드.
 *
 * @param productGroupId 상품그룹 ID
 * @param content 상세설명 HTML 컨텐츠
 * @param descriptionImages 상세설명 이미지 목록
 */
public record RegisterProductGroupDescriptionCommand(
        long productGroupId, String content, List<DescriptionImageCommand> descriptionImages) {

    /**
     * 상세설명 이미지 커맨드.
     *
     * @param imageUrl 이미지 URL
     * @param sortOrder 정렬 순서
     */
    public record DescriptionImageCommand(String imageUrl, int sortOrder) {}
}
