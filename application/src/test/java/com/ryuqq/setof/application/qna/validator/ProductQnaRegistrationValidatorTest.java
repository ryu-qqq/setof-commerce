package com.ryuqq.setof.application.qna.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.QnaCommandFixtures;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.application.qna.manager.QnaProductPendingReadManager;
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
@DisplayName("ProductQnaRegistrationValidator 단위 테스트")
class ProductQnaRegistrationValidatorTest {

    @InjectMocks private ProductQnaRegistrationValidator sut;

    @Mock private QnaProductPendingReadManager readManager;

    @Nested
    @DisplayName("validate() - 상품 Q&A 등록 검증")
    class ValidateTest {

        @Test
        @DisplayName("미답변 상품 Q&A가 없으면 검증을 통과한다")
        void validate_NoPendingQna_PassesValidation() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerProductQnaCommand();

            given(readManager.existsPendingProductQna(command.userId(), command.productGroupId()))
                    .willReturn(false);

            // when & then
            sut.validate(command);

            then(readManager)
                    .should()
                    .existsPendingProductQna(command.userId(), command.productGroupId());
        }

        @Test
        @DisplayName("미답변 상품 Q&A가 존재하면 QnaDuplicatePendingException을 던진다")
        void validate_PendingQnaExists_ThrowsQnaDuplicatePendingException() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerProductQnaCommand();

            given(readManager.existsPendingProductQna(command.userId(), command.productGroupId()))
                    .willReturn(true);

            // when & then
            assertThatThrownBy(() -> sut.validate(command))
                    .isInstanceOf(QnaDuplicatePendingException.class);

            then(readManager)
                    .should()
                    .existsPendingProductQna(command.userId(), command.productGroupId());
        }
    }
}
