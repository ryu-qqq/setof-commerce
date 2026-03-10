package com.ryuqq.setof.application.productgroup.manager;

import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupQueryPort;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupNotFoundException;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품그룹 Read Manager.
 *
 * <p>ProductGroupQueryPort를 감싸며 조회 전용 트랜잭션 경계를 관리합니다.
 */
@Component
public class ProductGroupReadManager {

    private final ProductGroupQueryPort queryPort;

    public ProductGroupReadManager(ProductGroupQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    /**
     * ID로 상품그룹을 조회합니다. 존재하지 않으면 예외를 던집니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품그룹 도메인 객체
     * @throws ProductGroupNotFoundException 상품그룹을 찾을 수 없을 때
     */
    @Transactional(readOnly = true)
    public ProductGroup getById(ProductGroupId productGroupId) {
        return queryPort
                .findById(productGroupId)
                .orElseThrow(() -> new ProductGroupNotFoundException(productGroupId.value()));
    }

    /**
     * ID 목록으로 상품그룹 목록을 조회합니다.
     *
     * @param ids 상품그룹 ID 목록
     * @return 상품그룹 목록
     */
    @Transactional(readOnly = true)
    public List<ProductGroup> findByIds(List<ProductGroupId> ids) {
        return queryPort.findByIds(ids);
    }
}
