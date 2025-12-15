package com.ryuqq.setof.domain.product.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import java.util.Map;

/**
 * 상품그룹을 찾을 수 없음 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>DomainException 상속 필수
 *   <li>Lombok 금지
 *   <li>Spring 의존성 금지
 * </ul>
 */
public class ProductGroupNotFoundException extends DomainException {

    /**
     * 상품그룹 ID로 예외 생성
     *
     * @param productGroupId 찾을 수 없는 상품그룹 ID
     */
    public ProductGroupNotFoundException(ProductGroupId productGroupId) {
        super(
                ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND,
                String.format(
                        "상품그룹을 찾을 수 없습니다. ID: %s",
                        productGroupId != null ? productGroupId.value() : "null"),
                Map.of(
                        "productGroupId",
                        productGroupId != null && productGroupId.value() != null
                                ? productGroupId.value()
                                : "null"));
    }

    /**
     * Long ID로 예외 생성
     *
     * @param productGroupId 찾을 수 없는 상품그룹 ID (Long)
     */
    public ProductGroupNotFoundException(Long productGroupId) {
        super(
                ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND,
                String.format("상품그룹을 찾을 수 없습니다. ID: %s", productGroupId),
                Map.of("productGroupId", productGroupId != null ? productGroupId : "null"));
    }
}
