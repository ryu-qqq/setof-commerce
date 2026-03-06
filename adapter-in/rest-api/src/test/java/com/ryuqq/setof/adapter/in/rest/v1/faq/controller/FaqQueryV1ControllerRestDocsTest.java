package com.ryuqq.setof.adapter.in.rest.v1.faq.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.faq.FaqApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.faq.FaqV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.faq.dto.response.FaqV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.faq.mapper.FaqV1ApiMapper;
import com.ryuqq.setof.application.faq.dto.response.FaqResult;
import com.ryuqq.setof.application.faq.port.in.GetFaqsByTypeUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * FaqQueryV1Controller REST Docs 테스트.
 *
 * <p>FAQ Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("FaqQueryV1Controller REST Docs 테스트")
@WebMvcTest(FaqQueryV1Controller.class)
@WithMockUser
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class FaqQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetFaqsByTypeUseCase getFaqsByTypeUseCase;

    @MockBean private FaqV1ApiMapper mapper;

    @Nested
    @DisplayName("FAQ 목록 조회 API")
    class GetFaqsTest {

        @Test
        @DisplayName("FAQ 목록 조회 성공")
        void getFaqs_Success() throws Exception {
            // given
            List<FaqResult> results = FaqApiFixtures.faqResultList();
            List<FaqV1ApiResponse> response = FaqApiFixtures.faqResponseList();

            given(getFaqsByTypeUseCase.execute(any())).willReturn(results);
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toListResponse(results)).willReturn(response);

            // when & then
            mockMvc.perform(get(FaqV1Endpoints.FAQS).param("faqType", "MEMBER_LOGIN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("faqType")
                                                    .description("FAQ 유형 (필수)")),
                                    responseFields(
                                            fieldWithPath("data[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("FAQ 목록"),
                                            fieldWithPath("data[].faqId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("FAQ ID"),
                                            fieldWithPath("data[].faqType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("FAQ 유형"),
                                            fieldWithPath("data[].title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("FAQ 제목"),
                                            fieldWithPath("data[].contents")
                                                    .type(JsonFieldType.STRING)
                                                    .description("FAQ 내용"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("FAQ 빈 목록 조회 성공")
        void getFaqs_Empty_Success() throws Exception {
            // given
            given(getFaqsByTypeUseCase.execute(any())).willReturn(List.of());
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toListResponse(List.of())).willReturn(List.of());

            // when & then
            mockMvc.perform(get(FaqV1Endpoints.FAQS).param("faqType", "MEMBER_LOGIN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(0));
        }
    }
}
