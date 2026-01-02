package com.ryuqq.setof.adapter.out.persistence.banner.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * BannerJpaEntity - Banner JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 cms_banners 테이블과 매핑됩니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "cms_banners")
public class BannerJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 제목 */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /** 배너 타입 */
    @Column(name = "banner_type", nullable = false, length = 30)
    private String bannerType;

    /** 상태 (ACTIVE, INACTIVE, DELETED) */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** 노출 시작일 */
    @Column(name = "display_start_date", nullable = false)
    private Instant displayStartDate;

    /** 노출 종료일 */
    @Column(name = "display_end_date", nullable = false)
    private Instant displayEndDate;

    /** JPA 기본 생성자 (protected) */
    protected BannerJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private BannerJpaEntity(
            Long id,
            String title,
            String bannerType,
            String status,
            Instant displayStartDate,
            Instant displayEndDate,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.title = title;
        this.bannerType = bannerType;
        this.status = status;
        this.displayStartDate = displayStartDate;
        this.displayEndDate = displayEndDate;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static BannerJpaEntity of(
            Long id,
            String title,
            String bannerType,
            String status,
            Instant displayStartDate,
            Instant displayEndDate,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new BannerJpaEntity(
                id,
                title,
                bannerType,
                status,
                displayStartDate,
                displayEndDate,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBannerType() {
        return bannerType;
    }

    public String getStatus() {
        return status;
    }

    public Instant getDisplayStartDate() {
        return displayStartDate;
    }

    public Instant getDisplayEndDate() {
        return displayEndDate;
    }
}
