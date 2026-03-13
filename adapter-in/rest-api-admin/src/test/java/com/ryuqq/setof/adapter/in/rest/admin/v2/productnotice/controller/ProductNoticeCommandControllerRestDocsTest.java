package com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.productnotice.ProductNoticeApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.RegisterProductNoticeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.UpdateProductNoticeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.mapper.ProductNoticeCommandApiMapper;
import com.ryuqq.setof.application.productnotice.port.in.command.RegisterProductNoticeUseCase;
import com.ryuqq.setof.application.productnotice.port.in.command.UpdateProductNoticeUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * ProductNoticeCommandController REST Docs 테스트.
 *
 * <p>상품 그룹 고시정보 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ProductNoticeCommandController REST Docs 테스트")
@WebMvcTest(ProductNoticeCommandController.class)
@WithMockUser
class ProductNoticeCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private RegisterProductNoticeUseCase registerUseCase;

    @MockBean private UpdateProductNoticeUseCase updateUseCase;

    @MockBean private ProductNoticeCommandApiMapper mapper;

    private static final Long PRODUCT_GROUP_ID = ProductNoticeApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
    private static final Long CREATED_NOTICE_ID = 100L;

    @Nested
    @DisplayName("고시정보 등록 API")
    class RegisterProductNoticeTest {

        @Test
        @DisplayName("고시정보 등록 성공")
        void registerProductNotice_Success() throws Exception {
            // given
            RegisterProductNoticeApiRequest request = ProductNoticeApiFixtures.registerRequest();
            given(registerUseCase.execute(any())).willReturn(CREATED_NOTICE_ID);

            // when & then
            mockMvc.perform(
                            post(
                                            "/api/v2/admin/product-groups/{productGroupId}/notice",
                                            PRODUCT_GROUP_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data").value(CREATED_NOTICE_ID))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("productGroupId")
                                                    .description("상품 그룹 ID")),
                                    requestFields(
                                            fieldWithPath("entries")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("고시 항목 목록 [필수]"),
                                            fieldWithPath("entries[].noticeFieldId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("고시 필드 ID [필수]"),
                                            fieldWithPath("entries[].fieldName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("필드명 [필수]"),
                                            fieldWithPath("entries[].fieldValue")
                                                    .type(JsonFieldType.STRING)
                                                    .description("필드값 [필수]")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("생성된 고시정보 ID"),
                                            fieldWithPath("timestamp")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 시각")
                                                    .optional(),
                                            fieldWithPath("requestId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("요청 추적 ID")
                                                    .optional())));
        }
    }

    @Nested
    @DisplayName("고시정보 수정 API")
    class UpdateProductNoticeTest {

        @Test
        @DisplayName("고시정보 수정 성공")
        void updateProductNotice_Success() throws Exception {
            // given
            UpdateProductNoticeApiRequest request = ProductNoticeApiFixtures.updateRequest();
            willDoNothing().given(updateUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            put(
                                            "/api/v2/admin/product-groups/{productGroupId}/notice",
                                            PRODUCT_GROUP_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("productGroupId")
                                                    .description("상품 그룹 ID")),
                                    requestFields(
                                            fieldWithPath("entries")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("고시 항목 목록 [필수]"),
                                            fieldWithPath("entries[].noticeFieldId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("고시 필드 ID [필수]"),
                                            fieldWithPath("entries[].fieldName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("필드명 [필수]"),
                                            fieldWithPath("entries[].fieldValue")
                                                    .type(JsonFieldType.STRING)
                                                    .description("필드값 [필수]"))));
        }
    }
}
