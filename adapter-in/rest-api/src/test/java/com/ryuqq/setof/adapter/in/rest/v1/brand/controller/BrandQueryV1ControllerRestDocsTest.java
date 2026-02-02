package com.ryuqq.setof.adapter.in.rest.v1.brand.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.brand.BrandV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response.BrandDisplayV1ApiResponse;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * BrandQueryV1Controller REST Docs 테스트.
 *
 * <p>브랜드 조회 V1 API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("BrandQueryV1Controller REST Docs 테스트")
@WebMvcTest(BrandQueryV1Controller.class)
@WithMockUser
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
            List<BrandDisplayResult> results = BrandV1ApiFixtures.multipleBrandDisplayResults();
            List<BrandDisplayV1ApiResponse> response =
                    BrandV1ApiFixtures.multipleBrandDisplayResponses();

            given(getBrandsForDisplayUseCase.execute(any())).willReturn(results);
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toListResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(
                            get("/api/v1/brand")
                                    .param("brandName", "NIKE")
                                    .param("displayed", "true"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].brandId").exists())
                    .andExpect(jsonPath("$.data[0].brandName").exists())
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("brandName")
                                                    .description("브랜드명 검색 +\n" + "부분 일치 검색")
                                                    .optional(),
                                            parameterWithName("displayed")
                                                    .description(
                                                            "노출 여부 필터 +\n" + "true: 노출, false: 미노출")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("브랜드 목록"),
                                            fieldWithPath("data[].brandId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("브랜드 ID"),
                                            fieldWithPath("data[].brandName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드명 (영문)"),
                                            fieldWithPath("data[].korBrandName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드명 (한글)"),
                                            fieldWithPath("data[].brandIconImageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드 아이콘 이미지 URL"),
                                            fieldWithPath("response")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("응답 메타 정보"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("응답 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("브랜드 목록 조회 - 결과 없음")
        void getBrands_Empty() throws Exception {
            // given
            List<BrandDisplayResult> results = List.of();
            List<BrandDisplayV1ApiResponse> response = List.of();

            given(getBrandsForDisplayUseCase.execute(any())).willReturn(results);
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toListResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(get("/api/v1/brand").param("brandName", "존재하지않는브랜드"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andExpect(jsonPath("$.response.status").value(200));
        }
    }

    @Nested
    @DisplayName("브랜드 단건 조회 API")
    class GetBrandTest {

        @Test
        @DisplayName("브랜드 단건 조회 성공")
        void getBrand_Success() throws Exception {
            // given
            BrandResult result = BrandV1ApiFixtures.brandResult();
            BrandDisplayV1ApiResponse response = BrandV1ApiFixtures.brandDisplayResponse();

            given(getBrandByIdUseCase.execute(anyLong())).willReturn(result);
            given(mapper.toResponse(any(BrandResult.class))).willReturn(response);

            // when & then
            mockMvc.perform(get("/api/v1/brand/{brandId}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.brandId").value(1))
                    .andExpect(jsonPath("$.data.brandName").value("NIKE"))
                    .andExpect(jsonPath("$.data.korBrandName").value("나이키"))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("brandId").description("브랜드 ID")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("브랜드 정보"),
                                            fieldWithPath("data.brandId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("브랜드 ID"),
                                            fieldWithPath("data.brandName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드명 (영문)"),
                                            fieldWithPath("data.korBrandName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드명 (한글)"),
                                            fieldWithPath("data.brandIconImageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드 아이콘 이미지 URL"),
                                            fieldWithPath("response")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("응답 메타 정보"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("응답 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }
}
