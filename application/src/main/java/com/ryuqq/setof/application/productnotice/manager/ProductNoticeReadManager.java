package com.ryuqq.setof.application.productnotice.manager;

import com.ryuqq.setof.application.productnotice.port.out.query.ProductNoticeQueryPort;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.exception.ProductNoticeNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductNoticeReadManager - 상품고시 조회 Manager.
 *
 * <p>QueryPort에 위임하고 도메인 예외를 던집니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@Transactional(readOnly = true)
public class ProductNoticeReadManager {

    private final ProductNoticeQueryPort queryPort;

    public ProductNoticeReadManager(ProductNoticeQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    /**
     * 상품그룹 ID로 상품고시를 조회합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품고시 도메인 객체
     * @throws ProductNoticeNotFoundException 상품고시를 찾을 수 없는 경우
     */
    public ProductNotice getByProductGroupId(ProductGroupId productGroupId) {
        return queryPort
                .findByProductGroupId(productGroupId)
                .orElseThrow(() -> new ProductNoticeNotFoundException(productGroupId));
    }
}
