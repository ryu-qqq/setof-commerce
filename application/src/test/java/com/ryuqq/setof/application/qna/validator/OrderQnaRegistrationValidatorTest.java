package com.ryuqq.setof.application.qna.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.QnaCommandFixtures;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.application.qna.manager.QnaOrderPendingReadManager;
import com.ryuqq.setof.domain.qna.exception.QnaDuplicatePendingException;
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
@DisplayName("OrderQnaRegistrationValidator 단위 테스트")
class OrderQnaRegistrationValidatorTest {

    @InjectMocks private OrderQnaRegistrationValidator sut;

    @Mock private QnaOrderPendingReadManager readManager;

    @Nested
    @DisplayName("validate() - 주문 Q&A 등록 검증")
    class ValidateTest {

        @Test
        @DisplayName("미답변 주문 Q&A가 없으면 검증을 통과한다")
        void validate_NoPendingQna_PassesValidation() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerOrderQnaCommand();

            given(readManager.existsPendingOrderQna(command.userId(), command.legacyOrderId()))
                    .willReturn(false);

            // when & then
            sut.validate(command);

            then(readManager)
                    .should()
                    .existsPendingOrderQna(command.userId(), command.legacyOrderId());
        }

        @Test
        @DisplayName("미답변 주문 Q&A가 존재하면 QnaDuplicatePendingException을 던진다")
        void validate_PendingQnaExists_ThrowsQnaDuplicatePendingException() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerOrderQnaCommand();

            given(readManager.existsPendingOrderQna(command.userId(), command.legacyOrderId()))
                    .willReturn(true);

            // when & then
            assertThatThrownBy(() -> sut.validate(command))
                    .isInstanceOf(QnaDuplicatePendingException.class);

            then(readManager)
                    .should()
                    .existsPendingOrderQna(command.userId(), command.legacyOrderId());
        }
    }
}
