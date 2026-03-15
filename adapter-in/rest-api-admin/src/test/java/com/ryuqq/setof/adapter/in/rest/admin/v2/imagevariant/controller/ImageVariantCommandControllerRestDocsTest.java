package com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.imagevariant.ImageVariantApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant.dto.command.SyncImageVariantsApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant.mapper.ImageVariantCommandApiMapper;
import com.ryuqq.setof.application.imagevariant.port.in.command.SyncImageVariantsUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * ImageVariantCommandController REST Docs 테스트.
 *
 * <p>이미지 Variant Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ImageVariantCommandController REST Docs 테스트")
@WebMvcTest(ImageVariantCommandController.class)
@WithMockUser
class ImageVariantCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private SyncImageVariantsUseCase syncUseCase;

    @MockBean private ImageVariantCommandApiMapper mapper;

    @Nested
    @DisplayName("이미지 Variant 동기화 API")
    class SyncImageVariantsTest {

        @Test
        @DisplayName("이미지 Variant 동기화 성공")
        void syncImageVariants_Success() throws Exception {
            // given
            SyncImageVariantsApiRequest request = ImageVariantApiFixtures.syncRequest();
            willDoNothing().given(syncUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            put("/api/v2/admin/image-variants/sync")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("sourceImageId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("원본 이미지 ID [필수, 1 이상]"),
                                            fieldWithPath("sourceType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "이미지 소스 타입 [필수] +\n"
                                                                    + "PRODUCT_GROUP_IMAGE:"
                                                                    + " 상품 그룹 이미지,"
                                                                    + " DESCRIPTION_IMAGE: 설명 이미지"),
                                            fieldWithPath("variants")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("Variant 목록 [필수, 최소 1개]"),
                                            fieldWithPath("variants[].variantType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "Variant 타입 [필수] +\n"
                                                                    + "SMALL_WEBP, MEDIUM_WEBP,"
                                                                    + " LARGE_WEBP,"
                                                                    + " ORIGINAL_WEBP"),
                                            fieldWithPath("variants[].resultAssetId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("FileFlow 변환 결과 에셋 ID [필수]"),
                                            fieldWithPath("variants[].variantUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("변환된 이미지 CDN URL [필수]"),
                                            fieldWithPath("variants[].width")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("이미지 너비 (px) [선택]")
                                                    .optional(),
                                            fieldWithPath("variants[].height")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("이미지 높이 (px) [선택]")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NULL)
                                                    .description("빈 응답")
                                                    .optional(),
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
}
