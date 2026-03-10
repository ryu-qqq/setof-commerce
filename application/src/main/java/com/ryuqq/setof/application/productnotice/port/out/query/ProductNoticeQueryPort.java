package com.ryuqq.setof.application.productnotice.port.out.query;

import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import java.util.Optional;

public interface ProductNoticeQueryPort {
    Optional<ProductNotice> findByProductGroupId(ProductGroupId productGroupId);
}
