package com.ryuqq.setof.application.bank.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.application.bank.dto.response.BankResponse;
import com.ryuqq.setof.domain.bank.BankFixture;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("BankAssembler")
class BankAssemblerTest {

    private BankAssembler bankAssembler;

    @BeforeEach
    void setUp() {
        bankAssembler = new BankAssembler();
    }

    @Nested
    @DisplayName("toBankResponse")
    class ToBankResponseTest {

        @Test
        @DisplayName("Bank 도메인을 BankResponse로 변환 성공")
        void shouldConvertBankToBankResponse() {
            // Given
            Bank bank = BankFixture.create();

            // When
            BankResponse result = bankAssembler.toBankResponse(bank);

            // Then
            assertNotNull(result);
            assertEquals(bank.getIdValue(), result.id());
            assertEquals(bank.getBankCodeValue(), result.bankCode());
            assertEquals(bank.getBankNameValue(), result.bankName());
            assertEquals(bank.getDisplayOrder(), result.displayOrder());
        }

        @Test
        @DisplayName("커스텀 은행 정보로 변환 성공")
        void shouldConvertCustomBankToBankResponse() {
            // Given
            Bank bank = BankFixture.createCustom(5L, "088", "신한은행", 2, true);

            // When
            BankResponse result = bankAssembler.toBankResponse(bank);

            // Then
            assertNotNull(result);
            assertEquals(5L, result.id());
            assertEquals("088", result.bankCode());
            assertEquals("신한은행", result.bankName());
            assertEquals(2, result.displayOrder());
        }
    }

    @Nested
    @DisplayName("toBankResponses")
    class ToBankResponsesTest {

        @Test
        @DisplayName("Bank 목록을 BankResponse 목록으로 변환 성공")
        void shouldConvertBankListToBankResponseList() {
            // Given
            List<Bank> banks = BankFixture.createList();

            // When
            List<BankResponse> results = bankAssembler.toBankResponses(banks);

            // Then
            assertNotNull(results);
            assertEquals(banks.size(), results.size());

            for (int i = 0; i < banks.size(); i++) {
                assertEquals(banks.get(i).getIdValue(), results.get(i).id());
                assertEquals(banks.get(i).getBankCodeValue(), results.get(i).bankCode());
                assertEquals(banks.get(i).getBankNameValue(), results.get(i).bankName());
            }
        }

        @Test
        @DisplayName("빈 목록 변환 시 빈 목록 반환")
        void shouldReturnEmptyListWhenInputIsEmpty() {
            // Given
            List<Bank> emptyList = List.of();

            // When
            List<BankResponse> results = bankAssembler.toBankResponses(emptyList);

            // Then
            assertNotNull(results);
            assertTrue(results.isEmpty());
        }
    }
}
