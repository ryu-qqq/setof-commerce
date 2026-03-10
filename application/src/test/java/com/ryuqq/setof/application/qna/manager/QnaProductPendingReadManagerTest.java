package com.ryuqq.setof.application.qna.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.port.out.query.QnaProductPendingQueryPort;
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
@DisplayName("QnaProductPendingReadManager 단위 테스트")
class QnaProductPendingReadManagerTest {

    @InjectMocks private QnaProductPendingReadManager sut;

    @Mock private QnaProductPendingQueryPort queryPort;

    @Nested
    @DisplayName("existsPendingProductQna() - 미답변 상품 Q&A 존재 여부 조회")
    class ExistsPendingProductQnaTest {

        @Test
        @DisplayName("미답변 상품 Q&A가 존재하면 true를 반환한다")
        void existsPendingProductQna_ExistingPending_ReturnsTrue() {
            // given
            long userId = 100L;
            long productGroupId = 500L;

            given(queryPort.existsPendingProductQna(userId, productGroupId)).willReturn(true);

            // when
            boolean result = sut.existsPendingProductQna(userId, productGroupId);

            // then
            assertThat(result).isTrue();
            then(queryPort).should().existsPendingProductQna(userId, productGroupId);
        }

        @Test
        @DisplayName("미답변 상품 Q&A가 없으면 false를 반환한다")
        void existsPendingProductQna_NoPending_ReturnsFalse() {
            // given
            long userId = 100L;
            long productGroupId = 500L;

            given(queryPort.existsPendingProductQna(userId, productGroupId)).willReturn(false);

            // when
            boolean result = sut.existsPendingProductQna(userId, productGroupId);

            // then
            assertThat(result).isFalse();
            then(queryPort).should().existsPendingProductQna(userId, productGroupId);
        }
    }
}
