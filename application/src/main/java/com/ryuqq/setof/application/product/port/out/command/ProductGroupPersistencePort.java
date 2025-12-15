package com.ryuqq.setof.application.product.port.out.command;

import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;

/**
 * ProductGroup Persistence Port (Command)
 *
 * <p>ProductGroup Aggregate를 영속화하는 쓰기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductGroupPersistencePort {

    /**
     * ProductGroup 저장 (신규 생성 또는 수정)
     *
     * @param productGroup 저장할 ProductGroup (Domain Aggregate)
     * @return 저장된 ProductGroup의 ID
     */
    ProductGroupId persist(ProductGroup productGroup);
}
