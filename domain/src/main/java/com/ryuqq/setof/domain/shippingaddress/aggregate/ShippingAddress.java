package com.ryuqq.setof.domain.shippingaddress.aggregate;

import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressNotOwnerException;
import com.ryuqq.setof.domain.shippingaddress.vo.AddressName;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryRequest;
import com.ryuqq.setof.domain.shippingaddress.vo.ReceiverInfo;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

/**
 * ShippingAddress Aggregate Root
 *
 * <p>회원의 배송지 정보를 관리하는 Aggregate Root입니다.
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>회원당 최대 5개까지 등록 가능
 *   <li>기본 배송지는 0~1개
 *   <li>기본 배송지 삭제 시 가장 최근 저장 주소로 자동 변경 (Application Layer에서 처리)
 *   <li>Soft Delete 적용
 * </ul>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드 (상태 변경은 도메인 메서드로만)
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 *   <li>Tell, Don't Ask - 상태 변경은 도메인 메서드로만
 * </ul>
 */
public class ShippingAddress {

    /** 회원당 최대 배송지 개수 */
    public static final int MAX_ADDRESS_COUNT = 5;

    private final ShippingAddressId id;
    private final UUID memberId;
    private AddressName addressName;
    private ReceiverInfo receiverInfo;
    private DeliveryAddress deliveryAddress;
    private DeliveryRequest deliveryRequest;
    private boolean isDefault;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private ShippingAddress(
            ShippingAddressId id,
            UUID memberId,
            AddressName addressName,
            ReceiverInfo receiverInfo,
            DeliveryAddress deliveryAddress,
            DeliveryRequest deliveryRequest,
            boolean isDefault,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        this.id = id;
        this.memberId = memberId;
        this.addressName = addressName;
        this.receiverInfo = receiverInfo;
        this.deliveryAddress = deliveryAddress;
        this.deliveryRequest = deliveryRequest;
        this.isDefault = isDefault;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * 신규 배송지 생성용 Static Factory Method
     *
     * <p>ID 없이 생성 (Persistence Layer에서 Auto-increment)
     *
     * @param memberId 회원 ID
     * @param addressName 배송지 이름
     * @param receiverInfo 수령인 정보
     * @param deliveryAddress 배송 주소
     * @param deliveryRequest 배송 요청사항
     * @param isDefault 기본 배송지 여부
     * @param clock 시간 제공자
     * @return ShippingAddress 인스턴스
     */
    public static ShippingAddress forNew(
            UUID memberId,
            AddressName addressName,
            ReceiverInfo receiverInfo,
            DeliveryAddress deliveryAddress,
            DeliveryRequest deliveryRequest,
            boolean isDefault,
            Clock clock) {
        Instant now = clock.instant();
        return new ShippingAddress(
                null,
                memberId,
                addressName,
                receiverInfo,
                deliveryAddress,
                deliveryRequest,
                isDefault,
                now,
                now,
                null);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @return ShippingAddress 인스턴스
     */
    public static ShippingAddress reconstitute(
            ShippingAddressId id,
            UUID memberId,
            AddressName addressName,
            ReceiverInfo receiverInfo,
            DeliveryAddress deliveryAddress,
            DeliveryRequest deliveryRequest,
            boolean isDefault,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ShippingAddress(
                id,
                memberId,
                addressName,
                receiverInfo,
                deliveryAddress,
                deliveryRequest,
                isDefault,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 배송지 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 배송지 ID Long 값 또는 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 배송지 이름 값 반환 (Law of Demeter 준수)
     *
     * @return 배송지 이름 문자열
     */
    public String getAddressNameValue() {
        return addressName.value();
    }

    /**
     * 수령인 이름 값 반환 (Law of Demeter 준수)
     *
     * @return 수령인 이름 문자열
     */
    public String getReceiverNameValue() {
        return receiverInfo.name();
    }

    /**
     * 수령인 연락처 값 반환 (Law of Demeter 준수)
     *
     * @return 수령인 연락처 문자열
     */
    public String getReceiverPhoneValue() {
        return receiverInfo.phone();
    }

    /**
     * 도로명 주소 값 반환 (Law of Demeter 준수)
     *
     * @return 도로명 주소 문자열 또는 null
     */
    public String getRoadAddressValue() {
        return deliveryAddress.roadAddress();
    }

    /**
     * 지번 주소 값 반환 (Law of Demeter 준수)
     *
     * @return 지번 주소 문자열 또는 null
     */
    public String getJibunAddressValue() {
        return deliveryAddress.jibunAddress();
    }

    /**
     * 상세 주소 값 반환 (Law of Demeter 준수)
     *
     * @return 상세 주소 문자열 또는 null
     */
    public String getDetailAddressValue() {
        return deliveryAddress.detailAddress();
    }

    /**
     * 우편번호 값 반환 (Law of Demeter 준수)
     *
     * @return 우편번호 문자열
     */
    public String getZipCodeValue() {
        return deliveryAddress.zipCode();
    }

    /**
     * 배송 요청사항 값 반환 (Law of Demeter 준수)
     *
     * @return 배송 요청사항 문자열 또는 null
     */
    public String getDeliveryRequestValue() {
        return deliveryRequest != null ? deliveryRequest.value() : null;
    }

    /**
     * 전체 주소 문자열 반환 (대표주소 + 상세주소)
     *
     * @return 전체 주소 문자열
     */
    public String getFullAddress() {
        return deliveryAddress.fullAddress();
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 배송지 정보 수정
     *
     * @param addressName 새 배송지 이름
     * @param receiverInfo 새 수령인 정보
     * @param deliveryAddress 새 배송 주소
     * @param deliveryRequest 새 배송 요청사항
     * @param clock 시간 제공자
     */
    public void update(
            AddressName addressName,
            ReceiverInfo receiverInfo,
            DeliveryAddress deliveryAddress,
            DeliveryRequest deliveryRequest,
            Clock clock) {
        this.addressName = addressName;
        this.receiverInfo = receiverInfo;
        this.deliveryAddress = deliveryAddress;
        this.deliveryRequest = deliveryRequest;
        this.updatedAt = clock.instant();
    }

    /**
     * 기본 배송지로 설정
     *
     * @param clock 시간 제공자
     */
    public void setAsDefault(Clock clock) {
        this.isDefault = true;
        this.updatedAt = clock.instant();
    }

    /**
     * 기본 배송지 해제
     *
     * @param clock 시간 제공자
     */
    public void unsetDefault(Clock clock) {
        this.isDefault = false;
        this.updatedAt = clock.instant();
    }

    /**
     * 배송지 삭제 (Soft Delete)
     *
     * @param clock 시간 제공자
     */
    public void delete(Clock clock) {
        Instant now = clock.instant();
        this.deletedAt = now;
        this.updatedAt = now;
    }

    /**
     * 소유자 확인 - 요청 회원이 소유자가 아니면 예외
     *
     * @param requestMemberId 요청 회원 ID
     * @throws ShippingAddressNotOwnerException 소유자가 아닌 경우
     */
    public void validateOwnership(UUID requestMemberId) {
        if (!this.memberId.equals(requestMemberId)) {
            throw new ShippingAddressNotOwnerException(getIdValue(), requestMemberId);
        }
    }

    /**
     * 기본 배송지 여부 확인 (Tell, Don't Ask)
     *
     * @return 기본 배송지이면 true
     */
    public boolean isDefault() {
        return this.isDefault;
    }

    /**
     * 삭제된 배송지인지 확인 (Tell, Don't Ask)
     *
     * @return 삭제되었으면 true
     */
    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    /**
     * 활성 배송지인지 확인 (Tell, Don't Ask)
     *
     * @return 삭제되지 않았으면 true
     */
    public boolean isActive() {
        return this.deletedAt == null;
    }

    /**
     * 해당 회원 소유인지 확인 (Tell, Don't Ask)
     *
     * @param memberId 확인할 회원 ID
     * @return 소유자이면 true
     */
    public boolean isOwnedBy(UUID memberId) {
        return this.memberId.equals(memberId);
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public ShippingAddressId getId() {
        return id;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public AddressName getAddressName() {
        return addressName;
    }

    public ReceiverInfo getReceiverInfo() {
        return receiverInfo;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public DeliveryRequest getDeliveryRequest() {
        return deliveryRequest;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
