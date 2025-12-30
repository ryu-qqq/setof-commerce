package com.ryuqq.setof.adapter.out.persistence.banneritem.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * CmsBannerItemJpaEntity - BannerItem JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 cms_banner_items 테이블과 매핑됩니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "cms_banner_items")
public class CmsBannerItemJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** Banner ID (Long FK) */
    @Column(name = "banner_id", nullable = false)
    private Long bannerId;

    /** 제목 */
    @Column(name = "title", length = 200)
    private String title;

    /** 이미지 URL */
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    /** 링크 URL */
    @Column(name = "link_url", length = 500)
    private String linkUrl;

    /** 노출 순서 */
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    /** 상태 (ACTIVE, INACTIVE, DELETED) */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** 노출 시작일 */
    @Column(name = "display_start_date")
    private Instant displayStartDate;

    /** 노출 종료일 */
    @Column(name = "display_end_date")
    private Instant displayEndDate;

    /** 이미지 너비 */
    @Column(name = "image_width")
    private Integer imageWidth;

    /** 이미지 높이 */
    @Column(name = "image_height")
    private Integer imageHeight;

    /** 이미지 대체 텍스트 */
    @Column(name = "alt_text", length = 200)
    private String altText;

    /** JPA 기본 생성자 (protected) */
    protected CmsBannerItemJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private CmsBannerItemJpaEntity(
            Long id,
            Long bannerId,
            String title,
            String imageUrl,
            String linkUrl,
            Integer displayOrder,
            String status,
            Instant displayStartDate,
            Instant displayEndDate,
            Integer imageWidth,
            Integer imageHeight,
            String altText,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.bannerId = bannerId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.displayOrder = displayOrder;
        this.status = status;
        this.displayStartDate = displayStartDate;
        this.displayEndDate = displayEndDate;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.altText = altText;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static CmsBannerItemJpaEntity of(
            Long id,
            Long bannerId,
            String title,
            String imageUrl,
            String linkUrl,
            Integer displayOrder,
            String status,
            Instant displayStartDate,
            Instant displayEndDate,
            Integer imageWidth,
            Integer imageHeight,
            String altText,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new CmsBannerItemJpaEntity(
                id,
                bannerId,
                title,
                imageUrl,
                linkUrl,
                displayOrder,
                status,
                displayStartDate,
                displayEndDate,
                imageWidth,
                imageHeight,
                altText,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getBannerId() {
        return bannerId;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public Integer getDisplayOrder() {
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

    public Integer getImageWidth() {
        return imageWidth;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public String getAltText() {
        return altText;
    }
}
