package com.ryuqq.setof.adapter.in.rest.v1.faq.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.v1.faq.FaqApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.faq.dto.request.SearchFaqsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.faq.dto.response.FaqV1ApiResponse;
import com.ryuqq.setof.application.faq.dto.query.FaqSearchParams;
import com.ryuqq.setof.application.faq.dto.response.FaqResult;
import com.ryuqq.setof.domain.faq.vo.FaqType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * FaqV1ApiMapper 단위 테스트.
 *
 * <p>Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("FaqV1ApiMapper 단위 테스트")
class FaqV1ApiMapperTest {

    private FaqV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new FaqV1ApiMapper();
    }

    @Nested
    @DisplayName("toSearchParams")
    class ToSearchParamsTest {

        @Test
        @DisplayName("검색 요청을 FaqSearchParams로 변환한다")
        void toSearchParams_Success() {
            // given
            SearchFaqsV1ApiRequest request = FaqApiFixtures.searchRequest();

            // when
            FaqSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.faqType()).isEqualTo(FaqType.MEMBER_LOGIN);
        }

        @Test
        @DisplayName("다른 FaqType으로 변환한다")
        void toSearchParams_OtherFaqType() {
            // given
            SearchFaqsV1ApiRequest request = FaqApiFixtures.searchRequest(FaqType.SHIPPING);

            // when
            FaqSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.faqType()).isEqualTo(FaqType.SHIPPING);
        }
    }

    @Nested
    @DisplayName("toListResponse")
    class ToListResponseTest {

        @Test
        @DisplayName("FaqResult 목록을 FaqV1ApiResponse 목록으로 변환한다")
        void toListResponse_Success() {
            // given
            List<FaqResult> results = FaqApiFixtures.faqResultList();

            // when
            List<FaqV1ApiResponse> response = mapper.toListResponse(results);

            // then
            assertThat(response).hasSize(2);
            assertThat(response.get(0).faqId()).isEqualTo(1L);
            assertThat(response.get(0).faqType()).isEqualTo("MEMBER_LOGIN");
            assertThat(response.get(0).title()).isEqualTo("회원 가입은 어떻게 하나요?");
            assertThat(response.get(0).contents()).isEqualTo("회원 가입 방법 안내입니다.");
            assertThat(response.get(1).faqId()).isEqualTo(2L);
            assertThat(response.get(1).faqType()).isEqualTo("SHIPPING");
        }

        @Test
        @DisplayName("빈 목록을 빈 응답으로 변환한다")
        void toListResponse_Empty() {
            // given
            List<FaqResult> results = List.of();

            // when
            List<FaqV1ApiResponse> response = mapper.toListResponse(results);

            // then
            assertThat(response).isEmpty();
        }
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponseTest {

        @Test
        @DisplayName("FaqResult를 FaqV1ApiResponse로 변환한다")
        void toResponse_Success() {
            // given
            FaqResult result = FaqApiFixtures.faqResult(1L);

            // when
            FaqV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.faqId()).isEqualTo(1L);
            assertThat(response.faqType()).isEqualTo("MEMBER_LOGIN");
            assertThat(response.title()).isEqualTo("회원 가입은 어떻게 하나요?");
            assertThat(response.contents()).isEqualTo("회원 가입 방법 안내입니다.");
        }

        @Test
        @DisplayName("faqType이 null이면 응답 faqType은 null이다")
        void toResponse_NullFaqType() {
            // given
            FaqResult result = FaqResult.of(1L, null, "제목", "내용", 0, null);

            // when
            FaqV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.faqType()).isNull();
            assertThat(response.faqId()).isEqualTo(1L);
            assertThat(response.title()).isEqualTo("제목");
        }
    }
}
