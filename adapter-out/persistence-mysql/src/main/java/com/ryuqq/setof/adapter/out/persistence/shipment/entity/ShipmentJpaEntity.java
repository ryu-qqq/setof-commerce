package com.ryuqq.setof.adapter.out.persistence.shipment.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ShipmentJpaEntity - Shipment JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 shipment 테이블과 매핑됩니다.
 *
 * <p><strong>BaseAuditEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt
 *   <li>Soft Delete 미적용 (운송장은 물리 삭제 없음)
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>sellerId: Seller와 Long FK
 *   <li>checkoutId: Checkout과 Long FK
 *   <li>carrierId: Carrier와 Long FK
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
@Table(
        name = "shipment",
        indexes = {
            @Index(name = "idx_shipment_seller_id", columnList = "seller_id"),
            @Index(name = "idx_shipment_checkout_id", columnList = "checkout_id"),
            @Index(
                    name = "idx_shipment_carrier_invoice",
                    columnList = "carrier_id, invoice_number"),
            @Index(name = "idx_shipment_status", columnList = "status")
        })
public class ShipmentJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 셀러 ID (Long FK) */
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    /** 결제건 ID (Long FK) */
    @Column(name = "checkout_id", nullable = false)
    private Long checkoutId;

    /** 택배사 ID (Long FK) */
    @Column(name = "carrier_id", nullable = false)
    private Long carrierId;

    /** 운송장 번호 */
    @Column(name = "invoice_number", nullable = false, length = 50)
    private String invoiceNumber;

    /** 발송인 이름 */
    @Column(name = "sender_name", nullable = false, length = 50)
    private String senderName;

    /** 발송인 연락처 */
    @Column(name = "sender_phone", nullable = false, length = 20)
    private String senderPhone;

    /** 발송지 주소 (nullable) */
    @Column(name = "sender_address", length = 200)
    private String senderAddress;

    /** 배송 유형 */
    @Column(name = "type", nullable = false, length = 20)
    private String type;

    /** 배송 상태 */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** 마지막 추적 위치 (nullable) */
    @Column(name = "last_location", length = 200)
    private String lastLocation;

    /** 마지막 추적 메시지 (nullable) */
    @Column(name = "last_message", length = 500)
    private String lastMessage;

    /** 마지막 추적 시각 (nullable) */
    @Column(name = "last_tracked_at")
    private Instant lastTrackedAt;

    /** 배송 완료 시각 (nullable) */
    @Column(name = "delivered_at")
    private Instant deliveredAt;

    /** 발송 시각 (nullable) */
    @Column(name = "shipped_at")
    private Instant shippedAt;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected ShipmentJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private ShipmentJpaEntity(
            Long id,
            Long sellerId,
            Long checkoutId,
            Long carrierId,
            String invoiceNumber,
            String senderName,
            String senderPhone,
            String senderAddress,
            String type,
            String status,
            String lastLocation,
            String lastMessage,
            Instant lastTrackedAt,
            Instant deliveredAt,
            Instant shippedAt,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.sellerId = sellerId;
        this.checkoutId = checkoutId;
        this.carrierId = carrierId;
        this.invoiceNumber = invoiceNumber;
        this.senderName = senderName;
        this.senderPhone = senderPhone;
        this.senderAddress = senderAddress;
        this.type = type;
        this.status = status;
        this.lastLocation = lastLocation;
        this.lastMessage = lastMessage;
        this.lastTrackedAt = lastTrackedAt;
        this.deliveredAt = deliveredAt;
        this.shippedAt = shippedAt;
    }

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * @param id Shipment ID (null이면 신규 생성)
     * @param sellerId 셀러 ID
     * @param checkoutId 결제건 ID
     * @param carrierId 택배사 ID
     * @param invoiceNumber 운송장 번호
     * @param senderName 발송인 이름
     * @param senderPhone 발송인 연락처
     * @param senderAddress 발송지 주소
     * @param type 배송 유형
     * @param status 배송 상태
     * @param lastLocation 마지막 추적 위치
     * @param lastMessage 마지막 추적 메시지
     * @param lastTrackedAt 마지막 추적 시각
     * @param deliveredAt 배송 완료 시각
     * @param shippedAt 발송 시각
     * @param createdAt 생성 일시 (UTC 기준 Instant)
     * @param updatedAt 수정 일시 (UTC 기준 Instant)
     * @return ShipmentJpaEntity 인스턴스
     */
    public static ShipmentJpaEntity of(
            Long id,
            Long sellerId,
            Long checkoutId,
            Long carrierId,
            String invoiceNumber,
            String senderName,
            String senderPhone,
            String senderAddress,
            String type,
            String status,
            String lastLocation,
            String lastMessage,
            Instant lastTrackedAt,
            Instant deliveredAt,
            Instant shippedAt,
            Instant createdAt,
            Instant updatedAt) {
        return new ShipmentJpaEntity(
                id,
                sellerId,
                checkoutId,
                carrierId,
                invoiceNumber,
                senderName,
                senderPhone,
                senderAddress,
                type,
                status,
                lastLocation,
                lastMessage,
                lastTrackedAt,
                deliveredAt,
                shippedAt,
                createdAt,
                updatedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public Long getCheckoutId() {
        return checkoutId;
    }

    public Long getCarrierId() {
        return carrierId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Instant getLastTrackedAt() {
        return lastTrackedAt;
    }

    public Instant getDeliveredAt() {
        return deliveredAt;
    }

    public Instant getShippedAt() {
        return shippedAt;
    }
}
