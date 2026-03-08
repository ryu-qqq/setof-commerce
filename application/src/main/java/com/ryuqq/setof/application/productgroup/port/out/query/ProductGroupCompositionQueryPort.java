package com.ryuqq.setof.application.productgroup.port.out.query;

import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupThumbnailCompositeResult;
import com.ryuqq.setof.domain.legacy.product.dto.query.LegacyProductGroupSearchCondition;
import com.ryuqq.setof.domain.legacy.search.dto.query.LegacySearchCondition;
import java.util.List;
import java.util.Optional;

/**
 * ProductGroupCompositionQueryPort - 상품그룹 Composite 조회 출력 포트.
 *
 * <p>레거시 DB(persistence-mysql-legacy)를 대상으로 하는 Composite 조회를 제공합니다.
 *
 * <p>product_group, product_group_image, brand, product_rating_stats, product_score 등 크로스 도메인 JOIN을
 * 통한 성능 최적화된 조회를 담당합니다.
 *
 * <p>Search(FULLTEXT) 조회도 동일한 포트에서 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ProductGroupCompositionQueryPort {

    /**
     * 상품그룹 단건 상세 조회.
     *
     * <p>기본 정보(쿼리 1) + 개별 상품 목록(쿼리 2) + 이미지 목록(쿼리 3)을 조합합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 상세 Composite 결과 Optional
     */
    Optional<ProductGroupDetailCompositeResult> fetchProductGroupDetail(Long productGroupId);

    /**
     * 상품그룹 썸네일 목록 조회 (커서 페이징).
     *
     * <p>pageSize + 1개 조회하여 hasNext 판별을 위임합니다.
     *
     * @param condition 검색 조건
     * @return 썸네일 목록
     */
    List<ProductGroupThumbnailCompositeResult> fetchProductGroupThumbnails(
            LegacyProductGroupSearchCondition condition);

    /**
     * 상품그룹 전체 건수 조회 (카운트 쿼리).
     *
     * @param condition 검색 조건
     * @return 전체 건수
     */
    long fetchProductGroupCount(LegacyProductGroupSearchCondition condition);

    /**
     * ID 목록 기반 상품그룹 썸네일 조회 (찜 목록 등).
     *
     * <p>요청 ID 순서로 재정렬하여 반환합니다.
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return 요청 ID 순서로 재정렬된 썸네일 목록
     */
    List<ProductGroupThumbnailCompositeResult> fetchProductGroupsByIds(List<Long> productGroupIds);

    /**
     * 브랜드별 상품그룹 썸네일 조회.
     *
     * <p>Redis 캐시 없이 DB 직접 조회합니다.
     *
     * @param brandId 브랜드 ID
     * @param pageSize 페이지 크기
     * @return 썸네일 목록 (score DESC 정렬)
     */
    List<ProductGroupThumbnailCompositeResult> fetchProductGroupsByBrand(
            Long brandId, int pageSize);

    /**
     * 셀러별 상품그룹 썸네일 조회.
     *
     * <p>Redis 캐시 없이 DB 직접 조회합니다.
     *
     * @param sellerId 셀러 ID
     * @param pageSize 페이지 크기
     * @return 썸네일 목록 (score DESC 정렬)
     */
    List<ProductGroupThumbnailCompositeResult> fetchProductGroupsBySeller(
            Long sellerId, int pageSize);

    /**
     * 키워드 검색 결과 목록 조회 (MySQL ngram FULLTEXT + 커서 페이징).
     *
     * <p>pageSize + 1개 조회하여 hasNext 판별을 위임합니다.
     *
     * @param condition 검색 조건 (searchWord 포함)
     * @return 검색 결과 목록
     */
    List<ProductGroupThumbnailCompositeResult> fetchSearchResults(LegacySearchCondition condition);

    /**
     * 키워드 검색 전체 건수 조회.
     *
     * @param condition 검색 조건
     * @return 전체 검색 건수
     */
    long fetchSearchCount(LegacySearchCondition condition);
}
