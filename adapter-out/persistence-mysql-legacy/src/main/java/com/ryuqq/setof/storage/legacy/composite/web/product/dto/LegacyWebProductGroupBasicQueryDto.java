package com.ryuqq.setof.storage.legacy.composite.web.product.dto;

import java.time.LocalDateTime;

/**
 * LegacyWebProductGroupBasicQueryDto - 레거시 웹 상품그룹 기본 정보 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * <p>상품그룹 상세 조회의 Query 1: 기본 정보 (8개 테이블 조인).
 *
 * @param productGroupId 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param sellerId 셀러 ID
 * @param sellerName 셀러명
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param displayKoreanName 표시용 한글 브랜드명
 * @param displayEnglishName 표시용 영문 브랜드명
 * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
 * @param categoryId 카테고리 ID
 * @param path 카테고리 경로
 * @param regularPrice 정가
 * @param currentPrice 현재가
 * @param salePrice 판매가
 * @param optionType 옵션 타입
 * @param displayYn 표시 여부
 * @param soldOutYn 품절 여부
 * @param detailDescription 상세 설명
 * @param deliveryNotice 배송 안내
 * @param refundNotice 환불 안내
 * @param averageRating 평균 평점
 * @param reviewCount 리뷰 수
 * @param createdAt 등록일
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
        String optionType,
        String displayYn,
        String soldOutYn,
        String detailDescription,
        String deliveryNotice,
        String refundNotice,
        Double averageRating,
        Long reviewCount,
        LocalDateTime createdAt) {}
