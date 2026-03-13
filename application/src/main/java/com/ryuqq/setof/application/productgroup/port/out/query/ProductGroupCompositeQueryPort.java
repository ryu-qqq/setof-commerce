package com.ryuqq.setof.application.productgroup.port.out.query;

import com.ryuqq.setof.application.product.dto.response.ProductResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeQueryResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupEnrichmentResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupExcelBaseBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupOffsetSearchCriteria;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ProductGroup Composite Query Port.
 *
 * <p>ProductGroup + Brand + Category + Seller 크로스 도메인 JOIN을 통한 성능 최적화된 조회를 제공합니다.
 *
 * <p>목록용 기본 Composition 조회, 배치 enrichment 쿼리, 상세용 정책 포함 조회를 함께 제공합니다.
 */
public interface ProductGroupCompositeQueryPort {

    Optional<ProductGroupListCompositeResult> findCompositeById(Long productGroupId);

    List<ProductGroupListCompositeResult> findCompositeByCriteria(
            ProductGroupOffsetSearchCriteria criteria);

    long countByCriteria(ProductGroupOffsetSearchCriteria criteria);

    /**
     * 상품 그룹 ID 목록에 대한 가격 + 옵션 통합 enrichment 배치 조회.
     *
     * <p>각 상품 그룹에 속한 Product 가격 요약(MIN/MAX currentPrice, MAX discountRate)과
     * SellerOptionGroup/SellerOptionValue 요약을 한 번에 조회합니다.
     */
    List<ProductGroupEnrichmentResult> findEnrichmentsByProductGroupIds(List<Long> productGroupIds);

    /**
     * 상품 그룹 상세용 Composition 조회 (정책 포함).
     *
     * <p>ProductGroup + Seller + Brand + Category + ShippingPolicy + RefundPolicy를 JOIN하여 기본 정보와 정책
     * 데이터를 한 번에 조회합니다.
     */
    Optional<ProductGroupDetailCompositeQueryResult> findDetailCompositeById(Long productGroupId);

    /**
     * 엑셀용 통합 Composite 조회 (base + 가격 enrichment + description cdnUrl).
     *
     * <p>기존 findCompositeByCriteria + findEnrichments + description 조회를 통합합니다.
     */
    ProductGroupExcelBaseBundle findExcelBaseBundleByCriteria(
            ProductGroupOffsetSearchCriteria criteria);

    /**
     * 상품 + 옵션 매핑 + 옵션 이름 해석 배치 조회.
     *
     * <p>product -> product_option_mappings -> seller_option_values -> seller_option_groups JOIN.
     */
    Map<Long, List<ProductResult>> findProductsWithOptionNamesByProductGroupIds(
            List<Long> productGroupIds);
}
