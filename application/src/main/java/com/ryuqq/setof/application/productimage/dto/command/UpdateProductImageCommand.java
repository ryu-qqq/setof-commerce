package com.ryuqq.setof.application.productimage.dto.command;

/**
 * 상품이미지 수정 Command DTO
 *
 * @param id 상품이미지 ID
 * @param imageType 이미지 타입 (MAIN, SUB, DETAIL)
 * @param cdnUrl CDN URL
 * @param displayOrder 표시 순서
 * @author development-team
 * @since 1.0.0
 */
public record UpdateProductImageCommand(
        Long id, String imageType, String cdnUrl, int displayOrder) {}
