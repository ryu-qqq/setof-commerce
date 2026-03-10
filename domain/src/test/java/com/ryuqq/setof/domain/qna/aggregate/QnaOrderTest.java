package com.ryuqq.setof.domain.qna.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaOrder Aggregate 단위 테스트")
class QnaOrderTest {

    @Nested
    @DisplayName("forNew() - 신규 Q&A 주문 매핑 생성")
    class ForNewTest {

        @Test
        @DisplayName("주문 ID로 신규 QnaOrder를 생성한다")
        void createNewQnaOrder() {
            // given
            LegacyOrderId legacyOrderId = LegacyOrderId.of(700L);
            Instant now = CommonVoFixtures.now();

            // when
            QnaOrder qnaOrder = QnaOrder.forNew(legacyOrderId, now);

            // then
            assertThat(qnaOrder.legacyOrderId()).isEqualTo(legacyOrderId);
            assertThat(qnaOrder.legacyOrderIdValue()).isEqualTo(700L);
            assertThat(qnaOrder.qnaId()).isNull();
            assertThat(qnaOrder.qnaIdValue()).isNull();
            assertThat(qnaOrder.createdAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("legacyOrderId가 null이면 예외가 발생한다")
        void createWithNullLegacyOrderIdThrowsException() {
            // when & then
            assertThatThrownBy(() -> QnaOrder.forNew(null, CommonVoFixtures.now()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("legacyOrderId");
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성에서 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("영속성에서 QnaOrder를 복원한다")
        void reconstituteQnaOrder() {
            // when
            QnaOrder qnaOrder = QnaFixtures.activeQnaOrder();

            // then
            assertThat(qnaOrder.qnaId()).isNotNull();
            assertThat(qnaOrder.legacyOrderId()).isNotNull();
        }
    }

    @Nested
    @DisplayName("withQnaId() - qnaId 설정")
    class WithQnaIdTest {

        @Test
        @DisplayName("Qna persist 후 qnaId를 설정한 새 인스턴스를 반환한다")
        void withQnaIdReturnNewInstanceWithQnaId() {
            // given
            QnaOrder qnaOrder = QnaFixtures.newQnaOrder();
            LegacyQnaId qnaId = QnaFixtures.defaultLegacyQnaId();

            // when
            QnaOrder withId = qnaOrder.withQnaId(qnaId);

            // then
            assertThat(withId.qnaId()).isEqualTo(qnaId);
            assertThat(withId.qnaIdValue()).isEqualTo(1001L);
            assertThat(withId.legacyOrderId()).isEqualTo(qnaOrder.legacyOrderId());
        }

        @Test
        @DisplayName("withQnaId()는 원본 인스턴스를 변경하지 않는다")
        void withQnaIdDoesNotMutateOriginal() {
            // given
            QnaOrder original = QnaFixtures.newQnaOrder();

            // when
            original.withQnaId(QnaFixtures.defaultLegacyQnaId());

            // then
            assertThat(original.qnaId()).isNull();
        }
    }
}
