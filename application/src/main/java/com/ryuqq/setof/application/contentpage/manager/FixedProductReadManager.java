package com.ryuqq.setof.application.contentpage.manager;

import com.ryuqq.setof.application.contentpage.port.out.ComponentFixedProductQueryPort;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * FixedProductReadManager - FIXED 상품 조회 매니저.
 *
 * <p>ComponentFixedProductQueryPort를 래핑하여 FIXED 상품(component_item 기반 고정 배치 상품)을 조회한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class FixedProductReadManager {

    private final ComponentFixedProductQueryPort queryPort;

    public FixedProductReadManager(ComponentFixedProductQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public Map<Long, List<ProductThumbnailSnapshot>> fetchFixedProducts(List<Long> componentIds) {
        return queryPort.fetchFixedProducts(componentIds);
    }

    @Transactional(readOnly = true)
    public Map<Long, List<ProductThumbnailSnapshot>> fetchFixedProductsByTab(
            List<Long> componentIds) {
        return queryPort.fetchFixedProductsByTab(componentIds);
    }
}
