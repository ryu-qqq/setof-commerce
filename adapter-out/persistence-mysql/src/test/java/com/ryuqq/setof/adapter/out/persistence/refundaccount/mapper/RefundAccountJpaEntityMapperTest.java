package com.ryuqq.setof.adapter.out.persistence.refundaccount.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.MapperTestSupport;
import com.ryuqq.setof.adapter.out.persistence.refundaccount.entity.RefundAccountJpaEntity;
import com.ryuqq.setof.domain.refundaccount.RefundAccountFixture;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * RefundAccountJpaEntityMapper 단위 테스트
 *
 * <p>RefundAccount Domain ↔ RefundAccountJpaEntity 간의 변환 로직을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("RefundAccountJpaEntityMapper 단위 테스트")
class RefundAccountJpaEntityMapperTest extends MapperTestSupport {

    private RefundAccountJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RefundAccountJpaEntityMapper();
    }

    @Nested
    @DisplayName("toEntity 메서드")
    class ToEntity {

        @Test
        @DisplayName("성공 - RefundAccount 도메인을 Entity로 변환한다")
        void toEntity_success() {
            // Given
            RefundAccount refundAccount = RefundAccountFixture.createWithId(1L);

            // When
            RefundAccountJpaEntity entity = mapper.toEntity(refundAccount);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(refundAccount.getIdValue());
            assertThat(entity.getMemberId()).isEqualTo(refundAccount.getMemberId().toString());
            assertThat(entity.getBankId()).isEqualTo(refundAccount.getBankId());
            assertThat(entity.getAccountNumber()).isEqualTo(refundAccount.getAccountNumberValue());
            assertThat(entity.getAccountHolderName())
                    .isEqualTo(refundAccount.getAccountHolderNameValue());
            assertThat(entity.isVerified()).isEqualTo(refundAccount.isVerified());
        }

        @Test
        @DisplayName("성공 - 미검증 도메인을 Entity로 변환한다")
        void toEntity_unverified_success() {
            // Given
            RefundAccount unverified = RefundAccountFixture.createUnverifiedWithId(2L);

            // When
            RefundAccountJpaEntity entity = mapper.toEntity(unverified);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.isVerified()).isFalse();
            assertThat(entity.getVerifiedAt()).isNull();
        }

        @Test
        @DisplayName("성공 - 검증 완료된 도메인을 Entity로 변환한다")
        void toEntity_verified_success() {
            // Given
            RefundAccount verified = RefundAccountFixture.createWithId(3L);

            // When
            RefundAccountJpaEntity entity = mapper.toEntity(verified);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.isVerified()).isTrue();
            assertThat(entity.getVerifiedAt()).isNotNull();
        }

        @Test
        @DisplayName("성공 - 삭제된 도메인을 Entity로 변환한다")
        void toEntity_deleted_success() {
            // Given
            RefundAccount deleted = RefundAccountFixture.createDeleted(4L);

            // When
            RefundAccountJpaEntity entity = mapper.toEntity(deleted);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getDeletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("toDomain 메서드")
    class ToDomain {

        @Test
        @DisplayName("성공 - Entity를 RefundAccount 도메인으로 변환한다")
        void toDomain_success() {
            // Given
            Instant now = Instant.now();
            UUID memberId = UUID.randomUUID();
            RefundAccountJpaEntity entity =
                    RefundAccountJpaEntity.of(
                            1L,
                            memberId.toString(),
                            1L,
                            "1234567890123",
                            "홍길동",
                            true,
                            now,
                            now,
                            now,
                            null);

            // When
            RefundAccount domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getIdValue()).isEqualTo(entity.getId());
            assertThat(domain.getMemberId()).isEqualTo(memberId);
            assertThat(domain.getBankId()).isEqualTo(entity.getBankId());
            assertThat(domain.getAccountNumberValue()).isEqualTo(entity.getAccountNumber());
            assertThat(domain.getAccountHolderNameValue()).isEqualTo(entity.getAccountHolderName());
            assertThat(domain.isVerified()).isEqualTo(entity.isVerified());
        }

        @Test
        @DisplayName("성공 - 미검증 Entity를 도메인으로 변환한다")
        void toDomain_unverified_success() {
            // Given
            Instant now = Instant.now();
            UUID memberId = UUID.randomUUID();
            RefundAccountJpaEntity entity =
                    RefundAccountJpaEntity.of(
                            2L,
                            memberId.toString(),
                            2L,
                            "9876543210987",
                            "김철수",
                            false,
                            null,
                            now,
                            now,
                            null);

            // When
            RefundAccount domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.isVerified()).isFalse();
        }

        @Test
        @DisplayName("성공 - 삭제된 Entity를 도메인으로 변환한다")
        void toDomain_deleted_success() {
            // Given
            Instant now = Instant.now();
            UUID memberId = UUID.randomUUID();
            RefundAccountJpaEntity entity =
                    RefundAccountJpaEntity.of(
                            3L,
                            memberId.toString(),
                            1L,
                            "1234567890123",
                            "홍길동",
                            true,
                            now,
                            now,
                            now,
                            now);

            // When
            RefundAccount domain = mapper.toDomain(entity);

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
            RefundAccount original = RefundAccountFixture.createWithId(1L);

            // When
            RefundAccountJpaEntity entity = mapper.toEntity(original);
            RefundAccount converted = mapper.toDomain(entity);

            // Then
            assertThat(converted.getIdValue()).isEqualTo(original.getIdValue());
            assertThat(converted.getMemberId()).isEqualTo(original.getMemberId());
            assertThat(converted.getBankId()).isEqualTo(original.getBankId());
            assertThat(converted.getAccountNumberValue())
                    .isEqualTo(original.getAccountNumberValue());
            assertThat(converted.getAccountHolderNameValue())
                    .isEqualTo(original.getAccountHolderNameValue());
            assertThat(converted.isVerified()).isEqualTo(original.isVerified());
        }

        @Test
        @DisplayName("성공 - Entity -> Domain -> Entity 변환 시 데이터가 보존된다")
        void roundTrip_entityToDomainToEntity_preservesData() {
            // Given
            Instant now = Instant.now();
            UUID memberId = UUID.randomUUID();
            RefundAccountJpaEntity original =
                    RefundAccountJpaEntity.of(
                            5L,
                            memberId.toString(),
                            3L,
                            "5555666677778888",
                            "박영희",
                            true,
                            now,
                            now,
                            now,
                            null);

            // When
            RefundAccount domain = mapper.toDomain(original);
            RefundAccountJpaEntity converted = mapper.toEntity(domain);

            // Then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getMemberId()).isEqualTo(original.getMemberId());
            assertThat(converted.getBankId()).isEqualTo(original.getBankId());
            assertThat(converted.getAccountNumber()).isEqualTo(original.getAccountNumber());
            assertThat(converted.getAccountHolderName()).isEqualTo(original.getAccountHolderName());
            assertThat(converted.isVerified()).isEqualTo(original.isVerified());
        }
    }
}
