package com.ryuqq.setof.application.productgroup.port.out.command;

import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;

/**
 * ProductGroupCommandPort - 상품 그룹 Command 출력 포트.
 *
 * <p>Adapter가 구현할 출력 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface ProductGroupCommandPort {

    /**
     * 상품 그룹을 저장합니다.
     *
     * @param productGroup 저장할 상품 그룹 도메인 객체
     * @return 저장된 상품 그룹 ID
     */
    Long persist(ProductGroup productGroup);
}
