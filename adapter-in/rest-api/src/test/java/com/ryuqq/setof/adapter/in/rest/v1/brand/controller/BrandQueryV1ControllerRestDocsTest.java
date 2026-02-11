package com.ryuqq.setof.adapter.in.rest.v1.brand.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.brand.BrandApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.brand.BrandV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response.BrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.brand.mapper.BrandV1ApiMapper;
import com.ryuqq.setof.application.brand.dto.response.BrandDisplayResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandByIdUseCase;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandsForDisplayUseCase;
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
 * BrandQueryV1Controller REST Docs 테스트.
 *
 * <p>브랜드 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("BrandQueryV1Controller REST Docs 테스트")
@WebMvcTest(BrandQueryV1Controller.class)
@WithMockUser
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class BrandQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetBrandsForDisplayUseCase getBrandsForDisplayUseCase;

    @MockBean private GetBrandByIdUseCase getBrandByIdUseCase;

    @MockBean private BrandV1ApiMapper mapper;

    @Nested
    @DisplayName("브랜드 목록 조회 API")
    class GetBrandsTest {

        @Test
        @DisplayName("브랜드 목록 조회 성공")
        void getBrands_Success() throws Exception {
            // given
            List<BrandDisplayResult> results = BrandApiFixtures.displayResultList();
            List<BrandV1ApiResponse> response = BrandApiFixtures.brandResponseList();

            given(getBrandsForDisplayUseCase.execute(any())).willReturn(results);
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toListResponse(results)).willReturn(response);

            // when & then
            mockMvc.perform(get(BrandV1Endpoints.BRANDS).param("searchWord", "나이키"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("searchWord")
                                                    .description("브랜드명 검색어 (한글/영문, 부분 일치)")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("브랜드 목록"),
                                            fieldWithPath("data[].brandId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("브랜드 ID"),
                                            fieldWithPath("data[].brandName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드명 (영문 코드)"),
                                            fieldWithPath("data[].korBrandName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드명 (한글)"),
                                            fieldWithPath("data[].brandIconImageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드 아이콘 이미지 URL"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("브랜드 빈 목록 조회 성공")
        void getBrands_Empty_Success() throws Exception {
            // given
            given(getBrandsForDisplayUseCase.execute(any())).willReturn(List.of());
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toListResponse(List.of())).willReturn(List.of());

            // when & then
            mockMvc.perform(get(BrandV1Endpoints.BRANDS))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(0));
        }
    }

    @Nested
    @DisplayName("브랜드 단건 조회 API")
    class GetBrandTest {

        @Test
        @DisplayName("브랜드 단건 조회 성공")
        void getBrand_Success() throws Exception {
            // given
            long brandId = 1L;
            BrandResult result = BrandApiFixtures.brandResult(brandId);
            BrandV1ApiResponse response = BrandApiFixtures.brandResponse(brandId);

            given(getBrandByIdUseCase.execute(eq(brandId))).willReturn(result);
            given(mapper.toResponse(result)).willReturn(response);

            // when & then
            mockMvc.perform(get(BrandV1Endpoints.BRAND_BY_ID, brandId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.brandId").value(brandId))
                    .andExpect(jsonPath("$.data.brandName").value("NIKE"))
                    .andExpect(jsonPath("$.data.korBrandName").value("나이키"))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    responseFields(
                                            fieldWithPath("data.brandId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("브랜드 ID"),
                                            fieldWithPath("data.brandName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드명 (영문 코드)"),
                                            fieldWithPath("data.korBrandName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드명 (한글)"),
                                            fieldWithPath("data.brandIconImageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드 아이콘 이미지 URL"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }
}
