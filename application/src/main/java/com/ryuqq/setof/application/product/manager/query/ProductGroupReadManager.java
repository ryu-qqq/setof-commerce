package com.ryuqq.setof.application.product.manager.query;

import com.ryuqq.setof.application.product.port.out.query.ProductGroupQueryPort;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.exception.ProductGroupNotFoundException;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroup Read Manager
 *
 * <p>ProductGroup 조회를 담당하는 Manager
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductGroupReadManager {

    private final ProductGroupQueryPort productGroupQueryPort;

    public ProductGroupReadManager(ProductGroupQueryPort productGroupQueryPort) {
        this.productGroupQueryPort = productGroupQueryPort;
    }

    /**
     * ID로 ProductGroup 조회 (없으면 예외)
     *
     * @param productGroupId 상품그룹 ID
     * @return ProductGroup
     * @throws ProductGroupNotFoundException 상품그룹이 존재하지 않으면
     */
    public ProductGroup findById(Long productGroupId) {
        ProductGroupId id = ProductGroupId.of(productGroupId);
        return productGroupQueryPort
                .findById(id)
                .orElseThrow(() -> new ProductGroupNotFoundException(productGroupId));
    }

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
    public List<ProductGroup> findByConditions(
            Long sellerId,
            Long categoryId,
            Long brandId,
            String name,
            String status,
            int offset,
            int limit) {
        return productGroupQueryPort.findByConditions(
                sellerId, categoryId, brandId, name, status, offset, limit);
    }

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
    public long countByConditions(
            Long sellerId, Long categoryId, Long brandId, String name, String status) {
        return productGroupQueryPort.countByConditions(sellerId, categoryId, brandId, name, status);
    }

    /**
     * ProductGroup 존재 여부 확인
     *
     * @param productGroupId 상품그룹 ID
     * @return 존재 여부
     */
    public boolean existsById(Long productGroupId) {
        ProductGroupId id = ProductGroupId.of(productGroupId);
        return productGroupQueryPort.existsById(id);
    }
}
