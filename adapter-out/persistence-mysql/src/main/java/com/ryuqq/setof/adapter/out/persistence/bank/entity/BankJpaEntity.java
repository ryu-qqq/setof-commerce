package com.ryuqq.setof.adapter.out.persistence.bank.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * BankJpaEntity - Bank JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 banks 테이블과 매핑됩니다.
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
@Table(name = "banks")
public class BankJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 은행 코드 (금융결제원 표준 코드) */
    @Column(name = "bank_code", nullable = false, length = 10, unique = true)
    private String bankCode;

    /** 은행명 */
    @Column(name = "bank_name", nullable = false, length = 50)
    private String bankName;

    /** 활성 상태 */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /** 노출 순서 (낮을수록 상위) */
    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected BankJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private BankJpaEntity(
            Long id,
            String bankCode,
            String bankName,
            boolean isActive,
            int displayOrder,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.isActive = isActive;
        this.displayOrder = displayOrder;
    }

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * @param id Bank ID (null이면 신규 생성)
     * @param bankCode 은행 코드
     * @param bankName 은행명
     * @param isActive 활성 상태
     * @param displayOrder 노출 순서
     * @param createdAt 생성 일시 (UTC 기준 Instant)
     * @param updatedAt 수정 일시 (UTC 기준 Instant)
     * @return BankJpaEntity 인스턴스
     */
    public static BankJpaEntity of(
            Long id,
            String bankCode,
            String bankName,
            boolean isActive,
            int displayOrder,
            Instant createdAt,
            Instant updatedAt) {
        return new BankJpaEntity(id, bankCode, bankName, isActive, displayOrder, createdAt, updatedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}
