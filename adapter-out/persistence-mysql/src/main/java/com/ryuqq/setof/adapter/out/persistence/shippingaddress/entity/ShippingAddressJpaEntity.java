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
 * ShippingAddressJpaEntity - ShippingAddress JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 shipping_addresses 테이블과 매핑됩니다.
 *
 * <p><strong>SoftDeletableEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt, deletedAt
 *   <li>소프트 딜리트 지원 (deletedAt != null -> 삭제)
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>member_id: UUID v7 문자열로 직접 관리 (FK 없음)
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "shipping_addresses")
public class ShippingAddressJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 회원 ID (UUID v7 - FK 없이 String으로 관리) */
    @Column(name = "member_id", nullable = false, length = 36)
    private String memberId;

    /** 배송지명 */
    @Column(name = "address_name", nullable = false, length = 50)
    private String addressName;

    /** 수령인 이름 */
    @Column(name = "receiver_name", nullable = false, length = 50)
    private String receiverName;

    /** 수령인 연락처 */
    @Column(name = "receiver_phone", nullable = false, length = 11)
    private String receiverPhone;

    /** 우편번호 */
    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    /** 도로명 주소 (nullable) */
    @Column(name = "road_address", length = 200)
    private String roadAddress;

    /** 지번 주소 (nullable) */
    @Column(name = "jibun_address", length = 200)
    private String jibunAddress;

    /** 상세 주소 */
    @Column(name = "detail_address", length = 100)
    private String detailAddress;

    /** 배송 요청사항 */
    @Column(name = "delivery_request", length = 200)
    private String deliveryRequest;

    /** 기본 배송지 여부 */
    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    /** JPA 기본 생성자 (protected) */
    protected ShippingAddressJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private ShippingAddressJpaEntity(
            Long id,
            String memberId,
            String addressName,
            String receiverName,
            String receiverPhone,
            String zipCode,
            String roadAddress,
            String jibunAddress,
            String detailAddress,
            String deliveryRequest,
            boolean isDefault,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.memberId = memberId;
        this.addressName = addressName;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.zipCode = zipCode;
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.detailAddress = detailAddress;
        this.deliveryRequest = deliveryRequest;
        this.isDefault = isDefault;
    }

    /** of() 스태틱 팩토리 메서드 (Mapper 전용) */
    public static ShippingAddressJpaEntity of(
            Long id,
            String memberId,
            String addressName,
            String receiverName,
            String receiverPhone,
            String zipCode,
            String roadAddress,
            String jibunAddress,
            String detailAddress,
            String deliveryRequest,
            boolean isDefault,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ShippingAddressJpaEntity(
                id,
                memberId,
                addressName,
                receiverName,
                receiverPhone,
                zipCode,
                roadAddress,
                jibunAddress,
                detailAddress,
                deliveryRequest,
                isDefault,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public String getJibunAddress() {
        return jibunAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public String getDeliveryRequest() {
        return deliveryRequest;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
