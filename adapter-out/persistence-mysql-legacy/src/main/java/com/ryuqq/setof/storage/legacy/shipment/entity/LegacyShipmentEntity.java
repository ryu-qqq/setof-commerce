package com.ryuqq.setof.storage.legacy.shipment.entity;

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
 * LegacyShipmentEntity - 레거시 배송 엔티티.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "shipment")
public class LegacyShipmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Long id;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private LegacyDeliveryStatus deliveryStatus;

    @Column(name = "company_code")
    @Enumerated(EnumType.STRING)
    private LegacyShipmentCompanyCode companyCode;

    @Column(name = "invoice_no")
    private String invoiceNo;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyShipmentEntity() {}

    public Long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public LegacyDeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public LegacyShipmentCompanyCode getCompanyCode() {
        return companyCode;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}
