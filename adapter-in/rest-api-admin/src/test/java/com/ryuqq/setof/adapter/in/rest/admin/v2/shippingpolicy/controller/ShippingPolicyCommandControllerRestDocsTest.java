package com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
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
import com.ryuqq.setof.adapter.in.rest.admin.shippingpolicy.ShippingPolicyApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.ChangeShippingPolicyStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.RegisterShippingPolicyApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.UpdateShippingPolicyApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.mapper.ShippingPolicyCommandApiMapper;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.ChangeShippingPolicyStatusUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.RegisterShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.UpdateShippingPolicyUseCase;
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
 * ShippingPolicyCommandController REST Docs 테스트.
 *
 * <p>배송정책 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("ShippingPolicyCommandController REST Docs 테스트")
@WebMvcTest(ShippingPolicyCommandController.class)
@WithMockUser
class ShippingPolicyCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private RegisterShippingPolicyUseCase registerUseCase;

    @MockBean private UpdateShippingPolicyUseCase updateUseCase;

    @MockBean private ChangeShippingPolicyStatusUseCase changeStatusUseCase;

    @MockBean private ShippingPolicyCommandApiMapper mapper;

    private static final Long SELLER_ID = 1L;
    private static final Long POLICY_ID = 200L;

    @Nested
    @DisplayName("배송정책 등록 API")
    class RegisterShippingPolicyTest {

        @Test
        @DisplayName("배송정책 등록 성공")
        void registerShippingPolicy_Success() throws Exception {
            // given
            RegisterShippingPolicyApiRequest request = ShippingPolicyApiFixtures.registerRequest();
            given(registerUseCase.execute(any())).willReturn(POLICY_ID);

            // when & then
            mockMvc.perform(
                            post("/api/v2/sellers/{sellerId}/shipping-policies", SELLER_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.policyId").value(POLICY_ID))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("sellerId").description("셀러 ID")),
                                    requestFields(
                                            fieldWithPath("policyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책명 [필수, 1~100자]"),
                                            fieldWithPath("defaultPolicy")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("기본 정책 여부 [필수]"),
                                            fieldWithPath("shippingFeeType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "배송비 유형 [필수] +\n"
                                                                    + "FREE: 무료배송, "
                                                                    + "PAID: 유료배송, "
                                                                    + "CONDITIONAL_FREE: 조건부 무료, "
                                                                    + "QUANTITY_BASED: 수량별 배송비"),
                                            fieldWithPath("baseFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("기본 배송비 (원) [0 이상]")
                                                    .optional(),
                                            fieldWithPath("freeThreshold")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description(
                                                            "무료배송 기준금액 (원) - CONDITIONAL_FREE 시 필수"
                                                                    + " [0 이상]")
                                                    .optional(),
                                            fieldWithPath("jejuExtraFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("제주 추가 배송비 (원) [0 이상]")
                                                    .optional(),
                                            fieldWithPath("islandExtraFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("도서산간 추가 배송비 (원) [0 이상]")
                                                    .optional(),
                                            fieldWithPath("returnFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("반품 배송비 (원) [0 이상]")
                                                    .optional(),
                                            fieldWithPath("exchangeFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("교환 배송비 (원) [0 이상]")
                                                    .optional(),
                                            fieldWithPath("leadTime")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("발송 소요일 정보")
                                                    .optional(),
                                            fieldWithPath("leadTime.minDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("최소 발송일 [0 이상]")
                                                    .optional(),
                                            fieldWithPath("leadTime.maxDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("최대 발송일 [0 이상]")
                                                    .optional(),
                                            fieldWithPath("leadTime.cutoffTime")
                                                    .type(JsonFieldType.STRING)
                                                    .description("당일발송 마감시간 (HH:mm)")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data.policyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("생성된 배송정책 ID"),
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
    @DisplayName("배송정책 수정 API")
    class UpdateShippingPolicyTest {

        @Test
        @DisplayName("배송정책 수정 성공")
        void updateShippingPolicy_Success() throws Exception {
            // given
            UpdateShippingPolicyApiRequest request = ShippingPolicyApiFixtures.updateRequest();
            willDoNothing().given(updateUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            put(
                                            "/api/v2/sellers/{sellerId}/shipping-policies/{policyId}",
                                            SELLER_ID,
                                            POLICY_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("sellerId").description("셀러 ID"),
                                            parameterWithName("policyId").description("배송정책 ID")),
                                    requestFields(
                                            fieldWithPath("policyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책명 [필수, 1~100자]"),
                                            fieldWithPath("defaultPolicy")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("기본 정책 여부 [필수]"),
                                            fieldWithPath("shippingFeeType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "배송비 유형 [필수] +\n"
                                                                    + "FREE: 무료배송, "
                                                                    + "PAID: 유료배송, "
                                                                    + "CONDITIONAL_FREE: 조건부 무료, "
                                                                    + "QUANTITY_BASED: 수량별 배송비"),
                                            fieldWithPath("baseFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("기본 배송비 (원) [0 이상]")
                                                    .optional(),
                                            fieldWithPath("freeThreshold")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description(
                                                            "무료배송 기준금액 (원) - CONDITIONAL_FREE 시 필수"
                                                                    + " [0 이상]")
                                                    .optional(),
                                            fieldWithPath("jejuExtraFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("제주 추가 배송비 (원) [0 이상]")
                                                    .optional(),
                                            fieldWithPath("islandExtraFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("도서산간 추가 배송비 (원) [0 이상]")
                                                    .optional(),
                                            fieldWithPath("returnFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("반품 배송비 (원) [0 이상]")
                                                    .optional(),
                                            fieldWithPath("exchangeFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("교환 배송비 (원) [0 이상]")
                                                    .optional(),
                                            fieldWithPath("leadTime")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("발송 소요일 정보")
                                                    .optional(),
                                            fieldWithPath("leadTime.minDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("최소 발송일 [0 이상]")
                                                    .optional(),
                                            fieldWithPath("leadTime.maxDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("최대 발송일 [0 이상]")
                                                    .optional(),
                                            fieldWithPath("leadTime.cutoffTime")
                                                    .type(JsonFieldType.STRING)
                                                    .description("당일발송 마감시간 (HH:mm)")
                                                    .optional())));
        }
    }

    @Nested
    @DisplayName("배송정책 상태 변경 API")
    class ChangeShippingPolicyStatusTest {

        @Test
        @DisplayName("배송정책 다건 상태 변경 성공")
        void changeShippingPolicyStatus_Success() throws Exception {
            // given
            ChangeShippingPolicyStatusApiRequest request =
                    ShippingPolicyApiFixtures.activateRequest(List.of(1L, 2L, 3L));
            willDoNothing().given(changeStatusUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            patch("/api/v2/sellers/{sellerId}/shipping-policies/status", SELLER_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("sellerId").description("셀러 ID")),
                                    requestFields(
                                            fieldWithPath("policyIds")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("상태 변경할 정책 ID 목록 [필수]"),
                                            fieldWithPath("active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description(
                                                            "변경할 활성화 상태 [필수] +\n"
                                                                    + "true: 활성화, false: 비활성화"))));
        }
    }
}
