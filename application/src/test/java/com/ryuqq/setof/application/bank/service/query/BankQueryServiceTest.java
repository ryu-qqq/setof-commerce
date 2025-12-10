package com.ryuqq.setof.application.bank.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.bank.assembler.BankAssembler;
import com.ryuqq.setof.application.bank.dto.response.BankResponse;
import com.ryuqq.setof.application.bank.manager.query.BankReadManager;
import com.ryuqq.setof.domain.bank.BankFixture;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("BankQueryService")
@ExtendWith(MockitoExtension.class)
class BankQueryServiceTest {

    @Mock private BankReadManager bankReadManager;

    private BankAssembler bankAssembler;
    private BankQueryService bankQueryService;

    @BeforeEach
    void setUp() {
        bankAssembler = new BankAssembler();
        bankQueryService = new BankQueryService(bankReadManager, bankAssembler);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("활성 은행 목록 조회 성공")
        void shouldReturnActiveBanks() {
            // Given
            List<Bank> activeBanks = BankFixture.createList();
            when(bankReadManager.findAllActive()).thenReturn(activeBanks);

            // When
            List<BankResponse> results = bankQueryService.execute();

            // Then
            assertNotNull(results);
            assertEquals(activeBanks.size(), results.size());
            verify(bankReadManager, times(1)).findAllActive();
        }

        @Test
        @DisplayName("활성 은행이 없을 때 빈 목록 반환")
        void shouldReturnEmptyListWhenNoActiveBanks() {
            // Given
            when(bankReadManager.findAllActive()).thenReturn(List.of());

            // When
            List<BankResponse> results = bankQueryService.execute();

            // Then
            assertNotNull(results);
            assertTrue(results.isEmpty());
            verify(bankReadManager, times(1)).findAllActive();
        }

        @Test
        @DisplayName("변환된 응답에 올바른 은행 정보 포함")
        void shouldContainCorrectBankInfoInResponse() {
            // Given
            Bank bank = BankFixture.create();
            when(bankReadManager.findAllActive()).thenReturn(List.of(bank));

            // When
            List<BankResponse> results = bankQueryService.execute();

            // Then
            assertEquals(1, results.size());
            BankResponse response = results.get(0);
            assertEquals(bank.getIdValue(), response.id());
            assertEquals(bank.getBankCodeValue(), response.bankCode());
            assertEquals(bank.getBankNameValue(), response.bankName());
            assertEquals(bank.getDisplayOrder(), response.displayOrder());
        }
    }
}
