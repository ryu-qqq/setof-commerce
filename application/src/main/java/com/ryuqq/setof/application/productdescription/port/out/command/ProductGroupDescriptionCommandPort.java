package com.ryuqq.setof.application.productdescription.port.out.command;

import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;

/** 상품그룹 상세설명 Command Port. */
public interface ProductGroupDescriptionCommandPort {

    /**
     * 상세설명을 저장합니다.
     *
     * @param productGroupDescription 저장할 상세설명 도메인 객체
     * @return 저장된 상세설명 ID
     */
    Long persist(ProductGroupDescription productGroupDescription);
}
