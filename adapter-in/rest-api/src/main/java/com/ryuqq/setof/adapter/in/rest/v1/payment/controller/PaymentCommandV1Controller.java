package com.ryuqq.setof.adapter.in.rest.v1.payment.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.PaymentV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request.CreatePaymentInCartV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request.CreatePaymentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentGatewayV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.mapper.PaymentV1ApiMapper;
import com.ryuqq.setof.application.payment.dto.command.CreatePaymentCommand;
import com.ryuqq.setof.application.payment.dto.command.CreatePaymentInCartCommand;
import com.ryuqq.setof.application.payment.dto.response.PaymentGatewayResult;
import com.ryuqq.setof.application.payment.port.in.command.CreatePaymentInCartUseCase;
import com.ryuqq.setof.application.payment.port.in.command.CreatePaymentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * PaymentCommandV1Controller - 결제 명령 V1 Public API.
 *
 * <p>결제 준비(주문서 생성) 엔드포인트입니다. 클라이언트는 응답의 paymentUniqueId로 PortOne SDK 결제를 진행합니다.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-005: Controller @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "결제 명령 V1", description = "결제 준비(주문서 생성) V1 Public API (인증 필요)")
@RestController
public class PaymentCommandV1Controller {

    private final CreatePaymentUseCase createPaymentUseCase;
    private final CreatePaymentInCartUseCase createPaymentInCartUseCase;
    private final PaymentV1ApiMapper mapper;

    public PaymentCommandV1Controller(
            CreatePaymentUseCase createPaymentUseCase,
            CreatePaymentInCartUseCase createPaymentInCartUseCase,
            PaymentV1ApiMapper mapper) {
        this.createPaymentUseCase = createPaymentUseCase;
        this.createPaymentInCartUseCase = createPaymentInCartUseCase;
        this.mapper = mapper;
    }

    /**
     * 직접 구매 결제 준비 API.
     *
     * <p>POST /api/v1/payment
     *
     * <p>가격 검증 → 재고 차감 → 주문/결제 레코드 생성 → paymentUniqueId 반환.
     *
     * @param userId 인증된 사용자 ID
     * @param request 결제 요청 (주문 항목, 배송지, 결제 수단 등)
     * @return 결제 준비 응답 (paymentUniqueId, paymentId, orderIds)
     */
    @Operation(
            summary = "직접 구매 결제 준비",
            description = "상품 직접 구매 주문서를 생성합니다. 응답의 paymentUniqueId로 PG 결제를 진행하세요.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "결제 준비 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "검증 실패 (가격 불일치, 재고 부족 등)")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping(PaymentV1Endpoints.PAYMENT)
    public ResponseEntity<V1ApiResponse<PaymentGatewayV1ApiResponse>> createPayment(
            @AuthenticatedUserId Long userId,
            @Valid @RequestBody CreatePaymentV1ApiRequest request) {
        CreatePaymentCommand command = mapper.toCreatePaymentCommand(userId, request);
        PaymentGatewayResult result = createPaymentUseCase.execute(command);
        PaymentGatewayV1ApiResponse response = mapper.toPaymentGatewayResponse(result);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 장바구니 구매 결제 준비 API.
     *
     * <p>POST /api/v1/payment/cart
     *
     * <p>직접 구매와 동일한 흐름 + 결제 성공 시 장바구니 아이템 소프트 삭제.
     *
     * @param userId 인증된 사용자 ID
     * @param request 장바구니 결제 요청 (cartId 포함 주문 항목)
     * @return 결제 준비 응답
     */
    @Operation(
            summary = "장바구니 구매 결제 준비",
            description = "장바구니 상품으로 주문서를 생성합니다. 결제 성공 시 장바구니 아이템이 자동 삭제됩니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "결제 준비 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "검증 실패")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping(PaymentV1Endpoints.PAYMENT_CART)
    public ResponseEntity<V1ApiResponse<PaymentGatewayV1ApiResponse>> createPaymentInCart(
            @AuthenticatedUserId Long userId,
            @Valid @RequestBody CreatePaymentInCartV1ApiRequest request) {
        CreatePaymentInCartCommand command = mapper.toCreatePaymentInCartCommand(userId, request);
        PaymentGatewayResult result = createPaymentInCartUseCase.execute(command);
        PaymentGatewayV1ApiResponse response = mapper.toPaymentGatewayResponse(result);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
