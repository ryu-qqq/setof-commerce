package com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ShippingAddressJpaEntity - 배송지 JPA 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "shipping_addresses")
public class ShippingAddressJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "legacy_member_id")
    private Long legacyMemberId;

    @Column(name = "receiver_name", nullable = false, length = 50)
    private String receiverName;

    @Column(name = "shipping_address_name", nullable = false, length = 50)
    private String shippingAddressName;

    @Column(name = "address_line1", nullable = false, length = 255)
    private String addressLine1;

    @Column(name = "address_line2", length = 255)
    private String addressLine2;

    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Column(name = "delivery_request", length = 500)
    private String deliveryRequest;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "default_address", nullable = false)
    private boolean defaultAddress;

    protected ShippingAddressJpaEntity() {
        super();
    }

    private ShippingAddressJpaEntity(
            Long id,
            Long memberId,
            Long legacyMemberId,
            String receiverName,
            String shippingAddressName,
            String addressLine1,
            String addressLine2,
            String zipCode,
            String country,
            String deliveryRequest,
            String phoneNumber,
            boolean defaultAddress,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.memberId = memberId;
        this.legacyMemberId = legacyMemberId;
        this.receiverName = receiverName;
        this.shippingAddressName = shippingAddressName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.zipCode = zipCode;
        this.country = country;
        this.deliveryRequest = deliveryRequest;
        this.phoneNumber = phoneNumber;
        this.defaultAddress = defaultAddress;
    }

    public static ShippingAddressJpaEntity create(
            Long id,
            Long memberId,
            Long legacyMemberId,
            String receiverName,
            String shippingAddressName,
            String addressLine1,
            String addressLine2,
            String zipCode,
            String country,
            String deliveryRequest,
            String phoneNumber,
            boolean defaultAddress,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ShippingAddressJpaEntity(
                id,
                memberId,
                legacyMemberId,
                receiverName,
                shippingAddressName,
                addressLine1,
                addressLine2,
                zipCode,
                country,
                deliveryRequest,
                phoneNumber,
                defaultAddress,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getLegacyMemberId() {
        return legacyMemberId;
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

    public boolean isDefaultAddress() {
        return defaultAddress;
    }
}
