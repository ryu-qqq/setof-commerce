package com.ryuqq.setof.migration.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 레거시 PRODUCT_GROUP 테이블 조회 DTO
 *
 * <p>레거시 DB의 PRODUCT_GROUP 테이블 데이터를 담는 DTO입니다.
 *
 * @param productGroupId 레거시 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param sellerId 셀러 ID
 * @param brandId 브랜드 ID
 * @param categoryId 카테고리 ID
 * @param optionType 옵션 타입 (SINGLE, COMBINATION)
 * @param regularPrice 정가
 * @param currentPrice 판매가
 * @param soldOutYn 품절 여부 (Y/N)
 * @param displayYn 전시 여부 (Y/N)
 * @param createdAt 생성 일시
 * @param updatedAt 수정 일시
 * @author development-team
 * @since 1.0.0
 */
public record LegacyProductGroupDto(
        Long productGroupId,
        String productGroupName,
        Long sellerId,
        Long brandId,
        Long categoryId,
        String optionType,
        BigDecimal regularPrice,
        BigDecimal currentPrice,
        String soldOutYn,
        String displayYn,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}
