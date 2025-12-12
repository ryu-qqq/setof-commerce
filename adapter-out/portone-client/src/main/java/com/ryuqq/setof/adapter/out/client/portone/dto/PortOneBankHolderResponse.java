package com.ryuqq.setof.adapter.out.client.portone.dto;

/**
 * PortOne Bank Holder Response
 *
 * <p>포트원 계좌 예금주 조회 응답 DTO
 *
 * @param bankHolderInfo 예금주 정보
 * @author development-team
 * @since 1.0.0
 */
public record PortOneBankHolderResponse(String bankHolderInfo) {

    /**
     * 빈 응답 생성
     *
     * @return 빈 예금주 정보
     */
    public static PortOneBankHolderResponse empty() {
        return new PortOneBankHolderResponse("");
    }

    /**
     * 예금주 정보 존재 여부
     *
     * @return 예금주 정보가 있으면 true
     */
    public boolean hasHolder() {
        return bankHolderInfo != null && !bankHolderInfo.isBlank();
    }
}
