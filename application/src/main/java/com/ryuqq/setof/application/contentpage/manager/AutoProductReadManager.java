package com.ryuqq.setof.application.contentpage.manager;

import com.ryuqq.setof.application.contentpage.port.out.ComponentAutoProductQueryPort;
import com.ryuqq.setof.domain.contentpage.vo.AutoProductCriteria;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * AutoProductReadManager - AUTO 상품 조회 매니저.
 *
 * <p>ComponentAutoProductQueryPort를 래핑하여 AUTO 상품(category/brand 기반 동적 상품)을 조회한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class AutoProductReadManager {

    private final ComponentAutoProductQueryPort queryPort;

    public AutoProductReadManager(ComponentAutoProductQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public List<ProductThumbnailSnapshot> fetchAutoProducts(AutoProductCriteria criteria) {
        return queryPort.fetchAutoProducts(criteria);
    }
}
