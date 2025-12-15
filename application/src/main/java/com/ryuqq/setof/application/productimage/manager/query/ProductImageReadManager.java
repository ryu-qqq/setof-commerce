package com.ryuqq.setof.application.productimage.manager.query;

import com.ryuqq.setof.application.productimage.port.out.query.ProductImageQueryPort;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productimage.exception.ProductImageNotFoundException;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductImage Read Manager
 *
 * <p>ProductImage 읽기 작업 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductImageReadManager {

    private final ProductImageQueryPort queryPort;

    public ProductImageReadManager(ProductImageQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    /**
     * ID로 상품이미지 조회
     *
     * @param productImageId 상품이미지 ID
     * @return ProductImage 도메인
     * @throws ProductImageNotFoundException 이미지를 찾을 수 없는 경우
     */
    public ProductImage findById(Long productImageId) {
        return queryPort
                .findById(productImageId)
                .orElseThrow(() -> new ProductImageNotFoundException(productImageId));
    }

    /**
     * 상품그룹 ID로 이미지 목록 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return ProductImage 목록
     */
    public List<ProductImage> findByProductGroupId(Long productGroupId) {
        return queryPort.findByProductGroupId(productGroupId);
    }
}
