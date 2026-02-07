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

    @Column(name = "user_id")
    private long userId;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private LegacyPaymentStatus paymentStatus;

    @Column(name = "site_name")
    private String siteName;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "canceled_date")
    private LocalDateTime canceledDate;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyPaymentEntity() {}

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
