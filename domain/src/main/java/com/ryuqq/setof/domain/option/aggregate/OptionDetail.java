package com.ryuqq.setof.domain.option.aggregate;

import com.ryuqq.setof.domain.option.id.OptionDetailId;
import java.time.Instant;
import java.util.Objects;

/** 옵션 상세(옵션값) 도메인 객체. 예: 블랙, M, 화이트 등 */
public class OptionDetail {

    private final OptionDetailId id;
    private final String value;
    private final int sortOrder;
    private final Instant createdAt;
    private final Instant updatedAt;

    private OptionDetail(
            OptionDetailId id, String value, int sortOrder, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.value = Objects.requireNonNull(value, "옵션값은 필수입니다");
        this.sortOrder = sortOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** 신규 옵션 상세 생성. */
    public static OptionDetail forNew(String value, int sortOrder, Instant now) {
        return new OptionDetail(OptionDetailId.forNew(), value, sortOrder, now, now);
    }

    /** 영속성에서 복원. */
    public static OptionDetail reconstitute(
            OptionDetailId id, String value, int sortOrder, Instant createdAt, Instant updatedAt) {
        return new OptionDetail(id, value, sortOrder, createdAt, updatedAt);
    }

    public OptionDetailId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public String value() {
        return value;
    }

    public int sortOrder() {
        return sortOrder;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
