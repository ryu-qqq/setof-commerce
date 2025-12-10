package com.ryuqq.setof.adapter.out.persistence.shippingaddress.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.MapperTestSupport;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.ShippingAddressJpaEntity;
import com.ryuqq.setof.domain.shippingaddress.ShippingAddressFixture;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ShippingAddressJpaEntityMapper 단위 테스트
 *
 * <p>ShippingAddress Domain ↔ ShippingAddressJpaEntity 간의 변환 로직을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("ShippingAddressJpaEntityMapper 단위 테스트")
class ShippingAddressJpaEntityMapperTest extends MapperTestSupport {

    private ShippingAddressJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ShippingAddressJpaEntityMapper();
    }

    @Nested
    @DisplayName("toEntity 메서드")
    class ToEntity {

        @Test
        @DisplayName("성공 - ShippingAddress 도메인을 Entity로 변환한다")
        void toEntity_success() {
            // Given
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(1L);

            // When
            ShippingAddressJpaEntity entity = mapper.toEntity(shippingAddress);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(shippingAddress.getIdValue());
            assertThat(entity.getMemberId()).isEqualTo(shippingAddress.getMemberId().toString());
            assertThat(entity.getAddressName()).isEqualTo(shippingAddress.getAddressNameValue());
            assertThat(entity.getReceiverName()).isEqualTo(shippingAddress.getReceiverNameValue());
            assertThat(entity.getReceiverPhone())
                    .isEqualTo(shippingAddress.getReceiverPhoneValue());
            assertThat(entity.getZipCode()).isEqualTo(shippingAddress.getZipCodeValue());
            assertThat(entity.getRoadAddress()).isEqualTo(shippingAddress.getRoadAddressValue());
            assertThat(entity.getJibunAddress()).isEqualTo(shippingAddress.getJibunAddressValue());
            assertThat(entity.getDetailAddress())
                    .isEqualTo(shippingAddress.getDetailAddressValue());
            assertThat(entity.getDeliveryRequest())
                    .isEqualTo(shippingAddress.getDeliveryRequestValue());
            assertThat(entity.isDefault()).isEqualTo(shippingAddress.isDefault());
        }

        @Test
        @DisplayName("성공 - 기본 배송지가 아닌 도메인을 Entity로 변환한다")
        void toEntity_nonDefault_success() {
            // Given
            ShippingAddress nonDefault = ShippingAddressFixture.createNonDefault(2L);

            // When
            ShippingAddressJpaEntity entity = mapper.toEntity(nonDefault);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.isDefault()).isFalse();
            assertThat(entity.getDeliveryRequest()).isNull();
        }

        @Test
        @DisplayName("성공 - 삭제된 도메인을 Entity로 변환한다")
        void toEntity_deleted_success() {
            // Given
            ShippingAddress deleted = ShippingAddressFixture.createDeleted(3L);

            // When
            ShippingAddressJpaEntity entity = mapper.toEntity(deleted);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getDeletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("toDomain 메서드")
    class ToDomain {

        @Test
        @DisplayName("성공 - Entity를 ShippingAddress 도메인으로 변환한다")
        void toDomain_success() {
            // Given
            Instant now = Instant.now();
            UUID memberId = UUID.randomUUID();
            ShippingAddressJpaEntity entity =
                    ShippingAddressJpaEntity.of(
                            1L,
                            memberId.toString(),
                            "집",
                            "홍길동",
                            "01012345678",
                            "06234",
                            "서울시 강남구 테헤란로 123",
                            "서울시 강남구 역삼동 123-45",
                            "101동 1001호",
                            "문 앞에 놔주세요",
                            true,
                            now,
                            now,
                            null);

            // When
            ShippingAddress domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getIdValue()).isEqualTo(entity.getId());
            assertThat(domain.getMemberId()).isEqualTo(memberId);
            assertThat(domain.getAddressNameValue()).isEqualTo(entity.getAddressName());
            assertThat(domain.getReceiverNameValue()).isEqualTo(entity.getReceiverName());
            assertThat(domain.getReceiverPhoneValue()).isEqualTo(entity.getReceiverPhone());
            assertThat(domain.getZipCodeValue()).isEqualTo(entity.getZipCode());
            assertThat(domain.getRoadAddressValue()).isEqualTo(entity.getRoadAddress());
            assertThat(domain.getJibunAddressValue()).isEqualTo(entity.getJibunAddress());
            assertThat(domain.getDetailAddressValue()).isEqualTo(entity.getDetailAddress());
            assertThat(domain.getDeliveryRequestValue()).isEqualTo(entity.getDeliveryRequest());
            assertThat(domain.isDefault()).isEqualTo(entity.isDefault());
        }

        @Test
        @DisplayName("성공 - 배송 요청사항이 없는 Entity를 도메인으로 변환한다")
        void toDomain_withoutDeliveryRequest_success() {
            // Given
            Instant now = Instant.now();
            UUID memberId = UUID.randomUUID();
            ShippingAddressJpaEntity entity =
                    ShippingAddressJpaEntity.of(
                            2L,
                            memberId.toString(),
                            "회사",
                            "홍길동",
                            "01012345678",
                            "06789",
                            "서울시 서초구 서초대로 456",
                            null,
                            "A동 501호",
                            null,
                            false,
                            now,
                            now,
                            null);

            // When
            ShippingAddress domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getDeliveryRequestValue()).isNull();
            assertThat(domain.getJibunAddressValue()).isNull();
            assertThat(domain.isDefault()).isFalse();
        }

        @Test
        @DisplayName("성공 - 삭제된 Entity를 도메인으로 변환한다")
        void toDomain_deleted_success() {
            // Given
            Instant now = Instant.now();
            UUID memberId = UUID.randomUUID();
            ShippingAddressJpaEntity entity =
                    ShippingAddressJpaEntity.of(
                            3L,
                            memberId.toString(),
                            "이전 집",
                            "홍길동",
                            "01012345678",
                            "04001",
                            "서울시 마포구 마포대로 789",
                            null,
                            "203호",
                            null,
                            false,
                            now,
                            now,
                            now);

            // When
            ShippingAddress domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getDeletedAt()).isNotNull();
            assertThat(domain.isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("양방향 변환 검증")
    class RoundTrip {

        @Test
        @DisplayName("성공 - Domain -> Entity -> Domain 변환 시 데이터가 보존된다")
        void roundTrip_domainToEntityToDomain_preservesData() {
            // Given
            ShippingAddress original = ShippingAddressFixture.createWithId(1L);

            // When
            ShippingAddressJpaEntity entity = mapper.toEntity(original);
            ShippingAddress converted = mapper.toDomain(entity);

            // Then
            assertThat(converted.getIdValue()).isEqualTo(original.getIdValue());
            assertThat(converted.getMemberId()).isEqualTo(original.getMemberId());
            assertThat(converted.getAddressNameValue()).isEqualTo(original.getAddressNameValue());
            assertThat(converted.getReceiverNameValue()).isEqualTo(original.getReceiverNameValue());
            assertThat(converted.getReceiverPhoneValue())
                    .isEqualTo(original.getReceiverPhoneValue());
            assertThat(converted.getZipCodeValue()).isEqualTo(original.getZipCodeValue());
            assertThat(converted.getRoadAddressValue()).isEqualTo(original.getRoadAddressValue());
            assertThat(converted.getJibunAddressValue()).isEqualTo(original.getJibunAddressValue());
            assertThat(converted.getDetailAddressValue())
                    .isEqualTo(original.getDetailAddressValue());
            assertThat(converted.getDeliveryRequestValue())
                    .isEqualTo(original.getDeliveryRequestValue());
            assertThat(converted.isDefault()).isEqualTo(original.isDefault());
        }

        @Test
        @DisplayName("성공 - Entity -> Domain -> Entity 변환 시 데이터가 보존된다")
        void roundTrip_entityToDomainToEntity_preservesData() {
            // Given
            Instant now = Instant.now();
            UUID memberId = UUID.randomUUID();
            ShippingAddressJpaEntity original =
                    ShippingAddressJpaEntity.of(
                            5L,
                            memberId.toString(),
                            "집",
                            "김철수",
                            "01087654321",
                            "12345",
                            "서울시 송파구 올림픽로 300",
                            "서울시 송파구 잠실동 300-1",
                            "202동 505호",
                            "경비실에 맡겨주세요",
                            true,
                            now,
                            now,
                            null);

            // When
            ShippingAddress domain = mapper.toDomain(original);
            ShippingAddressJpaEntity converted = mapper.toEntity(domain);

            // Then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getMemberId()).isEqualTo(original.getMemberId());
            assertThat(converted.getAddressName()).isEqualTo(original.getAddressName());
            assertThat(converted.getReceiverName()).isEqualTo(original.getReceiverName());
            assertThat(converted.getReceiverPhone()).isEqualTo(original.getReceiverPhone());
            assertThat(converted.getZipCode()).isEqualTo(original.getZipCode());
            assertThat(converted.getRoadAddress()).isEqualTo(original.getRoadAddress());
            assertThat(converted.getJibunAddress()).isEqualTo(original.getJibunAddress());
            assertThat(converted.getDetailAddress()).isEqualTo(original.getDetailAddress());
            assertThat(converted.getDeliveryRequest()).isEqualTo(original.getDeliveryRequest());
            assertThat(converted.isDefault()).isEqualTo(original.isDefault());
        }
    }
}
