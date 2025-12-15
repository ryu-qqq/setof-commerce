package com.ryuqq.setof.adapter.out.persistence.shippingaddress.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.ShippingAddressJpaEntity;
import com.ryuqq.setof.domain.shippingaddress.ShippingAddressFixture;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ShippingAddressPersistenceAdapter 통합 테스트
 *
 * <p>ShippingAddressPersistencePort 구현체의 저장 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("ShippingAddressPersistenceAdapter 통합 테스트")
class ShippingAddressPersistenceAdapterTest extends RepositoryTestSupport {

    @Autowired private ShippingAddressPersistenceAdapter shippingAddressPersistenceAdapter;

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");
    private static final UUID MEMBER_ID = UUID.fromString("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");

    @Nested
    @DisplayName("persist 메서드")
    class Persist {

        @Test
        @DisplayName("성공 - 새 배송지를 저장하고 ID를 반환한다")
        void persist_newShippingAddress_savesAndReturnsId() {
            // Given
            ShippingAddress newAddress = ShippingAddressFixture.createNew();

            // When
            ShippingAddressId savedId = shippingAddressPersistenceAdapter.persist(newAddress);
            flushAndClear();

            // Then
            assertThat(savedId).isNotNull();
            assertThat(savedId.value()).isNotNull();

            ShippingAddressJpaEntity found = find(ShippingAddressJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getAddressName()).isEqualTo("집");
            assertThat(found.getReceiverName()).isEqualTo("홍길동");
            assertThat(found.isDefault()).isTrue();
        }

        @Test
        @DisplayName("성공 - 기존 배송지를 수정한다")
        void persist_existingShippingAddress_updates() {
            // Given
            ShippingAddressJpaEntity existingEntity =
                    persistAndFlush(
                            ShippingAddressJpaEntity.of(
                                    null,
                                    MEMBER_ID.toString(),
                                    "집",
                                    "홍길동",
                                    "01012345678",
                                    "06234",
                                    "서울시 강남구 테헤란로 123",
                                    null,
                                    "101동 1001호",
                                    null,
                                    true,
                                    NOW,
                                    NOW,
                                    null));
            flushAndClear();

            ShippingAddress updatedAddress =
                    ShippingAddressFixture.createForMember(existingEntity.getId(), MEMBER_ID);

            // When
            ShippingAddressId savedId = shippingAddressPersistenceAdapter.persist(updatedAddress);
            flushAndClear();

            // Then
            assertThat(savedId.value()).isEqualTo(existingEntity.getId());
            ShippingAddressJpaEntity found = find(ShippingAddressJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
        }

        @Test
        @DisplayName("성공 - 기본 배송지가 아닌 배송지를 저장한다")
        void persist_nonDefaultShippingAddress_saves() {
            // Given
            ShippingAddress nonDefaultAddress = ShippingAddressFixture.createNonDefaultNew();

            // When
            ShippingAddressId savedId =
                    shippingAddressPersistenceAdapter.persist(nonDefaultAddress);
            flushAndClear();

            // Then
            ShippingAddressJpaEntity found = find(ShippingAddressJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.isDefault()).isFalse();
            assertThat(found.getAddressName()).isEqualTo("회사");
        }

        @Test
        @DisplayName("성공 - 삭제된 배송지를 저장한다 (soft delete)")
        void persist_deletedShippingAddress_savesSoftDeleted() {
            // Given
            ShippingAddress deletedAddress = ShippingAddressFixture.createDeletedNew();

            // When
            ShippingAddressId savedId = shippingAddressPersistenceAdapter.persist(deletedAddress);
            flushAndClear();

            // Then
            ShippingAddressJpaEntity found = find(ShippingAddressJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getDeletedAt()).isNotNull();
        }
    }
}
