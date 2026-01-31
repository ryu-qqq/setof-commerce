package com.ryuqq.setof.domain.seller.aggregate;

import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.seller.id.SellerAddressId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.vo.AddressName;
import com.ryuqq.setof.domain.seller.vo.AddressType;
import com.ryuqq.setof.domain.seller.vo.ContactInfo;
import java.time.Instant;

/** 셀러 주소 (출고지/반품지) Aggregate. */
public class SellerAddress {

    private final SellerAddressId id;
    private SellerId sellerId;
    private final AddressType addressType;
    private AddressName addressName;
    private Address address;
    private ContactInfo contactInfo;
    private boolean defaultAddress;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private SellerAddress(
            SellerAddressId id,
            SellerId sellerId,
            AddressType addressType,
            AddressName addressName,
            Address address,
            ContactInfo contactInfo,
            boolean defaultAddress,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.sellerId = sellerId;
        this.addressType = addressType;
        this.addressName = addressName;
        this.address = address;
        this.contactInfo = contactInfo;
        this.defaultAddress = defaultAddress;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * SellerId 없이 새 SellerAddress 생성.
     *
     * <p>SellerId는 나중에 assignSellerId()로 설정합니다.
     */
    public static SellerAddress forNew(
            AddressType addressType,
            AddressName addressName,
            Address address,
            ContactInfo contactInfo,
            boolean defaultAddress,
            Instant now) {
        return new SellerAddress(
                SellerAddressId.forNew(),
                null,
                addressType,
                addressName,
                address,
                contactInfo,
                defaultAddress,
                DeletionStatus.active(),
                now,
                now);
    }

    public static SellerAddress forNew(
            SellerId sellerId,
            AddressType addressType,
            AddressName addressName,
            Address address,
            ContactInfo contactInfo,
            boolean defaultAddress,
            Instant now) {
        return new SellerAddress(
                SellerAddressId.forNew(),
                sellerId,
                addressType,
                addressName,
                address,
                contactInfo,
                defaultAddress,
                DeletionStatus.active(),
                now,
                now);
    }

    public static SellerAddress reconstitute(
            SellerAddressId id,
            SellerId sellerId,
            AddressType addressType,
            AddressName addressName,
            Address address,
            ContactInfo contactInfo,
            boolean defaultAddress,
            Instant deletedAt,
            Instant createdAt,
            Instant updatedAt) {
        DeletionStatus status =
                deletedAt != null ? DeletionStatus.deletedAt(deletedAt) : DeletionStatus.active();
        return new SellerAddress(
                id,
                sellerId,
                addressType,
                addressName,
                address,
                contactInfo,
                defaultAddress,
                status,
                createdAt,
                updatedAt);
    }

    public boolean isNew() {
        return id.isNew();
    }

    /**
     * SellerId를 설정합니다.
     *
     * @param sellerId 셀러 ID
     */
    public void assignSellerId(SellerId sellerId) {
        this.sellerId = sellerId;
    }

    /**
     * 셀러 주소 정보 수정.
     *
     * @param updateData 수정 데이터
     * @param now 현재 시간
     */
    public void update(SellerAddressUpdateData updateData, Instant now) {
        this.addressName = updateData.addressName();
        this.address = updateData.address();
        this.contactInfo = updateData.contactInfo();
        this.updatedAt = now;
    }

    public void markAsDefault(Instant now) {
        this.defaultAddress = true;
        this.updatedAt = now;
    }

    public void unmarkDefault(Instant now) {
        this.defaultAddress = false;
        this.updatedAt = now;
    }

    /**
     * 셀러 주소 삭제 (Soft Delete).
     *
     * @param now 삭제 발생 시각
     */
    public void delete(Instant now) {
        this.deletionStatus = DeletionStatus.deletedAt(now);
        this.updatedAt = now;
    }

    /**
     * 삭제된 셀러 주소 복원.
     *
     * @param now 복원 시각
     */
    public void restore(Instant now) {
        this.deletionStatus = DeletionStatus.active();
        this.updatedAt = now;
    }

    public boolean isShippingAddress() {
        return addressType == AddressType.SHIPPING;
    }

    public boolean isReturnAddress() {
        return addressType == AddressType.RETURN;
    }

    // VO Getters
    public SellerAddressId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public SellerId sellerId() {
        return sellerId;
    }

    public Long sellerIdValue() {
        return sellerId.value();
    }

    public AddressType addressType() {
        return addressType;
    }

    public AddressName addressName() {
        return addressName;
    }

    public String addressNameValue() {
        return addressName.value();
    }

    public Address address() {
        return address;
    }

    public String addressZipCode() {
        return address != null ? address.zipcode() : null;
    }

    public String addressRoad() {
        return address != null ? address.line1() : null;
    }

    public String addressDetail() {
        return address != null ? address.line2() : null;
    }

    public ContactInfo contactInfo() {
        return contactInfo;
    }

    public String contactInfoName() {
        return contactInfo != null ? contactInfo.name() : null;
    }

    public String contactInfoPhone() {
        return contactInfo != null ? contactInfo.phoneValue() : null;
    }

    public boolean isDefaultAddress() {
        return defaultAddress;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    /** 삭제 여부 확인 */
    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    /**
     * 삭제 시각 반환.
     *
     * <p>AGG-014: Law of Demeter 준수를 위한 위임 메서드
     *
     * @return 삭제 시각 (활성 상태인 경우 null)
     */
    public Instant deletedAt() {
        return deletionStatus.deletedAt();
    }
}
