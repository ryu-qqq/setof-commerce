package com.ryuqq.setof.domain.product.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductGroupStatus;
import java.util.Map;

/**
 * 상품그룹 수정 불가 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>DomainException 상속 필수
 *   <li>Lombok 금지
 *   <li>Spring 의존성 금지
 * </ul>
 */
public class ProductGroupNotEditableException extends DomainException {

    /**
     * 상품그룹 ID와 현재 상태로 예외 생성
     *
     * @param productGroupId 수정 불가한 상품그룹 ID
     * @param currentStatus 현재 상태
     */
    public ProductGroupNotEditableException(
            ProductGroupId productGroupId, ProductGroupStatus currentStatus) {
        super(
                ProductGroupErrorCode.PRODUCT_GROUP_NOT_EDITABLE,
                String.format(
                        "수정할 수 없는 상품그룹입니다. ID: %s, 현재상태: %s",
                        productGroupId != null ? productGroupId.value() : "null",
                        currentStatus != null ? currentStatus.name() : "null"),
                Map.of(
                        "productGroupId",
                                productGroupId != null && productGroupId.value() != null
                                        ? productGroupId.value()
                                        : "null",
                        "currentStatus", currentStatus != null ? currentStatus.name() : "null"));
    }
}
