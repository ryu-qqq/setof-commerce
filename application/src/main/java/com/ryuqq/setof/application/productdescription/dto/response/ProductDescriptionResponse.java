package com.ryuqq.setof.application.productdescription.dto.response;

import java.time.Instant;
import java.util.List;

/**
 * 상품설명 Response DTO
 *
 * @param productDescriptionId 상품설명 ID
 * @param productGroupId 상품그룹 ID
 * @param htmlContent HTML 컨텐츠
 * @param images 이미지 목록
 * @param hasContent 컨텐츠 존재 여부
 * @param allImagesCdnConverted 모든 이미지 CDN 변환 여부
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 */
public record ProductDescriptionResponse(
        Long productDescriptionId,
        Long productGroupId,
        String htmlContent,
        List<DescriptionImageResponse> images,
        boolean hasContent,
        boolean allImagesCdnConverted,
        Instant createdAt,
        Instant updatedAt) {}
