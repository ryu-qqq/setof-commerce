package com.ryuqq.setof.application.productdescription.dto.response;

import java.time.Instant;

/**
 * 상품설명 이미지 Response DTO
 *
 * @param displayOrder 표시 순서
 * @param originUrl 원본 이미지 URL
 * @param cdnUrl CDN 이미지 URL
 * @param uploadedAt 업로드 일시
 * @param cdnConverted CDN 변환 여부
 */
public record DescriptionImageResponse(
        int displayOrder,
        String originUrl,
        String cdnUrl,
        Instant uploadedAt,
        boolean cdnConverted) {}
