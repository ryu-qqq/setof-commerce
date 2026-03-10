package com.ryuqq.setof.application.qna.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.QnaCommandFixtures;
import com.ryuqq.setof.application.qna.dto.bundle.ProductQnaBundle;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.application.qna.factory.QnaCommandFactory;
import com.ryuqq.setof.application.qna.validator.ProductQnaRegistrationValidator;
import com.ryuqq.setof.domain.qna.exception.QnaImageNotAllowedException;
import com.ryuqq.setof.domain.qna.vo.QnaType;
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
@DisplayName("ProductQnaRegistrationStrategy 단위 테스트")
class ProductQnaRegistrationStrategyTest {

    @InjectMocks private ProductQnaRegistrationStrategy sut;

    @Mock private ProductQnaRegistrationValidator validator;
    @Mock private QnaCommandFactory factory;
    @Mock private ProductQnaPersistFacade persistFacade;

    @Nested
    @DisplayName("supportType() - 지원 타입 확인")
    class SupportTypeTest {

        @Test
        @DisplayName("지원 타입은 PRODUCT이다")
        void supportType_ReturnsProduct() {
            assertThat(sut.supportType()).isEqualTo(QnaType.PRODUCT);
        }
    }

    @Nested
    @DisplayName("register() - 상품 Q&A 등록")
    class RegisterTest {

        @Test
        @DisplayName("유효한 커맨드로 상품 Q&A를 등록하고 qnaId를 반환한다")
        void register_ValidCommand_ReturnsQnaId() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerProductQnaCommand();
            ProductQnaBundle bundle = QnaCommandFixtures.productQnaBundle();
            Long expectedQnaId = 1001L;

            given(factory.createProductBundle(command)).willReturn(bundle);
            given(persistFacade.persist(bundle)).willReturn(expectedQnaId);

            // when
            Long result = sut.register(command);

            // then
            assertThat(result).isEqualTo(expectedQnaId);
            then(validator).should().validate(command);
            then(factory).should().createProductBundle(command);
            then(persistFacade).should().persist(bundle);
        }

        @Test
        @DisplayName("이미지가 포함된 상품 Q&A 등록 시 QnaImageNotAllowedException을 던진다")
        void register_CommandWithImages_ThrowsQnaImageNotAllowedException() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerProductQnaCommandWithImages();

            // when & then
            assertThatThrownBy(() -> sut.register(command))
                    .isInstanceOf(QnaImageNotAllowedException.class);

            then(factory).shouldHaveNoInteractions();
            then(persistFacade).shouldHaveNoInteractions();
        }
    }
}
