package com.ryuqq.setof.application.qna.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.qna.QnaQueryFixtures;
import com.ryuqq.setof.application.qna.dto.response.MyQnaDetailResult;
import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.application.qna.dto.response.MyQnaSliceResult;
import com.ryuqq.setof.application.qna.dto.response.QnaDetailResult;
import com.ryuqq.setof.application.qna.dto.response.QnaPageResult;
import com.ryuqq.setof.application.qna.dto.response.QnaWithAnswersResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaAssembler 단위 테스트")
class QnaAssemblerTest {

    private QnaAssembler sut;

    @BeforeEach
    void setUp() {
        sut = new QnaAssembler();
    }

    @Nested
    @DisplayName("toQnaPageResult() - 상품 Q&A 페이지 결과 조립")
    class ToQnaPageResultTest {

        @Test
        @DisplayName("Q&A 목록으로 QnaPageResult를 조립한다")
        void toQnaPageResult_ValidQnas_ReturnsQnaPageResult() {
            // given
            List<QnaWithAnswersResult> qnas = QnaQueryFixtures.qnaWithAnswersResultList();
            Long viewerUserId = QnaQueryFixtures.USER_ID;
            long totalElements = 2L;

            // when
            QnaPageResult result = sut.toQnaPageResult(qnas, viewerUserId, 0, 10, totalElements);

            // then
            assertThat(result).isNotNull();
            assertThat(result.content()).hasSize(qnas.size());
            assertThat(result.pageMeta().totalElements()).isEqualTo(totalElements);
        }

        @Test
        @DisplayName("빈 Q&A 목록으로 빈 QnaPageResult를 조립한다")
        void toQnaPageResult_EmptyQnas_ReturnsEmptyPageResult() {
            // given
            List<QnaWithAnswersResult> qnas = List.of();

            // when
            QnaPageResult result = sut.toQnaPageResult(qnas, null, 0, 10, 0L);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.pageMeta().totalElements()).isZero();
        }

        @Test
        @DisplayName("비로그인 조회(viewerUserId=null)일 때 작성자명이 마스킹된다")
        void toQnaPageResult_AnonymousViewer_MasksUserName() {
            // given
            QnaWithAnswersResult qna = QnaQueryFixtures.qnaWithAnswersResult(1001L, 100L);
            List<QnaWithAnswersResult> qnas = List.of(qna);

            // when
            QnaPageResult result = sut.toQnaPageResult(qnas, null, 0, 10, 1L);

            // then
            QnaDetailResult detailResult = result.content().get(0);
            assertThat(detailResult.userName()).endsWith("**");
        }

        @Test
        @DisplayName("작성자가 조회할 때 작성자명이 마스킹되지 않는다")
        void toQnaPageResult_OwnerViewer_DoesNotMaskUserName() {
            // given
            long userId = 100L;
            QnaWithAnswersResult qna = QnaQueryFixtures.qnaWithAnswersResult(1001L, userId);
            List<QnaWithAnswersResult> qnas = List.of(qna);

            // when
            QnaPageResult result = sut.toQnaPageResult(qnas, userId, 0, 10, 1L);

            // then
            QnaDetailResult detailResult = result.content().get(0);
            assertThat(detailResult.userName()).isEqualTo(qna.userName());
        }

        @Test
        @DisplayName("비밀글이고 비작성자일 때 content가 마스킹된다")
        void toQnaPageResult_PrivateQnaAndNotOwner_MasksContent() {
            // given
            long authorId = 100L;
            long viewerId = 999L;
            QnaWithAnswersResult privateQna = QnaQueryFixtures.qnaWithAnswersResultPrivate(1001L, authorId);
            List<QnaWithAnswersResult> qnas = List.of(privateQna);

            // when
            QnaPageResult result = sut.toQnaPageResult(qnas, viewerId, 0, 10, 1L);

            // then
            QnaDetailResult detailResult = result.content().get(0);
            assertThat(detailResult.content()).isEqualTo("비밀글 입니다.");
            assertThat(detailResult.answers()).isEmpty();
        }

        @Test
        @DisplayName("비밀글이고 작성자일 때 content가 마스킹되지 않는다")
        void toQnaPageResult_PrivateQnaAndOwner_DoesNotMaskContent() {
            // given
            long userId = 100L;
            QnaWithAnswersResult privateQna = QnaQueryFixtures.qnaWithAnswersResultPrivate(1001L, userId);
            List<QnaWithAnswersResult> qnas = List.of(privateQna);

            // when
            QnaPageResult result = sut.toQnaPageResult(qnas, userId, 0, 10, 1L);

            // then
            QnaDetailResult detailResult = result.content().get(0);
            assertThat(detailResult.content()).isNotEqualTo("비밀글 입니다.");
        }
    }

    @Nested
    @DisplayName("toMyQnaSliceResult() - 내 Q&A 슬라이스 결과 조립")
    class ToMyQnaSliceResultTest {

        @Test
        @DisplayName("Q&A 목록으로 MyQnaSliceResult를 조립한다")
        void toMyQnaSliceResult_ValidQnas_ReturnsSliceResult() {
            // given
            List<MyQnaResult> qnas = QnaQueryFixtures.myProductQnaResultList();
            int requestedSize = 10;

            // when
            MyQnaSliceResult result = sut.toMyQnaSliceResult(qnas, requestedSize);

            // then
            assertThat(result).isNotNull();
            assertThat(result.content()).hasSize(qnas.size());
        }

        @Test
        @DisplayName("빈 목록으로 빈 MyQnaSliceResult를 조립한다")
        void toMyQnaSliceResult_EmptyQnas_ReturnsEmptySliceResult() {
            // given
            List<MyQnaResult> qnas = List.of();

            // when
            MyQnaSliceResult result = sut.toMyQnaSliceResult(qnas, 10);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.hasNext()).isFalse();
            assertThat(result.sliceMeta().cursor()).isNull();
        }

        @Test
        @DisplayName("결과가 requestedSize보다 많으면 hasNext가 true이다")
        void toMyQnaSliceResult_MoreThanRequestedSize_HasNextIsTrue() {
            // given
            List<MyQnaResult> qnas = QnaQueryFixtures.myProductQnaResultList(11);
            int requestedSize = 10;

            // when
            MyQnaSliceResult result = sut.toMyQnaSliceResult(qnas, requestedSize);

            // then
            assertThat(result.hasNext()).isTrue();
            assertThat(result.content()).hasSize(requestedSize);
        }

        @Test
        @DisplayName("결과가 requestedSize 이하면 hasNext가 false이다")
        void toMyQnaSliceResult_LessThanRequestedSize_HasNextIsFalse() {
            // given
            List<MyQnaResult> qnas = QnaQueryFixtures.myProductQnaResultList(5);
            int requestedSize = 10;

            // when
            MyQnaSliceResult result = sut.toMyQnaSliceResult(qnas, requestedSize);

            // then
            assertThat(result.hasNext()).isFalse();
            assertThat(result.content()).hasSize(5);
        }

        @Test
        @DisplayName("마지막 항목의 qnaId가 lastId로 설정된다")
        void toMyQnaSliceResult_ValidQnas_SetsLastIdCorrectly() {
            // given
            MyQnaResult qnaResult = QnaQueryFixtures.myProductQnaResult();
            List<MyQnaResult> qnas = List.of(qnaResult);
            int requestedSize = 10;

            // when
            MyQnaSliceResult result = sut.toMyQnaSliceResult(qnas, requestedSize);

            // then
            assertThat(result.sliceMeta().cursor()).isEqualTo(String.valueOf(qnaResult.qnaId()));
        }
    }
}
