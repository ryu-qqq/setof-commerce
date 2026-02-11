package com.ryuqq.setof.storage.legacy.banner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyBannerEntity - 레거시 배너 엔티티.
 *
 * <p>레거시 DB의 banner 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "banner")
public class LegacyBannerEntity {

    @Id
    @Column(name = "banner_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "banner_type")
    @Enumerated(EnumType.STRING)
    private BannerType bannerType;

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

    protected LegacyBannerEntity() {}

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public BannerType getBannerType() {
        return bannerType;
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

    /** BannerType - 배너 타입 Enum. */
    public enum BannerType {
        CATEGORY,
        MY_PAGE,
        CART,
        PRODUCT_DETAIL_DESCRIPTION,
        RECOMMEND,
        LOGIN
    }

    /** Yn - Y/N 구분 Enum. */
    public enum Yn {
        Y,
        N
    }
}
