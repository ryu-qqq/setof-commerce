package com.ryuqq.setof.application.productnotice.internal;

import com.ryuqq.setof.application.productnotice.manager.ProductNoticeCommandManager;
import com.ryuqq.setof.application.productnotice.manager.ProductNoticeEntryCommandManager;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNoticeEntry;
import com.ryuqq.setof.domain.productnotice.id.ProductNoticeId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductNoticeCommandFacade - 상품고시 + 항목 영속화 Facade.
 *
 * <p>상품고시와 항목의 영속화를 조율합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductNoticeCommandFacade {

    private final ProductNoticeCommandManager noticeCommandManager;
    private final ProductNoticeEntryCommandManager entryCommandManager;

    public ProductNoticeCommandFacade(
            ProductNoticeCommandManager noticeCommandManager,
            ProductNoticeEntryCommandManager entryCommandManager) {
        this.noticeCommandManager = noticeCommandManager;
        this.entryCommandManager = entryCommandManager;
    }

    /**
     * 상품고시를 저장하고 항목을 일괄 저장합니다.
     *
     * @param notice 상품고시 도메인 객체
     * @return 저장된 상품고시 ID
     */
    public Long persistNoticeWithEntries(ProductNotice notice) {
        Long noticeId = noticeCommandManager.persist(notice);
        notice.assignId(ProductNoticeId.of(noticeId));
        List<ProductNoticeEntry> entries = notice.entries();
        if (!entries.isEmpty()) {
            entryCommandManager.persistAll(entries);
        }
        return noticeId;
    }

    /**
     * 기존 항목을 삭제하고 상품고시를 저장한 뒤 새 항목을 일괄 저장합니다.
     *
     * @param notice 상품고시 도메인 객체
     */
    public void updateNoticeWithEntries(ProductNotice notice) {
        Long noticeId = notice.idValue();
        entryCommandManager.deleteByProductNoticeId(noticeId);
        noticeCommandManager.persist(notice);
        notice.assignId(ProductNoticeId.of(noticeId));
        List<ProductNoticeEntry> entries = notice.entries();
        if (!entries.isEmpty()) {
            entryCommandManager.persistAll(entries);
        }
    }
}
