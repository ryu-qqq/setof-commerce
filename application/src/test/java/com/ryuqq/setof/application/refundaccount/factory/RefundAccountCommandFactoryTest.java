package com.ryuqq.setof.application.refundaccount.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.refundaccount.RefundAccountCommandFixtures;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccountUpdateData;
import java.time.Instant;
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
@DisplayName("RefundAccountCommandFactory 단위 테스트")
class RefundAccountCommandFactoryTest {

    @InjectMocks private RefundAccountCommandFactory sut;

    @Mock private TimeProvider timeProvider;

    @Nested
    @DisplayName("createNewRefundAccount() - Command → RefundAccount 변환")
    class CreateNewRefundAccountTest {

        @Test
        @DisplayName("RegisterRefundAccountCommand를 RefundAccount 도메인 객체로 변환한다")
        void createNewRefundAccount_ValidCommand_ReturnsRefundAccount() {
            // given
            RegisterRefundAccountCommand command = RefundAccountCommandFixtures.registerCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            RefundAccount result = sut.createNewRefundAccount(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.bankName()).isEqualTo(command.bankName());
            assertThat(result.accountNumber()).isEqualTo(command.accountNumber());
            assertThat(result.accountHolderName()).isEqualTo(command.accountHolderName());
        }

        @Test
        @DisplayName("커맨드의 userId가 RefundAccount memberId에 정확히 반영된다")
        void createNewRefundAccount_UserIdReflected_InRefundAccount() {
            // given
            RegisterRefundAccountCommand command = RefundAccountCommandFixtures.registerCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            RefundAccount result = sut.createNewRefundAccount(command);

            // then
            assertThat(result.memberIdValue()).isEqualTo(command.userId());
        }

        @Test
        @DisplayName("생성된 RefundAccount는 신규 ID 상태여야 한다")
        void createNewRefundAccount_CreatesNewIdState() {
            // given
            RegisterRefundAccountCommand command = RefundAccountCommandFixtures.registerCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            RefundAccount result = sut.createNewRefundAccount(command);

            // then
            assertThat(result.isNew()).isTrue();
        }

        @Test
        @DisplayName("생성된 RefundAccount는 삭제되지 않은 활성 상태여야 한다")
        void createNewRefundAccount_CreatesActiveAccount() {
            // given
            RegisterRefundAccountCommand command = RefundAccountCommandFixtures.registerCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            RefundAccount result = sut.createNewRefundAccount(command);

            // then
            assertThat(result.isDeleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("createUpdateData() - Command → RefundAccountUpdateData 변환")
    class CreateUpdateDataTest {

        @Test
        @DisplayName("UpdateRefundAccountCommand를 RefundAccountUpdateData로 변환한다")
        void createUpdateData_ValidCommand_ReturnsUpdateData() {
            // given
            UpdateRefundAccountCommand command = RefundAccountCommandFixtures.updateCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            RefundAccountUpdateData result = sut.createUpdateData(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.bankInfo().bankName()).isEqualTo(command.bankName());
            assertThat(result.bankInfo().accountNumber()).isEqualTo(command.accountNumber());
            assertThat(result.bankInfo().accountHolderName())
                    .isEqualTo(command.accountHolderName());
        }

        @Test
        @DisplayName("occurredAt이 TimeProvider가 제공한 시간으로 설정된다")
        void createUpdateData_OccurredAtSetFromTimeProvider() {
            // given
            UpdateRefundAccountCommand command = RefundAccountCommandFixtures.updateCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            RefundAccountUpdateData result = sut.createUpdateData(command);

            // then
            assertThat(result.occurredAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("커맨드의 은행 정보가 RefundAccountUpdateData에 정확히 반영된다")
        void createUpdateData_BankInfoReflected_InUpdateData() {
            // given
            String bankName = "우리은행";
            String accountNumber = "1002-123-456789";
            String holderName = "박민수";
            UpdateRefundAccountCommand command =
                    RefundAccountCommandFixtures.updateCommand(
                            1L, 100L, bankName, accountNumber, holderName);
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            RefundAccountUpdateData result = sut.createUpdateData(command);

            // then
            assertThat(result.bankInfo().bankName()).isEqualTo(bankName);
            assertThat(result.bankInfo().accountNumber()).isEqualTo(accountNumber);
            assertThat(result.bankInfo().accountHolderName()).isEqualTo(holderName);
        }
    }
}
