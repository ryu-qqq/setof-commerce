package com.ryuqq.setof.domain.qna.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaStatus Enum VO 단위 테스트")
class QnaStatusTest {

    @Nested
    @DisplayName("displayName() 테스트")
    class DisplayNameTest {

        @Test
        @DisplayName("PENDING의 displayName은 '답변 대기'이다")
        void pendingDisplayName() {
            assertThat(QnaStatus.PENDING.displayName()).isEqualTo("답변 대기");
        }

        @Test
        @DisplayName("ANSWERED의 displayName은 '답변 완료'이다")
        void answeredDisplayName() {
            assertThat(QnaStatus.ANSWERED.displayName()).isEqualTo("답변 완료");
        }

        @Test
        @DisplayName("CLOSED의 displayName은 '종료'이다")
        void closedDisplayName() {
            assertThat(QnaStatus.CLOSED.displayName()).isEqualTo("종료");
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드 테스트")
    class StatusCheckTest {

        @Test
        @DisplayName("PENDING.isPending()은 true를 반환한다")
        void pendingIsPending() {
            assertThat(QnaStatus.PENDING.isPending()).isTrue();
            assertThat(QnaStatus.PENDING.isAnswered()).isFalse();
            assertThat(QnaStatus.PENDING.isClosed()).isFalse();
        }

        @Test
        @DisplayName("ANSWERED.isAnswered()는 true를 반환한다")
        void answeredIsAnswered() {
            assertThat(QnaStatus.ANSWERED.isAnswered()).isTrue();
            assertThat(QnaStatus.ANSWERED.isPending()).isFalse();
            assertThat(QnaStatus.ANSWERED.isClosed()).isFalse();
        }

        @Test
        @DisplayName("CLOSED.isClosed()는 true를 반환한다")
        void closedIsClosed() {
            assertThat(QnaStatus.CLOSED.isClosed()).isTrue();
            assertThat(QnaStatus.CLOSED.isPending()).isFalse();
            assertThat(QnaStatus.CLOSED.isAnswered()).isFalse();
        }
    }

    @Nested
    @DisplayName("Enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 QnaStatus 값이 존재한다")
        void allValuesExist() {
            assertThat(QnaStatus.values())
                    .containsExactly(
                            QnaStatus.PENDING,
                            QnaStatus.ANSWERED,
                            QnaStatus.CLOSED);
        }
    }
}
