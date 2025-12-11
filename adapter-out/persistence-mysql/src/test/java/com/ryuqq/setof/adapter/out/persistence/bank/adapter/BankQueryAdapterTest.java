package com.ryuqq.setof.adapter.out.persistence.bank.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.bank.entity.BankJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.bank.vo.BankCode;
import com.ryuqq.setof.domain.bank.vo.BankId;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * BankQueryAdapter 통합 테스트
 *
 * <p>BankQueryPort 구현체의 Domain 변환 및 조회 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("BankQueryAdapter 통합 테스트")
class BankQueryAdapterTest extends RepositoryTestSupport {

    @Autowired private BankQueryAdapter bankQueryAdapter;

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
        @DisplayName("성공 - ID로 Bank 도메인을 조회한다")
        void findById_existingId_returnsBankDomain() {
            // When
            Optional<Bank> result = bankQueryAdapter.findById(BankId.of(savedBank.getId()));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getBankCodeValue()).isEqualTo("004");
            assertThat(result.get().getBankNameValue()).isEqualTo("KB국민은행");
            assertThat(result.get().isActive()).isTrue();
            assertThat(result.get().getDisplayOrder()).isEqualTo(1);
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_nonExistingId_returnsEmpty() {
            // When
            Optional<Bank> result = bankQueryAdapter.findById(BankId.of(9999L));

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
        @DisplayName("성공 - 은행 코드로 Bank 도메인을 조회한다")
        void findByBankCode_existingCode_returnsBankDomain() {
            // When
            Optional<Bank> result = bankQueryAdapter.findByBankCode(BankCode.of("088"));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getBankNameValue()).isEqualTo("신한은행");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 은행 코드로 조회 시 빈 Optional 반환")
        void findByBankCode_nonExistingCode_returnsEmpty() {
            // When
            Optional<Bank> result = bankQueryAdapter.findByBankCode(BankCode.of("999"));

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
        @DisplayName("성공 - 활성화된 Bank 도메인 목록만 조회한다")
        void findAllActive_returnsOnlyActiveBankDomains() {
            // When
            List<Bank> result = bankQueryAdapter.findAllActive();

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(Bank::isActive);
        }

        @Test
        @DisplayName("성공 - displayOrder 순으로 정렬된 도메인 목록 반환")
        void findAllActive_orderedByDisplayOrder() {
            // When
            List<Bank> result = bankQueryAdapter.findAllActive();

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getBankCodeValue()).isEqualTo("088");
            assertThat(result.get(1).getBankCodeValue()).isEqualTo("004");
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
        @DisplayName("성공 - 존재하는 BankId인 경우 true 반환")
        void existsById_existingBankId_returnsTrue() {
            // When
            boolean result = bankQueryAdapter.existsById(BankId.of(savedBank.getId()));

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 BankId인 경우 false 반환")
        void existsById_nonExistingBankId_returnsFalse() {
            // When
            boolean result = bankQueryAdapter.existsById(BankId.of(9999L));

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
            boolean result = bankQueryAdapter.existsActiveById(activeBank.getId());

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("성공 - 비활성 Bank ID인 경우 false 반환")
        void existsActiveById_inactiveBankId_returnsFalse() {
            // When
            boolean result = bankQueryAdapter.existsActiveById(inactiveBank.getId());

            // Then
            assertThat(result).isFalse();
        }
    }
}
