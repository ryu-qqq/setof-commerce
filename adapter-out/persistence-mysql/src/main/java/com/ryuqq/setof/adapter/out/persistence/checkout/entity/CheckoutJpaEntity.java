package com.ryuqq.setof.adapter.out.persistence.checkout.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * CheckoutJpaEntity - Checkout JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 checkouts 테이블과 매핑됩니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등)
 *   <li>자식 엔티티 조회는 별도 Repository 사용
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
@Table(name = "checkouts")
public class CheckoutJpaEntity extends BaseAuditEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "member_id", nullable = false, length = 36)
    private String memberId;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "receiver_name", nullable = false, length = 50)
    private String receiverName;

    @Column(name = "receiver_phone", nullable = false, length = 20)
    private String receiverPhone;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "address_detail", length = 100)
    private String addressDetail;

    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    @Column(name = "memo", length = 500)
    private String memo;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "discount_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "final_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal finalAmount;

    @Column(name = "expired_at")
    private Instant expiredAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    protected CheckoutJpaEntity() {
        // JPA 기본 생성자
    }

    private CheckoutJpaEntity(
            UUID id,
            String memberId,
            String status,
            String receiverName,
            String receiverPhone,
            String address,
            String addressDetail,
            String zipCode,
            String memo,
            BigDecimal totalAmount,
            BigDecimal discountAmount,
            BigDecimal finalAmount,
            Instant expiredAt,
            Instant completedAt,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.memberId = memberId;
        this.status = status;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.address = address;
        this.addressDetail = addressDetail;
        this.zipCode = zipCode;
        this.memo = memo;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.expiredAt = expiredAt;
        this.completedAt = completedAt;
    }

    public static CheckoutJpaEntity of(
            UUID id,
            String memberId,
            String status,
            String receiverName,
            String receiverPhone,
            String address,
            String addressDetail,
            String zipCode,
            String memo,
            BigDecimal totalAmount,
            BigDecimal discountAmount,
            BigDecimal finalAmount,
            Instant expiredAt,
            Instant completedAt,
            Instant createdAt,
            Instant updatedAt) {
        return new CheckoutJpaEntity(
                id,
                memberId,
                status,
                receiverName,
                receiverPhone,
                address,
                addressDetail,
                zipCode,
                memo,
                totalAmount,
                discountAmount,
                finalAmount,
                expiredAt,
                completedAt,
                createdAt,
                updatedAt);
    }

    public UUID getId() {
        return id;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getStatus() {
        return status;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getMemo() {
        return memo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }
}
