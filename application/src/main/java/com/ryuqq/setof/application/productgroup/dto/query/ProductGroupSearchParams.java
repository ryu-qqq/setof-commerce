package com.ryuqq.setof.application.productgroup.dto.query;

import com.ryuqq.setof.application.common.dto.query.CommonCursorParams;
import java.util.List;

/**
 * ProductGroupSearchParams - 상품그룹 커서 기반 검색 파라미터 DTO.
 *
 * <p>fetchProductGroups, fetchProductGroupLikes, fetchProductGroupWithBrand,
 * fetchProductGroupWithSeller, search 엔드포인트에서 공통으로 사용합니다.
 *
 * @param productGroupId 단건 조회용 상품그룹 ID
 * @param productGroupIds ID 목록 조회용 (찜 목록 등)
 * @param brandId 브랜드 ID 필터 (단일)
 * @param sellerId 셀러 ID 필터 (단일)
 * @param categoryId 카테고리 ID 필터 (단일)
 * @param categoryIds 다중 카테고리 ID 필터
 * @param brandIds 다중 브랜드 ID 필터
 * @param lowestPrice 최저가 필터
 * @param highestPrice 최고가 필터
 * @param searchWord 검색 키워드 (MySQL ngram FULLTEXT)
 * @param orderType 정렬 타입 (null이면 RECOMMEND 기본 적용)
 * @param cursorParams 커서 페이징 파라미터
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductGroupSearchParams(
        Long productGroupId,
        List<Long> productGroupIds,
        Long brandId,
        Long sellerId,
        Long categoryId,
        List<Long> categoryIds,
        List<Long> brandIds,
        Long lowestPrice,
        Long highestPrice,
        String searchWord,
        String orderType,
        CommonCursorParams cursorParams) {

    /** delegate 메서드 - cursor 값 직접 접근. */
    public String cursor() {
        return cursorParams.cursor();
    }

    /** delegate 메서드 - pageSize 직접 접근. */
    public int size() {
        return cursorParams.size();
    }

    /** 단건 상품그룹 조회용 파라미터 생성. */
    public static ProductGroupSearchParams ofProductGroupId(Long productGroupId) {
        return new ProductGroupSearchParams(
                productGroupId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                CommonCursorParams.first(1));
    }

    /** ID 목록(찜 목록 등) 조회용 파라미터 생성. */
    public static ProductGroupSearchParams ofProductGroupIds(List<Long> productGroupIds) {
        return new ProductGroupSearchParams(
                null,
                productGroupIds,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                CommonCursorParams.first(productGroupIds.size()));
    }

    /** 브랜드별 상품그룹 조회용 파라미터 생성. */
    public static ProductGroupSearchParams ofBrand(Long brandId, int pageSize) {
        return new ProductGroupSearchParams(
                null,
                null,
                brandId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "RECOMMEND",
                CommonCursorParams.first(pageSize));
    }

    /** 셀러별 상품그룹 조회용 파라미터 생성. */
    public static ProductGroupSearchParams ofSeller(Long sellerId, int pageSize) {
        return new ProductGroupSearchParams(
                null,
                null,
                null,
                sellerId,
                null,
                null,
                null,
                null,
                null,
                null,
                "RECOMMEND",
                CommonCursorParams.first(pageSize));
    }

    /** 커서 페이징이 있는지 확인. */
    public boolean hasCursor() {
        return cursorParams.hasCursor();
    }
}
