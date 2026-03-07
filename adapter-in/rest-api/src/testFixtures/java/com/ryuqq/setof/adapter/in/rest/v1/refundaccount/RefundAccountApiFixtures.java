package com.ryuqq.setof.adapter.in.rest.v1.refundaccount;

import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.request.RegisterRefundAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.request.UpdateRefundAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.response.RefundAccountV1ApiResponse;
import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResult;

/**
 * RefundAccount V1 API 테스트 Fixtures.
 *
 * <p>환불 계좌 관련 API Request/Response 및 Application Result/Command 객체를 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class RefundAccountApiFixtures {

    private RefundAccountApiFixtures() {}

    // ===== Request Fixtures =====

    public static RegisterRefundAccountV1ApiRequest registerRequest() {
        return new RegisterRefundAccountV1ApiRequest("국민은행", "123456789012", "홍길동");
    }

    public static UpdateRefundAccountV1ApiRequest updateRequest() {
        return new UpdateRefundAccountV1ApiRequest("신한은행", "110123456789", "홍길동");
    }

    // ===== Application Result Fixtures =====

    public static RefundAccountResult refundAccountResult(long refundAccountId) {
        return RefundAccountResult.of(refundAccountId, "국민은행", "123456789012", "홍길동");
    }

    // ===== Application Command Fixtures =====

    public static RegisterRefundAccountCommand registerCommand(Long userId) {
        return new RegisterRefundAccountCommand(userId, "국민은행", "123456789012", "홍길동");
    }

    public static UpdateRefundAccountCommand updateCommand(Long userId, Long refundAccountId) {
        return new UpdateRefundAccountCommand(
                userId, refundAccountId, "신한은행", "110123456789", "홍길동");
    }

    public static DeleteRefundAccountCommand deleteCommand(Long userId, Long refundAccountId) {
        return new DeleteRefundAccountCommand(userId, refundAccountId);
    }

    // ===== API Response Fixtures =====

    public static RefundAccountV1ApiResponse refundAccountResponse(long refundAccountId) {
        return new RefundAccountV1ApiResponse(refundAccountId, "국민은행", "123456789012", "홍길동");
    }
}
