package com.ryuqq.setof.domain.claim.exception;

import com.ryuqq.setof.domain.claim.vo.ReturnShippingStatus;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * ClaimShippingException - 클레임 배송 관련 예외
 *
 * <p>반품/교환 배송 처리 중 발생하는 예외입니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public class ClaimShippingException extends DomainException {

    private ClaimShippingException(
            ClaimErrorCode errorCode, String message, Map<String, Object> args) {
        super(errorCode, message, args);
    }

    /** 수거 예약 불가 */
    public static ClaimShippingException cannotSchedulePickup(ReturnShippingStatus currentStatus) {
        return new ClaimShippingException(
                ClaimErrorCode.CANNOT_SCHEDULE_PICKUP,
                String.format("현재 상태에서 수거 예약을 할 수 없습니다. 현재 상태: %s", currentStatus),
                Map.of("currentStatus", currentStatus.name()));
    }

    /** 반품 송장 등록 불가 */
    public static ClaimShippingException cannotRegisterReturnShipping(
            ReturnShippingStatus currentStatus) {
        return new ClaimShippingException(
                ClaimErrorCode.CANNOT_REGISTER_RETURN_SHIPPING,
                String.format("현재 상태에서 반품 송장을 등록할 수 없습니다. 현재 상태: %s", currentStatus),
                Map.of("currentStatus", currentStatus.name()));
    }

    /** 반품 수령 확인 불가 */
    public static ClaimShippingException cannotConfirmReturnReceived(
            ReturnShippingStatus currentStatus) {
        return new ClaimShippingException(
                ClaimErrorCode.CANNOT_CONFIRM_RETURN_RECEIVED,
                String.format("현재 상태에서 반품 수령을 확인할 수 없습니다. 현재 상태: %s", currentStatus),
                Map.of("currentStatus", currentStatus.name()));
    }

    /** 교환품 발송 등록 불가 */
    public static ClaimShippingException cannotRegisterExchangeShipping(String reason) {
        return new ClaimShippingException(
                ClaimErrorCode.CANNOT_REGISTER_EXCHANGE_SHIPPING,
                String.format("교환품 발송을 등록할 수 없습니다. 사유: %s", reason),
                Map.of("reason", reason));
    }

    /** 반품이 필요하지 않은 클레임 */
    public static ClaimShippingException returnNotRequired(String claimType) {
        return new ClaimShippingException(
                ClaimErrorCode.RETURN_NOT_REQUIRED,
                String.format("반품이 필요하지 않은 클레임 유형입니다: %s", claimType),
                Map.of("claimType", claimType));
    }

    /** 교환이 적용되지 않는 클레임 */
    public static ClaimShippingException exchangeNotApplicable(String claimType) {
        return new ClaimShippingException(
                ClaimErrorCode.EXCHANGE_NOT_APPLICABLE,
                String.format("교환이 적용되지 않는 클레임 유형입니다: %s", claimType),
                Map.of("claimType", claimType));
    }

    /** 유효하지 않은 수거 예약 일시 */
    public static ClaimShippingException invalidPickupSchedule(String reason) {
        return new ClaimShippingException(
                ClaimErrorCode.INVALID_PICKUP_SCHEDULE,
                String.format("유효하지 않은 수거 예약 일시입니다: %s", reason),
                Map.of("reason", reason));
    }
}
