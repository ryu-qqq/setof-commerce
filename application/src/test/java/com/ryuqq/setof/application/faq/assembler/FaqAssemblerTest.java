package com.ryuqq.setof.application.faq.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.faq.dto.response.FaqResult;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.id.FaqId;
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

@Tag("unit")
@DisplayName("FaqAssembler 단위 테스트")
class FaqAssemblerTest {

    private final FaqAssembler sut = new FaqAssembler();

    private Faq memberLoginFaq() {
        return Faq.reconstitute(
                FaqId.of(1L),
                FaqType.MEMBER_LOGIN,
                FaqTitle.of("로그인 관련 FAQ"),
                FaqContents.of("로그인 관련 내용입니다."),
                FaqDisplayOrder.of(1),
                null,
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
                Instant.now(),
                Instant.now());
    }

    @Nested
    @DisplayName("toResult() - Domain → Result 변환")
    class ToResultTest {

        @Test
        @DisplayName("일반 Faq를 FaqResult로 변환한다")
        void toResult_GeneralFaq_ConvertsToResult() {
            // given
            Faq faq = memberLoginFaq();

            // when
            FaqResult result = sut.toResult(faq);

            // then
            assertThat(result).isNotNull();
            assertThat(result.faqId()).isEqualTo(faq.idValue());
            assertThat(result.faqType()).isEqualTo(faq.faqType());
            assertThat(result.title()).isEqualTo(faq.titleValue());
            assertThat(result.contents()).isEqualTo(faq.contentsValue());
            assertThat(result.displayOrder()).isEqualTo(faq.displayOrderValue());
            assertThat(result.topDisplayOrder()).isNull();
        }

        @Test
        @DisplayName("TOP Faq를 FaqResult로 변환하면 topDisplayOrder가 포함된다")
        void toResult_TopFaq_ConvertsToResultWithTopDisplayOrder() {
            // given
            Faq faq = topFaq();

            // when
            FaqResult result = sut.toResult(faq);

            // then
            assertThat(result).isNotNull();
            assertThat(result.faqId()).isEqualTo(faq.idValue());
            assertThat(result.faqType()).isEqualTo(FaqType.TOP);
            assertThat(result.topDisplayOrder()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("toResults() - Domain List → Result List 변환")
    class ToResultsTest {

        @Test
        @DisplayName("Faq 목록을 FaqResult 목록으로 변환한다")
        void toResults_ConvertsAllToResults() {
            // given
            List<Faq> faqs = List.of(memberLoginFaq(), topFaq());

            // when
            List<FaqResult> results = sut.toResults(faqs);

            // then
            assertThat(results).hasSize(2);
        }

        @Test
        @DisplayName("빈 목록이면 빈 결과를 반환한다")
        void toResults_EmptyList_ReturnsEmptyList() {
            // given
            List<Faq> faqs = Collections.emptyList();

            // when
            List<FaqResult> results = sut.toResults(faqs);

            // then
            assertThat(results).isEmpty();
        }

        @Test
        @DisplayName("변환된 결과의 필드가 원본과 일치한다")
        void toResults_FieldsMatchSource() {
            // given
            Faq faq = memberLoginFaq();
            List<Faq> faqs = List.of(faq);

            // when
            List<FaqResult> results = sut.toResults(faqs);

            // then
            FaqResult result = results.get(0);
            assertThat(result.faqId()).isEqualTo(faq.idValue());
            assertThat(result.faqType()).isEqualTo(faq.faqType());
            assertThat(result.title()).isEqualTo(faq.titleValue());
            assertThat(result.contents()).isEqualTo(faq.contentsValue());
        }
    }
}
