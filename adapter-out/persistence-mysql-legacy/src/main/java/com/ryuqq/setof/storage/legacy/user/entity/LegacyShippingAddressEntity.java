package com.ryuqq.setof.storage.legacy.user.entity;

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
 * LegacyShippingAddressEntity - 레거시 배송지 엔티티.
 *
 * <p>레거시 DB의 shipping_address 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "shipping_address")
public class LegacyShippingAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_address_id")
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "shipping_address_name")
    private String shippingAddressName;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "country")
    @Enumerated(EnumType.STRING)
    private Origin country;

    @Column(name = "delivery_request")
    private String deliveryRequest;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "default_yn")
    @Enumerated(EnumType.STRING)
    private Yn defaultYn;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyShippingAddressEntity() {}

    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
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

    public String getZipCode() {
        return zipCode;
    }

    public Origin getCountry() {
        return country;
    }

    public String getDeliveryRequest() {
        return deliveryRequest;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Yn getDefaultYn() {
        return defaultYn;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /** Origin - 국가 Enum. */
    public enum Origin {
        KR,
        US,
        JP,
        CN
    }

    /** Yn - Y/N 구분 Enum. */
    public enum Yn {
        Y,
        N
    }
}
