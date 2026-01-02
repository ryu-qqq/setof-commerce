package com.ryuqq.setof.adapter.out.persistence.payment.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * PaymentJpaEntity - Payment JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 payments 테이블과 매핑됩니다.
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
@Table(name = "payments")
public class PaymentJpaEntity extends BaseAuditEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "checkout_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID checkoutId;

    @Column(name = "pg_provider", nullable = false, length = 30)
    private String pgProvider;

    @Column(name = "pg_transaction_id", length = 100)
    private String pgTransactionId;

    @Column(name = "method", nullable = false, length = 30)
    private String method;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "requested_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal requestedAmount;

    @Column(name = "approved_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal approvedAmount;

    @Column(name = "refunded_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal refundedAmount;

    @Column(name = "currency", nullable = false, length = 10)
    private String currency;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "member_id", length = 36)
    private String memberId;

    @Column(name = "legacy_payment_id")
    private Long legacyPaymentId;

    protected PaymentJpaEntity() {
        // JPA 기본 생성자
    }

    private PaymentJpaEntity(
            UUID id,
            UUID checkoutId,
            String pgProvider,
            String pgTransactionId,
            String method,
            String status,
            BigDecimal requestedAmount,
            BigDecimal approvedAmount,
            BigDecimal refundedAmount,
            String currency,
            Instant approvedAt,
            Instant cancelledAt,
            String memberId,
            Long legacyPaymentId,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.checkoutId = checkoutId;
        this.pgProvider = pgProvider;
        this.pgTransactionId = pgTransactionId;
        this.method = method;
        this.status = status;
        this.requestedAmount = requestedAmount;
        this.approvedAmount = approvedAmount;
        this.refundedAmount = refundedAmount;
        this.currency = currency;
        this.approvedAt = approvedAt;
        this.cancelledAt = cancelledAt;
        this.memberId = memberId;
        this.legacyPaymentId = legacyPaymentId;
    }

    public static PaymentJpaEntity of(
            UUID id,
            UUID checkoutId,
            String pgProvider,
            String pgTransactionId,
            String method,
            String status,
            BigDecimal requestedAmount,
            BigDecimal approvedAmount,
            BigDecimal refundedAmount,
            String currency,
            Instant approvedAt,
            Instant cancelledAt,
            String memberId,
            Long legacyPaymentId,
            Instant createdAt,
            Instant updatedAt) {
        return new PaymentJpaEntity(
                id,
                checkoutId,
                pgProvider,
                pgTransactionId,
                method,
                status,
                requestedAmount,
                approvedAmount,
                refundedAmount,
                currency,
                approvedAt,
                cancelledAt,
                memberId,
                legacyPaymentId,
                createdAt,
                updatedAt);
    }

    public UUID getId() {
        return id;
    }

    public UUID getCheckoutId() {
        return checkoutId;
    }

    public String getPgProvider() {
        return pgProvider;
    }

    public String getPgTransactionId() {
        return pgTransactionId;
    }

    public String getMethod() {
        return method;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public BigDecimal getRefundedAmount() {
        return refundedAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public String getMemberId() {
        return memberId;
    }

    public Long getLegacyPaymentId() {
        return legacyPaymentId;
    }
}
