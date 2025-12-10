package com.ryuqq.setof.domain.refundaccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.exception.RefundAccountNotOwnerException;
import com.ryuqq.setof.domain.refundaccount.vo.AccountHolderName;
import com.ryuqq.setof.domain.refundaccount.vo.AccountNumber;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;
import com.ryuqq.setof.domain.refundaccount.vo.VerificationInfo;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * RefundAccount Aggregate 테스트
 *
 * <p>환불계좌 정보 관리에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("RefundAccount Aggregate")
class RefundAccountTest {

    private static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("Asia/Seoul"));

    private static final UUID TEST_MEMBER_ID = UUID.fromString("01234567-89ab-cdef-0123-456789abcdef");

    @Nested
    @DisplayName("forNew() - 신규 환불계좌 생성 (미검증)")
    class ForNew {

        @Test
        @DisplayName("미검증 상태로 신규 환불계좌를 생성할 수 있다")
        void shouldCreateUnverifiedRefundAccount() {
            // given
            Long bankId = 1L;
            AccountNumber accountNumber = AccountNumber.of("1234567890123");
            AccountHolderName accountHolderName = AccountHolderName.of("홍길동");

            // when
            RefundAccount account =
                    RefundAccount.forNew(TEST_MEMBER_ID, bankId, accountNumber, accountHolderName, FIXED_CLOCK);

            // then
            assertNotNull(account);
            assertNull(account.getId()); // ID는 null (Persistence에서 설정)
            assertEquals(TEST_MEMBER_ID, account.getMemberId());
            assertEquals(bankId, account.getBankId());
            assertEquals("1234567890123", account.getAccountNumberValue());
            assertEquals("홍길동", account.getAccountHolderNameValue());
            assertFalse(account.isVerified());
            assertTrue(account.isUnverified());
            assertTrue(account.isActive());
            assertFalse(account.isDeleted());
            assertNull(account.getVerifiedAt());
        }
    }

    @Nested
    @DisplayName("forNewVerified() - 신규 환불계좌 생성 (검증 완료)")
    class ForNewVerified {

        @Test
        @DisplayName("검증 완료 상태로 신규 환불계좌를 생성할 수 있다")
        void shouldCreateVerifiedRefundAccount() {
            // given
            Long bankId = 88L;
            AccountNumber accountNumber = AccountNumber.of("9876543210");
            AccountHolderName accountHolderName = AccountHolderName.of("김철수");

            // when
            RefundAccount account =
                    RefundAccount.forNewVerified(
                            TEST_MEMBER_ID, bankId, accountNumber, accountHolderName, FIXED_CLOCK);

            // then
            assertNotNull(account);
            assertTrue(account.isVerified());
            assertFalse(account.isUnverified());
            assertNotNull(account.getVerifiedAt());
        }
    }

    @Nested
    @DisplayName("reconstitute() - Persistence 복원")
    class Reconstitute {

        @Test
        @DisplayName("Persistence에서 모든 필드를 복원할 수 있다")
        void shouldReconstituteRefundAccountFromPersistence() {
            // given
            RefundAccountId id = RefundAccountId.of(1L);
            Long bankId = 4L;
            AccountNumber accountNumber = AccountNumber.of("1234567890123");
            AccountHolderName accountHolderName = AccountHolderName.of("홍길동");
            Instant verifiedAt = Instant.parse("2024-06-01T00:00:00Z");
            VerificationInfo verificationInfo = VerificationInfo.verifiedAt(verifiedAt);
            Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");
            Instant updatedAt = Instant.parse("2024-06-01T00:00:00Z");

            // when
            RefundAccount account =
                    RefundAccount.reconstitute(
                            id,
                            TEST_MEMBER_ID,
                            bankId,
                            accountNumber,
                            accountHolderName,
                            verificationInfo,
                            createdAt,
                            updatedAt,
                            null);

            // then
            assertEquals(1L, account.getIdValue());
            assertEquals(TEST_MEMBER_ID, account.getMemberId());
            assertEquals(4L, account.getBankId());
            assertTrue(account.isVerified());
            assertTrue(account.isActive());
            assertNull(account.getDeletedAt());
            assertEquals(verifiedAt, account.getVerifiedAt());
        }

        @Test
        @DisplayName("삭제된 환불계좌를 복원할 수 있다")
        void shouldReconstituteDeletedRefundAccount() {
            // given
            RefundAccountId id = RefundAccountId.of(1L);
            Instant deletedAt = Instant.parse("2024-12-01T00:00:00Z");

            // when
            RefundAccount account =
                    RefundAccount.reconstitute(
                            id,
                            TEST_MEMBER_ID,
                            1L,
                            AccountNumber.of("1234567890"),
                            AccountHolderName.of("홍길동"),
                            VerificationInfo.unverified(),
                            FIXED_CLOCK.instant(),
                            FIXED_CLOCK.instant(),
                            deletedAt);

            // then
            assertTrue(account.isDeleted());
            assertFalse(account.isActive());
            assertEquals(deletedAt, account.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("update() - 계좌 정보 수정 (재검증 필요)")
    class Update {

        @Test
        @DisplayName("계좌 정보 수정 시 미검증 상태로 변경된다")
        void shouldUpdateAndBecomeUnverified() {
            // given
            RefundAccount account = createVerifiedRefundAccount();
            assertTrue(account.isVerified());

            Long newBankId = 88L;
            AccountNumber newAccountNumber = AccountNumber.of("9999888877776666");
            AccountHolderName newAccountHolderName = AccountHolderName.of("김철수");

            // when
            account.update(newBankId, newAccountNumber, newAccountHolderName, FIXED_CLOCK);

            // then
            assertEquals(88L, account.getBankId());
            assertEquals("9999888877776666", account.getAccountNumberValue());
            assertEquals("김철수", account.getAccountHolderNameValue());
            assertFalse(account.isVerified());
            assertTrue(account.isUnverified());
        }
    }

    @Nested
    @DisplayName("updateVerified() - 계좌 정보 수정 (검증 완료)")
    class UpdateVerified {

        @Test
        @DisplayName("검증된 상태로 계좌 정보를 수정할 수 있다")
        void shouldUpdateAsVerified() {
            // given
            RefundAccount account = createUnverifiedRefundAccount();
            assertFalse(account.isVerified());

            Long newBankId = 88L;
            AccountNumber newAccountNumber = AccountNumber.of("1111222233334444");
            AccountHolderName newAccountHolderName = AccountHolderName.of("박영희");

            // when
            account.updateVerified(newBankId, newAccountNumber, newAccountHolderName, FIXED_CLOCK);

            // then
            assertEquals(88L, account.getBankId());
            assertEquals("1111222233334444", account.getAccountNumberValue());
            assertEquals("박영희", account.getAccountHolderNameValue());
            assertTrue(account.isVerified());
            assertNotNull(account.getVerifiedAt());
        }
    }

    @Nested
    @DisplayName("verify() - 계좌 검증 완료 처리")
    class Verify {

        @Test
        @DisplayName("미검증 상태에서 검증 완료 처리할 수 있다")
        void shouldVerifyUnverifiedAccount() {
            // given
            RefundAccount account = createUnverifiedRefundAccount();
            assertFalse(account.isVerified());

            // when
            account.verify(FIXED_CLOCK);

            // then
            assertTrue(account.isVerified());
            assertNotNull(account.getVerifiedAt());
        }
    }

    @Nested
    @DisplayName("delete() - 환불계좌 삭제")
    class Delete {

        @Test
        @DisplayName("환불계좌를 소프트 삭제할 수 있다")
        void shouldSoftDeleteRefundAccount() {
            // given
            RefundAccount account = createVerifiedRefundAccount();
            assertTrue(account.isActive());

            // when
            account.delete(FIXED_CLOCK);

            // then
            assertTrue(account.isDeleted());
            assertFalse(account.isActive());
            assertNotNull(account.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("validateOwnership() - 소유권 검증")
    class ValidateOwnership {

        @Test
        @DisplayName("소유자가 맞으면 예외가 발생하지 않는다")
        void shouldNotThrowExceptionWhenOwnerMatches() {
            // given
            RefundAccount account = createVerifiedRefundAccount();

            // when & then (no exception)
            account.validateOwnership(TEST_MEMBER_ID);
        }

        @Test
        @DisplayName("소유자가 아니면 예외가 발생한다")
        void shouldThrowExceptionWhenOwnerDoesNotMatch() {
            // given
            RefundAccount account = createVerifiedRefundAccount();
            UUID differentMemberId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");

            // when & then
            assertThrows(
                    RefundAccountNotOwnerException.class,
                    () -> account.validateOwnership(differentMemberId));
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드")
    class StatusChecks {

        @Test
        @DisplayName("isOwnedBy()는 소유자 ID가 일치하면 true를 반환한다")
        void shouldReturnTrueWhenOwnedByMember() {
            // given
            RefundAccount account = createVerifiedRefundAccount();

            // then
            assertTrue(account.isOwnedBy(TEST_MEMBER_ID));
        }

        @Test
        @DisplayName("isOwnedBy()는 소유자 ID가 일치하지 않으면 false를 반환한다")
        void shouldReturnFalseWhenNotOwnedByMember() {
            // given
            RefundAccount account = createVerifiedRefundAccount();
            UUID otherMemberId = UUID.fromString("11111111-2222-3333-4444-555555555555");

            // then
            assertFalse(account.isOwnedBy(otherMemberId));
        }
    }

    @Nested
    @DisplayName("Law of Demeter Helper Methods")
    class HelperMethods {

        @Test
        @DisplayName("getMaskedAccountNumber()는 마스킹된 계좌번호를 반환한다")
        void shouldReturnMaskedAccountNumber() {
            // given
            RefundAccount account = createVerifiedRefundAccount();

            // then
            String masked = account.getMaskedAccountNumber();
            assertNotNull(masked);
            assertTrue(masked.contains("****"));
        }

        @Test
        @DisplayName("getNormalizedAccountNumber()는 정규화된 계좌번호를 반환한다")
        void shouldReturnNormalizedAccountNumber() {
            // given
            RefundAccount account =
                    RefundAccount.forNewVerified(
                            TEST_MEMBER_ID,
                            1L,
                            AccountNumber.of("123-456-789012"),
                            AccountHolderName.of("홍길동"),
                            FIXED_CLOCK);

            // then
            String normalized = account.getNormalizedAccountNumber();
            assertEquals("123456789012", normalized);
        }

        @Test
        @DisplayName("getIdValue()는 ID가 없으면 null을 반환한다")
        void shouldReturnNullWhenIdIsNull() {
            // given
            RefundAccount account =
                    RefundAccount.forNew(
                            TEST_MEMBER_ID,
                            1L,
                            AccountNumber.of("1234567890"),
                            AccountHolderName.of("홍길동"),
                            FIXED_CLOCK);

            // then
            assertNull(account.getIdValue());
        }
    }

    // ========== Helper Methods ==========

    private RefundAccount createVerifiedRefundAccount() {
        return RefundAccount.reconstitute(
                RefundAccountId.of(1L),
                TEST_MEMBER_ID,
                4L,
                AccountNumber.of("1234567890123"),
                AccountHolderName.of("홍길동"),
                VerificationInfo.verifiedAt(FIXED_CLOCK.instant()),
                FIXED_CLOCK.instant(),
                FIXED_CLOCK.instant(),
                null);
    }

    private RefundAccount createUnverifiedRefundAccount() {
        return RefundAccount.reconstitute(
                RefundAccountId.of(2L),
                TEST_MEMBER_ID,
                88L,
                AccountNumber.of("9876543210"),
                AccountHolderName.of("김철수"),
                VerificationInfo.unverified(),
                FIXED_CLOCK.instant(),
                FIXED_CLOCK.instant(),
                null);
    }
}
