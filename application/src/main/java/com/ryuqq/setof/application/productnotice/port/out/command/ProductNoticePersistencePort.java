package com.ryuqq.setof.application.productnotice.port.out.command;

import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;

/**
 * 상품고시 영속성 Port Out
 *
 * <p>상품고시 저장/수정/삭제를 위한 Port입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductNoticePersistencePort {

    /**
     * 상품고시 저장
     *
     * @param productNotice 저장할 상품고시
     * @return 저장된 상품고시 ID
     */
    Long persist(ProductNotice productNotice);

    /**
     * 상품고시 수정
     *
     * @param productNotice 수정할 상품고시
     */
    void update(ProductNotice productNotice);
}
