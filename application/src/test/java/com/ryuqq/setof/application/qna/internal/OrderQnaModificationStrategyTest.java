package com.ryuqq.setof.application.qna.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.qna.QnaCommandFixtures;
import com.ryuqq.setof.application.qna.dto.command.ModifyQnaCommand;
import com.ryuqq.setof.application.qna.factory.QnaCommandFactory;
import com.ryuqq.setof.application.qna.manager.QnaCommandManager;
import com.ryuqq.setof.application.qna.manager.QnaImageCommandManager;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.aggregate.QnaImages;
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
@DisplayName("OrderQnaModificationStrategy 단위 테스트")
class OrderQnaModificationStrategyTest {

    @InjectMocks private OrderQnaModificationStrategy sut;

    @Mock private QnaCommandManager commandManager;
    @Mock private QnaImageCommandManager imageCommandManager;
    @Mock private QnaCommandFactory factory;

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
    @DisplayName("modify() - 주문 Q&A 수정")
    class ModifyTest {

        @Test
        @DisplayName("이미지 없는 커맨드로 Q&A를 수정하면 이미지 관련 작업을 하지 않는다")
        void modify_CommandWithoutImages_DoesNotTouchImages() {
            // given
            Qna qna = QnaFixtures.pendingQna();
            ModifyQnaCommand command = QnaCommandFixtures.modifyOrderQnaCommand();
            Long expectedQnaId = qna.legacyIdValue();
            UpdateContext<Long, QnaUpdateData> context =
                    new UpdateContext<>(
                            command.qnaId(), QnaFixtures.defaultQnaUpdateData(), Instant.now());

            given(factory.createUpdateContext(command)).willReturn(context);
            given(commandManager.persist(any(Qna.class))).willReturn(expectedQnaId);

            // when
            sut.modify(qna, command);

            // then
            then(factory).should().createUpdateContext(command);
            then(commandManager).should().persist(any(Qna.class));
            then(imageCommandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("이미지가 포함된 커맨드로 Q&A를 수정하면 기존 이미지를 삭제하고 새 이미지를 저장한다")
        void modify_CommandWithImages_DeletesAndPersistsImages() {
            // given
            Qna qna = QnaFixtures.pendingQna();
            ModifyQnaCommand command = QnaCommandFixtures.modifyOrderQnaCommandWithImages();
            Long expectedQnaId = qna.legacyIdValue();
            UpdateContext<Long, QnaUpdateData> context =
                    new UpdateContext<>(
                            command.qnaId(), QnaFixtures.defaultQnaUpdateData(), Instant.now());
            QnaImages newImages = QnaFixtures.qnaImagesWithCount(command.imageUrls().size());

            given(factory.createUpdateContext(command)).willReturn(context);
            given(commandManager.persist(any(Qna.class))).willReturn(expectedQnaId);
            given(factory.createImages(command.imageUrls())).willReturn(newImages);

            // when
            sut.modify(qna, command);

            // then
            then(imageCommandManager).should().deleteAllByQnaId(expectedQnaId);
            then(imageCommandManager).should().persistAll(expectedQnaId, newImages);
        }
    }
}
