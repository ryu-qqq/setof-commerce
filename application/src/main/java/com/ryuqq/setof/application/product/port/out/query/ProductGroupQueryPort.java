package com.ryuqq.setof.application.product.port.out.query;

import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import java.util.List;
import java.util.Optional;

/**
 * ProductGroup Query Port (Query)
 *
 * <p>ProductGroup Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductGroupQueryPort {

    /**
     * ID로 ProductGroup 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return ProductGroup (없으면 empty)
     */
    Optional<ProductGroup> findById(ProductGroupId productGroupId);

    /**
     * 조건으로 ProductGroup 목록 조회
     *
     * @param sellerId 셀러 ID (nullable)
     * @param categoryId 카테고리 ID (nullable)
     * @param brandId 브랜드 ID (nullable)
     * @param name 상품그룹명 (nullable)
     * @param status 상태 (nullable)
     * @param offset 오프셋
     * @param limit 제한
     * @return ProductGroup 목록
     */
    List<ProductGroup> findByConditions(
            Long sellerId,
            Long categoryId,
            Long brandId,
            String name,
            String status,
            int offset,
            int limit);

    /**
     * 조건에 맞는 ProductGroup 총 개수 조회
     *
     * @param sellerId 셀러 ID (nullable)
     * @param categoryId 카테고리 ID (nullable)
     * @param brandId 브랜드 ID (nullable)
     * @param name 상품그룹명 (nullable)
     * @param status 상태 (nullable)
     * @return 총 개수
     */
    long countByConditions(
            Long sellerId, Long categoryId, Long brandId, String name, String status);

    /**
     * ProductGroup 존재 여부 확인
     *
     * @param productGroupId 상품그룹 ID
     * @return 존재 여부
     */
    boolean existsById(ProductGroupId productGroupId);
}
