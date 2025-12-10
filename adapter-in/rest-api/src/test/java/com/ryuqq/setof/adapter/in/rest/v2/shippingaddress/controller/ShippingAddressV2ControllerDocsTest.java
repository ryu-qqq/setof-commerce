package com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.dto.command.RegisterShippingAddressV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.dto.command.UpdateShippingAddressV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.mapper.ShippingAddressV2ApiMapper;
import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.SetDefaultShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.application.shippingaddress.port.in.command.DeleteShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.command.RegisterShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.command.SetDefaultShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.command.UpdateShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.query.GetShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.query.GetShippingAddressesUseCase;

/**
 * ShippingAddressV2Controller REST Docs 테스트
 *
 * <p>
 * 배송지 관리 API 문서 생성을 위한 테스트
 *
 * @author development-team
 * @since 2.0.0
 */
@WebMvcTest(controllers = ShippingAddressV2Controller.class)
@Import({ShippingAddressV2Controller.class,
        ShippingAddressV2ControllerDocsTest.TestSecurityConfig.class})
@DisplayName("ShippingAddressV2Controller REST Docs")
class ShippingAddressV2ControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean
    private GetShippingAddressesUseCase getShippingAddressesUseCase;

    @MockitoBean
    private RegisterShippingAddressUseCase registerShippingAddressUseCase;

    @MockitoBean
    private GetShippingAddressUseCase getShippingAddressUseCase;

    @MockitoBean
    private UpdateShippingAddressUseCase updateShippingAddressUseCase;

    @MockitoBean
    private DeleteShippingAddressUseCase deleteShippingAddressUseCase;

    @MockitoBean
    private SetDefaultShippingAddressUseCase setDefaultShippingAddressUseCase;

    @MockitoBean
    private ShippingAddressV2ApiMapper shippingAddressV2ApiMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MemberPrincipal principal;
    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
    private static final String PHONE_NUMBER = "01012345678";
    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");

    @BeforeEach
    void setUpWithSecurity(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation).operationPreprocessors()
                        .withRequestDefaults(prettyPrint()).withResponseDefaults(prettyPrint()))
                .build();
        this.principal = MemberPrincipal.of(MEMBER_ID, PHONE_NUMBER);
    }

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            return http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).build();
        }
    }

    @Test
    @DisplayName("GET /api/v2/members/me/shipping-addresses - 배송지 목록 조회 API 문서")
    void getShippingAddresses() throws Exception {
        // Given
        List<ShippingAddressResponse> addresses = List.of(
                ShippingAddressResponse.of(1L, "집", "홍길동", "01012345678", "서울시 강남구 테헤란로 123",
                        "서울시 강남구 역삼동 123-45", "101동 1001호", "06234", "문 앞에 놔주세요", true, FIXED_TIME,
                        FIXED_TIME),
                ShippingAddressResponse.of(2L, "회사", "홍길동", "01012345678", "서울시 서초구 서초대로 456", null,
                        "A동 501호", "06789", null, false, FIXED_TIME, FIXED_TIME));

        when(getShippingAddressesUseCase.execute(any(UUID.class))).thenReturn(addresses);

        // When & Then
        mockMvc.perform(get(ApiV2Paths.ShippingAddresses.BASE).with(user(principal)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andDo(document("shipping-address-list", responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("요청 성공 여부"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 시각"),
                        fieldWithPath("requestId").type(JsonFieldType.STRING)
                                .description("요청 추적 ID"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("배송지 목록"),
                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("배송지 ID"),
                        fieldWithPath("data[].addressName").type(JsonFieldType.STRING)
                                .description("배송지 이름"),
                        fieldWithPath("data[].receiverName").type(JsonFieldType.STRING)
                                .description("수령인 이름"),
                        fieldWithPath("data[].receiverPhone").type(JsonFieldType.STRING)
                                .description("수령인 연락처"),
                        fieldWithPath("data[].roadAddress").type(JsonFieldType.STRING)
                                .description("도로명 주소"),
                        fieldWithPath("data[].jibunAddress").type(JsonFieldType.STRING)
                                .description("지번 주소").optional(),
                        fieldWithPath("data[].detailAddress").type(JsonFieldType.STRING)
                                .description("상세 주소"),
                        fieldWithPath("data[].zipCode").type(JsonFieldType.STRING)
                                .description("우편번호"),
                        fieldWithPath("data[].deliveryRequest").type(JsonFieldType.STRING)
                                .description("배송 요청사항").optional(),
                        fieldWithPath("data[].isDefault").type(JsonFieldType.BOOLEAN)
                                .description("기본 배송지 여부"),
                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING)
                                .description("생성일시"),
                        fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING)
                                .description("수정일시"),
                        fieldWithPath("error").type(JsonFieldType.NULL).description("에러 정보")
                                .optional())));
    }

    @Test
    @DisplayName("POST /api/v2/members/me/shipping-addresses - 배송지 등록 API 문서")
    void registerShippingAddress() throws Exception {
        // Given
        RegisterShippingAddressV2ApiRequest request = new RegisterShippingAddressV2ApiRequest("집",
                "홍길동", "01012345678", "서울시 강남구 테헤란로 123", "서울시 강남구 역삼동 123-45", "101동 1001호",
                "06234", "문 앞에 놔주세요", true);

        ShippingAddressResponse response = ShippingAddressResponse.of(1L, "집", "홍길동", "01012345678",
                "서울시 강남구 테헤란로 123", "서울시 강남구 역삼동 123-45", "101동 1001호", "06234", "문 앞에 놔주세요", true,
                FIXED_TIME, FIXED_TIME);

        when(shippingAddressV2ApiMapper.toRegisterCommand(any(UUID.class), any()))
                .thenReturn(RegisterShippingAddressCommand.of(UUID.fromString(MEMBER_ID), "집",
                        "홍길동", "01012345678", "서울시 강남구 테헤란로 123", "서울시 강남구 역삼동 123-45",
                        "101동 1001호", "06234", "문 앞에 놔주세요", true));
        when(registerShippingAddressUseCase.execute(any(RegisterShippingAddressCommand.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post(ApiV2Paths.ShippingAddresses.BASE).with(user(principal)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andDo(document("shipping-address-register", requestFields(
                        fieldWithPath("addressName").type(JsonFieldType.STRING)
                                .description("배송지 이름"),
                        fieldWithPath("receiverName").type(JsonFieldType.STRING)
                                .description("수령인 이름"),
                        fieldWithPath("receiverPhone").type(JsonFieldType.STRING)
                                .description("수령인 연락처"),
                        fieldWithPath("roadAddress").type(JsonFieldType.STRING)
                                .description("도로명 주소"),
                        fieldWithPath("jibunAddress").type(JsonFieldType.STRING)
                                .description("지번 주소").optional(),
                        fieldWithPath("detailAddress").type(JsonFieldType.STRING)
                                .description("상세 주소"),
                        fieldWithPath("zipCode").type(JsonFieldType.STRING).description("우편번호"),
                        fieldWithPath("deliveryRequest").type(JsonFieldType.STRING)
                                .description("배송 요청사항").optional(),
                        fieldWithPath("isDefault").type(JsonFieldType.BOOLEAN)
                                .description("기본 배송지 여부")),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("요청 성공 여부"),
                                fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                        .description("응답 시각"),
                                fieldWithPath("requestId").type(JsonFieldType.STRING)
                                        .description("요청 추적 ID"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("배송지 ID"),
                                fieldWithPath("data.addressName").type(JsonFieldType.STRING)
                                        .description("배송지 이름"),
                                fieldWithPath("data.receiverName").type(JsonFieldType.STRING)
                                        .description("수령인 이름"),
                                fieldWithPath("data.receiverPhone").type(JsonFieldType.STRING)
                                        .description("수령인 연락처"),
                                fieldWithPath("data.roadAddress").type(JsonFieldType.STRING)
                                        .description("도로명 주소"),
                                fieldWithPath("data.jibunAddress").type(JsonFieldType.STRING)
                                        .description("지번 주소"),
                                fieldWithPath("data.detailAddress").type(JsonFieldType.STRING)
                                        .description("상세 주소"),
                                fieldWithPath("data.zipCode").type(JsonFieldType.STRING)
                                        .description("우편번호"),
                                fieldWithPath("data.deliveryRequest").type(JsonFieldType.STRING)
                                        .description("배송 요청사항"),
                                fieldWithPath("data.isDefault").type(JsonFieldType.BOOLEAN)
                                        .description("기본 배송지 여부"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING)
                                        .description("생성일시"),
                                fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING)
                                        .description("수정일시"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("에러 정보")
                                        .optional())));
    }

    @Test
    @DisplayName("PUT /api/v2/members/me/shipping-addresses/{shippingAddressId} - 배송지 수정 API 문서")
    void updateShippingAddress() throws Exception {
        // Given
        Long shippingAddressId = 1L;
        UpdateShippingAddressV2ApiRequest request =
                new UpdateShippingAddressV2ApiRequest("집", "홍길동", "01012345678", "서울시 강남구 테헤란로 456",
                        "서울시 강남구 역삼동 456-78", "201동 2002호", "06235", "경비실에 맡겨주세요");

        ShippingAddressResponse response = ShippingAddressResponse.of(1L, "집", "홍길동", "01012345678",
                "서울시 강남구 테헤란로 456", "서울시 강남구 역삼동 456-78", "201동 2002호", "06235", "경비실에 맡겨주세요", true,
                FIXED_TIME, FIXED_TIME);

        when(shippingAddressV2ApiMapper.toUpdateCommand(any(UUID.class), any(Long.class), any()))
                .thenReturn(UpdateShippingAddressCommand.of(UUID.fromString(MEMBER_ID),
                        shippingAddressId, "집", "홍길동", "01012345678", "서울시 강남구 테헤란로 456",
                        "서울시 강남구 역삼동 456-78", "201동 2002호", "06235", "경비실에 맡겨주세요"));
        when(updateShippingAddressUseCase.execute(any(UpdateShippingAddressCommand.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(
                put(ApiV2Paths.ShippingAddresses.BASE + "/{shippingAddressId}", shippingAddressId)
                        .with(user(principal)).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
                .andDo(document("shipping-address-update",
                        pathParameters(
                                parameterWithName("shippingAddressId").description("배송지 ID")),
                        requestFields(
                                fieldWithPath("addressName").type(JsonFieldType.STRING)
                                        .description("배송지 이름"),
                                fieldWithPath("receiverName").type(JsonFieldType.STRING)
                                        .description("수령인 이름"),
                                fieldWithPath("receiverPhone").type(JsonFieldType.STRING)
                                        .description("수령인 연락처"),
                                fieldWithPath("roadAddress").type(JsonFieldType.STRING)
                                        .description("도로명 주소"),
                                fieldWithPath("jibunAddress").type(JsonFieldType.STRING)
                                        .description("지번 주소").optional(),
                                fieldWithPath("detailAddress").type(JsonFieldType.STRING)
                                        .description("상세 주소"),
                                fieldWithPath("zipCode").type(JsonFieldType.STRING)
                                        .description("우편번호"),
                                fieldWithPath("deliveryRequest").type(JsonFieldType.STRING)
                                        .description("배송 요청사항").optional()),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("요청 성공 여부"),
                                fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                        .description("응답 시각"),
                                fieldWithPath("requestId").type(JsonFieldType.STRING)
                                        .description("요청 추적 ID"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("배송지 ID"),
                                fieldWithPath("data.addressName").type(JsonFieldType.STRING)
                                        .description("배송지 이름"),
                                fieldWithPath("data.receiverName").type(JsonFieldType.STRING)
                                        .description("수령인 이름"),
                                fieldWithPath("data.receiverPhone").type(JsonFieldType.STRING)
                                        .description("수령인 연락처"),
                                fieldWithPath("data.roadAddress").type(JsonFieldType.STRING)
                                        .description("도로명 주소"),
                                fieldWithPath("data.jibunAddress").type(JsonFieldType.STRING)
                                        .description("지번 주소"),
                                fieldWithPath("data.detailAddress").type(JsonFieldType.STRING)
                                        .description("상세 주소"),
                                fieldWithPath("data.zipCode").type(JsonFieldType.STRING)
                                        .description("우편번호"),
                                fieldWithPath("data.deliveryRequest").type(JsonFieldType.STRING)
                                        .description("배송 요청사항"),
                                fieldWithPath("data.isDefault").type(JsonFieldType.BOOLEAN)
                                        .description("기본 배송지 여부"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING)
                                        .description("생성일시"),
                                fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING)
                                        .description("수정일시"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("에러 정보")
                                        .optional())));
    }

    @Test
    @DisplayName("PATCH /api/v2/members/me/shipping-addresses/{shippingAddressId}/delete - 배송지 삭제"
            + " API 문서")
    void deleteShippingAddress() throws Exception {
        // Given
        Long shippingAddressId = 1L;

        when(shippingAddressV2ApiMapper.toDeleteCommand(any(UUID.class), any(Long.class)))
                .thenReturn(DeleteShippingAddressCommand.of(UUID.fromString(MEMBER_ID),
                        shippingAddressId));
        doNothing().when(deleteShippingAddressUseCase)
                .execute(any(DeleteShippingAddressCommand.class));

        // When & Then
        mockMvc.perform(patch(ApiV2Paths.ShippingAddresses.BASE + "/{shippingAddressId}"
                + ApiV2Paths.ShippingAddresses.DELETE_PATH, shippingAddressId).with(user(principal))
                        .with(csrf()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
                .andDo(document("shipping-address-delete",
                        pathParameters(
                                parameterWithName("shippingAddressId").description("배송지 ID")),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("요청 성공 여부"),
                                fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                        .description("응답 시각"),
                                fieldWithPath("requestId").type(JsonFieldType.STRING)
                                        .description("요청 추적 ID"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터 (삭제 시 null)"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("에러 정보")
                                        .optional())));
    }

    @Test
    @DisplayName("PATCH /api/v2/members/me/shipping-addresses/{shippingAddressId}/default - 기본 배송지"
            + " 설정 API 문서")
    void setDefaultShippingAddress() throws Exception {
        // Given
        Long shippingAddressId = 1L;
        ShippingAddressResponse response = ShippingAddressResponse.of(1L, "집", "홍길동", "01012345678",
                "서울시 강남구 테헤란로 123", "서울시 강남구 역삼동 123-45", "101동 1001호", "06234", "문 앞에 놔주세요", true,
                FIXED_TIME, FIXED_TIME);

        when(shippingAddressV2ApiMapper.toSetDefaultCommand(any(UUID.class), any(Long.class)))
                .thenReturn(SetDefaultShippingAddressCommand.of(UUID.fromString(MEMBER_ID),
                        shippingAddressId));
        when(setDefaultShippingAddressUseCase.execute(any(SetDefaultShippingAddressCommand.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(patch(ApiV2Paths.ShippingAddresses.BASE + "/{shippingAddressId}"
                + ApiV2Paths.ShippingAddresses.DEFAULT_PATH, shippingAddressId)
                        .with(user(principal)).with(csrf()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
                .andDo(document("shipping-address-set-default",
                        pathParameters(parameterWithName("shippingAddressId")
                                .description("기본 배송지로 설정할 배송지 ID")),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("요청 성공 여부"),
                                fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                        .description("응답 시각"),
                                fieldWithPath("requestId").type(JsonFieldType.STRING)
                                        .description("요청 추적 ID"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("배송지 ID"),
                                fieldWithPath("data.addressName").type(JsonFieldType.STRING)
                                        .description("배송지 이름"),
                                fieldWithPath("data.receiverName").type(JsonFieldType.STRING)
                                        .description("수령인 이름"),
                                fieldWithPath("data.receiverPhone").type(JsonFieldType.STRING)
                                        .description("수령인 연락처"),
                                fieldWithPath("data.roadAddress").type(JsonFieldType.STRING)
                                        .description("도로명 주소"),
                                fieldWithPath("data.jibunAddress").type(JsonFieldType.STRING)
                                        .description("지번 주소").optional(),
                                fieldWithPath("data.detailAddress").type(JsonFieldType.STRING)
                                        .description("상세 주소"),
                                fieldWithPath("data.zipCode").type(JsonFieldType.STRING)
                                        .description("우편번호"),
                                fieldWithPath("data.deliveryRequest").type(JsonFieldType.STRING)
                                        .description("배송 요청사항").optional(),
                                fieldWithPath("data.isDefault").type(JsonFieldType.BOOLEAN)
                                        .description("기본 배송지 여부"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING)
                                        .description("생성일시"),
                                fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING)
                                        .description("수정일시"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("에러 정보")
                                        .optional())));
    }
}
