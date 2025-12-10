package com.ryuqq.setof.domain.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.bank.vo.BankCode;
import com.ryuqq.setof.domain.bank.vo.BankId;
import com.ryuqq.setof.domain.bank.vo.BankName;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Bank Aggregate 테스트
 *
 * <p>Bank는 코드 테이블 엔티티로, 애플리케이션에서는 조회만 수행합니다. 따라서 reconstitute() 팩토리 메서드와 조회 관련 메서드만 테스트합니다.
 */
@DisplayName("Bank Aggregate")
class BankTest {

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");

    @Nested
    @DisplayName("reconstitute() - Persistence 복원")
    class Reconstitute {

        @Test
        @DisplayName("모든 필드를 복원할 수 있다")
        void shouldReconstituteBankFromPersistence() {
            // given
            BankId id = BankId.of(1L);
            BankCode bankCode = BankCode.of("004");
            BankName bankName = BankName.of("KB국민은행");
            int displayOrder = 1;
            boolean active = true;
            Instant createdAt = NOW;
            Instant updatedAt = NOW;

            // when
            Bank bank = Bank.reconstitute(id, bankCode, bankName, displayOrder, active, createdAt, updatedAt);

            // then
            assertNotNull(bank);
            assertEquals(1L, bank.getIdValue());
            assertEquals("004", bank.getBankCodeValue());
            assertEquals("KB국민은행", bank.getBankNameValue());
            assertEquals(1, bank.getDisplayOrder());
            assertTrue(bank.isActive());
            assertEquals(createdAt, bank.getCreatedAt());
            assertEquals(updatedAt, bank.getUpdatedAt());
        }

        @Test
        @DisplayName("비활성 은행을 복원할 수 있다")
        void shouldReconstituteInactiveBank() {
            // given
            BankId id = BankId.of(99L);
            BankCode bankCode = BankCode.of("999");
            BankName bankName = BankName.of("폐업은행");
            int displayOrder = 99;
            boolean active = false;

            // when
            Bank bank = Bank.reconstitute(id, bankCode, bankName, displayOrder, active, NOW, NOW);

            // then
            assertNotNull(bank);
            assertFalse(bank.isActive());
        }
    }

    @Nested
    @DisplayName("Law of Demeter Helper Methods")
    class HelperMethods {

        @Test
        @DisplayName("getIdValue()는 ID 값을 직접 반환한다")
        void shouldReturnIdValueDirectly() {
            // given
            Bank bank = createBank();

            // then
            assertEquals(1L, bank.getIdValue());
        }

        @Test
        @DisplayName("getBankCodeValue()는 은행 코드를 직접 반환한다")
        void shouldReturnBankCodeValueDirectly() {
            // given
            Bank bank = createBank();

            // then
            assertEquals("004", bank.getBankCodeValue());
        }

        @Test
        @DisplayName("getBankNameValue()는 은행 이름을 직접 반환한다")
        void shouldReturnBankNameValueDirectly() {
            // given
            Bank bank = createBank();

            // then
            assertEquals("KB국민은행", bank.getBankNameValue());
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드")
    class StatusChecks {

        @Test
        @DisplayName("isActive()는 활성 은행이면 true를 반환한다")
        void shouldReturnTrueForActiveBank() {
            // given
            Bank bank =
                    Bank.reconstitute(
                            BankId.of(1L), BankCode.of("004"), BankName.of("KB국민은행"), 1, true, NOW, NOW);

            // then
            assertTrue(bank.isActive());
        }

        @Test
        @DisplayName("isActive()는 비활성 은행이면 false를 반환한다")
        void shouldReturnFalseForInactiveBank() {
            // given
            Bank bank =
                    Bank.reconstitute(
                            BankId.of(1L), BankCode.of("004"), BankName.of("KB국민은행"), 1, false, NOW, NOW);

            // then
            assertFalse(bank.isActive());
        }
    }

    @Nested
    @DisplayName("Getter 메서드")
    class Getters {

        @Test
        @DisplayName("모든 Getter 메서드가 올바른 값을 반환한다")
        void shouldReturnCorrectValuesFromGetters() {
            // given
            BankId id = BankId.of(88L);
            BankCode bankCode = BankCode.of("088");
            BankName bankName = BankName.of("신한은행");
            int displayOrder = 2;
            Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");
            Instant updatedAt = Instant.parse("2024-06-01T00:00:00Z");

            Bank bank = Bank.reconstitute(id, bankCode, bankName, displayOrder, true, createdAt, updatedAt);

            // then
            assertEquals(id, bank.getId());
            assertEquals(bankCode, bank.getBankCode());
            assertEquals(bankName, bank.getBankName());
            assertEquals(displayOrder, bank.getDisplayOrder());
            assertEquals(createdAt, bank.getCreatedAt());
            assertEquals(updatedAt, bank.getUpdatedAt());
        }
    }

    // ========== Helper Methods ==========

    private Bank createBank() {
        return Bank.reconstitute(
                BankId.of(1L), BankCode.of("004"), BankName.of("KB국민은행"), 1, true, NOW, NOW);
    }
}
