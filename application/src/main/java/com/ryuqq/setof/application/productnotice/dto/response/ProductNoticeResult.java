package com.ryuqq.setof.application.productnotice.dto.response;

import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import java.time.Instant;
import java.util.List;

/** 상품 고시정보 조회 결과 DTO. */
public record ProductNoticeResult(
        Long id, List<ProductNoticeEntryResult> entries, Instant createdAt, Instant updatedAt) {

    public static ProductNoticeResult from(ProductNotice notice) {
        List<ProductNoticeEntryResult> entryResults =
                notice.entries().stream().map(ProductNoticeEntryResult::from).toList();

        return new ProductNoticeResult(
                notice.idValue(), entryResults, notice.createdAt(), notice.updatedAt());
    }
}
