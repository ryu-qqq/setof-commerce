package com.ryuqq.setof.integration.test.paymentorder.wiremock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.math.BigDecimal;

/**
 * PG Gateway WireMock 스텁 헬퍼
 *
 * <p>PG사 결제 API를 모킹하여 통합 테스트에서 사용합니다.
 *
 * <p>지원하는 PG사:
 *
 * <ul>
 *   <li>Toss Payments
 *   <li>Kakao Pay
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
public final class PgGatewayWireMockStub {

    private PgGatewayWireMockStub() {
        // Utility class
    }

    // ============================================================
    // Toss Payments 스텁
    // ============================================================

    /**
     * Toss 결제 승인 성공 스텁을 등록합니다.
     *
     * @param wireMockServer WireMock 서버
     * @param paymentKey 결제 키
     * @param orderId 주문 ID
     * @param amount 승인 금액
     */
    public static void stubTossPaymentApproveSuccess(
            WireMockServer wireMockServer, String paymentKey, String orderId, BigDecimal amount) {

        String transactionId = "toss_txn_" + System.currentTimeMillis();

        wireMockServer.stubFor(
                post(urlPathMatching("/v1/payments/" + paymentKey + "/confirm"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
{
    "paymentKey": "%s",
    "orderId": "%s",
    "mId": "tosspayments",
    "currency": "KRW",
    "method": "카드",
    "totalAmount": %s,
    "balanceAmount": %s,
    "status": "DONE",
    "requestedAt": "2025-12-23T10:00:00+09:00",
    "approvedAt": "2025-12-23T10:00:05+09:00",
    "receipt": {
        "url": "https://dashboard.tosspayments.com/receipt/%s"
    },
    "transactionKey": "%s",
    "card": {
        "company": "삼성",
        "number": "532092******1234",
        "installmentPlanMonths": 0,
        "isInterestFree": false,
        "approveNo": "00000000",
        "cardType": "신용",
        "ownerType": "개인"
    }
}
"""
                                                        .formatted(
                                                                paymentKey,
                                                                orderId,
                                                                amount.toPlainString(),
                                                                amount.toPlainString(),
                                                                transactionId,
                                                                transactionId))));
    }

    /**
     * Toss 결제 승인 실패 스텁을 등록합니다 (잔액 부족).
     *
     * @param wireMockServer WireMock 서버
     * @param paymentKey 결제 키
     */
    public static void stubTossPaymentApproveFailInsufficientBalance(
            WireMockServer wireMockServer, String paymentKey) {

        wireMockServer.stubFor(
                post(urlPathMatching("/v1/payments/" + paymentKey + "/confirm"))
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
                                                {
                                                    "code": "REJECT_CARD_PAYMENT",
                                                    "message": "잔액이 부족합니다."
                                                }
                                                """)));
    }

    /**
     * Toss 결제 승인 실패 스텁을 등록합니다 (금액 불일치).
     *
     * @param wireMockServer WireMock 서버
     * @param paymentKey 결제 키
     */
    public static void stubTossPaymentApproveFailAmountMismatch(
            WireMockServer wireMockServer, String paymentKey) {

        wireMockServer.stubFor(
                post(urlPathMatching("/v1/payments/" + paymentKey + "/confirm"))
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
                                                {
                                                    "code": "INVALID_AMOUNT",
                                                    "message": "결제 금액이 일치하지 않습니다."
                                                }
                                                """)));
    }

    /**
     * Toss 결제 승인 실패 스텁을 등록합니다 (만료된 결제).
     *
     * @param wireMockServer WireMock 서버
     * @param paymentKey 결제 키
     */
    public static void stubTossPaymentApproveFailExpired(
            WireMockServer wireMockServer, String paymentKey) {

        wireMockServer.stubFor(
                post(urlPathMatching("/v1/payments/" + paymentKey + "/confirm"))
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
                                                {
                                                    "code": "ALREADY_COMPLETED_PAYMENT",
                                                    "message": "이미 완료된 결제입니다."
                                                }
                                                """)));
    }

    /**
     * Toss 결제 취소 성공 스텁을 등록합니다.
     *
     * @param wireMockServer WireMock 서버
     * @param paymentKey 결제 키
     * @param cancelAmount 취소 금액
     */
    public static void stubTossPaymentCancelSuccess(
            WireMockServer wireMockServer, String paymentKey, BigDecimal cancelAmount) {

        wireMockServer.stubFor(
                post(urlPathMatching("/v1/payments/" + paymentKey + "/cancel"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
{
    "paymentKey": "%s",
    "status": "CANCELED",
    "cancels": [
        {
            "cancelAmount": %s,
            "cancelReason": "고객 요청",
            "canceledAt": "2025-12-23T11:00:00+09:00",
            "transactionKey": "cancel_txn_%s"
        }
    ]
}
"""
                                                        .formatted(
                                                                paymentKey,
                                                                cancelAmount.toPlainString(),
                                                                System.currentTimeMillis()))));
    }

    /**
     * Toss 부분 취소 성공 스텁을 등록합니다.
     *
     * @param wireMockServer WireMock 서버
     * @param paymentKey 결제 키
     * @param cancelAmount 취소 금액
     * @param remainingAmount 남은 금액
     */
    public static void stubTossPartialCancelSuccess(
            WireMockServer wireMockServer,
            String paymentKey,
            BigDecimal cancelAmount,
            BigDecimal remainingAmount) {

        wireMockServer.stubFor(
                post(urlPathMatching("/v1/payments/" + paymentKey + "/cancel"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
{
    "paymentKey": "%s",
    "status": "PARTIAL_CANCELED",
    "balanceAmount": %s,
    "cancels": [
        {
            "cancelAmount": %s,
            "cancelReason": "부분 환불",
            "canceledAt": "2025-12-23T11:00:00+09:00",
            "transactionKey": "partial_cancel_txn_%s"
        }
    ]
}
"""
                                                        .formatted(
                                                                paymentKey,
                                                                remainingAmount.toPlainString(),
                                                                cancelAmount.toPlainString(),
                                                                System.currentTimeMillis()))));
    }

    /**
     * Toss 결제 취소 실패 스텁을 등록합니다.
     *
     * @param wireMockServer WireMock 서버
     * @param paymentKey 결제 키
     */
    public static void stubTossPaymentCancelFail(WireMockServer wireMockServer, String paymentKey) {

        wireMockServer.stubFor(
                post(urlPathMatching("/v1/payments/" + paymentKey + "/cancel"))
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
                                                {
                                                    "code": "ALREADY_CANCELED_PAYMENT",
                                                    "message": "이미 취소된 결제입니다."
                                                }
                                                """)));
    }

    /**
     * Toss 서버 에러 스텁을 등록합니다.
     *
     * @param wireMockServer WireMock 서버
     */
    public static void stubTossServerError(WireMockServer wireMockServer) {
        wireMockServer.stubFor(
                any(urlPathMatching("/v1/payments/.*"))
                        .willReturn(
                                aResponse()
                                        .withStatus(500)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
                                                {
                                                    "code": "INTERNAL_SERVER_ERROR",
                                                    "message": "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
                                                }
                                                """)));
    }

    /**
     * Toss 타임아웃 스텁을 등록합니다.
     *
     * @param wireMockServer WireMock 서버
     * @param delayMilliseconds 지연 시간 (밀리초)
     */
    public static void stubTossTimeout(WireMockServer wireMockServer, int delayMilliseconds) {
        wireMockServer.stubFor(
                any(urlPathMatching("/v1/payments/.*"))
                        .willReturn(aResponse().withStatus(200).withFixedDelay(delayMilliseconds)));
    }

    // ============================================================
    // Kakao Pay 스텁
    // ============================================================

    /**
     * 카카오페이 결제 승인 성공 스텁을 등록합니다.
     *
     * @param wireMockServer WireMock 서버
     * @param tid 거래 ID
     * @param partnerOrderId 파트너 주문 ID
     * @param amount 승인 금액
     */
    public static void stubKakaoPayApproveSuccess(
            WireMockServer wireMockServer, String tid, String partnerOrderId, BigDecimal amount) {

        wireMockServer.stubFor(
                post(urlPathMatching("/v1/payment/approve"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
                                                {
                                                    "aid": "A%s",
                                                    "tid": "%s",
                                                    "cid": "TC0ONETIME",
                                                    "partner_order_id": "%s",
                                                    "partner_user_id": "user_id",
                                                    "payment_method_type": "MONEY",
                                                    "amount": {
                                                        "total": %s,
                                                        "tax_free": 0,
                                                        "vat": 0,
                                                        "point": 0,
                                                        "discount": 0
                                                    },
                                                    "card_info": null,
                                                    "item_name": "상품명",
                                                    "quantity": 1,
                                                    "created_at": "2025-12-23T10:00:00",
                                                    "approved_at": "2025-12-23T10:00:05"
                                                }
                                                """
                                                        .formatted(
                                                                System.currentTimeMillis(),
                                                                tid,
                                                                partnerOrderId,
                                                                amount.toPlainString()))));
    }

    /**
     * 카카오페이 결제 승인 실패 스텁을 등록합니다.
     *
     * @param wireMockServer WireMock 서버
     * @param errorCode 에러 코드
     * @param errorMessage 에러 메시지
     */
    public static void stubKakaoPayApproveFail(
            WireMockServer wireMockServer, String errorCode, String errorMessage) {

        wireMockServer.stubFor(
                post(urlPathMatching("/v1/payment/approve"))
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
                                                {
                                                    "error_code": "%s",
                                                    "error_message": "%s",
                                                    "extras": {}
                                                }
                                                """
                                                        .formatted(errorCode, errorMessage))));
    }

    /**
     * 카카오페이 결제 취소 성공 스텁을 등록합니다.
     *
     * @param wireMockServer WireMock 서버
     * @param tid 거래 ID
     * @param cancelAmount 취소 금액
     */
    public static void stubKakaoPayCancelSuccess(
            WireMockServer wireMockServer, String tid, BigDecimal cancelAmount) {

        wireMockServer.stubFor(
                post(urlPathMatching("/v1/payment/cancel"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
                                                {
                                                    "aid": "A%s",
                                                    "tid": "%s",
                                                    "status": "CANCEL_PAYMENT",
                                                    "canceled_amount": {
                                                        "total": %s,
                                                        "tax_free": 0,
                                                        "vat": 0,
                                                        "point": 0,
                                                        "discount": 0
                                                    },
                                                    "canceled_at": "2025-12-23T11:00:00"
                                                }
                                                """
                                                        .formatted(
                                                                System.currentTimeMillis(),
                                                                tid,
                                                                cancelAmount.toPlainString()))));
    }

    // ============================================================
    // 공통 유틸리티
    // ============================================================

    /**
     * 모든 PG 스텁을 리셋합니다.
     *
     * @param wireMockServer WireMock 서버
     */
    public static void resetAllStubs(WireMockServer wireMockServer) {
        wireMockServer.resetAll();
    }

    /**
     * 네트워크 에러를 시뮬레이션합니다.
     *
     * @param wireMockServer WireMock 서버
     */
    public static void stubNetworkError(WireMockServer wireMockServer) {
        wireMockServer.stubFor(
                any(anyUrl())
                        .willReturn(
                                aResponse()
                                        .withFault(
                                                com.github.tomakehurst.wiremock.http.Fault
                                                        .CONNECTION_RESET_BY_PEER)));
    }

    /**
     * 잘못된 JSON 응답 스텁을 등록합니다.
     *
     * @param wireMockServer WireMock 서버
     */
    public static void stubMalformedJsonResponse(WireMockServer wireMockServer) {
        wireMockServer.stubFor(
                any(anyUrl())
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody("{ invalid json }")));
    }
}
