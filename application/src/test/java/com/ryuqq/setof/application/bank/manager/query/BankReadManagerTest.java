package com.ryuqq.setof.application.bank.manager.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.bank.port.out.query.BankQueryPort;
import com.ryuqq.setof.domain.bank.BankFixture;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.bank.exception.BankNotFoundException;
import com.ryuqq.setof.domain.bank.vo.BankCode;
import com.ryuqq.setof.domain.bank.vo.BankId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("BankReadManager")
@ExtendWith(MockitoExtension.class)
class BankReadManagerTest {

    @Mock private BankQueryPort bankQueryPort;

    private BankReadManager bankReadManager;

    @BeforeEach
    void setUp() {
        bankReadManager = new BankReadManager(bankQueryPort);
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTest {

        @Test
        @DisplayName("Bank ID로 조회 성공")
        void shouldReturnBankWhenFound() {
            // Given
            Long bankId = 1L;
            Bank bank = BankFixture.createWithId(bankId);

            when(bankQueryPort.findById(any(BankId.class))).thenReturn(Optional.of(bank));

            // When
            Bank result = bankReadManager.findById(bankId);

            // Then
            assertNotNull(result);
            assertEquals(bankId, result.getIdValue());
            verify(bankQueryPort, times(1)).findById(any(BankId.class));
        }

        @Test
        @DisplayName("Bank ID로 조회 실패 시 예외 발생")
        void shouldThrowExceptionWhenNotFound() {
            // Given
            Long bankId = 999L;

            when(bankQueryPort.findById(any(BankId.class))).thenReturn(Optional.empty());

            // When & Then
            assertThrows(BankNotFoundException.class, () -> bankReadManager.findById(bankId));
        }
    }

    @Nested
    @DisplayName("findByIdOptional")
    class FindByIdOptionalTest {

        @Test
        @DisplayName("Bank ID로 Optional 조회 성공")
        void shouldReturnOptionalWhenFound() {
            // Given
            Long bankId = 1L;
            Bank bank = BankFixture.createWithId(bankId);

            when(bankQueryPort.findById(any(BankId.class))).thenReturn(Optional.of(bank));

            // When
            Optional<Bank> result = bankReadManager.findByIdOptional(bankId);

            // Then
            assertTrue(result.isPresent());
            assertEquals(bankId, result.get().getIdValue());
        }

        @Test
        @DisplayName("Bank ID로 Optional 조회 시 없으면 빈 Optional 반환")
        void shouldReturnEmptyOptionalWhenNotFound() {
            // Given
            Long bankId = 999L;

            when(bankQueryPort.findById(any(BankId.class))).thenReturn(Optional.empty());

            // When
            Optional<Bank> result = bankReadManager.findByIdOptional(bankId);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("findByBankCode")
    class FindByBankCodeTest {

        @Test
        @DisplayName("은행 코드로 조회 성공")
        void shouldReturnBankWhenFoundByCode() {
            // Given
            String bankCode = "004";
            Bank bank = BankFixture.create();

            when(bankQueryPort.findByBankCode(any(BankCode.class))).thenReturn(Optional.of(bank));

            // When
            Bank result = bankReadManager.findByBankCode(bankCode);

            // Then
            assertNotNull(result);
            verify(bankQueryPort, times(1)).findByBankCode(any(BankCode.class));
        }

        @Test
        @DisplayName("은행 코드로 조회 실패 시 예외 발생")
        void shouldThrowExceptionWhenCodeNotFound() {
            // Given
            String bankCode = "999";

            when(bankQueryPort.findByBankCode(any(BankCode.class))).thenReturn(Optional.empty());

            // When & Then
            assertThrows(
                    BankNotFoundException.class, () -> bankReadManager.findByBankCode(bankCode));
        }
    }

    @Nested
    @DisplayName("findAllActive")
    class FindAllActiveTest {

        @Test
        @DisplayName("활성화된 모든 은행 목록 조회")
        void shouldReturnAllActiveBanks() {
            // Given
            List<Bank> banks = BankFixture.createList();

            when(bankQueryPort.findAllActive()).thenReturn(banks);

            // When
            List<Bank> result = bankReadManager.findAllActive();

            // Then
            assertEquals(3, result.size());
            verify(bankQueryPort, times(1)).findAllActive();
        }

        @Test
        @DisplayName("활성화된 은행이 없으면 빈 목록 반환")
        void shouldReturnEmptyListWhenNoActiveBanks() {
            // Given
            when(bankQueryPort.findAllActive()).thenReturn(List.of());

            // When
            List<Bank> result = bankReadManager.findAllActive();

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("existsById")
    class ExistsByIdTest {

        @Test
        @DisplayName("Bank ID 존재 여부 확인 - 존재")
        void shouldReturnTrueWhenExists() {
            // Given
            Long bankId = 1L;

            when(bankQueryPort.existsById(any(BankId.class))).thenReturn(true);

            // When
            boolean result = bankReadManager.existsById(bankId);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Bank ID 존재 여부 확인 - 미존재")
        void shouldReturnFalseWhenNotExists() {
            // Given
            Long bankId = 999L;

            when(bankQueryPort.existsById(any(BankId.class))).thenReturn(false);

            // When
            boolean result = bankReadManager.existsById(bankId);

            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("existsActiveById")
    class ExistsActiveByIdTest {

        @Test
        @DisplayName("활성 Bank 존재 여부 확인 - 존재")
        void shouldReturnTrueWhenActiveExists() {
            // Given
            Long bankId = 1L;

            when(bankQueryPort.existsActiveById(bankId)).thenReturn(true);

            // When
            boolean result = bankReadManager.existsActiveById(bankId);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("활성 Bank 존재 여부 확인 - 미존재")
        void shouldReturnFalseWhenActiveNotExists() {
            // Given
            Long bankId = 999L;

            when(bankQueryPort.existsActiveById(bankId)).thenReturn(false);

            // When
            boolean result = bankReadManager.existsActiveById(bankId);

            // Then
            assertFalse(result);
        }
    }
}
