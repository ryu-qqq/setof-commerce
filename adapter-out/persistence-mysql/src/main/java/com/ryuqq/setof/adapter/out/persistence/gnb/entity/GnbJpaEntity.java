package com.ryuqq.setof.adapter.out.persistence.gnb.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * GnbJpaEntity - GNB JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 cms_gnbs 테이블과 매핑됩니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "cms_gnbs")
public class GnbJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 제목 */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /** 링크 URL */
    @Column(name = "link_url", length = 500)
    private String linkUrl;

    /** 노출 순서 */
    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    /** 상태 (ACTIVE, INACTIVE, DELETED) */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** 노출 시작일 */
    @Column(name = "display_start_date")
    private Instant displayStartDate;

    /** 노출 종료일 */
    @Column(name = "display_end_date")
    private Instant displayEndDate;

    /** JPA 기본 생성자 (protected) */
    protected GnbJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private GnbJpaEntity(
            Long id,
            String title,
            String linkUrl,
            int displayOrder,
            String status,
            Instant displayStartDate,
            Instant displayEndDate,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.title = title;
        this.linkUrl = linkUrl;
        this.displayOrder = displayOrder;
        this.status = status;
        this.displayStartDate = displayStartDate;
        this.displayEndDate = displayEndDate;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static GnbJpaEntity of(
            Long id,
            String title,
            String linkUrl,
            int displayOrder,
            String status,
            Instant displayStartDate,
            Instant displayEndDate,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new GnbJpaEntity(
                id,
                title,
                linkUrl,
                displayOrder,
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

    public String getLinkUrl() {
        return linkUrl;
    }

    public int getDisplayOrder() {
        return displayOrder;
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
