package com.ryuqq.setof.adapter.out.persistence.component.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ComponentJpaEntity - Component JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 cms_components 테이블과 매핑됩니다.
 *
 * <p><strong>ComponentDetail 저장:</strong>
 *
 * <ul>
 *   <li>componentDetail 필드에 JSON 문자열로 저장
 *   <li>componentType 필드로 역직렬화 시 타입 결정
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "cms_components")
public class ComponentJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** Content ID (Long FK) */
    @Column(name = "content_id", nullable = false)
    private Long contentId;

    /** 컴포넌트 타입 */
    @Column(name = "component_type", nullable = false, length = 30)
    private String componentType;

    /** 컴포넌트 이름 */
    @Column(name = "component_name", length = 100)
    private String componentName;

    /** 노출 순서 */
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    /** 상태 (ACTIVE, INACTIVE, DELETED) */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** 노출 상품 수 */
    @Column(name = "exposed_products", nullable = false)
    private Integer exposedProducts;

    /** 노출 시작일 (nullable) */
    @Column(name = "display_start_date")
    private Instant displayStartDate;

    /** 노출 종료일 (nullable) */
    @Column(name = "display_end_date")
    private Instant displayEndDate;

    /** 컴포넌트 상세 정보 (JSON) */
    @Column(name = "component_detail", columnDefinition = "TEXT")
    private String componentDetail;

    /** JPA 기본 생성자 (protected) */
    protected ComponentJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private ComponentJpaEntity(
            Long id,
            Long contentId,
            String componentType,
            String componentName,
            Integer displayOrder,
            String status,
            Integer exposedProducts,
            Instant displayStartDate,
            Instant displayEndDate,
            String componentDetail,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.contentId = contentId;
        this.componentType = componentType;
        this.componentName = componentName;
        this.displayOrder = displayOrder;
        this.status = status;
        this.exposedProducts = exposedProducts;
        this.displayStartDate = displayStartDate;
        this.displayEndDate = displayEndDate;
        this.componentDetail = componentDetail;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static ComponentJpaEntity of(
            Long id,
            Long contentId,
            String componentType,
            String componentName,
            Integer displayOrder,
            String status,
            Integer exposedProducts,
            Instant displayStartDate,
            Instant displayEndDate,
            String componentDetail,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ComponentJpaEntity(
                id,
                contentId,
                componentType,
                componentName,
                displayOrder,
                status,
                exposedProducts,
                displayStartDate,
                displayEndDate,
                componentDetail,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getContentId() {
        return contentId;
    }

    public String getComponentType() {
        return componentType;
    }

    public String getComponentName() {
        return componentName;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public String getStatus() {
        return status;
    }

    public Integer getExposedProducts() {
        return exposedProducts;
    }

    public Instant getDisplayStartDate() {
        return displayStartDate;
    }

    public Instant getDisplayEndDate() {
        return displayEndDate;
    }

    public String getComponentDetail() {
        return componentDetail;
    }
}
