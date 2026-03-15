package com.ryuqq.setof.domain.shippingaddress.aggregate;

import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.shippingaddress.id.ShippingAddressId;
import com.ryuqq.setof.domain.shippingaddress.vo.Country;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryRequest;
import com.ryuqq.setof.domain.shippingaddress.vo.ReceiverName;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressName;
import java.time.Instant;

/**
 * 배송지 Aggregate.
 *
 * <p>회원의 배송지 정보를 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ShippingAddress {

    private final ShippingAddressId id;
    private final MemberId memberId;
    private final LegacyMemberId legacyMemberId;
    private ReceiverName receiverName;
    private ShippingAddressName shippingAddressName;
    private Address address;
    private Country country;
    private DeliveryRequest deliveryRequest;
    private PhoneNumber phoneNumber;
    private boolean defaultAddress;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private ShippingAddress(
            ShippingAddressId id,
            MemberId memberId,
            LegacyMemberId legacyMemberId,
            ReceiverName receiverName,
            ShippingAddressName shippingAddressName,
            Address address,
            Country country,
            DeliveryRequest deliveryRequest,
            PhoneNumber phoneNumber,
            boolean defaultAddress,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.legacyMemberId = legacyMemberId;
        this.receiverName = receiverName;
        this.shippingAddressName = shippingAddressName;
        this.address = address;
        this.country = country;
        this.deliveryRequest = deliveryRequest;
        this.phoneNumber = phoneNumber;
        this.defaultAddress = defaultAddress;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** 레거시 시스템에서 신규 배송지 생성. */
    public static ShippingAddress forLegacy(
            LegacyMemberId legacyMemberId,
            ReceiverName receiverName,
            ShippingAddressName shippingAddressName,
            Address address,
            Country country,
            DeliveryRequest deliveryRequest,
            PhoneNumber phoneNumber,
            boolean defaultAddress,
            Instant occurredAt) {
        return new ShippingAddress(
                ShippingAddressId.forNew(),
                null,
                legacyMemberId,
                receiverName,
                shippingAddressName,
                address,
                country,
                deliveryRequest,
                phoneNumber,
                defaultAddress,
                DeletionStatus.active(),
                occurredAt,
                occurredAt);
    }

    /** 신규 배송지 생성 (새 스키마). */
    public static ShippingAddress forNew(
            MemberId memberId,
            ReceiverName receiverName,
            ShippingAddressName shippingAddressName,
            Address address,
            Country country,
            DeliveryRequest deliveryRequest,
            PhoneNumber phoneNumber,
            boolean defaultAddress,
            Instant occurredAt) {
        return new ShippingAddress(
                ShippingAddressId.forNew(),
                memberId,
                null,
                receiverName,
                shippingAddressName,
                address,
                country,
                deliveryRequest,
                phoneNumber,
                defaultAddress,
                DeletionStatus.active(),
                occurredAt,
                occurredAt);
    }

    /** 영속성 레이어에서 복원. */
    public static ShippingAddress reconstitute(
            ShippingAddressId id,
            MemberId memberId,
            LegacyMemberId legacyMemberId,
            ReceiverName receiverName,
            ShippingAddressName shippingAddressName,
            Address address,
            Country country,
            DeliveryRequest deliveryRequest,
            PhoneNumber phoneNumber,
            boolean defaultAddress,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new ShippingAddress(
                id,
                memberId,
                legacyMemberId,
                receiverName,
                shippingAddressName,
                address,
                country,
                deliveryRequest,
                phoneNumber,
                defaultAddress,
                deletionStatus,
                createdAt,
                updatedAt);
    }

    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 배송지 정보 수정.
     *
     * @param updateData 수정 데이터
     */
    public void update(ShippingAddressUpdateData updateData) {
        this.receiverName = updateData.receiverName();
        this.shippingAddressName = updateData.shippingAddressName();
        this.address = updateData.address();
        this.country = updateData.country();
        this.deliveryRequest = updateData.deliveryRequest();
        this.phoneNumber = updateData.phoneNumber();
        this.updatedAt = updateData.occurredAt();
    }

    /** 기본 배송지로 설정. */
    public void markAsDefault(Instant occurredAt) {
        this.defaultAddress = true;
        this.updatedAt = occurredAt;
    }

    /** 기본 배송지 해제. */
    public void unmarkAsDefault(Instant occurredAt) {
        this.defaultAddress = false;
        this.updatedAt = occurredAt;
    }

    /** 소프트 삭제. */
    public void delete(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
        this.updatedAt = occurredAt;
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public boolean isDefault() {
        return defaultAddress;
    }

    // VO Getters
    public ShippingAddressId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public MemberId memberId() {
        return memberId;
    }

    public Long memberIdValue() {
        return memberId != null ? memberId.value() : null;
    }

    public LegacyMemberId legacyMemberId() {
        return legacyMemberId;
    }

    public Long legacyMemberIdValue() {
        return legacyMemberId != null ? legacyMemberId.value() : null;
    }

    public ReceiverName receiverName() {
        return receiverName;
    }

    public String receiverNameValue() {
        return receiverName.value();
    }

    public ShippingAddressName shippingAddressName() {
        return shippingAddressName;
    }

    public String shippingAddressNameValue() {
        return shippingAddressName.value();
    }

    public Address address() {
        return address;
    }

    public Country country() {
        return country;
    }

    public DeliveryRequest deliveryRequest() {
        return deliveryRequest;
    }

    public String deliveryRequestValue() {
        return deliveryRequest != null ? deliveryRequest.value() : null;
    }

    public PhoneNumber phoneNumber() {
        return phoneNumber;
    }

    public String phoneNumberValue() {
        return phoneNumber.value();
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
