package com.ryuqq.setof.application.qna.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.QnaQueryFixtures;
import com.ryuqq.setof.application.qna.assembler.QnaAssembler;
import com.ryuqq.setof.application.qna.dto.query.ProductQnaSearchParams;
import com.ryuqq.setof.application.qna.dto.response.QnaPageResult;
import com.ryuqq.setof.application.qna.dto.response.QnaWithAnswersResult;
import com.ryuqq.setof.application.qna.factory.QnaQueryFactory;
import com.ryuqq.setof.application.qna.manager.QnaProductReadManager;
import com.ryuqq.setof.domain.qna.query.ProductQnaSearchCriteria;
import java.util.List;
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
@DisplayName("GetProductQnasService 단위 테스트")
class GetProductQnasServiceTest {

    @InjectMocks private GetProductQnasService sut;

    @Mock private QnaProductReadManager readManager;
    @Mock private QnaQueryFactory queryFactory;
    @Mock private QnaAssembler assembler;
    @Mock private ProductQnaSearchCriteria criteria;

    @Nested
    @DisplayName("execute() - 상품 Q&A 목록 조회")
    class ExecuteTest {

        @Test
        @DisplayName("Q&A가 존재하면 조회하고 Assembler로 페이지 결과를 조립한다")
        void execute_QnasExist_ReturnsQnaPageResult() {
            // given
            ProductQnaSearchParams params = QnaQueryFixtures.productQnaSearchParams();
            List<QnaWithAnswersResult> qnas = QnaQueryFixtures.qnaWithAnswersResultList();
            QnaPageResult expectedResult = QnaQueryFixtures.qnaPageResult();
            long totalElements = 2L;

            given(queryFactory.createProductQnaCriteria(params)).willReturn(criteria);
            given(criteria.page()).willReturn(0);
            given(criteria.size()).willReturn(10);
            given(readManager.countProductQnas(criteria)).willReturn(totalElements);
            given(readManager.fetchProductQnas(criteria)).willReturn(qnas);
            given(
                            assembler.toQnaPageResult(
                                    qnas,
                                    params.viewerUserId(),
                                    criteria.page(),
                                    criteria.size(),
                                    totalElements))
                    .willReturn(expectedResult);

            // when
            QnaPageResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expectedResult);
            then(queryFactory).should().createProductQnaCriteria(params);
            then(readManager).should().countProductQnas(criteria);
            then(readManager).should().fetchProductQnas(criteria);
            then(assembler)
                    .should()
                    .toQnaPageResult(
                            qnas,
                            params.viewerUserId(),
                            criteria.page(),
                            criteria.size(),
                            totalElements);
        }

        @Test
        @DisplayName("Q&A가 없으면 empty 결과를 반환하고 fetchProductQnas를 호출하지 않는다")
        void execute_NoQnas_ReturnsEmptyResult() {
            // given
            ProductQnaSearchParams params = QnaQueryFixtures.productQnaSearchParams();

            given(queryFactory.createProductQnaCriteria(params)).willReturn(criteria);
            given(criteria.page()).willReturn(0);
            given(criteria.size()).willReturn(10);
            given(readManager.countProductQnas(criteria)).willReturn(0L);

            // when
            QnaPageResult result = sut.execute(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.content()).isEmpty();
            then(readManager).should().countProductQnas(criteria);
            then(readManager).shouldHaveNoMoreInteractions();
            then(assembler).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("비로그인 사용자 조회 시 viewerUserId가 null로 Assembler에 전달된다")
        void execute_AnonymousViewer_PassesNullViewerUserId() {
            // given
            ProductQnaSearchParams params = QnaQueryFixtures.productQnaSearchParamsAnonymous();
            List<QnaWithAnswersResult> qnas = QnaQueryFixtures.qnaWithAnswersResultList(1);
            QnaPageResult expectedResult = QnaQueryFixtures.qnaPageResult();
            long totalElements = 1L;

            given(queryFactory.createProductQnaCriteria(params)).willReturn(criteria);
            given(criteria.page()).willReturn(0);
            given(criteria.size()).willReturn(10);
            given(readManager.countProductQnas(criteria)).willReturn(totalElements);
            given(readManager.fetchProductQnas(criteria)).willReturn(qnas);
            given(
                            assembler.toQnaPageResult(
                                    qnas, null, criteria.page(), criteria.size(), totalElements))
                    .willReturn(expectedResult);

            // when
            QnaPageResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expectedResult);
            then(assembler)
                    .should()
                    .toQnaPageResult(qnas, null, criteria.page(), criteria.size(), totalElements);
        }
    }
}
