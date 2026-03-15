package com.ryuqq.setof.domain.imagevariant.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ImageVariantStatus Enum 테스트")
class ImageVariantStatusTest {

    @Nested
    @DisplayName("상태 확인 메서드 테스트")
    class StatusCheckTest {

        @Test
        @DisplayName("PENDING 상태는 isPending()이 true이다")
        void pendingIsPending() {
            assertThat(ImageVariantStatus.PENDING.isPending()).isTrue();
            assertThat(ImageVariantStatus.PENDING.isProcessing()).isFalse();
            assertThat(ImageVariantStatus.PENDING.isCompleted()).isFalse();
            assertThat(ImageVariantStatus.PENDING.isFailed()).isFalse();
        }

        @Test
        @DisplayName("PROCESSING 상태는 isProcessing()이 true이다")
        void processingIsProcessing() {
            assertThat(ImageVariantStatus.PROCESSING.isProcessing()).isTrue();
            assertThat(ImageVariantStatus.PROCESSING.isPending()).isFalse();
        }

        @Test
        @DisplayName("COMPLETED 상태는 isCompleted()이 true이다")
        void completedIsCompleted() {
            assertThat(ImageVariantStatus.COMPLETED.isCompleted()).isTrue();
            assertThat(ImageVariantStatus.COMPLETED.isPending()).isFalse();
        }

        @Test
        @DisplayName("FAILED 상태는 isFailed()이 true이다")
        void failedIsFailed() {
            assertThat(ImageVariantStatus.FAILED.isFailed()).isTrue();
            assertThat(ImageVariantStatus.FAILED.isPending()).isFalse();
        }
    }

    @Nested
    @DisplayName("isTerminal() - 종료 상태 확인")
    class IsTerminalTest {

        @Test
        @DisplayName("COMPLETED는 종료 상태이다")
        void completedIsTerminal() {
            assertThat(ImageVariantStatus.COMPLETED.isTerminal()).isTrue();
        }

        @Test
        @DisplayName("FAILED는 종료 상태이다")
        void failedIsTerminal() {
            assertThat(ImageVariantStatus.FAILED.isTerminal()).isTrue();
        }

        @Test
        @DisplayName("PENDING은 종료 상태가 아니다")
        void pendingIsNotTerminal() {
            assertThat(ImageVariantStatus.PENDING.isTerminal()).isFalse();
        }

        @Test
        @DisplayName("PROCESSING은 종료 상태가 아니다")
        void processingIsNotTerminal() {
            assertThat(ImageVariantStatus.PROCESSING.isTerminal()).isFalse();
        }
    }

    @Nested
    @DisplayName("canProcess() - 처리 가능 여부")
    class CanProcessTest {

        @Test
        @DisplayName("PENDING만 처리 가능하다")
        void onlyPendingCanProcess() {
            assertThat(ImageVariantStatus.PENDING.canProcess()).isTrue();
            assertThat(ImageVariantStatus.PROCESSING.canProcess()).isFalse();
            assertThat(ImageVariantStatus.COMPLETED.canProcess()).isFalse();
            assertThat(ImageVariantStatus.FAILED.canProcess()).isFalse();
        }
    }

    @Nested
    @DisplayName("nextOnSuccess() - 다음 상태 전환")
    class NextOnSuccessTest {

        @Test
        @DisplayName("PENDING에서 성공 시 PROCESSING으로 전환된다")
        void pendingNextOnSuccessIsProcessing() {
            assertThat(ImageVariantStatus.PENDING.nextOnSuccess())
                    .isEqualTo(ImageVariantStatus.PROCESSING);
        }

        @Test
        @DisplayName("PROCESSING에서 성공 시 COMPLETED로 전환된다")
        void processingNextOnSuccessIsCompleted() {
            assertThat(ImageVariantStatus.PROCESSING.nextOnSuccess())
                    .isEqualTo(ImageVariantStatus.COMPLETED);
        }

        @Test
        @DisplayName("COMPLETED에서 nextOnSuccess() 호출 시 예외가 발생한다")
        void completedNextOnSuccessThrowsException() {
            assertThatThrownBy(() -> ImageVariantStatus.COMPLETED.nextOnSuccess())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("종료 상태");
        }

        @Test
        @DisplayName("FAILED에서 nextOnSuccess() 호출 시 예외가 발생한다")
        void failedNextOnSuccessThrowsException() {
            assertThatThrownBy(() -> ImageVariantStatus.FAILED.nextOnSuccess())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("종료 상태");
        }
    }

    @Nested
    @DisplayName("description() 테스트")
    class DescriptionTest {

        @Test
        @DisplayName("각 상태의 설명을 반환한다")
        void eachStatusHasCorrectDescription() {
            assertThat(ImageVariantStatus.PENDING.description()).isEqualTo("대기");
            assertThat(ImageVariantStatus.PROCESSING.description()).isEqualTo("처리중");
            assertThat(ImageVariantStatus.COMPLETED.description()).isEqualTo("완료");
            assertThat(ImageVariantStatus.FAILED.description()).isEqualTo("실패");
        }
    }
}
