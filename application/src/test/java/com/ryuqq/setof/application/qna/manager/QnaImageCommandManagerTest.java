package com.ryuqq.setof.application.qna.manager;

import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.port.out.command.QnaImageCommandPort;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import com.ryuqq.setof.domain.qna.aggregate.QnaImages;
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
@DisplayName("QnaImageCommandManager 단위 테스트")
class QnaImageCommandManagerTest {

    @InjectMocks private QnaImageCommandManager sut;

    @Mock private QnaImageCommandPort imageCommandPort;

    @Nested
    @DisplayName("persistAll() - Q&A 이미지 전체 저장")
    class PersistAllTest {

        @Test
        @DisplayName("qnaId와 이미지 컬렉션을 전달하여 저장한다")
        void persistAll_ValidImages_DelegatesToPort() {
            // given
            long qnaId = 1001L;
            QnaImages images = QnaFixtures.defaultQnaImages();

            // when
            sut.persistAll(qnaId, images);

            // then
            then(imageCommandPort).should().persistAll(qnaId, images);
        }

        @Test
        @DisplayName("빈 이미지 컬렉션도 저장 위임한다")
        void persistAll_EmptyImages_DelegatesToPort() {
            // given
            long qnaId = 1001L;
            QnaImages emptyImages = QnaFixtures.emptyQnaImages();

            // when
            sut.persistAll(qnaId, emptyImages);

            // then
            then(imageCommandPort).should().persistAll(qnaId, emptyImages);
        }
    }

    @Nested
    @DisplayName("deleteAllByQnaId() - Q&A 이미지 전체 삭제")
    class DeleteAllByQnaIdTest {

        @Test
        @DisplayName("qnaId로 이미지를 전체 삭제한다")
        void deleteAllByQnaId_ValidId_DelegatesToPort() {
            // given
            long qnaId = 1001L;

            // when
            sut.deleteAllByQnaId(qnaId);

            // then
            then(imageCommandPort).should().deleteAllByQnaId(qnaId);
        }
    }
}
