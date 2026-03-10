package com.ryuqq.setof.application.productnotice.manager;

import com.ryuqq.setof.application.productnotice.port.out.command.ProductNoticeEntryCommandPort;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNoticeEntry;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductNoticeEntryCommandManager - 상품고시 항목 명령 Manager.
 *
 * <p>ProductNoticeEntryCommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@Transactional
public class ProductNoticeEntryCommandManager {

    private final ProductNoticeEntryCommandPort commandPort;

    public ProductNoticeEntryCommandManager(ProductNoticeEntryCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public void persistAll(List<ProductNoticeEntry> entries) {
        commandPort.persistAll(entries);
    }

    public void deleteByProductNoticeId(Long productNoticeId) {
        commandPort.deleteByProductNoticeId(productNoticeId);
    }
}
