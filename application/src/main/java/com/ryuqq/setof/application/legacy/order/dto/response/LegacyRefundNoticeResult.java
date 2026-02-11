package com.ryuqq.setof.application.legacy.order.dto.response;

/**
 * LegacyRefundNoticeResult - 레거시 환불 정보 결과 DTO.
 *
 * @param returnMethodDomestic 국내 반품 방법
 * @param returnCourierDomestic 국내 반품 택배사
 * @param returnChargeDomestic 국내 반품 비용
 * @param returnAddress 반품 주소
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyRefundNoticeResult(
        String returnMethodDomestic,
        String returnCourierDomestic,
        long returnChargeDomestic,
        String returnAddress) {

    /**
     * 정적 팩토리 메서드.
     *
     * @param returnMethodDomestic 국내 반품 방법
     * @param returnCourierDomestic 국내 반품 택배사
     * @param returnChargeDomestic 국내 반품 비용
     * @param returnAddress 반품 주소
     * @return LegacyRefundNoticeResult
     */
    public static LegacyRefundNoticeResult of(
            String returnMethodDomestic,
            String returnCourierDomestic,
            long returnChargeDomestic,
            String returnAddress) {
        return new LegacyRefundNoticeResult(
                returnMethodDomestic, returnCourierDomestic, returnChargeDomestic, returnAddress);
    }
}
