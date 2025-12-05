package com.ryuqq.setof.storage.mysql.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * SoftDeletableEntity - 소프트 딜리트 지원 추상 클래스
 *
 * <p>BaseAuditEntity를 상속받아 삭제 일시 필드를 추가합니다.
 *
 * <p><strong>제공 필드:</strong>
 *
 * <ul>
 *   <li>createdAt - 생성 일시 (BaseAuditEntity 상속)
 *   <li>updatedAt - 수정 일시 (BaseAuditEntity 상속)
 *   <li>deletedAt - 삭제 일시 (null이면 활성)
 * </ul>
 *
 * <p><strong>제공 메서드:</strong>
 *
 * <ul>
 *   <li>isDeleted() - 삭제 여부 확인 (deletedAt != null)
 *   <li>isActive() - 활성 여부 확인 (deletedAt == null)
 * </ul>
 *
 * <p><strong>사용 방법:</strong>
 *
 * <ul>
 *   <li>소프트 딜리트가 필요한 Entity는 이 클래스 상속
 *   <li>회원, 주문 등 논리 삭제가 필요한 도메인
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
public abstract class SoftDeletableEntity extends BaseAuditEntity {

    /**
     * 삭제 일시
     *
     * <p>null이면 활성 상태, 값이 있으면 삭제된 상태입니다.
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected SoftDeletableEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (protected)
     *
     * <p>하위 클래스에서만 호출 가능합니다.
     *
     * @param createdAt 생성 일시
     * @param updatedAt 수정 일시
     * @param deletedAt 삭제 일시 (nullable)
     */
    protected SoftDeletableEntity(
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(createdAt, updatedAt);
        this.deletedAt = deletedAt;
    }

    /**
     * 삭제 일시 조회
     *
     * @return 삭제 일시 (null이면 활성 상태)
     */
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    /**
     * 삭제 여부 확인
     *
     * @return 삭제 여부 (deletedAt != null이면 true)
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * 활성 여부 확인
     *
     * @return 활성 여부 (deletedAt == null이면 true)
     */
    public boolean isActive() {
        return deletedAt == null;
    }
}
