package com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.controller;

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
import com.ryuqq.setof.adapter.in.rest.admin.refundpolicy.RefundPolicyApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.ChangeRefundPolicyStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.RegisterRefundPolicyApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.UpdateRefundPolicyApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.mapper.RefundPolicyCommandApiMapper;
import com.ryuqq.setof.application.refundpolicy.port.in.command.ChangeRefundPolicyStatusUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.RegisterRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.UpdateRefundPolicyUseCase;
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
 * RefundPolicyCommandController REST Docs 테스트.
 *
 * <p>환불정책 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("RefundPolicyCommandController REST Docs 테스트")
@WebMvcTest(RefundPolicyCommandController.class)
@WithMockUser
class RefundPolicyCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private RegisterRefundPolicyUseCase registerUseCase;

    @MockBean private UpdateRefundPolicyUseCase updateUseCase;

    @MockBean private ChangeRefundPolicyStatusUseCase changeStatusUseCase;

    @MockBean private RefundPolicyCommandApiMapper mapper;

    private static final Long SELLER_ID = 1L;
    private static final Long POLICY_ID = 100L;

    @Nested
    @DisplayName("환불정책 등록 API")
    class RegisterRefundPolicyTest {

        @Test
        @DisplayName("환불정책 등록 성공")
        void registerRefundPolicy_Success() throws Exception {
            // given
            RegisterRefundPolicyApiRequest request = RefundPolicyApiFixtures.registerRequest();
            given(registerUseCase.execute(any())).willReturn(POLICY_ID);

            // when & then
            mockMvc.perform(
                            post("/api/v2/sellers/{sellerId}/refund-policies", SELLER_ID)
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
                                            fieldWithPath("returnPeriodDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("반품 가능 기간 [필수, 1~90일]"),
                                            fieldWithPath("exchangePeriodDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("교환 가능 기간 [필수, 1~90일]"),
                                            fieldWithPath("nonReturnableConditions")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description(
                                                            "반품 불가 조건 목록 +\n"
                                                                    + "OPENED_PACKAGING: 포장 개봉, "
                                                                    + "USED_PRODUCT: 사용 흔적, "
                                                                    + "TIME_EXPIRED: 기간 만료, "
                                                                    + "DIGITAL_CONTENT: 디지털 콘텐츠, "
                                                                    + "CUSTOM_MADE: 주문제작, "
                                                                    + "HYGIENE_PRODUCT: 위생용품, "
                                                                    + "PARTIAL_SET: 일부 누락, "
                                                                    + "MISSING_TAG: 택 제거, "
                                                                    + "DAMAGED_BY_CUSTOMER: 고객 훼손")
                                                    .optional(),
                                            fieldWithPath("partialRefundEnabled")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("부분 환불 허용 여부")
                                                    .optional(),
                                            fieldWithPath("inspectionRequired")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("검수 필요 여부")
                                                    .optional(),
                                            fieldWithPath("inspectionPeriodDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("검수 소요 기간 (일) [0 이상]")
                                                    .optional(),
                                            fieldWithPath("additionalInfo")
                                                    .type(JsonFieldType.STRING)
                                                    .description("추가 안내 문구 [최대 1000자]")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data.policyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("생성된 환불정책 ID"),
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
    @DisplayName("환불정책 수정 API")
    class UpdateRefundPolicyTest {

        @Test
        @DisplayName("환불정책 수정 성공")
        void updateRefundPolicy_Success() throws Exception {
            // given
            UpdateRefundPolicyApiRequest request = RefundPolicyApiFixtures.updateRequest();
            willDoNothing().given(updateUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            put(
                                            "/api/v2/sellers/{sellerId}/refund-policies/{policyId}",
                                            SELLER_ID,
                                            POLICY_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("sellerId").description("셀러 ID"),
                                            parameterWithName("policyId").description("환불정책 ID")),
                                    requestFields(
                                            fieldWithPath("policyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책명 [필수, 1~100자]"),
                                            fieldWithPath("defaultPolicy")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("기본 정책 여부 [필수]"),
                                            fieldWithPath("returnPeriodDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("반품 가능 기간 [필수, 1~90일]"),
                                            fieldWithPath("exchangePeriodDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("교환 가능 기간 [필수, 1~90일]"),
                                            fieldWithPath("nonReturnableConditions")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description(
                                                            "반품 불가 조건 목록 +\n"
                                                                    + "OPENED_PACKAGING: 포장 개봉, "
                                                                    + "USED_PRODUCT: 사용 흔적, "
                                                                    + "TIME_EXPIRED: 기간 만료, "
                                                                    + "DIGITAL_CONTENT: 디지털 콘텐츠, "
                                                                    + "CUSTOM_MADE: 주문제작, "
                                                                    + "HYGIENE_PRODUCT: 위생용품, "
                                                                    + "PARTIAL_SET: 일부 누락, "
                                                                    + "MISSING_TAG: 택 제거, "
                                                                    + "DAMAGED_BY_CUSTOMER: 고객 훼손")
                                                    .optional(),
                                            fieldWithPath("partialRefundEnabled")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("부분 환불 허용 여부")
                                                    .optional(),
                                            fieldWithPath("inspectionRequired")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("검수 필요 여부")
                                                    .optional(),
                                            fieldWithPath("inspectionPeriodDays")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("검수 소요 기간 (일) [0 이상]")
                                                    .optional(),
                                            fieldWithPath("additionalInfo")
                                                    .type(JsonFieldType.STRING)
                                                    .description("추가 안내 문구 [최대 1000자]")
                                                    .optional())));
        }
    }

    @Nested
    @DisplayName("환불정책 상태 변경 API")
    class ChangeRefundPolicyStatusTest {

        @Test
        @DisplayName("환불정책 다건 상태 변경 성공")
        void changeRefundPolicyStatus_Success() throws Exception {
            // given
            ChangeRefundPolicyStatusApiRequest request =
                    RefundPolicyApiFixtures.activateRequest(List.of(1L, 2L, 3L));
            willDoNothing().given(changeStatusUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            patch("/api/v2/sellers/{sellerId}/refund-policies/status", SELLER_ID)
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
