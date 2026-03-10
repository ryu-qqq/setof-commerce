package com.ryuqq.setof.storage.legacy.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyPaymentEntity - 레거시 결제 엔티티.
 *
 * <p>레거시 DB의 payment 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "payment")
public class LegacyPaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "PAYMENT_AMOUNT")
    private long paymentAmount;

    @Column(name = "PAYMENT_STATUS")
    @Enumerated(EnumType.STRING)
    private LegacyPaymentStatus paymentStatus;

    @Column(name = "SITE_NAME")
    private String siteName;

    @Column(name = "PAYMENT_DATE")
    private LocalDateTime paymentDate;

    @Column(name = "CANCELED_DATE")
    private LocalDateTime canceledDate;

    @Column(name = "INSERT_DATE")
    private LocalDateTime insertDate;

    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;

    @Column(name = "delete_yn")
    private String deleteYn;

    @Column(name = "INSERT_OPERATOR")
    private String insertOperator;

    @Column(name = "UPDATE_OPERATOR")
    private String updateOperator;

    protected LegacyPaymentEntity() {}

    /**
     * 결제 레코드 생성용 팩토리 메서드.
     *
     * @param userId 사용자 ID
     * @param paymentStatus 결제 상태
     * @param siteName 사이트명
     * @return 새 결제 엔티티
     */
    public static LegacyPaymentEntity create(
            long userId, long paymentAmount, LegacyPaymentStatus paymentStatus, String siteName) {
        LegacyPaymentEntity entity = new LegacyPaymentEntity();
        entity.userId = userId;
        entity.paymentAmount = paymentAmount;
        entity.paymentStatus = paymentStatus;
        entity.siteName = siteName;
        entity.insertDate = java.time.LocalDateTime.now();
        entity.updateDate = java.time.LocalDateTime.now();
        entity.deleteYn = "N";
        entity.insertOperator = "SYSTEM";
        entity.updateOperator = "SYSTEM";
        return entity;
    }

    public long getPaymentAmount() {
        return paymentAmount;
    }

    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public LegacyPaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public String getSiteName() {
        return siteName;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public LocalDateTime getCanceledDate() {
        return canceledDate;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}
