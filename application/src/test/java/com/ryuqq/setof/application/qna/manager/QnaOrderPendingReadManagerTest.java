package com.ryuqq.setof.application.qna.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.port.out.query.QnaOrderPendingQueryPort;
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
@DisplayName("QnaOrderPendingReadManager 단위 테스트")
class QnaOrderPendingReadManagerTest {

    @InjectMocks private QnaOrderPendingReadManager sut;

    @Mock private QnaOrderPendingQueryPort queryPort;

    @Nested
    @DisplayName("existsPendingOrderQna() - 미답변 주문 Q&A 존재 여부 조회")
    class ExistsPendingOrderQnaTest {

        @Test
        @DisplayName("미답변 주문 Q&A가 존재하면 true를 반환한다")
        void existsPendingOrderQna_ExistingPending_ReturnsTrue() {
            // given
            long userId = 100L;
            long legacyOrderId = 700L;

            given(queryPort.existsPendingOrderQna(userId, legacyOrderId)).willReturn(true);

            // when
            boolean result = sut.existsPendingOrderQna(userId, legacyOrderId);

            // then
            assertThat(result).isTrue();
            then(queryPort).should().existsPendingOrderQna(userId, legacyOrderId);
        }

        @Test
        @DisplayName("미답변 주문 Q&A가 없으면 false를 반환한다")
        void existsPendingOrderQna_NoPending_ReturnsFalse() {
            // given
            long userId = 100L;
            long legacyOrderId = 700L;

            given(queryPort.existsPendingOrderQna(userId, legacyOrderId)).willReturn(false);

            // when
            boolean result = sut.existsPendingOrderQna(userId, legacyOrderId);

            // then
            assertThat(result).isFalse();
            then(queryPort).should().existsPendingOrderQna(userId, legacyOrderId);
        }
    }
}
