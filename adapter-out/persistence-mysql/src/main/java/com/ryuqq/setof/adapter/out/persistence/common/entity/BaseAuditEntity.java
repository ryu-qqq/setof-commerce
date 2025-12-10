package com.ryuqq.setof.adapter.out.persistence.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;

/**
 * BaseAuditEntity - 공통 감사 필드 추상 클래스
 *
 * <p>모든 Entity가 상속받아 생성/수정 일시를 관리합니다.
 *
 * <p><strong>제공 필드:</strong>
 *
 * <ul>
 *   <li>createdAt - 생성 일시 (UTC 기준 Instant)
 *   <li>updatedAt - 수정 일시 (UTC 기준 Instant)
 * </ul>
 *
 * <p><strong>Instant 사용 이유:</strong>
 *
 * <ul>
 *   <li>Domain Layer와 시간 타입 일관성 유지
 *   <li>타임존 독립적 - UTC 기준 저장으로 글로벌 서비스 대응
 *   <li>Mapper에서 불필요한 시간 변환 로직 제거
 * </ul>
 *
 * <p><strong>사용 방법:</strong>
 *
 * <ul>
 *   <li>시간 정보만 필요한 Entity는 이 클래스 상속
 *   <li>소프트 딜리트 필요 시 SoftDeletableEntity 상속
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
@MappedSuperclass
public abstract class BaseAuditEntity {

    /** 생성 일시 (UTC 기준) */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /** 수정 일시 (UTC 기준) */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected BaseAuditEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (protected)
     *
     * <p>하위 클래스에서만 호출 가능합니다.
     *
     * @param createdAt 생성 일시 (UTC 기준 Instant)
     * @param updatedAt 수정 일시 (UTC 기준 Instant)
     */
    protected BaseAuditEntity(Instant createdAt, Instant updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 생성 일시 조회
     *
     * @return 생성 일시 (UTC 기준 Instant)
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * 수정 일시 조회
     *
     * @return 수정 일시 (UTC 기준 Instant)
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
