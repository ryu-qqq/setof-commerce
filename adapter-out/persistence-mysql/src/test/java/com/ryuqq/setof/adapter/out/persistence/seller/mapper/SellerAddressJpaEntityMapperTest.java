package com.ryuqq.setof.adapter.out.persistence.seller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.seller.SellerAddressJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerAddressJpaEntity;
import com.ryuqq.setof.domain.seller.SellerFixtures;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * SellerAddressJpaEntityMapperTest - 셀러 주소 Entity-Domain 매퍼 단위 테스트.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("SellerAddressJpaEntityMapper 단위 테스트")
class SellerAddressJpaEntityMapperTest {

    private SellerAddressJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SellerAddressJpaEntityMapper();
    }

    // ========================================================================
    // 1. toEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toEntity 메서드 테스트")
    class ToEntityTest {

        @Test
        @DisplayName("활성 상태 배송지 Domain을 Entity로 변환합니다")
        void toEntity_WithActiveShippingAddress_ConvertsCorrectly() {
            // given
            SellerAddress domain = SellerFixtures.activeShippingAddress();

            // when
            SellerAddressJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isEqualTo(domain.idValue());
            assertThat(entity.getSellerId()).isEqualTo(domain.sellerIdValue());
            assertThat(entity.getAddressType()).isEqualTo(domain.addressType().name());
            assertThat(entity.getAddressName()).isEqualTo(domain.addressNameValue());
            assertThat(entity.getZipcode()).isEqualTo(domain.addressZipCode());
            assertThat(entity.getAddress()).isEqualTo(domain.addressRoad());
            assertThat(entity.getAddressDetail()).isEqualTo(domain.addressDetail());
            assertThat(entity.getContactName()).isEqualTo(domain.contactInfoName());
            assertThat(entity.getContactPhone()).isEqualTo(domain.contactInfoPhone());
            assertThat(entity.isDefaultAddress()).isTrue();
        }

        @Test
        @DisplayName("활성 상태 반품지 Domain을 Entity로 변환합니다")
        void toEntity_WithActiveReturnAddress_ConvertsCorrectly() {
            // given
            SellerAddress domain = SellerFixtures.activeReturnAddress();

            // when
            SellerAddressJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getAddressType()).isEqualTo("RETURN");
            assertThat(entity.isDefaultAddress()).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithDeletedAddress_ConvertsCorrectly() {
            // given
            SellerAddress domain = SellerFixtures.deletedSellerAddress();

            // when
            SellerAddressJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("새로운 배송지 Domain을 Entity로 변환합니다")
        void toEntity_WithNewShippingAddress_ConvertsCorrectly() {
            // given
            SellerAddress domain = SellerFixtures.newShippingAddress();

            // when
            SellerAddressJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isNull();
            assertThat(entity.getAddressType()).isEqualTo("SHIPPING");
        }
    }

    // ========================================================================
    // 2. toDomain 테스트
    // ========================================================================

    @Nested
    @DisplayName("toDomain 메서드 테스트")
    class ToDomainTest {

        @Test
        @DisplayName("활성 상태 배송지 Entity를 Domain으로 변환합니다")
        void toDomain_WithActiveShippingEntity_ConvertsCorrectly() {
            // given
            SellerAddressJpaEntity entity = SellerAddressJpaEntityFixtures.activeShippingEntity();

            // when
            SellerAddress domain = mapper.toDomain(entity);

            // then
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.sellerIdValue()).isEqualTo(entity.getSellerId());
            assertThat(domain.addressType().name()).isEqualTo(entity.getAddressType());
            assertThat(domain.addressNameValue()).isEqualTo(entity.getAddressName());
            assertThat(domain.addressZipCode()).isEqualTo(entity.getZipcode());
            assertThat(domain.addressRoad()).isEqualTo(entity.getAddress());
            assertThat(domain.addressDetail()).isEqualTo(entity.getAddressDetail());
            assertThat(domain.contactInfoName()).isEqualTo(entity.getContactName());
            assertThat(domain.contactInfoPhone()).isEqualTo(entity.getContactPhone());
            assertThat(domain.isDefaultAddress()).isTrue();
        }

        @Test
        @DisplayName("활성 상태 반품지 Entity를 Domain으로 변환합니다")
        void toDomain_WithActiveReturnEntity_ConvertsCorrectly() {
            // given
            SellerAddressJpaEntity entity = SellerAddressJpaEntityFixtures.activeReturnEntity();

            // when
            SellerAddress domain = mapper.toDomain(entity);

            // then
            assertThat(domain.addressType().name()).isEqualTo("RETURN");
            assertThat(domain.isDefaultAddress()).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithDeletedEntity_ConvertsCorrectly() {
            // given
            SellerAddressJpaEntity entity = SellerAddressJpaEntityFixtures.deletedEntity();

            // when
            SellerAddress domain = mapper.toDomain(entity);

            // then
            assertThat(domain.isDeleted()).isTrue();
            assertThat(domain.deletedAt()).isNotNull();
        }

        @Test
        @DisplayName("기본 주소가 아닌 Entity를 Domain으로 변환합니다")
        void toDomain_WithNonDefaultEntity_ConvertsCorrectly() {
            // given
            SellerAddressJpaEntity entity = SellerAddressJpaEntityFixtures.nonDefaultEntity();

            // when
            SellerAddress domain = mapper.toDomain(entity);

            // then
            assertThat(domain.isDefaultAddress()).isFalse();
        }
    }

    // ========================================================================
    // 3. 양방향 변환 테스트
    // ========================================================================

    @Nested
    @DisplayName("양방향 변환 테스트")
    class BidirectionalConversionTest {

        @Test
        @DisplayName("Domain -> Entity -> Domain 변환 시 데이터가 보존됩니다")
        void roundTrip_DomainToEntityToDomain_PreservesData() {
            // given
            SellerAddress original = SellerFixtures.activeShippingAddress();

            // when
            SellerAddressJpaEntity entity = mapper.toEntity(original);
            SellerAddress converted = mapper.toDomain(entity);

            // then
            assertThat(converted.idValue()).isEqualTo(original.idValue());
            assertThat(converted.sellerIdValue()).isEqualTo(original.sellerIdValue());
            assertThat(converted.addressType()).isEqualTo(original.addressType());
            assertThat(converted.addressNameValue()).isEqualTo(original.addressNameValue());
            assertThat(converted.isDefaultAddress()).isEqualTo(original.isDefaultAddress());
        }

        @Test
        @DisplayName("Entity -> Domain -> Entity 변환 시 데이터가 보존됩니다")
        void roundTrip_EntityToDomainToEntity_PreservesData() {
            // given
            SellerAddressJpaEntity original = SellerAddressJpaEntityFixtures.activeShippingEntity();

            // when
            SellerAddress domain = mapper.toDomain(original);
            SellerAddressJpaEntity converted = mapper.toEntity(domain);

            // then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getSellerId()).isEqualTo(original.getSellerId());
            assertThat(converted.getAddressType()).isEqualTo(original.getAddressType());
            assertThat(converted.getAddressName()).isEqualTo(original.getAddressName());
            assertThat(converted.isDefaultAddress()).isEqualTo(original.isDefaultAddress());
        }
    }
}
