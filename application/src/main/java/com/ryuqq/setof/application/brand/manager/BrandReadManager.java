package com.ryuqq.setof.application.brand.manager;

import com.ryuqq.setof.application.brand.port.out.query.BrandQueryPort;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.exception.BrandNotFoundException;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Brand Read Manager.
 *
 * <p>브랜드 조회 관련 Manager입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class BrandReadManager {

    private final BrandQueryPort queryPort;

    public BrandReadManager(BrandQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public Brand getById(BrandId id) {
        return queryPort.findById(id).orElseThrow(() -> new BrandNotFoundException(id.value()));
    }

    @Transactional(readOnly = true)
    public List<Brand> getByIds(List<BrandId> ids) {
        return queryPort.findByIds(ids);
    }

    @Transactional(readOnly = true)
    public boolean existsById(BrandId id) {
        return queryPort.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<Brand> findByCriteria(BrandSearchCriteria criteria) {
        return queryPort.findByCriteria(criteria);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(BrandSearchCriteria criteria) {
        return queryPort.countByCriteria(criteria);
    }

    @Transactional(readOnly = true)
    public List<Brand> findAllDisplayed() {
        return queryPort.findAllDisplayed();
    }
}
