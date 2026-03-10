package com.ryuqq.setof.application.productnotice.port.out.command;

import com.ryuqq.setof.domain.productnotice.aggregate.ProductNoticeEntry;
import java.util.List;

public interface ProductNoticeEntryCommandPort {
    void persistAll(List<ProductNoticeEntry> entries);

    void deleteByProductNoticeId(Long productNoticeId);
}
