package com.ryuqq.setof.storage.legacy.shippingaddress.entity;

import com.ryuqq.setof.storage.legacy.common.Yn;
import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
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
public class LegacyShippingAddressEntity extends LegacyBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_address_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

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
    private String country;

    @Column(name = "delivery_request")
    private String deliveryRequest;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "default_yn")
    @Enumerated(EnumType.STRING)
    private Yn defaultYn;

    protected LegacyShippingAddressEntity() {}

    public static LegacyShippingAddressEntity create(
            Long userId,
            String receiverName,
            String shippingAddressName,
            String addressLine1,
            String addressLine2,
            String zipCode,
            String country,
            String deliveryRequest,
            String phoneNumber,
            Yn defaultYn,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        LegacyShippingAddressEntity entity = new LegacyShippingAddressEntity();
        entity.userId = userId;
        entity.receiverName = receiverName;
        entity.shippingAddressName = shippingAddressName;
        entity.addressLine1 = addressLine1;
        entity.addressLine2 = addressLine2;
        entity.zipCode = zipCode;
        entity.country = country;
        entity.deliveryRequest = deliveryRequest;
        entity.phoneNumber = phoneNumber;
        entity.defaultYn = defaultYn;
        return new LegacyShippingAddressEntity(insertDate, updateDate, entity);
    }

    private LegacyShippingAddressEntity(
            LocalDateTime insertDate,
            LocalDateTime updateDate,
            LegacyShippingAddressEntity source) {
        super(insertDate, updateDate);
        this.id = source.id;
        this.userId = source.userId;
        this.receiverName = source.receiverName;
        this.shippingAddressName = source.shippingAddressName;
        this.addressLine1 = source.addressLine1;
        this.addressLine2 = source.addressLine2;
        this.zipCode = source.zipCode;
        this.country = source.country;
        this.deliveryRequest = source.deliveryRequest;
        this.phoneNumber = source.phoneNumber;
        this.defaultYn = source.defaultYn;
    }

    public static LegacyShippingAddressEntity reconstitute(
            Long id,
            Long userId,
            String receiverName,
            String shippingAddressName,
            String addressLine1,
            String addressLine2,
            String zipCode,
            String country,
            String deliveryRequest,
            String phoneNumber,
            Yn defaultYn,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        LegacyShippingAddressEntity entity = new LegacyShippingAddressEntity();
        entity.id = id;
        entity.userId = userId;
        entity.receiverName = receiverName;
        entity.shippingAddressName = shippingAddressName;
        entity.addressLine1 = addressLine1;
        entity.addressLine2 = addressLine2;
        entity.zipCode = zipCode;
        entity.country = country;
        entity.deliveryRequest = deliveryRequest;
        entity.phoneNumber = phoneNumber;
        entity.defaultYn = defaultYn;
        return new LegacyShippingAddressEntity(insertDate, updateDate, entity);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
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

    public String getCountry() {
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
}
