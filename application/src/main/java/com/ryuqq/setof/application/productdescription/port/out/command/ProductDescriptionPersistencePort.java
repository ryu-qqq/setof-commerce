package com.ryuqq.setof.application.productdescription.port.out.command;

import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;

/**
 * 상품설명 영속성 Port Out
 *
 * <p>상품설명 저장/수정/삭제를 위한 Port입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductDescriptionPersistencePort {

    /**
     * 상품설명 저장
     *
     * @param productDescription 저장할 상품설명
     * @return 저장된 상품설명 ID
     */
    Long persist(ProductDescription productDescription);

    /**
     * 상품설명 수정
     *
     * @param productDescription 수정할 상품설명
     */
    void update(ProductDescription productDescription);
}
