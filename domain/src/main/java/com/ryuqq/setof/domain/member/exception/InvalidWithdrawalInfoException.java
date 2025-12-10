package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 탈퇴 정보에 대한 도메인 예외
 *
 * <p>HTTP 응답: 400 BAD REQUEST
 *
 * @author development-team
 * @since 1.0.0
 */
public final class InvalidWithdrawalInfoException extends DomainException {

    public InvalidWithdrawalInfoException(String reason) {
        super(
                MemberErrorCode.INVALID_WITHDRAWAL_INFO,
                String.format("탈퇴 정보가 올바르지 않습니다: %s", reason),
                Map.of("reason", reason));
    }

    public InvalidWithdrawalInfoException(String field, String reason) {
        super(
                MemberErrorCode.INVALID_WITHDRAWAL_INFO,
                String.format("탈퇴 정보가 올바르지 않습니다. 필드: %s, 사유: %s", field, reason),
                Map.of(
                        "field", field,
                        "reason", reason));
    }
}
