package com.ryuqq.setof.application.faq.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.faq.port.out.FaqQueryPort;
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
@DisplayName("FaqReadManager 단위 테스트")
class FaqReadManagerTest {

    @InjectMocks private FaqReadManager sut;

    @Mock private FaqQueryPort queryPort;

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

    @Nested
    @DisplayName("findByCriteria() - 검색 조건으로 FAQ 조회")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 FAQ 목록을 조회한다")
        void findByCriteria_ReturnsMatchingFaqs() {
            // given
            FaqSearchCriteria criteria = FaqSearchCriteria.of(FaqType.MEMBER_LOGIN);
            List<Faq> expected = List.of(memberLoginFaq());

            given(queryPort.findByCriteria(criteria)).willReturn(expected);

            // when
            List<Faq> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().findByCriteria(criteria);
        }

        @Test
        @DisplayName("TOP 유형 검색 조건으로 TOP FAQ 목록을 조회한다")
        void findByCriteria_TopCriteria_ReturnsTopFaqs() {
            // given
            FaqSearchCriteria criteria = FaqSearchCriteria.ofTop();
            List<Faq> expected = List.of(topFaq());

            given(queryPort.findByCriteria(criteria)).willReturn(expected);

            // when
            List<Faq> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            then(queryPort).should().findByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록을 반환한다")
        void findByCriteria_NoResults_ReturnsEmptyList() {
            // given
            FaqSearchCriteria criteria = FaqSearchCriteria.of(FaqType.CANCEL_REFUND);

            given(queryPort.findByCriteria(criteria)).willReturn(Collections.emptyList());

            // when
            List<Faq> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().findByCriteria(criteria);
        }

        @Test
        @DisplayName("여러 FAQ를 포함하는 목록을 반환한다")
        void findByCriteria_MultipleResults_ReturnsAllFaqs() {
            // given
            FaqSearchCriteria criteria = FaqSearchCriteria.ofTop();
            List<Faq> expected = List.of(topFaq(), topFaq());

            given(queryPort.findByCriteria(criteria)).willReturn(expected);

            // when
            List<Faq> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
        }
    }
}
