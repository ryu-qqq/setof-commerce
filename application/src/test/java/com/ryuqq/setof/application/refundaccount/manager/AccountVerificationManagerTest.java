package com.ryuqq.setof.application.refundaccount.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.refundaccount.port.out.client.AccountVerificationPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("AccountVerificationManager 단위 테스트")
class AccountVerificationManagerTest {

    @InjectMocks private AccountVerificationManager sut;

    @Mock private AccountVerificationPort accountVerificationPort;

    @Nested
    @DisplayName("verify() - 계좌 실명 검증")
    class VerifyTest {

        @Test
        @DisplayName("계좌 실명 검증 성공 시 true를 반환한다")
        void verify_ValidAccount_ReturnsTrue() {
            // given
            String bankCode = "신한은행";
            String accountNumber = "110-123-456789";
            String accountHolderName = "홍길동";

            given(accountVerificationPort.verifyAccount(bankCode, accountNumber, accountHolderName))
                    .willReturn(true);

            // when
            boolean result = sut.verify(bankCode, accountNumber, accountHolderName);

            // then
            assertThat(result).isTrue();
            then(accountVerificationPort)
                    .should()
                    .verifyAccount(bankCode, accountNumber, accountHolderName);
        }

        @Test
        @DisplayName("계좌 실명 검증 실패 시 false를 반환한다")
        void verify_InvalidAccount_ReturnsFalse() {
            // given
            String bankCode = "신한은행";
            String accountNumber = "110-123-456789";
            String accountHolderName = "김철수";

            given(accountVerificationPort.verifyAccount(bankCode, accountNumber, accountHolderName))
                    .willReturn(false);

            // when
            boolean result = sut.verify(bankCode, accountNumber, accountHolderName);

            // then
            assertThat(result).isFalse();
            then(accountVerificationPort)
                    .should()
                    .verifyAccount(bankCode, accountNumber, accountHolderName);
        }

        @Test
        @DisplayName("verifyAccount 메서드에 정확한 인자를 전달한다")
        void verify_PassesCorrectArgumentsToPort() {
            // given
            String bankCode = "국민은행";
            String accountNumber = "123-456-789012";
            String accountHolderName = "박민수";

            given(accountVerificationPort.verifyAccount(bankCode, accountNumber, accountHolderName))
                    .willReturn(true);

            // when
            sut.verify(bankCode, accountNumber, accountHolderName);

            // then
            then(accountVerificationPort)
                    .should()
                    .verifyAccount(bankCode, accountNumber, accountHolderName);
        }
    }
}
