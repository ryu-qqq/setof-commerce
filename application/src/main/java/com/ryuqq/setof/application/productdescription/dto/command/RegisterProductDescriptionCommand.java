package com.ryuqq.setof.application.productdescription.dto.command;

import java.util.List;

/**
 * 상품설명 등록 Command DTO
 *
 * @param productGroupId 상품그룹 ID
 * @param htmlContent HTML 컨텐츠
 * @param images 이미지 목록
 */
public record RegisterProductDescriptionCommand(
        Long productGroupId, String htmlContent, List<DescriptionImageDto> images) {}
