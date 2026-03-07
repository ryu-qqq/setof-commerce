package com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.ShippingAddressApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.ShippingAddressV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.response.ShippingAddressV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.mapper.ShippingAddressV1ApiMapper;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResult;
import com.ryuqq.setof.application.shippingaddress.port.in.query.GetShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.query.GetShippingAddressesUseCase;
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
 * ShippingAddressQueryV1Controller REST Docs 테스트.
 *
 * <p>배송지 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ShippingAddressQueryV1Controller REST Docs 테스트")
@WebMvcTest(ShippingAddressQueryV1Controller.class)
@WithMockUser(username = "1", authorities = "NORMAL_GRADE")
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class ShippingAddressQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetShippingAddressesUseCase getShippingAddressesUseCase;

    @MockBean private GetShippingAddressUseCase getShippingAddressUseCase;

    @MockBean private ShippingAddressV1ApiMapper mapper;

    @Nested
    @DisplayName("배송지 목록 조회 API")
    class GetShippingAddressesTest {

        @Test
        @DisplayName("배송지 목록 조회 성공")
        void getShippingAddresses_Success() throws Exception {
            // given
            List<ShippingAddressResult> results =
                    ShippingAddressApiFixtures.shippingAddressResultList();
            List<ShippingAddressV1ApiResponse> response =
                    ShippingAddressApiFixtures.shippingAddressResponseList();

            given(getShippingAddressesUseCase.execute(any())).willReturn(results);
            given(mapper.toResponseList(results)).willReturn(response);

            // when & then
            mockMvc.perform(get(ShippingAddressV1Endpoints.ADDRESS_BOOK))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andExpect(jsonPath("$.data[0].shippingAddressId").value(1L))
                    .andExpect(jsonPath("$.data[0].defaultYn").value("Y"))
                    .andExpect(jsonPath("$.data[0].shippingDetails.receiverName").value("홍길동"))
                    .andExpect(
                            jsonPath("$.data[0].shippingDetails.shippingAddressName")
                                    .value("기본 배송지"))
                    .andExpect(jsonPath("$.data[1].shippingAddressId").value(2L))
                    .andExpect(jsonPath("$.data[1].defaultYn").value("N"))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    responseFields(
                                            fieldWithPath("data[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("배송지 목록"),
                                            fieldWithPath("data[].shippingAddressId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("배송지 ID"),
                                            fieldWithPath("data[].defaultYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("기본 배송지 여부 (Y/N)"),
                                            fieldWithPath("data[].shippingDetails")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("배송지 상세 정보"),
                                            fieldWithPath("data[].shippingDetails.receiverName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수취인명"),
                                            fieldWithPath(
                                                            "data[].shippingDetails.shippingAddressName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배송지명"),
                                            fieldWithPath("data[].shippingDetails.addressLine1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소1"),
                                            fieldWithPath("data[].shippingDetails.addressLine2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소2"),
                                            fieldWithPath("data[].shippingDetails.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("우편번호"),
                                            fieldWithPath("data[].shippingDetails.country")
                                                    .type(JsonFieldType.STRING)
                                                    .description("국가"),
                                            fieldWithPath("data[].shippingDetails.deliveryRequest")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배송 요청사항"),
                                            fieldWithPath("data[].shippingDetails.phoneNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전화번호"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("배송지 빈 목록 조회 성공")
        void getShippingAddresses_Empty_Success() throws Exception {
            // given
            given(getShippingAddressesUseCase.execute(any())).willReturn(List.of());
            given(mapper.toResponseList(List.of())).willReturn(List.of());

            // when & then
            mockMvc.perform(get(ShippingAddressV1Endpoints.ADDRESS_BOOK))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(0))
                    .andExpect(jsonPath("$.response.status").value(200));
        }
    }

    @Nested
    @DisplayName("배송지 단건 조회 API")
    class GetShippingAddressTest {

        @Test
        @DisplayName("배송지 단건 조회 성공")
        void getShippingAddress_Success() throws Exception {
            // given
            long shippingAddressId = 1L;
            ShippingAddressResult result =
                    ShippingAddressApiFixtures.shippingAddressResultAsDefault(shippingAddressId);
            ShippingAddressV1ApiResponse response =
                    ShippingAddressApiFixtures.shippingAddressResponseAsDefault(shippingAddressId);

            given(getShippingAddressUseCase.execute(any(), eq(shippingAddressId)))
                    .willReturn(result);
            given(mapper.toResponse(result)).willReturn(response);

            // when & then
            mockMvc.perform(get(ShippingAddressV1Endpoints.ADDRESS_BOOK_BY_ID, shippingAddressId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.shippingAddressId").value(shippingAddressId))
                    .andExpect(jsonPath("$.data.defaultYn").value("Y"))
                    .andExpect(jsonPath("$.data.shippingDetails.receiverName").value("홍길동"))
                    .andExpect(
                            jsonPath("$.data.shippingDetails.shippingAddressName").value("기본 배송지"))
                    .andExpect(
                            jsonPath("$.data.shippingDetails.addressLine1")
                                    .value("서울특별시 강남구 테헤란로 123"))
                    .andExpect(jsonPath("$.data.shippingDetails.zipCode").value("06234"))
                    .andExpect(jsonPath("$.data.shippingDetails.country").value("KR"))
                    .andExpect(jsonPath("$.data.shippingDetails.phoneNumber").value("01012345678"))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName(
                                                            ShippingAddressV1Endpoints
                                                                    .PATH_SHIPPING_ADDRESS_ID)
                                                    .description("배송지 ID")),
                                    responseFields(
                                            fieldWithPath("data.shippingAddressId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("배송지 ID"),
                                            fieldWithPath("data.defaultYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("기본 배송지 여부 (Y/N)"),
                                            fieldWithPath("data.shippingDetails")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("배송지 상세 정보"),
                                            fieldWithPath("data.shippingDetails.receiverName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수취인명"),
                                            fieldWithPath(
                                                            "data.shippingDetails.shippingAddressName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배송지명"),
                                            fieldWithPath("data.shippingDetails.addressLine1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소1"),
                                            fieldWithPath("data.shippingDetails.addressLine2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소2"),
                                            fieldWithPath("data.shippingDetails.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("우편번호"),
                                            fieldWithPath("data.shippingDetails.country")
                                                    .type(JsonFieldType.STRING)
                                                    .description("국가"),
                                            fieldWithPath("data.shippingDetails.deliveryRequest")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배송 요청사항"),
                                            fieldWithPath("data.shippingDetails.phoneNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전화번호"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }
}
