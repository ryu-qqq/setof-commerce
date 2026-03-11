package com.ryuqq.setof.application.faq.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.faq.assembler.FaqAssembler;
import com.ryuqq.setof.application.faq.dto.query.FaqSearchParams;
import com.ryuqq.setof.application.faq.dto.response.FaqResult;
import com.ryuqq.setof.application.faq.factory.FaqQueryFactory;
import com.ryuqq.setof.application.faq.manager.FaqReadManager;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.id.FaqId;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqContents;
import com.ryuqq.setof.domain.faq.vo.FaqDisplayOrder;
import com.ryuqq.setof.domain.faq.vo.FaqTitle;
import com.ryuqq.setof.domain.faq.vo.FaqType;
import java.time.Instant;
import java.util.Collections;
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
@DisplayName("GetFaqsByTypeService 단위 테스트")
class GetFaqsByTypeServiceTest {

    @InjectMocks private GetFaqsByTypeService sut;

    @Mock private FaqReadManager readManager;
    @Mock private FaqQueryFactory queryFactory;
    @Mock private FaqAssembler assembler;

    private Faq memberLoginFaq() {
        return Faq.reconstitute(
                FaqId.of(1L),
                FaqType.MEMBER_LOGIN,
                FaqTitle.of("로그인 관련 FAQ"),
                FaqContents.of("로그인 관련 내용입니다."),
                FaqDisplayOrder.of(1),
                null,
                DeletionStatus.active(),
                Instant.now(),
                Instant.now());
    }

    private Faq topFaq() {
        return Faq.reconstitute(
                FaqId.of(2L),
                FaqType.TOP,
                FaqTitle.of("상단 고정 FAQ"),
                FaqContents.of("상단 고정 FAQ 내용입니다."),
                FaqDisplayOrder.of(1),
                1,
                DeletionStatus.active(),
                Instant.now(),
                Instant.now());
    }

    private FaqResult memberLoginResult() {
        return FaqResult.of(1L, FaqType.MEMBER_LOGIN, "로그인 관련 FAQ", "로그인 관련 내용입니다.", 1, null);
    }

    private FaqResult topResult() {
        return FaqResult.of(2L, FaqType.TOP, "상단 고정 FAQ", "상단 고정 FAQ 내용입니다.", 1, 1);
    }

    @Nested
    @DisplayName("execute() - FAQ 유형별 목록 조회")
    class ExecuteTest {

        @Test
        @DisplayName("검색 조건으로 FAQ 목록을 반환한다")
        void execute_ValidParams_ReturnsFaqResults() {
            // given
            FaqSearchParams params = FaqSearchParams.of(FaqType.MEMBER_LOGIN);
            FaqSearchCriteria criteria = FaqSearchCriteria.of(FaqType.MEMBER_LOGIN);
            List<Faq> faqs = List.of(memberLoginFaq());
            List<FaqResult> expected = List.of(memberLoginResult());

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(faqs);
            given(assembler.toResults(faqs)).willReturn(expected);

            // when
            List<FaqResult> result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result).hasSize(1);
            then(queryFactory).should().createCriteria(params);
            then(readManager).should().findByCriteria(criteria);
            then(assembler).should().toResults(faqs);
        }

        @Test
        @DisplayName("TOP 유형으로 조회하면 상단 고정 FAQ 목록을 반환한다")
        void execute_TopType_ReturnsTopFaqResults() {
            // given
            FaqSearchParams params = FaqSearchParams.of(FaqType.TOP);
            FaqSearchCriteria criteria = FaqSearchCriteria.ofTop();
            List<Faq> faqs = List.of(topFaq());
            List<FaqResult> expected = List.of(topResult());

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(faqs);
            given(assembler.toResults(faqs)).willReturn(expected);

            // when
            List<FaqResult> result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryFactory).should().createCriteria(params);
            then(readManager).should().findByCriteria(criteria);
            then(assembler).should().toResults(faqs);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록을 반환한다")
        void execute_NoResults_ReturnsEmptyList() {
            // given
            FaqSearchParams params = FaqSearchParams.of(FaqType.SHIPPING);
            FaqSearchCriteria criteria = FaqSearchCriteria.of(FaqType.SHIPPING);
            List<Faq> emptyFaqs = Collections.emptyList();
            List<FaqResult> expected = Collections.emptyList();

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(emptyFaqs);
            given(assembler.toResults(emptyFaqs)).willReturn(expected);

            // when
            List<FaqResult> result = sut.execute(params);

            // then
            assertThat(result).isEmpty();
            then(queryFactory).should().createCriteria(params);
            then(readManager).should().findByCriteria(criteria);
            then(assembler).should().toResults(emptyFaqs);
        }

        @Test
        @DisplayName("여러 FAQ가 있을 때 전체 목록을 반환한다")
        void execute_MultipleResults_ReturnsAllFaqResults() {
            // given
            FaqSearchParams params = FaqSearchParams.of(FaqType.TOP);
            FaqSearchCriteria criteria = FaqSearchCriteria.ofTop();
            List<Faq> faqs = List.of(topFaq(), topFaq());
            List<FaqResult> expected = List.of(topResult(), topResult());

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(faqs);
            given(assembler.toResults(faqs)).willReturn(expected);

            // when
            List<FaqResult> result = sut.execute(params);

            // then
            assertThat(result).hasSize(2);
        }
    }
}
