package com.ryuqq.setof.domain.option.aggregate;

import com.ryuqq.setof.domain.option.id.OptionGroupId;
import com.ryuqq.setof.domain.option.vo.OptionGroupStatus;
import com.ryuqq.setof.domain.option.vo.OptionName;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/** 옵션 그룹 도메인 객체. 예: 색상, 사이즈 등 */
public class OptionGroup {

    private final OptionGroupId id;
    private final OptionName name;
    private final OptionGroupStatus status;
    private final List<OptionDetail> details;
    private final Instant createdAt;
    private final Instant updatedAt;

    private OptionGroup(
            OptionGroupId id,
            OptionName name,
            OptionGroupStatus status,
            List<OptionDetail> details,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "옵션 그룹 이름은 필수입니다");
        this.status = Objects.requireNonNull(status, "옵션 그룹 상태는 필수입니다");
        this.details = List.copyOf(details);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** 신규 옵션 그룹 생성. */
    public static OptionGroup forNew(OptionName name, Instant now) {
        return new OptionGroup(
                OptionGroupId.forNew(), name, OptionGroupStatus.ACTIVE, List.of(), now, now);
    }

    /** 영속성에서 복원. */
    public static OptionGroup reconstitute(
            OptionGroupId id,
            OptionName name,
            OptionGroupStatus status,
            List<OptionDetail> details,
            Instant createdAt,
            Instant updatedAt) {
        return new OptionGroup(id, name, status, details, createdAt, updatedAt);
    }

    public OptionGroupId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public OptionName name() {
        return name;
    }

    public OptionGroupStatus status() {
        return status;
    }

    public List<OptionDetail> details() {
        return details;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
