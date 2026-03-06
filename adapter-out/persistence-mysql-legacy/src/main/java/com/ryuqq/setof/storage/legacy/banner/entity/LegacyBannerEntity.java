package com.ryuqq.setof.storage.legacy.banner.entity;

import com.ryuqq.setof.storage.legacy.common.Yn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    /**
     * 신규 배너 엔티티 생성.
     *
     * @return LegacyBannerEntity
     */
    public static LegacyBannerEntity create(
            String title,
            BannerType bannerType,
            Yn displayYn,
            LocalDateTime displayStartDate,
            LocalDateTime displayEndDate,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        LegacyBannerEntity entity = new LegacyBannerEntity();
        entity.title = title;
        entity.bannerType = bannerType;
        entity.displayYn = displayYn;
        entity.deleteYn = Yn.N;
        entity.displayStartDate = displayStartDate;
        entity.displayEndDate = displayEndDate;
        entity.insertDate = insertDate;
        entity.updateDate = updateDate;
        return entity;
    }

    /** 배너 정보 수정. */
    public void updateDetails(
            String title,
            BannerType bannerType,
            Yn displayYn,
            LocalDateTime displayStartDate,
            LocalDateTime displayEndDate,
            LocalDateTime updateDate) {
        this.title = title;
        this.bannerType = bannerType;
        this.displayYn = displayYn;
        this.displayStartDate = displayStartDate;
        this.displayEndDate = displayEndDate;
        this.updateDate = updateDate;
    }

    /**
     * 배너 삭제 마킹.
     *
     * @param updateDate 삭제 시각
     */
    public void markDeleted(LocalDateTime updateDate) {
        this.displayYn = Yn.N;
        this.deleteYn = Yn.Y;
        this.updateDate = updateDate;
    }

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
}
