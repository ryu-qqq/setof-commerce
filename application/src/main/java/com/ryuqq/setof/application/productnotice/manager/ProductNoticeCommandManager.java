package com.ryuqq.setof.application.productnotice.manager;

import com.ryuqq.setof.application.productnotice.port.out.command.ProductNoticeCommandPort;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductNoticeCommandManager - 상품고시 명령 Manager.
 *
 * <p>CommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@Transactional
public class ProductNoticeCommandManager {

    private final ProductNoticeCommandPort commandPort;

    public ProductNoticeCommandManager(ProductNoticeCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public Long persist(ProductNotice notice) {
        return commandPort.persist(notice);
    }
}
