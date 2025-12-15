package com.ryuqq.setof.application.productimage.dto.response;

import java.time.Instant;

/**
 * 상품이미지 조회 Response DTO
 *
 * @param id 상품이미지 ID
 * @param productGroupId 상품그룹 ID
 * @param imageType 이미지 타입
 * @param originUrl 원본 URL
 * @param cdnUrl CDN URL
 * @param displayOrder 표시 순서
 * @param createdAt 생성일시
 * @author development-team
 * @since 1.0.0
 */
public record ProductImageResponse(
        Long id,
        Long productGroupId,
        String imageType,
        String originUrl,
        String cdnUrl,
        int displayOrder,
        Instant createdAt) {}
