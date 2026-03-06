package com.ryuqq.setof.storage.legacy.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * LegacyBaseEntity - 레거시 감사 정보 공통 추상 클래스.
 *
 * <p>레거시 DB의 공통 감사 필드(insert_date, update_date)를 제공합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@MappedSuperclass
public abstract class LegacyBaseEntity {

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyBaseEntity() {}

    protected LegacyBaseEntity(LocalDateTime insertDate, LocalDateTime updateDate) {
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}
