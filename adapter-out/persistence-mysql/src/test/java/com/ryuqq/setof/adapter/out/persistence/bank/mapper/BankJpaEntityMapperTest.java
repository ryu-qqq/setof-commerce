package com.ryuqq.setof.adapter.out.persistence.bank.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.bank.entity.BankJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.common.MapperTestSupport;
import com.ryuqq.setof.domain.bank.BankFixture;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * BankJpaEntityMapper 단위 테스트
 *
 * <p>Bank Domain ↔ BankJpaEntity 간의 변환 로직을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("BankJpaEntityMapper 단위 테스트")
class BankJpaEntityMapperTest extends MapperTestSupport {

    private BankJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BankJpaEntityMapper();
    }

    @Nested
    @DisplayName("toEntity 메서드")
    class ToEntity {

        @Test
        @DisplayName("성공 - Bank 도메인을 Entity로 변환한다")
        void toEntity_success() {
            // Given
            Bank bank = BankFixture.create();

            // When
            BankJpaEntity entity = mapper.toEntity(bank);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(bank.getIdValue());
            assertThat(entity.getBankCode()).isEqualTo(bank.getBankCodeValue());
            assertThat(entity.getBankName()).isEqualTo(bank.getBankNameValue());
            assertThat(entity.isActive()).isEqualTo(bank.isActive());
            assertThat(entity.getDisplayOrder()).isEqualTo(bank.getDisplayOrder());
            assertThat(entity.getCreatedAt()).isEqualTo(bank.getCreatedAt());
            assertThat(entity.getUpdatedAt()).isEqualTo(bank.getUpdatedAt());
        }

        @Test
        @DisplayName("성공 - 비활성 Bank를 Entity로 변환한다")
        void toEntity_inactiveBank_success() {
            // Given
            Bank inactiveBank = BankFixture.createInactive();

            // When
            BankJpaEntity entity = mapper.toEntity(inactiveBank);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.isActive()).isFalse();
            assertThat(entity.getId()).isEqualTo(inactiveBank.getIdValue());
        }

        @Test
        @DisplayName("성공 - 커스텀 Bank를 Entity로 변환한다")
        void toEntity_customBank_success() {
            // Given
            Bank customBank = BankFixture.createCustom(100L, "999", "테스트은행", 50, true);

            // When
            BankJpaEntity entity = mapper.toEntity(customBank);

            // Then
            assertThat(entity.getId()).isEqualTo(100L);
            assertThat(entity.getBankCode()).isEqualTo("999");
            assertThat(entity.getBankName()).isEqualTo("테스트은행");
            assertThat(entity.getDisplayOrder()).isEqualTo(50);
            assertThat(entity.isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("toDomain 메서드")
    class ToDomain {

        @Test
        @DisplayName("성공 - Entity를 Bank 도메인으로 변환한다")
        void toDomain_success() {
            // Given
            Instant now = Instant.now();
            BankJpaEntity entity =
                    BankJpaEntity.of(1L, "004", "KB국민은행", true, 1, now, now);

            // When
            Bank domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getIdValue()).isEqualTo(entity.getId());
            assertThat(domain.getBankCodeValue()).isEqualTo(entity.getBankCode());
            assertThat(domain.getBankNameValue()).isEqualTo(entity.getBankName());
            assertThat(domain.isActive()).isEqualTo(entity.isActive());
            assertThat(domain.getDisplayOrder()).isEqualTo(entity.getDisplayOrder());
            assertThat(domain.getCreatedAt()).isEqualTo(entity.getCreatedAt());
            assertThat(domain.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
        }

        @Test
        @DisplayName("성공 - 비활성 Entity를 도메인으로 변환한다")
        void toDomain_inactiveEntity_success() {
            // Given
            Instant now = Instant.now();
            BankJpaEntity inactiveEntity =
                    BankJpaEntity.of(99L, "999", "테스트은행", false, 99, now, now);

            // When
            Bank domain = mapper.toDomain(inactiveEntity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.isActive()).isFalse();
            assertThat(domain.getIdValue()).isEqualTo(99L);
        }
    }

    @Nested
    @DisplayName("양방향 변환 검증")
    class RoundTrip {

        @Test
        @DisplayName("성공 - Domain -> Entity -> Domain 변환 시 데이터가 보존된다")
        void roundTrip_domainToEntityToDomain_preservesData() {
            // Given
            Bank original = BankFixture.create();

            // When
            BankJpaEntity entity = mapper.toEntity(original);
            Bank converted = mapper.toDomain(entity);

            // Then
            assertThat(converted.getIdValue()).isEqualTo(original.getIdValue());
            assertThat(converted.getBankCodeValue()).isEqualTo(original.getBankCodeValue());
            assertThat(converted.getBankNameValue()).isEqualTo(original.getBankNameValue());
            assertThat(converted.isActive()).isEqualTo(original.isActive());
            assertThat(converted.getDisplayOrder()).isEqualTo(original.getDisplayOrder());
            assertThat(converted.getCreatedAt()).isEqualTo(original.getCreatedAt());
            assertThat(converted.getUpdatedAt()).isEqualTo(original.getUpdatedAt());
        }

        @Test
        @DisplayName("성공 - Entity -> Domain -> Entity 변환 시 데이터가 보존된다")
        void roundTrip_entityToDomainToEntity_preservesData() {
            // Given
            Instant now = Instant.now();
            BankJpaEntity original = BankJpaEntity.of(5L, "088", "신한은행", true, 2, now, now);

            // When
            Bank domain = mapper.toDomain(original);
            BankJpaEntity converted = mapper.toEntity(domain);

            // Then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getBankCode()).isEqualTo(original.getBankCode());
            assertThat(converted.getBankName()).isEqualTo(original.getBankName());
            assertThat(converted.isActive()).isEqualTo(original.isActive());
            assertThat(converted.getDisplayOrder()).isEqualTo(original.getDisplayOrder());
            assertThat(converted.getCreatedAt()).isEqualTo(original.getCreatedAt());
            assertThat(converted.getUpdatedAt()).isEqualTo(original.getUpdatedAt());
        }
    }
}
