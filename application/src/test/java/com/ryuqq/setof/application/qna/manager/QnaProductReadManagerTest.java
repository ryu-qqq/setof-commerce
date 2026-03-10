package com.ryuqq.setof.application.qna.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.QnaQueryFixtures;
import com.ryuqq.setof.application.qna.dto.response.QnaWithAnswersResult;
import com.ryuqq.setof.application.qna.port.out.query.QnaProductQueryPort;
import com.ryuqq.setof.domain.qna.exception.QnaNotFoundException;
import com.ryuqq.setof.domain.qna.query.ProductQnaSearchCriteria;
import java.util.List;
import java.util.Optional;
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
@DisplayName("QnaProductReadManager 단위 테스트")
class QnaProductReadManagerTest {

    @InjectMocks private QnaProductReadManager sut;

    @Mock private QnaProductQueryPort queryPort;
    @Mock private ProductQnaSearchCriteria criteria;

    @Nested
    @DisplayName("fetchProductQnas() - 상품 Q&A 목록 조회")
    class FetchProductQnasTest {

        @Test
        @DisplayName("상품 Q&A 목록을 조회하여 반환한다")
        void fetchProductQnas_ValidCriteria_ReturnsQnaList() {
            // given
            List<QnaWithAnswersResult> expected = QnaQueryFixtures.qnaWithAnswersResultList();
            given(queryPort.fetchProductQnas(criteria)).willReturn(expected);

            // when
            List<QnaWithAnswersResult> result = sut.fetchProductQnas(criteria);

            // then
            assertThat(result).hasSize(expected.size());
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchProductQnas(criteria);
        }

        @Test
        @DisplayName("Q&A가 없으면 빈 목록을 반환한다")
        void fetchProductQnas_NoQnas_ReturnsEmptyList() {
            // given
            given(queryPort.fetchProductQnas(criteria)).willReturn(List.of());

            // when
            List<QnaWithAnswersResult> result = sut.fetchProductQnas(criteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("countProductQnas() - 상품 Q&A 건수 조회")
    class CountProductQnasTest {

        @Test
        @DisplayName("상품 Q&A 건수를 조회하여 반환한다")
        void countProductQnas_ValidCriteria_ReturnsCount() {
            // given
            long expectedCount = 5L;
            given(queryPort.countProductQnas(criteria)).willReturn(expectedCount);

            // when
            long result = sut.countProductQnas(criteria);

            // then
            assertThat(result).isEqualTo(expectedCount);
            then(queryPort).should().countProductQnas(criteria);
        }

        @Test
        @DisplayName("Q&A가 없으면 0을 반환한다")
        void countProductQnas_NoQnas_ReturnsZero() {
            // given
            given(queryPort.countProductQnas(criteria)).willReturn(0L);

            // when
            long result = sut.countProductQnas(criteria);

            // then
            assertThat(result).isZero();
        }
    }

    @Nested
    @DisplayName("getProductQnaById() - 상품 Q&A 단건 조회")
    class GetProductQnaByIdTest {

        @Test
        @DisplayName("존재하는 qnaId로 조회하면 QnaWithAnswersResult를 반환한다")
        void getProductQnaById_ExistingId_ReturnsQnaWithAnswersResult() {
            // given
            long qnaId = QnaQueryFixtures.QNA_ID;
            QnaWithAnswersResult expected = QnaQueryFixtures.qnaWithAnswersResult();
            given(queryPort.findProductQnaById(qnaId)).willReturn(Optional.of(expected));

            // when
            QnaWithAnswersResult result = sut.getProductQnaById(qnaId);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().findProductQnaById(qnaId);
        }

        @Test
        @DisplayName("존재하지 않는 qnaId로 조회하면 QnaNotFoundException을 던진다")
        void getProductQnaById_NonExistingId_ThrowsQnaNotFoundException() {
            // given
            long qnaId = 9999L;
            given(queryPort.findProductQnaById(qnaId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getProductQnaById(qnaId))
                    .isInstanceOf(QnaNotFoundException.class);
        }
    }
}
