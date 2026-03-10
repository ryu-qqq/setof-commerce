package com.ryuqq.setof.application.qna.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.QnaCommandFixtures;
import com.ryuqq.setof.application.qna.dto.bundle.OrderQnaBundle;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.application.qna.factory.QnaCommandFactory;
import com.ryuqq.setof.application.qna.validator.OrderQnaRegistrationValidator;
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
@DisplayName("OrderQnaRegistrationStrategy 단위 테스트")
class OrderQnaRegistrationStrategyTest {

    @InjectMocks private OrderQnaRegistrationStrategy sut;

    @Mock private OrderQnaRegistrationValidator validator;
    @Mock private QnaCommandFactory factory;
    @Mock private OrderQnaPersistFacade persistFacade;

    @Nested
    @DisplayName("supportType() - 지원 타입 확인")
    class SupportTypeTest {

        @Test
        @DisplayName("지원 타입은 ORDER이다")
        void supportType_ReturnsOrder() {
            assertThat(sut.supportType()).isEqualTo(QnaType.ORDER);
        }
    }

    @Nested
    @DisplayName("register() - 주문 Q&A 등록")
    class RegisterTest {

        @Test
        @DisplayName("유효한 커맨드로 주문 Q&A를 등록하고 qnaId를 반환한다")
        void register_ValidCommand_ReturnsQnaId() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerOrderQnaCommand();
            OrderQnaBundle bundle = QnaCommandFixtures.orderQnaBundle();
            Long expectedQnaId = 2001L;

            given(factory.createOrderBundle(command)).willReturn(bundle);
            given(persistFacade.persist(bundle)).willReturn(expectedQnaId);

            // when
            Long result = sut.register(command);

            // then
            assertThat(result).isEqualTo(expectedQnaId);
            then(validator).should().validate(command);
            then(factory).should().createOrderBundle(command);
            then(persistFacade).should().persist(bundle);
        }

        @Test
        @DisplayName("이미지가 포함된 주문 Q&A도 등록할 수 있다")
        void register_CommandWithImages_ReturnsQnaId() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerOrderQnaCommandWithImages();
            OrderQnaBundle bundle = QnaCommandFixtures.orderQnaBundleWithImages();
            Long expectedQnaId = 2002L;

            given(factory.createOrderBundle(command)).willReturn(bundle);
            given(persistFacade.persist(bundle)).willReturn(expectedQnaId);

            // when
            Long result = sut.register(command);

            // then
            assertThat(result).isEqualTo(expectedQnaId);
            then(validator).should().validate(command);
            then(factory).should().createOrderBundle(command);
            then(persistFacade).should().persist(bundle);
        }
    }
}
