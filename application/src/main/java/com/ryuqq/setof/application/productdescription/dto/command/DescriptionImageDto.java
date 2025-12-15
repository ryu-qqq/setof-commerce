package com.ryuqq.setof.application.productdescription.dto.command;

/**
 * 상품설명 이미지 DTO
 *
 * @param displayOrder 표시 순서
 * @param originUrl 원본 이미지 URL
 * @param cdnUrl CDN 이미지 URL
 */
public record DescriptionImageDto(int displayOrder, String originUrl, String cdnUrl) {}
