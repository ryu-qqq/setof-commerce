package com.ryuqq.setof.adapter.out.persistence.carrier.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * CarrierJpaEntity - Carrier JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 carrier 테이블과 매핑됩니다.
 *
 * <p><strong>BaseAuditEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt
 *   <li>Soft Delete 미적용 (마스터 데이터)
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 *   <li>명시적 생성자 제공
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "carrier")
public class CarrierJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 택배사 코드 (스마트택배 API 코드) */
    @Column(name = "code", nullable = false, length = 50, unique = true)
    private String code;

    /** 택배사명 */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /** 상태 (ACTIVE, INACTIVE) */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** 배송 조회 URL 템플릿 (선택) */
    @Column(name = "tracking_url_template", length = 500)
    private String trackingUrlTemplate;

    /** 표시 순서 (선택) */
    @Column(name = "display_order")
    private Integer displayOrder;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected CarrierJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private CarrierJpaEntity(
            Long id,
            String code,
            String name,
            String status,
            String trackingUrlTemplate,
            Integer displayOrder,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.code = code;
        this.name = name;
        this.status = status;
        this.trackingUrlTemplate = trackingUrlTemplate;
        this.displayOrder = displayOrder;
    }

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * @param id Carrier ID (null이면 신규 생성)
     * @param code 택배사 코드
     * @param name 택배사명
     * @param status 상태
     * @param trackingUrlTemplate 배송 조회 URL 템플릿
     * @param displayOrder 표시 순서
     * @param createdAt 생성 일시 (UTC 기준 Instant)
     * @param updatedAt 수정 일시 (UTC 기준 Instant)
     * @return CarrierJpaEntity 인스턴스
     */
    public static CarrierJpaEntity of(
            Long id,
            String code,
            String name,
            String status,
            String trackingUrlTemplate,
            Integer displayOrder,
            Instant createdAt,
            Instant updatedAt) {
        return new CarrierJpaEntity(
                id, code, name, status, trackingUrlTemplate, displayOrder, createdAt, updatedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getTrackingUrlTemplate() {
        return trackingUrlTemplate;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }
}
