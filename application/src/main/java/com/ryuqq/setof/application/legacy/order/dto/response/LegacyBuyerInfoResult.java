package com.ryuqq.setof.application.legacy.order.dto.response;

/**
 * LegacyBuyerInfoResult - 레거시 구매자 정보 결과 DTO.
 *
 * @param name 구매자명
 * @param phoneNumber 전화번호
 * @param email 이메일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyBuyerInfoResult(String name, String phoneNumber, String email) {

    /**
     * 정적 팩토리 메서드.
     *
     * @param name 구매자명
     * @param phoneNumber 전화번호
     * @param email 이메일
     * @return LegacyBuyerInfoResult
     */
    public static LegacyBuyerInfoResult of(String name, String phoneNumber, String email) {
        return new LegacyBuyerInfoResult(name, phoneNumber, email);
    }
}
