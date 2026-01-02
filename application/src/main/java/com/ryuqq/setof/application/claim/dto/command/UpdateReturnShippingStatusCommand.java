package com.ryuqq.setof.application.claim.dto.command;

import com.ryuqq.setof.domain.claim.vo.ReturnShippingStatus;

/**
 * UpdateReturnShippingStatusCommand - 반품 배송 상태 업데이트 Command
 *
 * <p>택배사 Webhook이나 관리자에 의해 배송 상태가 변경될 때 사용합니다.
 *
 * @param claimId 클레임 ID
 * @param newStatus 새로운 배송 상태
 * @param trackingNumber 송장 번호 (수거 완료 시 업데이트)
 * @author development-team
 * @since 2.0.0
 */
public record UpdateReturnShippingStatusCommand(
        String claimId, ReturnShippingStatus newStatus, String trackingNumber) {

    /**
     * String 기반 팩토리 (REST API Layer용)
     *
     * <p>REST API Layer에서 Domain VO를 직접 의존하지 않도록 String으로 파라미터를 받아 변환합니다.
     *
     * @param claimId 클레임 ID
     * @param newStatus 새로운 배송 상태 (String)
     * @param trackingNumber 송장 번호
     * @return UpdateReturnShippingStatusCommand
     */
    public static UpdateReturnShippingStatusCommand ofStrings(
            String claimId, String newStatus, String trackingNumber) {
        return new UpdateReturnShippingStatusCommand(
                claimId, ReturnShippingStatus.valueOf(newStatus), trackingNumber);
    }
}
