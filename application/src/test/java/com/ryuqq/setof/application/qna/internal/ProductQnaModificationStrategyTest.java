package com.ryuqq.setof.application.qna.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.qna.QnaCommandFixtures;
import com.ryuqq.setof.application.qna.dto.command.ModifyQnaCommand;
import com.ryuqq.setof.application.qna.factory.QnaCommandFactory;
import com.ryuqq.setof.application.qna.manager.QnaCommandManager;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.exception.QnaImageNotAllowedException;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import com.ryuqq.setof.domain.qna.vo.QnaUpdateData;
import java.time.Instant;
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
@DisplayName("ProductQnaModificationStrategy 단위 테스트")
class ProductQnaModificationStrategyTest {

    @InjectMocks private ProductQnaModificationStrategy sut;

    @Mock private QnaCommandFactory factory;
    @Mock private QnaCommandManager commandManager;

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
    @DisplayName("modify() - 상품 Q&A 수정")
    class ModifyTest {

        @Test
        @DisplayName("유효한 커맨드로 Q&A를 수정하고 저장한다")
        void modify_ValidCommand_ModifiesAndPersists() {
            // given
            Qna qna = QnaFixtures.pendingQna();
            ModifyQnaCommand command = QnaCommandFixtures.modifyProductQnaCommand();
            UpdateContext<Long, QnaUpdateData> context =
                    new UpdateContext<>(
                            command.qnaId(), QnaFixtures.defaultQnaUpdateData(), Instant.now());

            given(factory.createUpdateContext(command)).willReturn(context);
            given(commandManager.persist(any(Qna.class))).willReturn(qna.legacyIdValue());

            // when
            sut.modify(qna, command);

            // then
            then(factory).should().createUpdateContext(command);
            then(commandManager).should().persist(any(Qna.class));
        }

        @Test
        @DisplayName("이미지가 포함된 상품 Q&A 수정 시 QnaImageNotAllowedException을 던진다")
        void modify_CommandWithImages_ThrowsQnaImageNotAllowedException() {
            // given
            Qna qna = QnaFixtures.pendingQna();
            ModifyQnaCommand command = QnaCommandFixtures.modifyProductQnaCommandWithImages();

            // when & then
            assertThatThrownBy(() -> sut.modify(qna, command))
                    .isInstanceOf(QnaImageNotAllowedException.class);

            then(factory).shouldHaveNoInteractions();
            then(commandManager).shouldHaveNoInteractions();
        }
    }
}
