package com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.ShippingAddressApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.ShippingAddressV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.request.RegisterShippingAddressV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.request.UpdateShippingAddressV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.mapper.ShippingAddressV1ApiMapper;
import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.port.in.command.DeleteShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.command.RegisterShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.command.UpdateShippingAddressUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * ShippingAddressCommandV1Controller REST Docs 테스트.
 *
 * <p>배송지 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ShippingAddressCommandV1Controller REST Docs 테스트")
@WebMvcTest(ShippingAddressCommandV1Controller.class)
@WithMockUser(username = "1", authorities = "NORMAL_GRADE")
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class ShippingAddressCommandV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private RegisterShippingAddressUseCase registerShippingAddressUseCase;

    @MockBean private UpdateShippingAddressUseCase updateShippingAddressUseCase;

    @MockBean private DeleteShippingAddressUseCase deleteShippingAddressUseCase;

    @MockBean private ShippingAddressV1ApiMapper mapper;

    @Nested
    @DisplayName("배송지 등록 API")
    class RegisterShippingAddressTest {

        @Test
        @DisplayName("배송지 등록 성공")
        void registerShippingAddress_Success() throws Exception {
            // given
            Long newShippingAddressId = 1L;
            RegisterShippingAddressV1ApiRequest request =
                    ShippingAddressApiFixtures.registerRequest();
            RegisterShippingAddressCommand command = ShippingAddressApiFixtures.registerCommand(1L);

            given(mapper.toRegisterCommand(any(), any())).willReturn(command);
            given(registerShippingAddressUseCase.execute(command)).willReturn(newShippingAddressId);

            // when & then
            mockMvc.perform(
                            post(ShippingAddressV1Endpoints.ADDRESS_BOOK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(newShippingAddressId))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("receiverName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수취인명"),
                                            fieldWithPath("shippingAddressName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배송지명"),
                                            fieldWithPath("addressLine1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소1"),
                                            fieldWithPath("addressLine2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소2")
                                                    .optional(),
                                            fieldWithPath("zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("우편번호"),
                                            fieldWithPath("country")
                                                    .type(JsonFieldType.STRING)
                                                    .description("국가"),
                                            fieldWithPath("deliveryRequest")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배송 요청사항")
                                                    .optional(),
                                            fieldWithPath("phoneNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전화번호"),
                                            fieldWithPath("defaultAddress")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("기본 배송지 여부")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("등록된 배송지 ID"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }

    @Nested
    @DisplayName("배송지 수정 API")
    class UpdateShippingAddressTest {

        @Test
        @DisplayName("배송지 수정 성공")
        void updateShippingAddress_Success() throws Exception {
            // given
            long shippingAddressId = 1L;
            UpdateShippingAddressV1ApiRequest request = ShippingAddressApiFixtures.updateRequest();
            UpdateShippingAddressCommand command =
                    ShippingAddressApiFixtures.updateCommand(1L, shippingAddressId);

            given(mapper.toUpdateCommand(any(), any(), any())).willReturn(command);
            willDoNothing().given(updateShippingAddressUseCase).execute(command);

            // when & then
            mockMvc.perform(
                            put(ShippingAddressV1Endpoints.ADDRESS_BOOK_BY_ID, shippingAddressId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName(
                                                            ShippingAddressV1Endpoints
                                                                    .PATH_SHIPPING_ADDRESS_ID)
                                                    .description("수정할 배송지 ID")),
                                    requestFields(
                                            fieldWithPath("receiverName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수취인명"),
                                            fieldWithPath("shippingAddressName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배송지명"),
                                            fieldWithPath("addressLine1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소1"),
                                            fieldWithPath("addressLine2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소2")
                                                    .optional(),
                                            fieldWithPath("zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("우편번호"),
                                            fieldWithPath("country")
                                                    .type(JsonFieldType.STRING)
                                                    .description("국가"),
                                            fieldWithPath("deliveryRequest")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배송 요청사항")
                                                    .optional(),
                                            fieldWithPath("phoneNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전화번호"),
                                            fieldWithPath("defaultAddress")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("기본 배송지 여부")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NULL)
                                                    .description("응답 데이터 없음"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }

    @Nested
    @DisplayName("배송지 삭제 API")
    class DeleteShippingAddressTest {

        @Test
        @DisplayName("배송지 삭제 성공")
        void deleteShippingAddress_Success() throws Exception {
            // given
            long shippingAddressId = 1L;
            DeleteShippingAddressCommand command =
                    ShippingAddressApiFixtures.deleteCommand(1L, shippingAddressId);

            given(mapper.toDeleteCommand(any(), any())).willReturn(command);
            willDoNothing().given(deleteShippingAddressUseCase).execute(command);

            // when & then
            mockMvc.perform(
                            delete(
                                    ShippingAddressV1Endpoints.ADDRESS_BOOK_BY_ID,
                                    shippingAddressId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName(
                                                            ShippingAddressV1Endpoints
                                                                    .PATH_SHIPPING_ADDRESS_ID)
                                                    .description("삭제할 배송지 ID")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NULL)
                                                    .description("응답 데이터 없음"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }
}
