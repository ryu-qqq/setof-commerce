package com.ryuqq.setof.adapter.out.persistence.seller;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerAddressJpaEntity;
import java.time.Instant;

/**
 * SellerAddressJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 SellerAddressJpaEntity 관련 객체들을 생성합니다.
 */
public final class SellerAddressJpaEntityFixtures {

    private SellerAddressJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_ID = 1L;
    public static final Long DEFAULT_SELLER_ID = 1L;
    public static final String ADDRESS_TYPE_SHIPPING = "SHIPPING";
    public static final String ADDRESS_TYPE_RETURN = "RETURN";
    public static final String DEFAULT_ADDRESS_NAME = "본사 창고";
    public static final String DEFAULT_ZIPCODE = "06141";
    public static final String DEFAULT_ADDRESS = "서울시 강남구 테헤란로 123";
    public static final String DEFAULT_ADDRESS_DETAIL = "테스트빌딩 5층";
    public static final String DEFAULT_CONTACT_NAME = "김담당";
    public static final String DEFAULT_CONTACT_PHONE = "010-9876-5432";

    // ===== Entity Fixtures =====

    /** 활성 상태의 배송지 주소 Entity 생성. */
    public static SellerAddressJpaEntity activeShippingEntity() {
        Instant now = Instant.now();
        return SellerAddressJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_SELLER_ID,
                ADDRESS_TYPE_SHIPPING,
                DEFAULT_ADDRESS_NAME,
                DEFAULT_ZIPCODE,
                DEFAULT_ADDRESS,
                DEFAULT_ADDRESS_DETAIL,
                DEFAULT_CONTACT_NAME,
                DEFAULT_CONTACT_PHONE,
                true,
                now,
                now,
                null);
    }

    /** ID를 지정한 활성 상태 주소 Entity 생성. */
    public static SellerAddressJpaEntity activeEntity(Long id) {
        Instant now = Instant.now();
        return SellerAddressJpaEntity.create(
                id,
                DEFAULT_SELLER_ID,
                ADDRESS_TYPE_SHIPPING,
                DEFAULT_ADDRESS_NAME,
                DEFAULT_ZIPCODE,
                DEFAULT_ADDRESS,
                DEFAULT_ADDRESS_DETAIL,
                DEFAULT_CONTACT_NAME,
                DEFAULT_CONTACT_PHONE,
                true,
                now,
                now,
                null);
    }

    /** 활성 상태의 반품지 주소 Entity 생성. */
    public static SellerAddressJpaEntity activeReturnEntity() {
        Instant now = Instant.now();
        return SellerAddressJpaEntity.create(
                2L,
                DEFAULT_SELLER_ID,
                ADDRESS_TYPE_RETURN,
                "반품 센터",
                "12345",
                "경기도 성남시 분당구 판교로 789",
                "물류센터 1층",
                "박담당",
                "010-1111-2222",
                false,
                now,
                now,
                null);
    }

    /** 셀러 ID를 지정한 활성 상태 주소 Entity 생성. ID는 null로 새 엔티티 생성. */
    public static SellerAddressJpaEntity activeEntityWithSellerId(Long sellerId) {
        Instant now = Instant.now();
        return SellerAddressJpaEntity.create(
                null,
                sellerId,
                ADDRESS_TYPE_SHIPPING,
                DEFAULT_ADDRESS_NAME,
                DEFAULT_ZIPCODE,
                DEFAULT_ADDRESS,
                DEFAULT_ADDRESS_DETAIL,
                DEFAULT_CONTACT_NAME,
                DEFAULT_CONTACT_PHONE,
                true,
                now,
                now,
                null);
    }

    /** 삭제된 상태 주소 Entity 생성. */
    public static SellerAddressJpaEntity deletedEntity() {
        Instant now = Instant.now();
        return SellerAddressJpaEntity.create(
                3L,
                DEFAULT_SELLER_ID,
                ADDRESS_TYPE_SHIPPING,
                DEFAULT_ADDRESS_NAME,
                DEFAULT_ZIPCODE,
                DEFAULT_ADDRESS,
                DEFAULT_ADDRESS_DETAIL,
                DEFAULT_CONTACT_NAME,
                DEFAULT_CONTACT_PHONE,
                false,
                now,
                now,
                now);
    }

    /** 새로 생성될 Entity (ID가 null). */
    public static SellerAddressJpaEntity newEntity() {
        Instant now = Instant.now();
        return SellerAddressJpaEntity.create(
                null,
                DEFAULT_SELLER_ID,
                ADDRESS_TYPE_SHIPPING,
                DEFAULT_ADDRESS_NAME,
                DEFAULT_ZIPCODE,
                DEFAULT_ADDRESS,
                DEFAULT_ADDRESS_DETAIL,
                DEFAULT_CONTACT_NAME,
                DEFAULT_CONTACT_PHONE,
                true,
                now,
                now,
                null);
    }

    /** 기본 주소가 아닌 Entity 생성. */
    public static SellerAddressJpaEntity nonDefaultEntity() {
        Instant now = Instant.now();
        return SellerAddressJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_SELLER_ID,
                ADDRESS_TYPE_SHIPPING,
                DEFAULT_ADDRESS_NAME,
                DEFAULT_ZIPCODE,
                DEFAULT_ADDRESS,
                DEFAULT_ADDRESS_DETAIL,
                DEFAULT_CONTACT_NAME,
                DEFAULT_CONTACT_PHONE,
                false,
                now,
                now,
                null);
    }
}
