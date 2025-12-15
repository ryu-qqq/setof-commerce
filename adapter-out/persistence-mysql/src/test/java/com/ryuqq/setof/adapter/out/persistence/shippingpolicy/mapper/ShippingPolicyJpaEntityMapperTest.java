package com.ryuqq.setof.adapter.out.persistence.shippingpolicy.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.MapperTestSupport;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.entity.ShippingPolicyJpaEntity;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryCost;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryGuide;
import com.ryuqq.setof.domain.shippingpolicy.vo.DisplayOrder;
import com.ryuqq.setof.domain.shippingpolicy.vo.FreeShippingThreshold;
import com.ryuqq.setof.domain.shippingpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ShippingPolicyJpaEntityMapper 단위 테스트
 *
 * <p>ShippingPolicy Domain ↔ ShippingPolicyJpaEntity 간의 변환 로직을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("ShippingPolicyJpaEntityMapper 단위 테스트")
class ShippingPolicyJpaEntityMapperTest extends MapperTestSupport {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long TEST_SELLER_ID = 1L;

    private ShippingPolicyJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ShippingPolicyJpaEntityMapper();
    }

    @Nested
    @DisplayName("toEntity 메서드")
    class ToEntity {

        @Test
        @DisplayName("성공 - ShippingPolicy 도메인을 Entity로 변환한다")
        void toEntity_success() {
            // Given
            ShippingPolicy shippingPolicy = createDefaultShippingPolicy();

            // When
            ShippingPolicyJpaEntity entity = mapper.toEntity(shippingPolicy);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(shippingPolicy.getIdValue());
            assertThat(entity.getSellerId()).isEqualTo(shippingPolicy.getSellerId());
            assertThat(entity.getPolicyName()).isEqualTo(shippingPolicy.getPolicyNameValue());
            assertThat(entity.getDefaultDeliveryCost())
                    .isEqualTo(shippingPolicy.getDefaultDeliveryCostValue());
            assertThat(entity.getFreeShippingThreshold())
                    .isEqualTo(shippingPolicy.getFreeShippingThresholdValue());
            assertThat(entity.getDeliveryGuide()).isEqualTo(shippingPolicy.getDeliveryGuideValue());
            assertThat(entity.getIsDefault()).isEqualTo(shippingPolicy.isDefault());
            assertThat(entity.getDisplayOrder()).isEqualTo(shippingPolicy.getDisplayOrderValue());
        }

        @Test
        @DisplayName("성공 - 기본 정책이 아닌 도메인을 Entity로 변환한다")
        void toEntity_nonDefault_success() {
            // Given
            ShippingPolicy nonDefault = createNonDefaultShippingPolicy();

            // When
            ShippingPolicyJpaEntity entity = mapper.toEntity(nonDefault);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getIsDefault()).isFalse();
            assertThat(entity.getDeliveryGuide()).isNull();
            assertThat(entity.getFreeShippingThreshold()).isNull();
        }

        @Test
        @DisplayName("성공 - 삭제된 도메인을 Entity로 변환한다")
        void toEntity_deleted_success() {
            // Given
            ShippingPolicy deleted = createDeletedShippingPolicy();

            // When
            ShippingPolicyJpaEntity entity = mapper.toEntity(deleted);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getDeletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("toDomain 메서드")
    class ToDomain {

        @Test
        @DisplayName("성공 - Entity를 ShippingPolicy 도메인으로 변환한다")
        void toDomain_success() {
            // Given
            ShippingPolicyJpaEntity entity =
                    ShippingPolicyJpaEntity.of(
                            1L,
                            TEST_SELLER_ID,
                            "기본 배송",
                            3000,
                            50000,
                            "평일 1~2일 내 발송",
                            true,
                            1,
                            FIXED_TIME,
                            FIXED_TIME,
                            null);

            // When
            ShippingPolicy domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getIdValue()).isEqualTo(entity.getId());
            assertThat(domain.getSellerId()).isEqualTo(entity.getSellerId());
            assertThat(domain.getPolicyNameValue()).isEqualTo(entity.getPolicyName());
            assertThat(domain.getDefaultDeliveryCostValue())
                    .isEqualTo(entity.getDefaultDeliveryCost());
            assertThat(domain.getFreeShippingThresholdValue())
                    .isEqualTo(entity.getFreeShippingThreshold());
            assertThat(domain.getDeliveryGuideValue()).isEqualTo(entity.getDeliveryGuide());
            assertThat(domain.isDefault()).isEqualTo(entity.getIsDefault());
            assertThat(domain.getDisplayOrderValue()).isEqualTo(entity.getDisplayOrder());
        }

        @Test
        @DisplayName("성공 - 무료배송 기준금액이 없는 Entity를 도메인으로 변환한다")
        void toDomain_withoutFreeShippingThreshold_success() {
            // Given
            ShippingPolicyJpaEntity entity =
                    ShippingPolicyJpaEntity.of(
                            2L,
                            TEST_SELLER_ID,
                            "유료 배송",
                            5000,
                            null,
                            null,
                            false,
                            2,
                            FIXED_TIME,
                            FIXED_TIME,
                            null);

            // When
            ShippingPolicy domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getFreeShippingThresholdValue()).isNull();
            assertThat(domain.getDeliveryGuideValue()).isNull();
            assertThat(domain.isDefault()).isFalse();
        }

        @Test
        @DisplayName("성공 - 삭제된 Entity를 도메인으로 변환한다")
        void toDomain_deleted_success() {
            // Given
            ShippingPolicyJpaEntity entity =
                    ShippingPolicyJpaEntity.of(
                            3L,
                            TEST_SELLER_ID,
                            "이전 배송",
                            3000,
                            null,
                            null,
                            false,
                            3,
                            FIXED_TIME,
                            FIXED_TIME,
                            FIXED_TIME);

            // When
            ShippingPolicy domain = mapper.toDomain(entity);

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
            ShippingPolicy original = createDefaultShippingPolicy();

            // When
            ShippingPolicyJpaEntity entity = mapper.toEntity(original);
            ShippingPolicy converted = mapper.toDomain(entity);

            // Then
            assertThat(converted.getIdValue()).isEqualTo(original.getIdValue());
            assertThat(converted.getSellerId()).isEqualTo(original.getSellerId());
            assertThat(converted.getPolicyNameValue()).isEqualTo(original.getPolicyNameValue());
            assertThat(converted.getDefaultDeliveryCostValue())
                    .isEqualTo(original.getDefaultDeliveryCostValue());
            assertThat(converted.getFreeShippingThresholdValue())
                    .isEqualTo(original.getFreeShippingThresholdValue());
            assertThat(converted.getDeliveryGuideValue())
                    .isEqualTo(original.getDeliveryGuideValue());
            assertThat(converted.isDefault()).isEqualTo(original.isDefault());
            assertThat(converted.getDisplayOrderValue()).isEqualTo(original.getDisplayOrderValue());
        }

        @Test
        @DisplayName("성공 - Entity -> Domain -> Entity 변환 시 데이터가 보존된다")
        void roundTrip_entityToDomainToEntity_preservesData() {
            // Given
            ShippingPolicyJpaEntity original =
                    ShippingPolicyJpaEntity.of(
                            5L,
                            TEST_SELLER_ID,
                            "라운드트립 배송",
                            3500,
                            70000,
                            "빠른 배송 가능",
                            true,
                            1,
                            FIXED_TIME,
                            FIXED_TIME,
                            null);

            // When
            ShippingPolicy domain = mapper.toDomain(original);
            ShippingPolicyJpaEntity converted = mapper.toEntity(domain);

            // Then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getSellerId()).isEqualTo(original.getSellerId());
            assertThat(converted.getPolicyName()).isEqualTo(original.getPolicyName());
            assertThat(converted.getDefaultDeliveryCost())
                    .isEqualTo(original.getDefaultDeliveryCost());
            assertThat(converted.getFreeShippingThreshold())
                    .isEqualTo(original.getFreeShippingThreshold());
            assertThat(converted.getDeliveryGuide()).isEqualTo(original.getDeliveryGuide());
            assertThat(converted.getIsDefault()).isEqualTo(original.getIsDefault());
            assertThat(converted.getDisplayOrder()).isEqualTo(original.getDisplayOrder());
        }
    }

    // ========== Helper Methods ==========

    private ShippingPolicy createDefaultShippingPolicy() {
        return ShippingPolicy.reconstitute(
                ShippingPolicyId.of(1L),
                TEST_SELLER_ID,
                PolicyName.of("기본 배송"),
                DeliveryCost.of(3000),
                FreeShippingThreshold.of(50000),
                DeliveryGuide.of("평일 1~2일 내 발송"),
                true,
                DisplayOrder.of(1),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private ShippingPolicy createNonDefaultShippingPolicy() {
        return ShippingPolicy.reconstitute(
                ShippingPolicyId.of(2L),
                TEST_SELLER_ID,
                PolicyName.of("추가 배송"),
                DeliveryCost.of(5000),
                null,
                null,
                false,
                DisplayOrder.of(2),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private ShippingPolicy createDeletedShippingPolicy() {
        return ShippingPolicy.reconstitute(
                ShippingPolicyId.of(3L),
                TEST_SELLER_ID,
                PolicyName.of("삭제된 배송"),
                DeliveryCost.of(3000),
                null,
                null,
                false,
                DisplayOrder.of(3),
                FIXED_TIME,
                FIXED_TIME,
                FIXED_TIME);
    }
}
