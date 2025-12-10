package com.ryuqq.setof.application.bank.dto.response;

/**
 * 은행 정보 응답 DTO
 *
 * <p>은행 목록 조회 시 반환되는 응답 DTO입니다.
 *
 * @param id 은행 ID
 * @param bankCode 은행 코드 (3자리)
 * @param bankName 은행 이름
 * @param displayOrder 표시 순서
 */
public record BankResponse(Long id, String bankCode, String bankName, int displayOrder) {

    /**
     * Static Factory Method
     *
     * @param id 은행 ID
     * @param bankCode 은행 코드
     * @param bankName 은행 이름
     * @param displayOrder 표시 순서
     * @return BankResponse 인스턴스
     */
    public static BankResponse of(Long id, String bankCode, String bankName, int displayOrder) {
        return new BankResponse(id, bankCode, bankName, displayOrder);
    }
}
