package com.ryuqq.setof.adapter.out.persistence.refundpolicy.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.MapperTestSupport;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.entity.RefundPolicyJpaEntity;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundDeliveryCost;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundGuide;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPeriodDays;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import com.ryuqq.setof.domain.refundpolicy.vo.ReturnAddress;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * RefundPolicyJpaEntityMapper 단위 테스트
 *
 * <p>RefundPolicy Domain ↔ RefundPolicyJpaEntity 간의 변환 로직을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("RefundPolicyJpaEntityMapper 단위 테스트")
class RefundPolicyJpaEntityMapperTest extends MapperTestSupport {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long TEST_SELLER_ID = 1L;

    private RefundPolicyJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RefundPolicyJpaEntityMapper();
    }

    @Nested
    @DisplayName("toEntity 메서드")
    class ToEntity {

        @Test
        @DisplayName("성공 - RefundPolicy 도메인을 Entity로 변환한다")
        void toEntity_success() {
            // Given
            RefundPolicy refundPolicy = createDefaultRefundPolicy();

            // When
            RefundPolicyJpaEntity entity = mapper.toEntity(refundPolicy);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(refundPolicy.getIdValue());
            assertThat(entity.getSellerId()).isEqualTo(refundPolicy.getSellerId());
            assertThat(entity.getPolicyName()).isEqualTo(refundPolicy.getPolicyNameValue());
            assertThat(entity.getReturnAddressLine1())
                    .isEqualTo(refundPolicy.getReturnAddressLine1());
            assertThat(entity.getReturnAddressLine2())
                    .isEqualTo(refundPolicy.getReturnAddressLine2());
            assertThat(entity.getReturnZipCode()).isEqualTo(refundPolicy.getReturnZipCode());
            assertThat(entity.getRefundPeriodDays())
                    .isEqualTo(refundPolicy.getRefundPeriodDaysValue());
            assertThat(entity.getRefundDeliveryCost())
                    .isEqualTo(refundPolicy.getRefundDeliveryCostValue());
            assertThat(entity.getRefundGuide()).isEqualTo(refundPolicy.getRefundGuideValue());
            assertThat(entity.getIsDefault()).isEqualTo(refundPolicy.isDefault());
            assertThat(entity.getDisplayOrder()).isEqualTo(refundPolicy.getDisplayOrder());
        }

        @Test
        @DisplayName("성공 - 기본 정책이 아닌 도메인을 Entity로 변환한다")
        void toEntity_nonDefault_success() {
            // Given
            RefundPolicy nonDefault = createNonDefaultRefundPolicy();

            // When
            RefundPolicyJpaEntity entity = mapper.toEntity(nonDefault);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getIsDefault()).isFalse();
            assertThat(entity.getRefundGuide()).isNull();
        }

        @Test
        @DisplayName("성공 - 삭제된 도메인을 Entity로 변환한다")
        void toEntity_deleted_success() {
            // Given
            RefundPolicy deleted = createDeletedRefundPolicy();

            // When
            RefundPolicyJpaEntity entity = mapper.toEntity(deleted);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getDeletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("toDomain 메서드")
    class ToDomain {

        @Test
        @DisplayName("성공 - Entity를 RefundPolicy 도메인으로 변환한다")
        void toDomain_success() {
            // Given
            RefundPolicyJpaEntity entity =
                    RefundPolicyJpaEntity.of(
                            1L,
                            TEST_SELLER_ID,
                            "기본 환불",
                            "서울시 강남구 테헤란로 123",
                            "101동 1001호",
                            "06234",
                            7,
                            3000,
                            "상품 수령 후 7일 이내 환불 가능",
                            true,
                            1,
                            FIXED_TIME,
                            FIXED_TIME,
                            null);

            // When
            RefundPolicy domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getIdValue()).isEqualTo(entity.getId());
            assertThat(domain.getSellerId()).isEqualTo(entity.getSellerId());
            assertThat(domain.getPolicyNameValue()).isEqualTo(entity.getPolicyName());
            assertThat(domain.getReturnAddressLine1()).isEqualTo(entity.getReturnAddressLine1());
            assertThat(domain.getReturnAddressLine2()).isEqualTo(entity.getReturnAddressLine2());
            assertThat(domain.getReturnZipCode()).isEqualTo(entity.getReturnZipCode());
            assertThat(domain.getRefundPeriodDaysValue()).isEqualTo(entity.getRefundPeriodDays());
            assertThat(domain.getRefundDeliveryCostValue())
                    .isEqualTo(entity.getRefundDeliveryCost());
            assertThat(domain.getRefundGuideValue()).isEqualTo(entity.getRefundGuide());
            assertThat(domain.isDefault()).isEqualTo(entity.getIsDefault());
            assertThat(domain.getDisplayOrder()).isEqualTo(entity.getDisplayOrder());
        }

        @Test
        @DisplayName("성공 - 환불 안내가 없는 Entity를 도메인으로 변환한다")
        void toDomain_withoutRefundGuide_success() {
            // Given
            RefundPolicyJpaEntity entity =
                    RefundPolicyJpaEntity.of(
                            2L,
                            TEST_SELLER_ID,
                            "심플 환불",
                            "서울시 서초구 서초대로 456",
                            null,
                            "06789",
                            14,
                            5000,
                            null,
                            false,
                            2,
                            FIXED_TIME,
                            FIXED_TIME,
                            null);

            // When
            RefundPolicy domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getRefundGuideValue()).isNull();
            assertThat(domain.getReturnAddressLine2()).isNull();
            assertThat(domain.isDefault()).isFalse();
        }

        @Test
        @DisplayName("성공 - 삭제된 Entity를 도메인으로 변환한다")
        void toDomain_deleted_success() {
            // Given
            RefundPolicyJpaEntity entity =
                    RefundPolicyJpaEntity.of(
                            3L,
                            TEST_SELLER_ID,
                            "이전 환불",
                            "서울시 마포구 마포대로 789",
                            null,
                            "04001",
                            7,
                            3000,
                            null,
                            false,
                            3,
                            FIXED_TIME,
                            FIXED_TIME,
                            FIXED_TIME);

            // When
            RefundPolicy domain = mapper.toDomain(entity);

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
            RefundPolicy original = createDefaultRefundPolicy();

            // When
            RefundPolicyJpaEntity entity = mapper.toEntity(original);
            RefundPolicy converted = mapper.toDomain(entity);

            // Then
            assertThat(converted.getIdValue()).isEqualTo(original.getIdValue());
            assertThat(converted.getSellerId()).isEqualTo(original.getSellerId());
            assertThat(converted.getPolicyNameValue()).isEqualTo(original.getPolicyNameValue());
            assertThat(converted.getReturnAddressLine1())
                    .isEqualTo(original.getReturnAddressLine1());
            assertThat(converted.getReturnAddressLine2())
                    .isEqualTo(original.getReturnAddressLine2());
            assertThat(converted.getReturnZipCode()).isEqualTo(original.getReturnZipCode());
            assertThat(converted.getRefundPeriodDaysValue())
                    .isEqualTo(original.getRefundPeriodDaysValue());
            assertThat(converted.getRefundDeliveryCostValue())
                    .isEqualTo(original.getRefundDeliveryCostValue());
            assertThat(converted.getRefundGuideValue()).isEqualTo(original.getRefundGuideValue());
            assertThat(converted.isDefault()).isEqualTo(original.isDefault());
            assertThat(converted.getDisplayOrder()).isEqualTo(original.getDisplayOrder());
        }

        @Test
        @DisplayName("성공 - Entity -> Domain -> Entity 변환 시 데이터가 보존된다")
        void roundTrip_entityToDomainToEntity_preservesData() {
            // Given
            RefundPolicyJpaEntity original =
                    RefundPolicyJpaEntity.of(
                            5L,
                            TEST_SELLER_ID,
                            "라운드트립 환불",
                            "서울시 송파구 올림픽로 300",
                            "202동 505호",
                            "12345",
                            10,
                            4000,
                            "경비실에서 수령 가능",
                            true,
                            1,
                            FIXED_TIME,
                            FIXED_TIME,
                            null);

            // When
            RefundPolicy domain = mapper.toDomain(original);
            RefundPolicyJpaEntity converted = mapper.toEntity(domain);

            // Then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getSellerId()).isEqualTo(original.getSellerId());
            assertThat(converted.getPolicyName()).isEqualTo(original.getPolicyName());
            assertThat(converted.getReturnAddressLine1())
                    .isEqualTo(original.getReturnAddressLine1());
            assertThat(converted.getReturnAddressLine2())
                    .isEqualTo(original.getReturnAddressLine2());
            assertThat(converted.getReturnZipCode()).isEqualTo(original.getReturnZipCode());
            assertThat(converted.getRefundPeriodDays()).isEqualTo(original.getRefundPeriodDays());
            assertThat(converted.getRefundDeliveryCost())
                    .isEqualTo(original.getRefundDeliveryCost());
            assertThat(converted.getRefundGuide()).isEqualTo(original.getRefundGuide());
            assertThat(converted.getIsDefault()).isEqualTo(original.getIsDefault());
            assertThat(converted.getDisplayOrder()).isEqualTo(original.getDisplayOrder());
        }
    }

    // ========== Helper Methods ==========

    private RefundPolicy createDefaultRefundPolicy() {
        return RefundPolicy.reconstitute(
                RefundPolicyId.of(1L),
                TEST_SELLER_ID,
                PolicyName.of("기본 환불"),
                ReturnAddress.of("서울시 강남구 테헤란로 123", "101동 1001호", "06234"),
                RefundPeriodDays.of(7),
                RefundDeliveryCost.of(3000),
                RefundGuide.of("상품 수령 후 7일 이내 환불 가능"),
                true,
                1,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private RefundPolicy createNonDefaultRefundPolicy() {
        return RefundPolicy.reconstitute(
                RefundPolicyId.of(2L),
                TEST_SELLER_ID,
                PolicyName.of("추가 환불"),
                ReturnAddress.of("서울시 서초구 서초대로 456", null, "06789"),
                RefundPeriodDays.of(14),
                RefundDeliveryCost.of(5000),
                null,
                false,
                2,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private RefundPolicy createDeletedRefundPolicy() {
        return RefundPolicy.reconstitute(
                RefundPolicyId.of(3L),
                TEST_SELLER_ID,
                PolicyName.of("삭제된 환불"),
                ReturnAddress.of("서울시 마포구 마포대로 789", null, "04001"),
                RefundPeriodDays.of(7),
                RefundDeliveryCost.of(3000),
                null,
                false,
                3,
                FIXED_TIME,
                FIXED_TIME,
                FIXED_TIME);
    }
}
