package com.ryuqq.setof.domain.productnotice.id;

/** 상품고시 ID Value Object. */
public record ProductNoticeId(Long value) {

    public static ProductNoticeId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("ProductNoticeId 값은 null일 수 없습니다");
        }
        return new ProductNoticeId(value);
    }

    public static ProductNoticeId forNew() {
        return new ProductNoticeId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
