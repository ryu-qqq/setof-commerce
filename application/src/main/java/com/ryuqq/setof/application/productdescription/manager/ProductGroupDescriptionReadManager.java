package com.ryuqq.setof.application.productdescription.manager;

import com.ryuqq.setof.application.productdescription.port.out.query.ProductGroupDescriptionQueryPort;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;
import com.ryuqq.setof.domain.productdescription.exception.ProductDescriptionNotFoundException;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductGroupDescriptionReadManager - 상세설명 조회 Manager.
 *
 * <p>QueryPort에 위임하고 도메인 예외를 던집니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@Transactional(readOnly = true)
public class ProductGroupDescriptionReadManager {

    private final ProductGroupDescriptionQueryPort queryPort;

    public ProductGroupDescriptionReadManager(ProductGroupDescriptionQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    /**
     * 상품그룹 ID로 상세설명을 조회합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 상세설명 도메인 객체
     * @throws ProductDescriptionNotFoundException 상세설명을 찾을 수 없는 경우
     */
    public ProductGroupDescription getByProductGroupId(ProductGroupId productGroupId) {
        return queryPort
                .findByProductGroupId(productGroupId)
                .orElseThrow(() -> new ProductDescriptionNotFoundException(productGroupId));
    }
}
