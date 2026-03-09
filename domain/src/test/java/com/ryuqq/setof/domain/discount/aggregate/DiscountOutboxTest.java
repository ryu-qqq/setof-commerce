package com.ryuqq.setof.domain.discount.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.discount.id.DiscountOutboxId;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.OutboxStatus;
import com.ryuqq.setof.domain.discount.vo.OutboxTargetKey;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DiscountOutbox Aggregate 테스트")
class DiscountOutboxTest {

    @Nested
    @DisplayName("forNew() - 신규 아웃박스 생성")
    class ForNewTest {

        @Test
        @DisplayName("BRAND 타입으로 신규 아웃박스를 생성한다")
        void createNewOutboxForBrand() {
            Instant now = CommonVoFixtures.now();

            DiscountOutbox outbox = DiscountOutbox.forNew(DiscountTargetType.BRAND, 100L, now);

            assertThat(outbox.isNew()).isTrue();
            assertThat(outbox.status()).isEqualTo(OutboxStatus.PENDING);
            assertThat(outbox.retryCount()).isZero();
            assertThat(outbox.payload()).isNull();
            assertThat(outbox.failReason()).isNull();
            assertThat(outbox.createdAt()).isEqualTo(now);
            assertThat(outbox.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("SELLER 타입으로 신규 아웃박스를 생성한다")
        void createNewOutboxForSeller() {
            Instant now = CommonVoFixtures.now();

            DiscountOutbox outbox = DiscountOutbox.forNew(DiscountTargetType.SELLER, 50L, now);

            assertThat(outbox.targetType()).isEqualTo(DiscountTargetType.SELLER);
            assertThat(outbox.targetId()).isEqualTo(50L);
            assertThat(outbox.isPending()).isTrue();
        }

        @Test
        @DisplayName("신규 생성 시 ID는 null이고 isNew()는 true이다")
        void newOutboxHasNullId() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(DiscountTargetType.PRODUCT, 1L, CommonVoFixtures.now());

            assertThat(outbox.isNew()).isTrue();
            assertThat(outbox.idValue()).isNull();
        }

        @Test
        @DisplayName("targetKeyValue()는 'TYPE:id' 형식을 반환한다")
        void targetKeyValueReturnsFormattedString() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(DiscountTargetType.BRAND, 123L, CommonVoFixtures.now());

            assertThat(outbox.targetKeyValue()).isEqualTo("BRAND:123");
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("PENDING 상태의 아웃박스를 복원한다")
        void reconstitutesPendingOutbox() {
            Instant createdAt = CommonVoFixtures.yesterday();
            Instant updatedAt = CommonVoFixtures.yesterday();

            DiscountOutbox outbox =
                    DiscountOutbox.reconstitute(
                            DiscountOutboxId.of(1L),
                            OutboxTargetKey.of(DiscountTargetType.BRAND, 100L),
                            OutboxStatus.PENDING,
                            0,
                            null,
                            null,
                            createdAt,
                            updatedAt);

            assertThat(outbox.isNew()).isFalse();
            assertThat(outbox.idValue()).isEqualTo(1L);
            assertThat(outbox.status()).isEqualTo(OutboxStatus.PENDING);
            assertThat(outbox.retryCount()).isZero();
            assertThat(outbox.createdAt()).isEqualTo(createdAt);
        }

        @Test
        @DisplayName("PUBLISHED 상태의 아웃박스를 복원한다")
        void reconstitutesPublishedOutbox() {
            DiscountOutbox outbox =
                    DiscountOutbox.reconstitute(
                            DiscountOutboxId.of(2L),
                            OutboxTargetKey.of(DiscountTargetType.SELLER, 50L),
                            OutboxStatus.PUBLISHED,
                            1,
                            "{\"key\":\"value\"}",
                            null,
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            assertThat(outbox.isPublished()).isTrue();
            assertThat(outbox.retryCount()).isEqualTo(1);
            assertThat(outbox.payload()).isEqualTo("{\"key\":\"value\"}");
        }

        @Test
        @DisplayName("FAILED 상태의 아웃박스를 복원한다")
        void reconstitutesFailedOutbox() {
            DiscountOutbox outbox =
                    DiscountOutbox.reconstitute(
                            DiscountOutboxId.of(3L),
                            OutboxTargetKey.of(DiscountTargetType.CATEGORY, 200L),
                            OutboxStatus.FAILED,
                            3,
                            null,
                            "처리 실패",
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            assertThat(outbox.status()).isEqualTo(OutboxStatus.FAILED);
            assertThat(outbox.isMaxRetryExceeded()).isTrue();
            assertThat(outbox.failReason()).isEqualTo("처리 실패");
        }
    }

    @Nested
    @DisplayName("markPublished() - SQS 발행 처리")
    class MarkPublishedTest {

        @Test
        @DisplayName("PENDING 상태에서 PUBLISHED로 전이한다")
        void transitionFromPendingToPublished() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(
                            DiscountTargetType.BRAND, 100L, CommonVoFixtures.yesterday());
            Instant publishedAt = CommonVoFixtures.now();
            String payload = "{\"targetType\":\"BRAND\",\"targetId\":100}";

            outbox.markPublished(payload, publishedAt);

            assertThat(outbox.status()).isEqualTo(OutboxStatus.PUBLISHED);
            assertThat(outbox.isPublished()).isTrue();
            assertThat(outbox.isPending()).isFalse();
            assertThat(outbox.payload()).isEqualTo(payload);
            assertThat(outbox.updatedAt()).isEqualTo(publishedAt);
        }

        @Test
        @DisplayName("PUBLISHED 상태에서 markPublished() 호출 시 예외가 발생한다")
        void throwExceptionWhenNotPending() {
            DiscountOutbox outbox =
                    DiscountOutbox.reconstitute(
                            DiscountOutboxId.of(1L),
                            OutboxTargetKey.of(DiscountTargetType.BRAND, 100L),
                            OutboxStatus.PUBLISHED,
                            0,
                            "payload",
                            null,
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            assertThatThrownBy(() -> outbox.markPublished("new-payload", CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("PENDING");
        }

        @Test
        @DisplayName("COMPLETED 상태에서 markPublished() 호출 시 예외가 발생한다")
        void throwExceptionWhenCompleted() {
            DiscountOutbox outbox =
                    DiscountOutbox.reconstitute(
                            DiscountOutboxId.of(1L),
                            OutboxTargetKey.of(DiscountTargetType.BRAND, 100L),
                            OutboxStatus.COMPLETED,
                            0,
                            "payload",
                            null,
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            assertThatThrownBy(() -> outbox.markPublished("new-payload", CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    @DisplayName("markCompleted() - 처리 완료")
    class MarkCompletedTest {

        @Test
        @DisplayName("PUBLISHED 상태에서 COMPLETED로 전이한다")
        void transitionFromPublishedToCompleted() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(
                            DiscountTargetType.BRAND, 100L, CommonVoFixtures.yesterday());
            outbox.markPublished("payload", CommonVoFixtures.yesterday());
            Instant completedAt = CommonVoFixtures.now();

            outbox.markCompleted(completedAt);

            assertThat(outbox.status()).isEqualTo(OutboxStatus.COMPLETED);
            assertThat(outbox.isPublished()).isFalse();
            assertThat(outbox.updatedAt()).isEqualTo(completedAt);
        }

        @Test
        @DisplayName("PENDING 상태에서 markCompleted() 호출 시 예외가 발생한다")
        void throwExceptionWhenNotPublished() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(
                            DiscountTargetType.BRAND, 100L, CommonVoFixtures.yesterday());

            assertThatThrownBy(() -> outbox.markCompleted(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("PUBLISHED");
        }
    }

    @Nested
    @DisplayName("markFailed() - 처리 실패 및 재시도 판단")
    class MarkFailedTest {

        @Test
        @DisplayName("재시도 횟수가 MAX_RETRY 미만이면 PENDING으로 복구한다")
        void recoverToPendingWhenRetryCountBelowMax() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(
                            DiscountTargetType.BRAND, 100L, CommonVoFixtures.yesterday());
            outbox.markPublished("payload", CommonVoFixtures.yesterday());

            outbox.markFailed("첫 번째 실패", CommonVoFixtures.now());

            assertThat(outbox.status()).isEqualTo(OutboxStatus.PENDING);
            assertThat(outbox.retryCount()).isEqualTo(1);
            assertThat(outbox.failReason()).isEqualTo("첫 번째 실패");
        }

        @Test
        @DisplayName("재시도 횟수가 두 번이면 아직 PENDING이다")
        void stillPendingAfterTwoFailures() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(
                            DiscountTargetType.BRAND, 100L, CommonVoFixtures.yesterday());
            outbox.markPublished("payload", CommonVoFixtures.yesterday());
            outbox.markFailed("1차 실패", CommonVoFixtures.now());

            outbox.markPublished("payload2", CommonVoFixtures.now());
            outbox.markFailed("2차 실패", CommonVoFixtures.now());

            assertThat(outbox.status()).isEqualTo(OutboxStatus.PENDING);
            assertThat(outbox.retryCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("재시도 횟수가 MAX_RETRY(3)에 도달하면 FAILED로 전환한다")
        void transitionToFailedWhenMaxRetryReached() {
            DiscountOutbox outbox =
                    DiscountOutbox.reconstitute(
                            DiscountOutboxId.of(1L),
                            OutboxTargetKey.of(DiscountTargetType.BRAND, 100L),
                            OutboxStatus.PUBLISHED,
                            2,
                            "payload",
                            null,
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            outbox.markFailed("최종 실패", CommonVoFixtures.now());

            assertThat(outbox.status()).isEqualTo(OutboxStatus.FAILED);
            assertThat(outbox.retryCount()).isEqualTo(3);
            assertThat(outbox.isMaxRetryExceeded()).isTrue();
            assertThat(outbox.failReason()).isEqualTo("최종 실패");
        }

        @Test
        @DisplayName("markFailed() 후 updatedAt이 갱신된다")
        void updatedAtIsRefreshedAfterFailure() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(
                            DiscountTargetType.BRAND, 100L, CommonVoFixtures.yesterday());
            outbox.markPublished("payload", CommonVoFixtures.yesterday());
            Instant failedAt = CommonVoFixtures.now();

            outbox.markFailed("실패", failedAt);

            assertThat(outbox.updatedAt()).isEqualTo(failedAt);
        }
    }

    @Nested
    @DisplayName("recoverStuck() - Stuck 복구")
    class RecoverStuckTest {

        @Test
        @DisplayName("PUBLISHED 상태에서 retryCount < MAX_RETRY이면 PENDING으로 복구한다")
        void recoverStuckPublishedToPending() {
            DiscountOutbox outbox =
                    DiscountOutbox.reconstitute(
                            DiscountOutboxId.of(1L),
                            OutboxTargetKey.of(DiscountTargetType.BRAND, 100L),
                            OutboxStatus.PUBLISHED,
                            1,
                            "payload",
                            null,
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());
            Instant now = CommonVoFixtures.now();

            outbox.recoverStuck(now);

            assertThat(outbox.status()).isEqualTo(OutboxStatus.PENDING);
            assertThat(outbox.retryCount()).isEqualTo(2);
            assertThat(outbox.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("PUBLISHED 상태에서 recoverStuck() 시 retryCount >= MAX_RETRY이면 FAILED로 전환한다")
        void recoverStuckTransitionsToFailedWhenMaxRetryReached() {
            DiscountOutbox outbox =
                    DiscountOutbox.reconstitute(
                            DiscountOutboxId.of(1L),
                            OutboxTargetKey.of(DiscountTargetType.BRAND, 100L),
                            OutboxStatus.PUBLISHED,
                            2,
                            "payload",
                            null,
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            outbox.recoverStuck(CommonVoFixtures.now());

            assertThat(outbox.status()).isEqualTo(OutboxStatus.FAILED);
            assertThat(outbox.retryCount()).isEqualTo(3);
            assertThat(outbox.failReason()).isEqualTo("Stuck recovery exceeded max retry");
        }

        @Test
        @DisplayName("PENDING 상태에서 recoverStuck() 호출 시 예외가 발생한다")
        void throwExceptionWhenNotPublished() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(
                            DiscountTargetType.BRAND, 100L, CommonVoFixtures.yesterday());

            assertThatThrownBy(() -> outbox.recoverStuck(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("PUBLISHED");
        }

        @Test
        @DisplayName("COMPLETED 상태에서 recoverStuck() 호출 시 예외가 발생한다")
        void throwExceptionWhenCompleted() {
            DiscountOutbox outbox =
                    DiscountOutbox.reconstitute(
                            DiscountOutboxId.of(1L),
                            OutboxTargetKey.of(DiscountTargetType.BRAND, 100L),
                            OutboxStatus.COMPLETED,
                            0,
                            "payload",
                            null,
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            assertThatThrownBy(() -> outbox.recoverStuck(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드 테스트")
    class StatusCheckTest {

        @Test
        @DisplayName("isPending()은 PENDING 상태일 때만 true이다")
        void isPendingReturnsTrueOnlyForPendingStatus() {
            DiscountOutbox pending =
                    DiscountOutbox.forNew(DiscountTargetType.BRAND, 100L, CommonVoFixtures.now());

            assertThat(pending.isPending()).isTrue();
            assertThat(pending.isPublished()).isFalse();
        }

        @Test
        @DisplayName("isPublished()는 PUBLISHED 상태일 때만 true이다")
        void isPublishedReturnsTrueOnlyForPublishedStatus() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(
                            DiscountTargetType.BRAND, 100L, CommonVoFixtures.yesterday());
            outbox.markPublished("payload", CommonVoFixtures.now());

            assertThat(outbox.isPublished()).isTrue();
            assertThat(outbox.isPending()).isFalse();
        }

        @Test
        @DisplayName("isMaxRetryExceeded()는 retryCount >= 3일 때 true이다")
        void isMaxRetryExceededReturnsTrueWhenCountReachesThree() {
            DiscountOutbox notExceeded =
                    DiscountOutbox.reconstitute(
                            DiscountOutboxId.of(1L),
                            OutboxTargetKey.of(DiscountTargetType.BRAND, 100L),
                            OutboxStatus.PENDING,
                            2,
                            null,
                            null,
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            DiscountOutbox exceeded =
                    DiscountOutbox.reconstitute(
                            DiscountOutboxId.of(2L),
                            OutboxTargetKey.of(DiscountTargetType.BRAND, 200L),
                            OutboxStatus.FAILED,
                            3,
                            null,
                            "실패",
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            assertThat(notExceeded.isMaxRetryExceeded()).isFalse();
            assertThat(exceeded.isMaxRetryExceeded()).isTrue();
        }
    }

    @Nested
    @DisplayName("상태 전이 시나리오 테스트")
    class StateTransitionScenarioTest {

        @Test
        @DisplayName("PENDING → PUBLISHED → COMPLETED 정상 흐름을 검증한다")
        void happyPathPendingToPublishedToCompleted() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(
                            DiscountTargetType.SELLER, 50L, CommonVoFixtures.yesterday());

            assertThat(outbox.status()).isEqualTo(OutboxStatus.PENDING);

            outbox.markPublished(
                    "{\"targetType\":\"SELLER\",\"targetId\":50}", CommonVoFixtures.now());
            assertThat(outbox.status()).isEqualTo(OutboxStatus.PUBLISHED);

            outbox.markCompleted(CommonVoFixtures.now());
            assertThat(outbox.status()).isEqualTo(OutboxStatus.COMPLETED);
        }

        @Test
        @DisplayName("재시도 2회 후 3회째 실패 시 FAILED로 전환하는 흐름을 검증한다")
        void retryThenFailScenario() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(
                            DiscountTargetType.CATEGORY, 300L, CommonVoFixtures.yesterday());

            outbox.markPublished("payload1", CommonVoFixtures.now());
            outbox.markFailed("1차 실패", CommonVoFixtures.now());
            assertThat(outbox.status()).isEqualTo(OutboxStatus.PENDING);
            assertThat(outbox.retryCount()).isEqualTo(1);

            outbox.markPublished("payload2", CommonVoFixtures.now());
            outbox.markFailed("2차 실패", CommonVoFixtures.now());
            assertThat(outbox.status()).isEqualTo(OutboxStatus.PENDING);
            assertThat(outbox.retryCount()).isEqualTo(2);

            outbox.markPublished("payload3", CommonVoFixtures.now());
            outbox.markFailed("3차 실패", CommonVoFixtures.now());
            assertThat(outbox.status()).isEqualTo(OutboxStatus.FAILED);
            assertThat(outbox.retryCount()).isEqualTo(3);
            assertThat(outbox.isMaxRetryExceeded()).isTrue();
        }
    }

    @Nested
    @DisplayName("Accessor 메서드 테스트")
    class AccessorTest {

        @Test
        @DisplayName("id()는 DiscountOutboxId를 반환한다")
        void returnsDiscountOutboxId() {
            DiscountOutbox outbox =
                    DiscountOutbox.reconstitute(
                            DiscountOutboxId.of(99L),
                            OutboxTargetKey.of(DiscountTargetType.BRAND, 100L),
                            OutboxStatus.PENDING,
                            0,
                            null,
                            null,
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            assertThat(outbox.id()).isNotNull();
            assertThat(outbox.id().value()).isEqualTo(99L);
        }

        @Test
        @DisplayName("targetKey()는 OutboxTargetKey를 반환한다")
        void returnsTargetKey() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(DiscountTargetType.PRODUCT, 777L, CommonVoFixtures.now());

            assertThat(outbox.targetKey()).isNotNull();
            assertThat(outbox.targetKey().targetType()).isEqualTo(DiscountTargetType.PRODUCT);
            assertThat(outbox.targetKey().targetId()).isEqualTo(777L);
        }

        @Test
        @DisplayName("targetType()은 DiscountTargetType을 반환한다")
        void returnsTargetType() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(DiscountTargetType.CATEGORY, 10L, CommonVoFixtures.now());

            assertThat(outbox.targetType()).isEqualTo(DiscountTargetType.CATEGORY);
        }

        @Test
        @DisplayName("targetId()는 대상 ID를 반환한다")
        void returnsTargetId() {
            DiscountOutbox outbox =
                    DiscountOutbox.forNew(DiscountTargetType.BRAND, 555L, CommonVoFixtures.now());

            assertThat(outbox.targetId()).isEqualTo(555L);
        }
    }
}
