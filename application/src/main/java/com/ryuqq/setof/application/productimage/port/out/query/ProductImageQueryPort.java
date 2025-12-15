package com.ryuqq.setof.application.productimage.port.out.query;

import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import java.util.List;
import java.util.Optional;

/**
 * ProductImage Query Port (Query)
 *
 * <p>ProductImage Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductImageQueryPort {

    /**
     * ID로 상품이미지 조회
     *
     * @param productImageId 상품이미지 ID
     * @return ProductImage (없으면 empty)
     */
    Optional<ProductImage> findById(Long productImageId);

    /**
     * 상품그룹 ID로 이미지 목록 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return ProductImage 목록
     */
    List<ProductImage> findByProductGroupId(Long productGroupId);
}
