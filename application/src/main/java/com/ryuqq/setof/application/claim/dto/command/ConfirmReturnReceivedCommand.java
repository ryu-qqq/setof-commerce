package com.ryuqq.setof.application.claim.dto.command;

import com.ryuqq.setof.domain.claim.vo.InspectionResult;

/**
 * ConfirmReturnReceivedCommand - 반품 수령 확인 및 검수 Command
 *
 * <p>판매자가 반품 상품을 수령하고 검수 결과를 등록합니다.
 *
 * @param claimId 클레임 ID
 * @param inspectionResult 검수 결과 (PASS, FAIL, PARTIAL)
 * @param inspectionNote 검수 메모 (선택)
 * @author development-team
 * @since 2.0.0
 */
public record ConfirmReturnReceivedCommand(
        String claimId, InspectionResult inspectionResult, String inspectionNote) {

    /**
     * String 기반 팩토리 (REST API Layer용)
     *
     * <p>REST API Layer에서 Domain VO를 직접 의존하지 않도록 String으로 파라미터를 받아 변환합니다.
     *
     * @param claimId 클레임 ID
     * @param inspectionResult 검수 결과 (String)
     * @param inspectionNote 검수 메모
     * @return ConfirmReturnReceivedCommand
     */
    public static ConfirmReturnReceivedCommand ofStrings(
            String claimId, String inspectionResult, String inspectionNote) {
        return new ConfirmReturnReceivedCommand(
                claimId, InspectionResult.valueOf(inspectionResult), inspectionNote);
    }
}
