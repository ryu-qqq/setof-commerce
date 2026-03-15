package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.ryuqq.setof.adapter.in.rest.admin.discount.DiscountPolicyApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountFromExcelV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountTargetV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateDiscountStatusV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountPolicyV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.mapper.DiscountPolicyCommandApiMapper;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResult;
import com.ryuqq.setof.application.discount.port.in.command.CreateDiscountPoliciesFromExcelUseCase;
import com.ryuqq.setof.application.discount.port.in.command.CreateDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.command.ModifyDiscountTargetsUseCase;
import com.ryuqq.setof.application.discount.port.in.command.UpdateDiscountPolicyStatusUseCase;
import com.ryuqq.setof.application.discount.port.in.command.UpdateDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPolicyDetailUseCase;
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
 * DiscountPolicyCommandController REST Docs 테스트.
 *
 * <p>할인 정책 생성/수정/상태변경/대상 관리 v1 API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("DiscountPolicyCommandController REST Docs 테스트")
@WebMvcTest(DiscountPolicyCommandController.class)
@WithMockUser
class DiscountPolicyCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private CreateDiscountPolicyUseCase createUseCase;

    @MockBean private UpdateDiscountPolicyUseCase updateUseCase;

    @MockBean private UpdateDiscountPolicyStatusUseCase updateStatusUseCase;

    @MockBean private ModifyDiscountTargetsUseCase modifyTargetsUseCase;

    @MockBean private CreateDiscountPoliciesFromExcelUseCase createFromExcelUseCase;

    @MockBean private GetDiscountPolicyDetailUseCase getDetailUseCase;

    @MockBean private DiscountPolicyCommandApiMapper mapper;

    private static final Long POLICY_ID = DiscountPolicyApiFixtures.DEFAULT_DISCOUNT_POLICY_ID;

    @Nested
    @DisplayName("할인 정책 생성 API")
    class CreateDiscountPolicyTest {

        @Test
        @DisplayName("할인 정책 생성 성공")
        void create_Success() throws Exception {
            // given
            CreateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.createRequest();
            DiscountPolicyResult result = DiscountPolicyApiFixtures.discountPolicyResult(POLICY_ID);
            DiscountPolicyV1ApiResponse response =
                    DiscountPolicyApiFixtures.discountPolicyApiResponse(POLICY_ID);

            given(mapper.toCommand(any(CreateDiscountV1ApiRequest.class))).willReturn(null);
            given(createUseCase.execute(any())).willReturn(POLICY_ID);
            given(getDetailUseCase.execute(anyLong())).willReturn(result);
            given(mapper.toResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(
                            post("/api/v1/discount")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.discountPolicyId").value(POLICY_ID))
                    .andExpect(
                            jsonPath("$.data.discountDetails.discountPolicyName")
                                    .value(DiscountPolicyApiFixtures.DEFAULT_POLICY_NAME))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("discountPolicyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description(
                                                            "복사 생성 시 원본 할인 정책 ID [선택, null이면 신규"
                                                                    + " 생성]")
                                                    .optional(),
                                            fieldWithPath("discountDetails")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("할인 상세 정보 [필수]"),
                                            fieldWithPath("discountDetails.discountPolicyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 정책명 [필수, 50자 이하]"),
                                            fieldWithPath("discountDetails.discountType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 유형 [필수, RATE/PRICE]"),
                                            fieldWithPath("discountDetails.publisherType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("발행자 유형 [필수, ADMIN/SELLER]"),
                                            fieldWithPath("discountDetails.issueType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "적용 대상 유형 [필수, PRODUCT/SELLER/BRAND]"),
                                            fieldWithPath("discountDetails.discountLimitYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 한도 적용 여부 [필수, Y/N]"),
                                            fieldWithPath("discountDetails.maxDiscountPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description(
                                                            "최대 할인 금액 [0 이상, discountLimitYn=Y일 때"
                                                                    + " 유효]"),
                                            fieldWithPath("discountDetails.shareYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("분담 여부 [필수, Y/N]"),
                                            fieldWithPath("discountDetails.shareRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("분담 비율 [0~100, shareYn=Y일 때 유효]"),
                                            fieldWithPath("discountDetails.discountRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description(
                                                            "할인율 [0~100, discountType=RATE일 때 유효] /"
                                                                    + " 할인금액 [discountType=PRICE일 때"
                                                                    + " 유효]"),
                                            fieldWithPath("discountDetails.policyStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "정책 시작일 [필수, yyyy-MM-dd HH:mm:ss]"),
                                            fieldWithPath("discountDetails.policyEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "정책 종료일 [필수, yyyy-MM-dd HH:mm:ss]"),
                                            fieldWithPath("discountDetails.memo")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메모 [100자 이하]")
                                                    .optional(),
                                            fieldWithPath("discountDetails.priority")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("우선순위 [0~100]"),
                                            fieldWithPath("discountDetails.activeYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("활성화 여부 [필수, Y/N]"),
                                            fieldWithPath("copyCreate")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("복사 생성 여부 (편의 메서드)"),
                                            fieldWithPath("discountDetails.rateDiscount")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("비율 할인 여부 (편의 메서드)"),
                                            fieldWithPath("discountDetails.priceDiscount")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("금액 할인 여부 (편의 메서드)"),
                                            fieldWithPath("discountDetails.active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성화 여부 (편의 메서드)"),
                                            fieldWithPath("discountDetails.shared")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("분담 여부 (편의 메서드)")),
                                    responseFields(
                                            fieldWithPath("data.discountPolicyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("생성된 할인 정책 ID"),
                                            fieldWithPath("data.discountDetails")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("할인 상세 정보"),
                                            fieldWithPath("data.discountDetails.discountPolicyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 정책명"),
                                            fieldWithPath("data.discountDetails.discountType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 유형"),
                                            fieldWithPath("data.discountDetails.publisherType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("발행자 유형"),
                                            fieldWithPath("data.discountDetails.issueType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("적용 대상 유형")
                                                    .optional(),
                                            fieldWithPath("data.discountDetails.discountLimitYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 한도 적용 여부"),
                                            fieldWithPath("data.discountDetails.maxDiscountPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("최대 할인 금액"),
                                            fieldWithPath("data.discountDetails.shareYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("분담 여부"),
                                            fieldWithPath("data.discountDetails.shareRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("분담 비율"),
                                            fieldWithPath("data.discountDetails.discountRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("할인율 또는 할인금액"),
                                            fieldWithPath("data.discountDetails.policyStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책 시작일"),
                                            fieldWithPath("data.discountDetails.policyEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책 종료일"),
                                            fieldWithPath("data.discountDetails.memo")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메모"),
                                            fieldWithPath("data.discountDetails.priority")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("우선순위"),
                                            fieldWithPath("data.discountDetails.activeYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("활성화 여부"),
                                            fieldWithPath("data.insertDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록일시"),
                                            fieldWithPath("data.updateDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시"),
                                            fieldWithPath("data.insertOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록자"),
                                            fieldWithPath("data.updateOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정자"),
                                            fieldWithPath("data.active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성화 여부 (편의 메서드)"),
                                            fieldWithPath("data.discountDetails.rateDiscount")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("비율 할인 여부 (편의 메서드)"),
                                            fieldWithPath("data.discountDetails.priceDiscount")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("금액 할인 여부 (편의 메서드)"),
                                            fieldWithPath("data.discountDetails.withinPolicyPeriod")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("정책 기간 유효 여부 (편의 메서드)"),
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
    @DisplayName("할인 정책 수정 API")
    class UpdateDiscountPolicyTest {

        @Test
        @DisplayName("할인 정책 수정 성공")
        void update_Success() throws Exception {
            // given
            UpdateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.updateRequest();
            DiscountPolicyResult result = DiscountPolicyApiFixtures.discountPolicyResult(POLICY_ID);
            DiscountPolicyV1ApiResponse response =
                    DiscountPolicyApiFixtures.discountPolicyApiResponse(POLICY_ID);

            given(mapper.toCommand(anyLong(), any(UpdateDiscountV1ApiRequest.class)))
                    .willReturn(null);
            willDoNothing().given(updateUseCase).execute(any());
            given(getDetailUseCase.execute(anyLong())).willReturn(result);
            given(mapper.toResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(
                            put("/api/v1/discount/{discountPolicyId}", POLICY_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.discountPolicyId").value(POLICY_ID))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("discountPolicyId")
                                                    .description("수정할 할인 정책 ID")),
                                    requestFields(
                                            fieldWithPath("discountDetails")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("할인 상세 정보 [필수]"),
                                            fieldWithPath("discountDetails.discountPolicyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 정책명 [필수, 50자 이하]"),
                                            fieldWithPath("discountDetails.discountType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 유형 [필수, RATE/PRICE]"),
                                            fieldWithPath("discountDetails.publisherType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("발행자 유형 [필수, ADMIN/SELLER]"),
                                            fieldWithPath("discountDetails.issueType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "적용 대상 유형 [필수, PRODUCT/SELLER/BRAND]"),
                                            fieldWithPath("discountDetails.discountLimitYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 한도 적용 여부 [필수, Y/N]"),
                                            fieldWithPath("discountDetails.maxDiscountPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("최대 할인 금액"),
                                            fieldWithPath("discountDetails.shareYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("분담 여부 [필수, Y/N]"),
                                            fieldWithPath("discountDetails.shareRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("분담 비율"),
                                            fieldWithPath("discountDetails.discountRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("할인율 또는 할인금액"),
                                            fieldWithPath("discountDetails.policyStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "정책 시작일 [필수, yyyy-MM-dd HH:mm:ss]"),
                                            fieldWithPath("discountDetails.policyEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "정책 종료일 [필수, yyyy-MM-dd HH:mm:ss]"),
                                            fieldWithPath("discountDetails.memo")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메모 [100자 이하]")
                                                    .optional(),
                                            fieldWithPath("discountDetails.priority")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("우선순위 [0~100]"),
                                            fieldWithPath("discountDetails.activeYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("활성화 여부 [필수, Y/N]"),
                                            fieldWithPath("targetIds")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("수정 대상 ID 목록 [선택]")
                                                    .optional(),
                                            fieldWithPath("discountDetails.rateDiscount")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("비율 할인 여부 (편의 메서드)"),
                                            fieldWithPath("discountDetails.priceDiscount")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("금액 할인 여부 (편의 메서드)"),
                                            fieldWithPath("discountDetails.active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성화 여부 (편의 메서드)"),
                                            fieldWithPath("discountDetails.shared")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("분담 여부 (편의 메서드)")),
                                    responseFields(
                                            fieldWithPath("data.discountPolicyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("수정된 할인 정책 ID"),
                                            fieldWithPath("data.discountDetails")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("할인 상세 정보"),
                                            fieldWithPath("data.discountDetails.discountPolicyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 정책명"),
                                            fieldWithPath("data.discountDetails.discountType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 유형"),
                                            fieldWithPath("data.discountDetails.publisherType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("발행자 유형"),
                                            fieldWithPath("data.discountDetails.issueType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("적용 대상 유형")
                                                    .optional(),
                                            fieldWithPath("data.discountDetails.discountLimitYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 한도 적용 여부"),
                                            fieldWithPath("data.discountDetails.maxDiscountPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("최대 할인 금액"),
                                            fieldWithPath("data.discountDetails.shareYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("분담 여부"),
                                            fieldWithPath("data.discountDetails.shareRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("분담 비율"),
                                            fieldWithPath("data.discountDetails.discountRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("할인율 또는 할인금액"),
                                            fieldWithPath("data.discountDetails.policyStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책 시작일"),
                                            fieldWithPath("data.discountDetails.policyEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책 종료일"),
                                            fieldWithPath("data.discountDetails.memo")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메모"),
                                            fieldWithPath("data.discountDetails.priority")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("우선순위"),
                                            fieldWithPath("data.discountDetails.activeYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("활성화 여부"),
                                            fieldWithPath("data.insertDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록일시"),
                                            fieldWithPath("data.updateDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시"),
                                            fieldWithPath("data.insertOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록자"),
                                            fieldWithPath("data.updateOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정자"),
                                            fieldWithPath("data.active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성화 여부 (편의 메서드)"),
                                            fieldWithPath("data.discountDetails.rateDiscount")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("비율 할인 여부 (편의 메서드)"),
                                            fieldWithPath("data.discountDetails.priceDiscount")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("금액 할인 여부 (편의 메서드)"),
                                            fieldWithPath("data.discountDetails.withinPolicyPeriod")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("정책 기간 유효 여부 (편의 메서드)"),
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
    @DisplayName("할인 정책 상태 일괄 변경 API")
    class UpdateDiscountPolicyStatusTest {

        @Test
        @DisplayName("할인 정책 상태 일괄 변경 성공")
        void updateStatus_Success() throws Exception {
            // given
            UpdateDiscountStatusV1ApiRequest request =
                    DiscountPolicyApiFixtures.updateStatusRequest();

            given(mapper.toCommand(any(UpdateDiscountStatusV1ApiRequest.class))).willReturn(null);
            willDoNothing().given(updateStatusUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            patch("/api/v1/discounts")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("discountPolicyIds")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description(
                                                            "상태 변경 대상 할인 정책 ID 목록 [필수, 1개 이상]"),
                                            fieldWithPath("activeYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("활성화 여부 [필수, Y: 활성화, N: 비활성화]"),
                                            fieldWithPath("active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성화 여부 (편의 메서드)")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("빈 목록 (변경된 항목 없음)"),
                                            fieldWithPath("timestamp")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 시각")
                                                    .optional(),
                                            fieldWithPath("requestId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("요청 추적 ID")
                                                    .optional())));
        }

        @Test
        @DisplayName("비활성화 요청도 성공한다")
        void updateStatus_Deactivate_Success() throws Exception {
            // given
            UpdateDiscountStatusV1ApiRequest request =
                    DiscountPolicyApiFixtures.updateStatusRequestDeactivate();

            given(mapper.toCommand(any(UpdateDiscountStatusV1ApiRequest.class))).willReturn(null);
            willDoNothing().given(updateStatusUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            patch("/api/v1/discounts")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isEmpty());
        }
    }

    @Nested
    @DisplayName("엑셀 기반 할인 정책 일괄 생성 API")
    class CreateDiscountPoliciesFromExcelTest {

        @Test
        @DisplayName("엑셀 기반 할인 정책 일괄 생성 성공")
        void createFromExcel_Success() throws Exception {
            // given
            List<CreateDiscountFromExcelV1ApiRequest> requests =
                    DiscountPolicyApiFixtures.excelRequests();
            DiscountPolicyResult result1 = DiscountPolicyApiFixtures.discountPolicyResult(1L);
            DiscountPolicyResult result2 =
                    DiscountPolicyApiFixtures.discountPolicyResultFixedAmount(2L);
            DiscountPolicyV1ApiResponse response1 =
                    DiscountPolicyApiFixtures.discountPolicyApiResponse(1L);
            DiscountPolicyV1ApiResponse response2 =
                    DiscountPolicyApiFixtures.discountPolicyApiResponseFixedAmount(2L);

            given(mapper.toCommands(any())).willReturn(null);
            given(createFromExcelUseCase.execute(any())).willReturn(List.of(1L, 2L));
            given(getDetailUseCase.execute(1L)).willReturn(result1);
            given(getDetailUseCase.execute(2L)).willReturn(result2);
            given(mapper.toResponse(result1)).willReturn(response1);
            given(mapper.toResponse(result2)).willReturn(response2);

            // when & then
            mockMvc.perform(
                            post("/api/v1/discounts/excel")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(requests)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data").isArray())
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("엑셀 기반 할인 정책 생성 요청 목록"),
                                            fieldWithPath("[].discountDetails")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("할인 상세 정보 [필수]"),
                                            fieldWithPath("[].discountDetails.discountPolicyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 정책명 [필수, 50자 이하]"),
                                            fieldWithPath("[].discountDetails.discountType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 유형 [필수, RATE/PRICE]"),
                                            fieldWithPath("[].discountDetails.publisherType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("발행자 유형 [필수, ADMIN/SELLER]"),
                                            fieldWithPath("[].discountDetails.issueType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "적용 대상 유형 [필수, PRODUCT/SELLER/BRAND]"),
                                            fieldWithPath("[].discountDetails.discountLimitYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 한도 적용 여부 [필수, Y/N]"),
                                            fieldWithPath("[].discountDetails.maxDiscountPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("최대 할인 금액"),
                                            fieldWithPath("[].discountDetails.shareYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("분담 여부 [필수, Y/N]"),
                                            fieldWithPath("[].discountDetails.shareRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("분담 비율"),
                                            fieldWithPath("[].discountDetails.discountRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("할인율 또는 할인금액"),
                                            fieldWithPath("[].discountDetails.policyStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "정책 시작일 [필수, yyyy-MM-dd HH:mm:ss]"),
                                            fieldWithPath("[].discountDetails.policyEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "정책 종료일 [필수, yyyy-MM-dd HH:mm:ss]"),
                                            fieldWithPath("[].discountDetails.memo")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메모 [100자 이하]")
                                                    .optional(),
                                            fieldWithPath("[].discountDetails.priority")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("우선순위 [0~100]"),
                                            fieldWithPath("[].discountDetails.activeYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("활성화 여부 [필수, Y/N]"),
                                            fieldWithPath("[].targetIds")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("적용 대상 ID 목록 [필수, 1개 이상]"),
                                            fieldWithPath("[].discountDetails.rateDiscount")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("비율 할인 여부 (편의 메서드)"),
                                            fieldWithPath("[].discountDetails.priceDiscount")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("금액 할인 여부 (편의 메서드)"),
                                            fieldWithPath("[].discountDetails.active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성화 여부 (편의 메서드)"),
                                            fieldWithPath("[].discountDetails.shared")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("분담 여부 (편의 메서드)"),
                                            fieldWithPath("[].singleTarget")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("단건 대상 여부 (편의 메서드)")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("생성된 할인 정책 상세 목록"),
                                            fieldWithPath("data[].discountPolicyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("할인 정책 ID"),
                                            fieldWithPath("data[].discountDetails")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("할인 상세 정보"),
                                            fieldWithPath(
                                                            "data[].discountDetails.discountPolicyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 정책명"),
                                            fieldWithPath("data[].discountDetails.discountType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 유형"),
                                            fieldWithPath("data[].discountDetails.publisherType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("발행자 유형"),
                                            fieldWithPath("data[].discountDetails.issueType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("적용 대상 유형")
                                                    .optional(),
                                            fieldWithPath("data[].discountDetails.discountLimitYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 한도 적용 여부"),
                                            fieldWithPath("data[].discountDetails.maxDiscountPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("최대 할인 금액"),
                                            fieldWithPath("data[].discountDetails.shareYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("분담 여부"),
                                            fieldWithPath("data[].discountDetails.shareRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("분담 비율"),
                                            fieldWithPath("data[].discountDetails.discountRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("할인율 또는 할인금액"),
                                            fieldWithPath("data[].discountDetails.policyStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책 시작일"),
                                            fieldWithPath("data[].discountDetails.policyEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책 종료일"),
                                            fieldWithPath("data[].discountDetails.memo")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메모"),
                                            fieldWithPath("data[].discountDetails.priority")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("우선순위"),
                                            fieldWithPath("data[].discountDetails.activeYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("활성화 여부"),
                                            fieldWithPath("data[].insertDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록일시"),
                                            fieldWithPath("data[].updateDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시"),
                                            fieldWithPath("data[].insertOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록자"),
                                            fieldWithPath("data[].updateOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정자"),
                                            fieldWithPath("data[].active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성화 여부 (편의 메서드)"),
                                            fieldWithPath("data[].discountDetails.rateDiscount")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("비율 할인 여부 (편의 메서드)"),
                                            fieldWithPath("data[].discountDetails.priceDiscount")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("금액 할인 여부 (편의 메서드)"),
                                            fieldWithPath(
                                                            "data[].discountDetails.withinPolicyPeriod")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("정책 기간 유효 여부 (편의 메서드)"),
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
    @DisplayName("할인 대상 추가 API")
    class AddDiscountTargetsTest {

        @Test
        @DisplayName("할인 대상 추가 성공")
        void addTargets_Success() throws Exception {
            // given
            CreateDiscountTargetV1ApiRequest request =
                    DiscountPolicyApiFixtures.createTargetRequest();

            given(mapper.toCommand(anyLong(), any(CreateDiscountTargetV1ApiRequest.class)))
                    .willReturn(null);
            willDoNothing().given(modifyTargetsUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            post("/api/v1/discount/{discountPolicyId}/targets", POLICY_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("discountPolicyId")
                                                    .description("할인 정책 ID")),
                                    requestFields(
                                            fieldWithPath("issueType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "적용 대상 유형 [필수, PRODUCT/SELLER/BRAND]"),
                                            fieldWithPath("targetIds")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("대상 ID 목록 [필수, 1개 이상]"),
                                            fieldWithPath("productTarget")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("상품 대상 여부 (편의 메서드)"),
                                            fieldWithPath("sellerTarget")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("판매자 대상 여부 (편의 메서드)"),
                                            fieldWithPath("brandTarget")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("브랜드 대상 여부 (편의 메서드)")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("빈 목록"),
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
    @DisplayName("할인 대상 교체 API")
    class ReplaceDiscountTargetsTest {

        @Test
        @DisplayName("할인 대상 교체 성공")
        void replaceTargets_Success() throws Exception {
            // given
            CreateDiscountTargetV1ApiRequest request =
                    DiscountPolicyApiFixtures.createTargetRequest();

            given(mapper.toCommand(anyLong(), any(CreateDiscountTargetV1ApiRequest.class)))
                    .willReturn(null);
            willDoNothing().given(modifyTargetsUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            put("/api/v1/discount/{discountPolicyId}/targets", POLICY_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("discountPolicyId")
                                                    .description("할인 정책 ID")),
                                    requestFields(
                                            fieldWithPath("issueType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "적용 대상 유형 [필수, PRODUCT/SELLER/BRAND]"),
                                            fieldWithPath("targetIds")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("교체할 대상 ID 목록 [필수, 1개 이상]"),
                                            fieldWithPath("productTarget")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("상품 대상 여부 (편의 메서드)"),
                                            fieldWithPath("sellerTarget")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("판매자 대상 여부 (편의 메서드)"),
                                            fieldWithPath("brandTarget")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("브랜드 대상 여부 (편의 메서드)")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("빈 목록"),
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
