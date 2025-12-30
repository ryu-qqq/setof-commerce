package com.ryuqq.setof.application.claim.dto.command;

import com.ryuqq.setof.domain.claim.vo.ReturnShippingMethod;

/**
 * RegisterReturnShippingCommand - 반품 송장 등록 Command
 *
 * <p>고객이 직접 발송하거나 착불 송장 발급 시 사용합니다.
 *
 * @param claimId 클레임 ID
 * @param shippingMethod 배송 방식
 * @param trackingNumber 송장 번호
 * @param carrier 배송사
 * @author development-team
 * @since 2.0.0
 */
public record RegisterReturnShippingCommand(
        String claimId,
        ReturnShippingMethod shippingMethod,
        String trackingNumber,
        String carrier) {

    /**
     * String 기반 팩토리 (REST API Layer용)
     *
     * <p>REST API Layer에서 Domain VO를 직접 의존하지 않도록 String으로 파라미터를 받아 변환합니다.
     *
     * @param claimId 클레임 ID
     * @param shippingMethod 배송 방식 (String)
     * @param trackingNumber 송장 번호
     * @param carrier 배송사
     * @return RegisterReturnShippingCommand
     */
    public static RegisterReturnShippingCommand ofStrings(
            String claimId, String shippingMethod, String trackingNumber, String carrier) {
        return new RegisterReturnShippingCommand(
                claimId, ReturnShippingMethod.valueOf(shippingMethod), trackingNumber, carrier);
    }
}
