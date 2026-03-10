package com.ryuqq.setof.application.qna.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.QnaQueryFixtures;
import com.ryuqq.setof.application.qna.assembler.QnaAssembler;
import com.ryuqq.setof.application.qna.dto.query.MyQnaSearchParams;
import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.application.qna.dto.response.MyQnaSliceResult;
import com.ryuqq.setof.application.qna.factory.QnaQueryFactory;
import com.ryuqq.setof.application.qna.internal.MyQnaReadStrategy;
import com.ryuqq.setof.application.qna.internal.MyQnaReadStrategyProvider;
import com.ryuqq.setof.domain.qna.query.QnaSearchCriteria;
import com.ryuqq.setof.domain.qna.vo.QnaType;
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
@DisplayName("GetMyQnasService 단위 테스트")
class GetMyQnasServiceTest {

    @InjectMocks private GetMyQnasService sut;

    @Mock private MyQnaReadStrategyProvider strategyProvider;
    @Mock private QnaQueryFactory queryFactory;
    @Mock private QnaAssembler assembler;
    @Mock private MyQnaReadStrategy strategy;
    @Mock private QnaSearchCriteria criteria;

    @Nested
    @DisplayName("execute() - 내 Q&A 목록 조회")
    class ExecuteTest {

        @Test
        @DisplayName("상품 Q&A 조회 파라미터로 실행하면 PRODUCT 전략으로 조회하고 슬라이스 결과를 반환한다")
        void execute_ProductQnaParams_ReturnsMyQnaSliceResult() {
            // given
            MyQnaSearchParams params = QnaQueryFixtures.myProductQnaSearchParams();
            List<MyQnaResult> qnas = QnaQueryFixtures.myProductQnaResultList();
            MyQnaSliceResult expectedResult = QnaQueryFixtures.myQnaSliceResult();

            given(queryFactory.createMyQnaCriteria(params)).willReturn(criteria);
            given(criteria.qnaType()).willReturn(QnaType.PRODUCT);
            given(criteria.size()).willReturn(10);
            given(strategyProvider.getStrategy(QnaType.PRODUCT)).willReturn(strategy);
            given(strategy.fetchMyQnas(criteria)).willReturn(qnas);
            given(assembler.toMyQnaSliceResult(qnas, criteria.size())).willReturn(expectedResult);

            // when
            MyQnaSliceResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expectedResult);
            then(queryFactory).should().createMyQnaCriteria(params);
            then(strategyProvider).should().getStrategy(QnaType.PRODUCT);
            then(strategy).should().fetchMyQnas(criteria);
            then(assembler).should().toMyQnaSliceResult(qnas, criteria.size());
        }

        @Test
        @DisplayName("주문 Q&A 조회 파라미터로 실행하면 ORDER 전략으로 조회한다")
        void execute_OrderQnaParams_DelegatesToOrderStrategy() {
            // given
            MyQnaSearchParams params = QnaQueryFixtures.myOrderQnaSearchParams();
            List<MyQnaResult> qnas = List.of(QnaQueryFixtures.myOrderQnaResult());
            MyQnaSliceResult expectedResult = QnaQueryFixtures.myQnaSliceResult();

            given(queryFactory.createMyQnaCriteria(params)).willReturn(criteria);
            given(criteria.qnaType()).willReturn(QnaType.ORDER);
            given(criteria.size()).willReturn(10);
            given(strategyProvider.getStrategy(QnaType.ORDER)).willReturn(strategy);
            given(strategy.fetchMyQnas(criteria)).willReturn(qnas);
            given(assembler.toMyQnaSliceResult(qnas, criteria.size())).willReturn(expectedResult);

            // when
            MyQnaSliceResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expectedResult);
            then(strategyProvider).should().getStrategy(QnaType.ORDER);
        }

        @Test
        @DisplayName("Q&A가 없으면 empty 슬라이스 결과를 반환한다")
        void execute_NoQnas_ReturnsEmptySliceResult() {
            // given
            MyQnaSearchParams params = QnaQueryFixtures.myProductQnaSearchParams();
            List<MyQnaResult> emptyList = List.of();
            MyQnaSliceResult expectedResult = QnaQueryFixtures.emptyMyQnaSliceResult();

            given(queryFactory.createMyQnaCriteria(params)).willReturn(criteria);
            given(criteria.qnaType()).willReturn(QnaType.PRODUCT);
            given(criteria.size()).willReturn(10);
            given(strategyProvider.getStrategy(QnaType.PRODUCT)).willReturn(strategy);
            given(strategy.fetchMyQnas(criteria)).willReturn(emptyList);
            given(assembler.toMyQnaSliceResult(emptyList, criteria.size()))
                    .willReturn(expectedResult);

            // when
            MyQnaSliceResult result = sut.execute(params);

            // then
            assertThat(result.content()).isEmpty();
        }
    }
}
