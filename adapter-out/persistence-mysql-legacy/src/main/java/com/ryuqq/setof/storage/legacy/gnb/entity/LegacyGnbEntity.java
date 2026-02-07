package com.ryuqq.setof.storage.legacy.gnb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyGnbEntity - 레거시 GNB 엔티티.
 *
 * <p>레거시 DB의 gnb 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "gnb")
public class LegacyGnbEntity {

    @Id
    @Column(name = "gnb_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "link_url")
    private String linkUrl;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "display_yn")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    @Column(name = "display_start_date")
    private LocalDateTime displayStartDate;

    @Column(name = "display_end_date")
    private LocalDateTime displayEndDate;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyGnbEntity() {}

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public Yn getDisplayYn() {
        return displayYn;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    public LocalDateTime getDisplayStartDate() {
        return displayStartDate;
    }

    public LocalDateTime getDisplayEndDate() {
        return displayEndDate;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /** Yn - Y/N 구분 Enum. */
    public enum Yn {
        Y,
        N
    }
}
