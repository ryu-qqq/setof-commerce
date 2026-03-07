package com.ryuqq.setof.domain.refundaccount.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.refundaccount.exception.AccountVerificationFailedException;
import com.ryuqq.setof.domain.refundaccount.id.RefundAccountId;
import com.ryuqq.setof.domain.refundaccount.vo.RefundBankInfo;
import com.setof.commerce.domain.refundaccount.RefundAccountFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("RefundAccount Aggregate 단위 테스트")
class RefundAccountTest {

    @Nested
    @DisplayName("forNew() - 신규 환불 계좌 생성")
    class ForNewTest {

        @Test
        @DisplayName("MemberId와 은행 정보로 신규 환불 계좌를 생성한다")
        void createNewRefundAccountWithMemberIdAndBankInfo() {
            // given
            MemberId memberId = RefundAccountFixtures.defaultMemberId();
            RefundBankInfo bankInfo = RefundAccountFixtures.defaultRefundBankInfo();
            Instant now = CommonVoFixtures.now();

            // when
            RefundAccount account = RefundAccount.forNew(memberId, bankInfo, now);

            // then
            assertThat(account).isNotNull();
            assertThat(account.isNew()).isTrue();
            assertThat(account.memberId()).isEqualTo(memberId);
            assertThat(account.bankInfo()).isEqualTo(bankInfo);
            assertThat(account.isDeleted()).isFalse();
            assertThat(account.createdAt()).isEqualTo(now);
            assertThat(account.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("신규 생성된 환불 계좌의 ID는 null이다")
        void newRefundAccountHasNullId() {
            // when
            RefundAccount account = RefundAccountFixtures.newRefundAccount();

            // then
            assertThat(account.isNew()).isTrue();
            assertThat(account.id().isNew()).isTrue();
            assertThat(account.id().value()).isNull();
        }

        @Test
        @DisplayName("신규 생성된 환불 계좌는 활성 상태이다")
        void newRefundAccountIsActive() {
            // when
            RefundAccount account = RefundAccountFixtures.newRefundAccount();

            // then
            assertThat(account.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("신규 생성된 환불 계좌의 bankName()은 은행명을 반환한다")
        void newRefundAccountBankNameIsCorrect() {
            // when
            RefundAccount account = RefundAccountFixtures.newRefundAccount();

            // then
            assertThat(account.bankName()).isEqualTo(RefundAccountFixtures.DEFAULT_BANK_NAME);
        }

        @Test
        @DisplayName("신규 생성된 환불 계좌의 accountNumber()는 계좌번호를 반환한다")
        void newRefundAccountAccountNumberIsCorrect() {
            // when
            RefundAccount account = RefundAccountFixtures.newRefundAccount();

            // then
            assertThat(account.accountNumber())
                    .isEqualTo(RefundAccountFixtures.DEFAULT_ACCOUNT_NUMBER);
        }

        @Test
        @DisplayName("신규 생성된 환불 계좌의 accountHolderName()은 예금주명을 반환한다")
        void newRefundAccountAccountHolderNameIsCorrect() {
            // when
            RefundAccount account = RefundAccountFixtures.newRefundAccount();

            // then
            assertThat(account.accountHolderName())
                    .isEqualTo(RefundAccountFixtures.DEFAULT_ACCOUNT_HOLDER_NAME);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 레이어에서 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("영속성에서 활성 환불 계좌를 복원한다")
        void reconstituteActiveRefundAccount() {
            // given
            RefundAccountId id = RefundAccountId.of(10L);
            MemberId memberId = RefundAccountFixtures.defaultMemberId();
            RefundBankInfo bankInfo = RefundAccountFixtures.defaultRefundBankInfo();
            Instant createdAt = CommonVoFixtures.yesterday();
            Instant updatedAt = CommonVoFixtures.yesterday();

            // when
            RefundAccount account =
                    RefundAccount.reconstitute(
                            id, memberId, bankInfo, DeletionStatus.active(), createdAt, updatedAt);

            // then
            assertThat(account.isNew()).isFalse();
            assertThat(account.id()).isEqualTo(id);
            assertThat(account.idValue()).isEqualTo(10L);
            assertThat(account.memberId()).isEqualTo(memberId);
            assertThat(account.bankInfo()).isEqualTo(bankInfo);
            assertThat(account.isDeleted()).isFalse();
            assertThat(account.createdAt()).isEqualTo(createdAt);
            assertThat(account.updatedAt()).isEqualTo(updatedAt);
        }

        @Test
        @DisplayName("영속성에서 삭제된 환불 계좌를 복원한다")
        void reconstituteDeletedRefundAccount() {
            // when
            RefundAccount account = RefundAccountFixtures.deletedRefundAccount();

            // then
            assertThat(account.isDeleted()).isTrue();
            assertThat(account.deletionStatus().deletedAt()).isNotNull();
        }

        @Test
        @DisplayName("영속성 복원 시 isNew()는 false를 반환한다")
        void reconstituteReturnsNotNew() {
            // when
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();

            // then
            assertThat(account.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("update() - 환불 계좌 정보 수정")
    class UpdateTest {

        @Test
        @DisplayName("환불 계좌 정보를 수정한다")
        void updateRefundAccountInfo() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();
            RefundAccountUpdateData updateData = RefundAccountFixtures.defaultUpdateData();

            // when
            account.update(updateData);

            // then
            assertThat(account.bankInfo()).isEqualTo(updateData.bankInfo());
            assertThat(account.updatedAt()).isEqualTo(updateData.occurredAt());
        }

        @Test
        @DisplayName("수정 후 은행명이 변경된다")
        void updateChangesBankName() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();
            String originalBankName = account.bankName();
            RefundAccountUpdateData updateData = RefundAccountFixtures.defaultUpdateData();

            // when
            account.update(updateData);

            // then
            assertThat(account.bankName()).isNotEqualTo(originalBankName);
            assertThat(account.bankName()).isEqualTo("신한은행");
        }

        @Test
        @DisplayName("수정 후 계좌번호가 변경된다")
        void updateChangesAccountNumber() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();
            String originalAccountNumber = account.accountNumber();
            RefundAccountUpdateData updateData = RefundAccountFixtures.defaultUpdateData();

            // when
            account.update(updateData);

            // then
            assertThat(account.accountNumber()).isNotEqualTo(originalAccountNumber);
            assertThat(account.accountNumber()).isEqualTo("987654321098");
        }

        @Test
        @DisplayName("수정 후 예금주명이 변경된다")
        void updateChangesAccountHolderName() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();
            String originalHolderName = account.accountHolderName();
            RefundAccountUpdateData updateData = RefundAccountFixtures.defaultUpdateData();

            // when
            account.update(updateData);

            // then
            assertThat(account.accountHolderName()).isNotEqualTo(originalHolderName);
            assertThat(account.accountHolderName()).isEqualTo("김철수");
        }
    }

    @Nested
    @DisplayName("delete() - 소프트 삭제")
    class DeleteTest {

        @Test
        @DisplayName("환불 계좌를 소프트 삭제한다")
        void deleteRefundAccount() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();
            assertThat(account.isDeleted()).isFalse();
            Instant now = CommonVoFixtures.now();

            // when
            account.delete(now);

            // then
            assertThat(account.isDeleted()).isTrue();
            assertThat(account.deletionStatus().deletedAt()).isEqualTo(now);
            assertThat(account.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("삭제된 환불 계좌의 DeletionStatus는 삭제 상태이다")
        void deletedAccountHasDeletedStatus() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();
            Instant now = CommonVoFixtures.now();

            // when
            account.delete(now);

            // then
            assertThat(account.deletionStatus().isDeleted()).isTrue();
            assertThat(account.deletionStatus().isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("validateAccountHolder() - 예금주명 검증")
    class ValidateAccountHolderTest {

        @Test
        @DisplayName("등록된 예금주명과 일치하면 예외가 발생하지 않는다")
        void matchingHolderNamePassesValidation() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();

            // when & then
            org.assertj.core.api.Assertions.assertThatCode(
                            () ->
                                    account.validateAccountHolder(
                                            RefundAccountFixtures.DEFAULT_ACCOUNT_HOLDER_NAME))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("공백이 다른 예금주명도 검증을 통과한다")
        void holderNameWithDifferentWhitespacePassesValidation() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();

            // when & then
            org.assertj.core.api.Assertions.assertThatCode(
                            () -> account.validateAccountHolder("홍 길 동"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("다른 예금주명이면 AccountVerificationFailedException이 발생한다")
        void differentHolderNameThrowsException() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();

            // when & then
            assertThatThrownBy(() -> account.validateAccountHolder("김철수"))
                    .isInstanceOf(AccountVerificationFailedException.class);
        }

        @Test
        @DisplayName("null 예금주명이면 AccountVerificationFailedException이 발생한다")
        void nullHolderNameThrowsException() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();

            // when & then
            assertThatThrownBy(() -> account.validateAccountHolder(null))
                    .isInstanceOf(AccountVerificationFailedException.class);
        }

        @Test
        @DisplayName("빈 문자열 예금주명이면 AccountVerificationFailedException이 발생한다")
        void blankHolderNameThrowsException() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();

            // when & then
            assertThatThrownBy(() -> account.validateAccountHolder("   "))
                    .isInstanceOf(AccountVerificationFailedException.class);
        }
    }

    @Nested
    @DisplayName("isDeleted() / isNew() - 상태 조회")
    class StateQueryTest {

        @Test
        @DisplayName("활성 환불 계좌는 isDeleted()가 false를 반환한다")
        void activeAccountIsNotDeleted() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();

            // then
            assertThat(account.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 환불 계좌는 isDeleted()가 true를 반환한다")
        void deletedAccountIsDeleted() {
            // given
            RefundAccount account = RefundAccountFixtures.deletedRefundAccount();

            // then
            assertThat(account.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("신규 환불 계좌는 isNew()가 true를 반환한다")
        void newAccountIsNew() {
            // given
            RefundAccount account = RefundAccountFixtures.newRefundAccount();

            // then
            assertThat(account.isNew()).isTrue();
        }

        @Test
        @DisplayName("복원된 환불 계좌는 isNew()가 false를 반환한다")
        void reconstitutedAccountIsNotNew() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();

            // then
            assertThat(account.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("idValue()는 ID의 Long 값을 반환한다")
        void idValueReturnsLongValue() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount(42L);

            // then
            assertThat(account.idValue()).isEqualTo(42L);
        }

        @Test
        @DisplayName("memberIdValue()는 MemberId 문자열 값을 반환한다")
        void memberIdValueReturnsString() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();

            // then
            assertThat(account.memberIdValue()).isEqualTo(RefundAccountFixtures.DEFAULT_MEMBER_ID);
        }

        @Test
        @DisplayName("bankName()은 은행명 문자열을 반환한다")
        void bankNameReturnsString() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();

            // then
            assertThat(account.bankName()).isEqualTo(RefundAccountFixtures.DEFAULT_BANK_NAME);
        }

        @Test
        @DisplayName("accountNumber()는 계좌번호 문자열을 반환한다")
        void accountNumberReturnsString() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();

            // then
            assertThat(account.accountNumber())
                    .isEqualTo(RefundAccountFixtures.DEFAULT_ACCOUNT_NUMBER);
        }

        @Test
        @DisplayName("accountHolderName()은 예금주명 문자열을 반환한다")
        void accountHolderNameReturnsString() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();

            // then
            assertThat(account.accountHolderName())
                    .isEqualTo(RefundAccountFixtures.DEFAULT_ACCOUNT_HOLDER_NAME);
        }

        @Test
        @DisplayName("maskedAccountNumber()는 마스킹된 계좌번호를 반환한다")
        void maskedAccountNumberReturnsMasked() {
            // given
            RefundAccount account = RefundAccountFixtures.activeRefundAccount();

            // then
            String masked = account.maskedAccountNumber();
            assertThat(masked)
                    .endsWith(
                            RefundAccountFixtures.DEFAULT_ACCOUNT_NUMBER.substring(
                                    RefundAccountFixtures.DEFAULT_ACCOUNT_NUMBER.length() - 4));
            assertThat(masked).contains("*");
        }
    }
}
