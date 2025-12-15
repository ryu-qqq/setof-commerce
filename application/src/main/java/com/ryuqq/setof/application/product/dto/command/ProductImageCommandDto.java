package com.ryuqq.setof.application.product.dto.command;

/**
 * 상품이미지 Command DTO
 *
 * <p>상품그룹 이미지 등록/수정 데이터
 *
 * @param id 이미지 ID (수정 시 사용, null이면 신규)
 * @param imageType 이미지 타입 (MAIN, SUB, DETAIL)
 * @param originUrl 원본 URL
 * @param cdnUrl CDN URL (nullable, 없으면 원본과 동일)
 * @param displayOrder 표시 순서
 * @author development-team
 * @since 1.0.0
 */
public record ProductImageCommandDto(
        Long id, String imageType, String originUrl, String cdnUrl, int displayOrder) {}
