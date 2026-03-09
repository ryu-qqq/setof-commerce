package com.ryuqq.setof.storage.legacy.composite.productgroup.dto;

import java.time.LocalDateTime;

/**
 * LegacyWebProductGroupBasicQueryDto - 레거시 Web 상품그룹 기본 정보 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * <p>fetchProductGroup 단건 조회에서 기본 정보(쿼리 1)를 담습니다. product_group, seller, brand, category,
 * product_delivery, product_notice, product_group_detail_description, product_rating_stats 조인 결과.
 *
 * @param productGroupId 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param sellerId 셀러 ID
 * @param sellerName 셀러명
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param displayKoreanName 브랜드 한글 표시명
 * @param displayEnglishName 브랜드 영문 표시명
 * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
 * @param categoryId 카테고리 ID
 * @param path 카테고리 경로 (쉼표 구분)
 * @param regularPrice 정가
 * @param currentPrice 현재가
 * @param salePrice 판매가
 * @param directDiscountRate 직접 할인율
 * @param directDiscountPrice 직접 할인액
 * @param discountRate 총 할인율
 * @param optionType 옵션 타입 (SINGLE/COMBINATION)
 * @param displayYn 표시 여부
 * @param soldOutYn 품절 여부
 * @param detailDescription 상세 설명 이미지 URL
 * @param deliveryNotice 배송 안내
 * @param refundNotice 환불 안내
 * @param averageRating 평균 평점
 * @param reviewCount 리뷰 수
 * @param insertDate 등록일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebProductGroupBasicQueryDto(
        long productGroupId,
        String productGroupName,
        long sellerId,
        String sellerName,
        long brandId,
        String brandName,
        String displayKoreanName,
        String displayEnglishName,
        String brandIconImageUrl,
        long categoryId,
        String path,
        int regularPrice,
        int currentPrice,
        int salePrice,
        int directDiscountRate,
        int directDiscountPrice,
        int discountRate,
        String optionType,
        String displayYn,
        String soldOutYn,
        String detailDescription,
        String deliveryNotice,
        String refundNotice,
        double averageRating,
        long reviewCount,
        LocalDateTime insertDate) {}
