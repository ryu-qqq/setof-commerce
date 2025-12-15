package com.ryuqq.setof.adapter.out.persistence.refundaccount.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
import com.ryuqq.setof.adapter.out.persistence.refundaccount.entity.RefundAccountJpaEntity;
import com.ryuqq.setof.domain.refundaccount.RefundAccountFixture;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RefundAccountPersistenceAdapter 통합 테스트
 *
 * <p>RefundAccountPersistencePort 구현체의 저장 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("RefundAccountPersistenceAdapter 통합 테스트")
class RefundAccountPersistenceAdapterTest extends RepositoryTestSupport {

    @Autowired private RefundAccountPersistenceAdapter refundAccountPersistenceAdapter;

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");
    private static final UUID MEMBER_ID = UUID.fromString("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");
    private static final Long BANK_ID = 1L;

    @Nested
    @DisplayName("persist 메서드")
    class Persist {

        @Test
        @DisplayName("성공 - 새 환불계좌를 저장하고 ID를 반환한다")
        void persist_newRefundAccount_savesAndReturnsId() {
            // Given
            RefundAccount newAccount = RefundAccountFixture.createNew();

            // When
            RefundAccountId savedId = refundAccountPersistenceAdapter.persist(newAccount);
            flushAndClear();

            // Then
            assertThat(savedId).isNotNull();
            assertThat(savedId.value()).isNotNull();

            RefundAccountJpaEntity found = find(RefundAccountJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getAccountNumber()).isEqualTo("1234567890123");
            assertThat(found.getAccountHolderName()).isEqualTo("홍길동");
            assertThat(found.isVerified()).isFalse();
        }

        @Test
        @DisplayName("성공 - 검증된 환불계좌를 저장한다")
        void persist_verifiedRefundAccount_savesWithVerification() {
            // Given
            RefundAccount verifiedAccount = RefundAccountFixture.createNewVerified();

            // When
            RefundAccountId savedId = refundAccountPersistenceAdapter.persist(verifiedAccount);
            flushAndClear();

            // Then
            RefundAccountJpaEntity found = find(RefundAccountJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.isVerified()).isTrue();
            assertThat(found.getVerifiedAt()).isNotNull();
        }

        @Test
        @DisplayName("성공 - 기존 환불계좌를 수정한다")
        void persist_existingRefundAccount_updates() {
            // Given
            RefundAccountJpaEntity existingEntity =
                    persistAndFlush(
                            RefundAccountJpaEntity.of(
                                    null,
                                    MEMBER_ID.toString(),
                                    BANK_ID,
                                    "1234567890123",
                                    "홍길동",
                                    false,
                                    null,
                                    NOW,
                                    NOW,
                                    null));
            flushAndClear();

            RefundAccount updatedAccount =
                    RefundAccountFixture.createForMember(existingEntity.getId(), MEMBER_ID);

            // When
            RefundAccountId savedId = refundAccountPersistenceAdapter.persist(updatedAccount);
            flushAndClear();

            // Then
            assertThat(savedId.value()).isEqualTo(existingEntity.getId());
            RefundAccountJpaEntity found = find(RefundAccountJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
        }

        @Test
        @DisplayName("성공 - 삭제된 환불계좌를 저장한다 (soft delete)")
        void persist_deletedRefundAccount_savesSoftDeleted() {
            // Given
            RefundAccount deletedAccount = RefundAccountFixture.createDeletedNew();

            // When
            RefundAccountId savedId = refundAccountPersistenceAdapter.persist(deletedAccount);
            flushAndClear();

            // Then
            RefundAccountJpaEntity found = find(RefundAccountJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("성공 - 다른 은행의 환불계좌를 저장한다")
        void persist_differentBank_saves() {
            // Given
            UUID otherMemberId = UUID.randomUUID();
            Long otherBankId = 2L;
            RefundAccount customAccount =
                    RefundAccountFixture.createCustomNew(
                            otherMemberId, otherBankId, "9999888877776666", "김철수", true);

            // When
            RefundAccountId savedId = refundAccountPersistenceAdapter.persist(customAccount);
            flushAndClear();

            // Then
            RefundAccountJpaEntity found = find(RefundAccountJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getBankId()).isEqualTo(otherBankId);
            assertThat(found.getAccountNumber()).isEqualTo("9999888877776666");
            assertThat(found.getAccountHolderName()).isEqualTo("김철수");
        }
    }
}
