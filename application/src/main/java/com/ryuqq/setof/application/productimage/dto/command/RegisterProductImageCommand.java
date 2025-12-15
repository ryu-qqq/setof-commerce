package com.ryuqq.setof.application.productimage.dto.command;

/**
 * 상품이미지 등록 Command DTO
 *
 * @param productGroupId 상품그룹 ID
 * @param imageType 이미지 타입 (MAIN, SUB, DETAIL)
 * @param originUrl 원본 URL
 * @param cdnUrl CDN URL (nullable, 없으면 원본과 동일)
 * @param displayOrder 표시 순서
 * @author development-team
 * @since 1.0.0
 */
public record RegisterProductImageCommand(
        Long productGroupId, String imageType, String originUrl, String cdnUrl, int displayOrder) {}
