package com.ryuqq.setof.storage.legacy.composite.web.user.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 레거시 찜 목록 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param userFavoriteId 찜 ID
 * @param productGroupId 상품 그룹 ID
 * @param sellerId 셀러 ID
 * @param productGroupName 상품 그룹명
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param productImageUrl 상품 이미지 URL
 * @param regularPrice 정상가
 * @param currentPrice 판매가
 * @param discountRate 할인율
 * @param soldOutYn 품절 여부
 * @param displayYn 전시 여부
 * @param insertDate 등록일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebUserFavoriteQueryDto(
        long userFavoriteId,
        long productGroupId,
        long sellerId,
        String productGroupName,
        long brandId,
        String brandName,
        String productImageUrl,
        BigDecimal regularPrice,
        BigDecimal currentPrice,
        int discountRate,
        String soldOutYn,
        String displayYn,
        LocalDateTime insertDate) {}
