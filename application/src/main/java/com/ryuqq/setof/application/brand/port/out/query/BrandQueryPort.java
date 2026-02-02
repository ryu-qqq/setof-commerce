package com.ryuqq.setof.application.brand.port.out.query;

import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import java.util.List;
import java.util.Optional;

/**
 * Brand Query Port.
 *
 * <p>브랜드 조회 관련 Port-Out 인터페이스입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface BrandQueryPort {

    Optional<Brand> findById(BrandId id);

    List<Brand> findByIds(List<BrandId> ids);

    boolean existsById(BrandId id);

    List<Brand> findByCriteria(BrandSearchCriteria criteria);

    long countByCriteria(BrandSearchCriteria criteria);

    List<Brand> findAllDisplayed();
}
