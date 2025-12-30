package com.ryuqq.setof.adapter.out.persistence.componentitem.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * CmsComponentItemJpaEntity - ComponentItem JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 cms_component_items 테이블과 매핑됩니다.
 *
 * <p><strong>용도</strong>:
 *
 * <ul>
 *   <li>PRODUCT 타입 컴포넌트의 개별 상품 목록
 *   <li>BRAND 타입 컴포넌트의 개별 브랜드 목록
 *   <li>CATEGORY 타입 컴포넌트의 개별 카테고리 참조
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "cms_component_items")
public class CmsComponentItemJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** Component ID (Long FK) */
    @Column(name = "component_id", nullable = false)
    private Long componentId;

    /** 아이템 타입 (PRODUCT, BRAND, CATEGORY, IMAGE, TAB) */
    @Column(name = "item_type", nullable = false, length = 30)
    private String itemType;

    /** 참조 ID (productGroupId, brandId, categoryId 등) */
    @Column(name = "reference_id")
    private Long referenceId;

    /** 표시 제목 (오버라이드용) */
    @Column(name = "title", length = 200)
    private String title;

    /** 표시 이미지 URL (오버라이드용) */
    @Column(name = "image_url", length = 500)
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

    /** 정렬 타입 (LATEST, POPULAR, SALES 등) */
    @Column(name = "sort_type", length = 30)
    private String sortType;

    /** 추가 데이터 (JSON) */
    @Column(name = "extra_data", columnDefinition = "TEXT")
    private String extraData;

    /** JPA 기본 생성자 (protected) */
    protected CmsComponentItemJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private CmsComponentItemJpaEntity(
            Long id,
            Long componentId,
            String itemType,
            Long referenceId,
            String title,
            String imageUrl,
            String linkUrl,
            Integer displayOrder,
            String status,
            String sortType,
            String extraData,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.componentId = componentId;
        this.itemType = itemType;
        this.referenceId = referenceId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.displayOrder = displayOrder;
        this.status = status;
        this.sortType = sortType;
        this.extraData = extraData;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static CmsComponentItemJpaEntity of(
            Long id,
            Long componentId,
            String itemType,
            Long referenceId,
            String title,
            String imageUrl,
            String linkUrl,
            Integer displayOrder,
            String status,
            String sortType,
            String extraData,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new CmsComponentItemJpaEntity(
                id,
                componentId,
                itemType,
                referenceId,
                title,
                imageUrl,
                linkUrl,
                displayOrder,
                status,
                sortType,
                extraData,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getComponentId() {
        return componentId;
    }

    public String getItemType() {
        return itemType;
    }

    public Long getReferenceId() {
        return referenceId;
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

    public String getSortType() {
        return sortType;
    }

    public String getExtraData() {
        return extraData;
    }
}
