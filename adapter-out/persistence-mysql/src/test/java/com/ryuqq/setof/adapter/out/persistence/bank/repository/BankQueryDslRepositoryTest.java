package com.ryuqq.setof.adapter.out.persistence.bank.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.bank.entity.BankJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.common.JpaSliceTestSupport;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * BankQueryDslRepository Slice 테스트
 *
 * <p>QueryDSL 기반 Bank 조회 쿼리를 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("BankQueryDslRepository Slice 테스트")
@Import(BankQueryDslRepository.class)
class BankQueryDslRepositoryTest extends JpaSliceTestSupport {

    @Autowired private BankQueryDslRepository bankQueryDslRepository;

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");

    @Nested
    @DisplayName("findById 메서드")
    class FindById {

        private BankJpaEntity savedBank;

        @BeforeEach
        void setUp() {
            savedBank = persistAndFlush(BankJpaEntity.of(null, "004", "KB국민은행", true, 1, NOW, NOW));
        }

        @Test
        @DisplayName("성공 - ID로 Bank를 조회한다")
        void findById_existingId_returnsBank() {
            // When
            Optional<BankJpaEntity> result = bankQueryDslRepository.findById(savedBank.getId());

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getBankCode()).isEqualTo("004");
            assertThat(result.get().getBankName()).isEqualTo("KB국민은행");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_nonExistingId_returnsEmpty() {
            // When
            Optional<BankJpaEntity> result = bankQueryDslRepository.findById(9999L);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByBankCode 메서드")
    class FindByBankCode {

        @BeforeEach
        void setUp() {
            persistAndFlush(BankJpaEntity.of(null, "004", "KB국민은행", true, 1, NOW, NOW));
            persistAndFlush(BankJpaEntity.of(null, "088", "신한은행", true, 2, NOW, NOW));
        }

        @Test
        @DisplayName("성공 - 은행 코드로 Bank를 조회한다")
        void findByBankCode_existingCode_returnsBank() {
            // When
            Optional<BankJpaEntity> result = bankQueryDslRepository.findByBankCode("088");

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getBankName()).isEqualTo("신한은행");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 은행 코드로 조회 시 빈 Optional 반환")
        void findByBankCode_nonExistingCode_returnsEmpty() {
            // When
            Optional<BankJpaEntity> result = bankQueryDslRepository.findByBankCode("999");

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAllActive 메서드")
    class FindAllActive {

        @BeforeEach
        void setUp() {
            persistAndFlush(BankJpaEntity.of(null, "004", "KB국민은행", true, 2, NOW, NOW));
            persistAndFlush(BankJpaEntity.of(null, "088", "신한은행", true, 1, NOW, NOW));
            persistAndFlush(BankJpaEntity.of(null, "999", "테스트은행", false, 99, NOW, NOW));
        }

        @Test
        @DisplayName("성공 - 활성화된 Bank 목록만 조회한다")
        void findAllActive_returnsOnlyActiveBanks() {
            // When
            List<BankJpaEntity> result = bankQueryDslRepository.findAllActive();

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(BankJpaEntity::isActive);
        }

        @Test
        @DisplayName("성공 - displayOrder 순으로 정렬된다")
        void findAllActive_orderedByDisplayOrder() {
            // When
            List<BankJpaEntity> result = bankQueryDslRepository.findAllActive();

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getBankCode()).isEqualTo("088");
            assertThat(result.get(1).getBankCode()).isEqualTo("004");
        }
    }

    @Nested
    @DisplayName("existsById 메서드")
    class ExistsById {

        private BankJpaEntity savedBank;

        @BeforeEach
        void setUp() {
            savedBank = persistAndFlush(BankJpaEntity.of(null, "004", "KB국민은행", true, 1, NOW, NOW));
        }

        @Test
        @DisplayName("성공 - 존재하는 ID인 경우 true 반환")
        void existsById_existingId_returnsTrue() {
            // When
            boolean result = bankQueryDslRepository.existsById(savedBank.getId());

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID인 경우 false 반환")
        void existsById_nonExistingId_returnsFalse() {
            // When
            boolean result = bankQueryDslRepository.existsById(9999L);

            // Then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("existsActiveById 메서드")
    class ExistsActiveById {

        private BankJpaEntity activeBank;
        private BankJpaEntity inactiveBank;

        @BeforeEach
        void setUp() {
            activeBank =
                    persistAndFlush(BankJpaEntity.of(null, "004", "KB국민은행", true, 1, NOW, NOW));
            inactiveBank =
                    persistAndFlush(BankJpaEntity.of(null, "999", "테스트은행", false, 99, NOW, NOW));
        }

        @Test
        @DisplayName("성공 - 활성 Bank ID인 경우 true 반환")
        void existsActiveById_activeBankId_returnsTrue() {
            // When
            boolean result = bankQueryDslRepository.existsActiveById(activeBank.getId());

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("성공 - 비활성 Bank ID인 경우 false 반환")
        void existsActiveById_inactiveBankId_returnsFalse() {
            // When
            boolean result = bankQueryDslRepository.existsActiveById(inactiveBank.getId());

            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID인 경우 false 반환")
        void existsActiveById_nonExistingId_returnsFalse() {
            // When
            boolean result = bankQueryDslRepository.existsActiveById(9999L);

            // Then
            assertThat(result).isFalse();
        }
    }
}
