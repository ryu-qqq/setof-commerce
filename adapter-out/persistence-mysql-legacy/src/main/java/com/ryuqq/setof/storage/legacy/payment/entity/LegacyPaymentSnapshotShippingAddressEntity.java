package com.ryuqq.setof.storage.legacy.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyPaymentSnapshotShippingAddressEntity - 결제 시점 배송지 스냅샷 엔티티.
 *
 * <p>결제 시점의 배송지 정보를 스냅샷으로 보관합니다.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "payment_snapshot_shipping_address")
public class LegacyPaymentSnapshotShippingAddressEntity {

    @Id
    @Column(name = "payment_snapshot_shipping_address_id")
    private Long id;

    @Column(name = "PAYMENT_ID")
    private long paymentId;

    @Column(name = "RECEIVER_NAME")
    private String receiverName;

    @Column(name = "SHIPPING_ADDRESS_NAME")
    private String shippingAddressName;

    @Column(name = "ADDRESS_LINE1")
    private String addressLine1;

    @Column(name = "ADDRESS_LINE2")
    private String addressLine2;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "DELIVERY_REQUEST")
    private String deliveryRequest;

    protected LegacyPaymentSnapshotShippingAddressEntity() {}

    public Long getId() {
        return id;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getShippingAddressName() {
        return shippingAddressName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    public String getDeliveryRequest() {
        return deliveryRequest;
    }
}
