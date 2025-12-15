package com.ryuqq.setof.application.productdescription.dto.command;

import java.util.List;

/**
 * 상품설명 수정 Command DTO
 *
 * @param productDescriptionId 상품설명 ID
 * @param htmlContent HTML 컨텐츠
 * @param images 이미지 목록
 */
public record UpdateProductDescriptionCommand(
        Long productDescriptionId, String htmlContent, List<DescriptionImageDto> images) {}
