package com.ryuqq.setof.adapter.out.persistence.content.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ContentJpaEntity - Content JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 cms_contents 테이블과 매핑됩니다.
 *
 * <p><strong>SoftDeletableEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt, deletedAt
 *   <li>Soft Delete 지원
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "cms_contents")
public class ContentJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 제목 */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /** 메모 */
    @Column(name = "memo", length = 500)
    private String memo;

    /** 이미지 URL */
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /** 상태 (DRAFT, ACTIVE, INACTIVE, DELETED) */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** 노출 시작일 */
    @Column(name = "display_start_date", nullable = false)
    private Instant displayStartDate;

    /** 노출 종료일 */
    @Column(name = "display_end_date", nullable = false)
    private Instant displayEndDate;

    /** JPA 기본 생성자 (protected) */
    protected ContentJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private ContentJpaEntity(
            Long id,
            String title,
            String memo,
            String imageUrl,
            String status,
            Instant displayStartDate,
            Instant displayEndDate,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.title = title;
        this.memo = memo;
        this.imageUrl = imageUrl;
        this.status = status;
        this.displayStartDate = displayStartDate;
        this.displayEndDate = displayEndDate;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static ContentJpaEntity of(
            Long id,
            String title,
            String memo,
            String imageUrl,
            String status,
            Instant displayStartDate,
            Instant displayEndDate,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ContentJpaEntity(
                id,
                title,
                memo,
                imageUrl,
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

    public String getMemo() {
        return memo;
    }

    public String getImageUrl() {
        return imageUrl;
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
