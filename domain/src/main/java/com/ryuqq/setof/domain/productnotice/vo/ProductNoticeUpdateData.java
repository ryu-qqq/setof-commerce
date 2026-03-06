package com.ryuqq.setof.domain.productnotice.vo;

import com.ryuqq.setof.domain.productnotice.aggregate.ProductNoticeEntry;
import java.time.Instant;
import java.util.List;

/**
 * 고시정보 수정 데이터.
 *
 * <p>새 항목 목록과 수정 시각을 불변으로 보관합니다.
 */
public class ProductNoticeUpdateData {

    private final List<ProductNoticeEntry> entries;
    private final Instant updatedAt;

    private ProductNoticeUpdateData(List<ProductNoticeEntry> entries, Instant updatedAt) {
        this.entries = entries;
        this.updatedAt = updatedAt;
    }

    public static ProductNoticeUpdateData of(List<ProductNoticeEntry> entries, Instant updatedAt) {
        return new ProductNoticeUpdateData(List.copyOf(entries), updatedAt);
    }

    public List<ProductNoticeEntry> entries() {
        return entries;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
