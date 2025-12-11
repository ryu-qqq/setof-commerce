package com.ryuqq.setof.adapter.out.persistence.refundaccount.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
import com.ryuqq.setof.adapter.out.persistence.refundaccount.entity.RefundAccountJpaEntity;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RefundAccountQueryAdapter 통합 테스트
 *
 * <p>RefundAccountQueryPort 구현체의 Domain 변환 및 조회 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("RefundAccountQueryAdapter 통합 테스트")
class RefundAccountQueryAdapterTest extends RepositoryTestSupport {

    @Autowired private RefundAccountQueryAdapter refundAccountQueryAdapter;

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");
    private static final UUID MEMBER_ID = UUID.fromString("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");
    private static final UUID OTHER_MEMBER_ID =
            UUID.fromString("01936ddc-8d37-7c6e-8ad6-18c76adc9dfb");
    private static final Long BANK_ID = 1L;

    private RefundAccountJpaEntity createEntity(
            UUID memberId, String accountNumber, boolean isVerified, Instant deletedAt) {
        return RefundAccountJpaEntity.of(
                null,
                memberId.toString(),
                BANK_ID,
                accountNumber,
                "홍길동",
                isVerified,
                isVerified ? NOW : null,
                NOW,
                NOW,
                deletedAt);
    }

    @Nested
    @DisplayName("findById 메서드")
    class FindById {

        private RefundAccountJpaEntity savedAccount;

        @BeforeEach
        void setUp() {
            savedAccount = persistAndFlush(createEntity(MEMBER_ID, "1234567890123", true, null));
        }

        @Test
        @DisplayName("성공 - ID로 환불계좌 도메인을 조회한다")
        void findById_existingId_returnsRefundAccountDomain() {
            // When
            Optional<RefundAccount> result =
                    refundAccountQueryAdapter.findById(RefundAccountId.of(savedAccount.getId()));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getAccountNumberValue()).isEqualTo("1234567890123");
            assertThat(result.get().getAccountHolderNameValue()).isEqualTo("홍길동");
            assertThat(result.get().isVerified()).isTrue();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_nonExistingId_returnsEmpty() {
            // When
            Optional<RefundAccount> result =
                    refundAccountQueryAdapter.findById(RefundAccountId.of(9999L));

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("성공 - 삭제된 환불계좌는 조회되지 않는다")
        void findById_deletedAccount_returnsEmpty() {
            // Given
            RefundAccountJpaEntity deleted =
                    persistAndFlush(createEntity(OTHER_MEMBER_ID, "9876543210987", true, NOW));
            flushAndClear();

            // When
            Optional<RefundAccount> result =
                    refundAccountQueryAdapter.findById(RefundAccountId.of(deleted.getId()));

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByMemberId 메서드")
    class FindByMemberId {

        @BeforeEach
        void setUp() {
            persistAndFlush(createEntity(MEMBER_ID, "1234567890123", true, null));
        }

        @Test
        @DisplayName("성공 - 회원 ID로 환불계좌 도메인을 조회한다")
        void findByMemberId_existingMember_returnsRefundAccountDomain() {
            // When
            Optional<RefundAccount> result = refundAccountQueryAdapter.findByMemberId(MEMBER_ID);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getAccountNumberValue()).isEqualTo("1234567890123");
            assertThat(result.get().getMemberId()).isEqualTo(MEMBER_ID);
        }

        @Test
        @DisplayName("성공 - 환불계좌가 없는 회원 조회 시 빈 Optional 반환")
        void findByMemberId_nonExistingMember_returnsEmpty() {
            // When
            Optional<RefundAccount> result =
                    refundAccountQueryAdapter.findByMemberId(UUID.randomUUID());

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("성공 - 삭제된 환불계좌는 조회되지 않는다")
        void findByMemberId_deletedAccount_returnsEmpty() {
            // Given
            UUID memberWithDeletedAccount = UUID.randomUUID();
            persistAndFlush(createEntity(memberWithDeletedAccount, "1111111111111", true, NOW));
            flushAndClear();

            // When
            Optional<RefundAccount> result =
                    refundAccountQueryAdapter.findByMemberId(memberWithDeletedAccount);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsByMemberId 메서드")
    class ExistsByMemberId {

        @BeforeEach
        void setUp() {
            persistAndFlush(createEntity(MEMBER_ID, "1234567890123", true, null));
        }

        @Test
        @DisplayName("성공 - 환불계좌가 존재하는 회원인 경우 true 반환")
        void existsByMemberId_existingAccount_returnsTrue() {
            // When
            boolean result = refundAccountQueryAdapter.existsByMemberId(MEMBER_ID);

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("성공 - 환불계좌가 없는 회원인 경우 false 반환")
        void existsByMemberId_nonExistingAccount_returnsFalse() {
            // When
            boolean result = refundAccountQueryAdapter.existsByMemberId(UUID.randomUUID());

            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("성공 - 삭제된 환불계좌는 존재하지 않는 것으로 처리")
        void existsByMemberId_deletedAccount_returnsFalse() {
            // Given
            UUID memberWithDeletedAccount = UUID.randomUUID();
            persistAndFlush(createEntity(memberWithDeletedAccount, "2222222222222", true, NOW));
            flushAndClear();

            // When
            boolean result = refundAccountQueryAdapter.existsByMemberId(memberWithDeletedAccount);

            // Then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("미검증 환불계좌 테스트")
    class UnverifiedAccount {

        @Test
        @DisplayName("성공 - 미검증 환불계좌 도메인을 조회한다")
        void findById_unverifiedAccount_returnsAccountDomain() {
            // Given
            RefundAccountJpaEntity unverified =
                    persistAndFlush(createEntity(OTHER_MEMBER_ID, "5555555555555", false, null));
            flushAndClear();

            // When
            Optional<RefundAccount> result =
                    refundAccountQueryAdapter.findById(RefundAccountId.of(unverified.getId()));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().isVerified()).isFalse();
        }
    }
}
