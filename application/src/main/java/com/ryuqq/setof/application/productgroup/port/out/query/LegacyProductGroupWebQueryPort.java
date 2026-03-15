package com.ryuqq.setof.application.productgroup.port.out.query;

import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSearchCriteria;
import java.util.List;
import java.util.Optional;

/**
 * LegacyProductGroupWebQueryPort - 레거시 DB 기반 Web 상품그룹 조회 포트.
 *
 * <p>레거시 DB(luxurydb)에서 Web 프론트용 상품그룹 썸네일/검색/상세 조회를 담당합니다.
 *
 * <p>새 DB로 완전 마이그레이션 완료 시 삭제 대상입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @deprecated 새 DB 마이그레이션 완료 시 {@link ProductGroupCompositeQueryPort}로 통합 예정
 */
@Deprecated
public interface LegacyProductGroupWebQueryPort {

    /** 상품그룹 단건 상세 조회 (레거시 v1). */
    Optional<LegacyProductGroupDetailCompositeResult> fetchProductGroupDetail(Long productGroupId);

    /** 상품그룹 썸네일 목록 조회 (커서 페이징). */
    List<ProductGroupListCompositeResult> fetchProductGroupThumbnails(
            ProductGroupSearchCriteria criteria);

    /** 상품그룹 전체 건수 조회. */
    long fetchProductGroupCount(ProductGroupSearchCriteria criteria);

    /** ID 목록 기반 상품그룹 조회 (찜 목록 등). */
    List<ProductGroupListCompositeResult> fetchProductGroupsByIds(List<Long> productGroupIds);

    /** 브랜드별 상품그룹 조회. */
    List<ProductGroupListCompositeResult> fetchProductGroupsByBrand(Long brandId, int pageSize);

    /** 셀러별 상품그룹 조회. */
    List<ProductGroupListCompositeResult> fetchProductGroupsBySeller(Long sellerId, int pageSize);

    /** 키워드 검색 결과 목록 조회. */
    List<ProductGroupListCompositeResult> fetchSearchResults(ProductGroupSearchCriteria criteria);

    /** 키워드 검색 전체 건수 조회. */
    long fetchSearchCount(ProductGroupSearchCriteria criteria);
}
