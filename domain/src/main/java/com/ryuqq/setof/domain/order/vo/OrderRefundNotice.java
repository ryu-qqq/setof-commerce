package com.ryuqq.setof.domain.order.vo;

/**
 * 주문 조회용 환불/반품 안내 VO.
 *
 * <p>order_snapshot_product_delivery 테이블의 반품 안내 정보를 담는 불변 값 객체입니다.
 *
 * @param returnMethodDomestic 국내 반품 방법
 * @param returnCourierDomestic 국내 반품 택배사
 * @param returnChargeDomestic 국내 반품 비용
 * @param returnAddress 반품 주소
 */
public record OrderRefundNotice(
        String returnMethodDomestic,
        String returnCourierDomestic,
        String returnChargeDomestic,
        String returnAddress) {

    public static OrderRefundNotice of(
            String returnMethodDomestic,
            String returnCourierDomestic,
            Integer returnChargeDomestic,
            String returnAddress) {
        return new OrderRefundNotice(
                returnMethodDomestic != null ? returnMethodDomestic : "",
                returnCourierDomestic != null ? returnCourierDomestic : "",
                returnChargeDomestic != null ? String.valueOf(returnChargeDomestic) : "0",
                returnAddress != null ? returnAddress : "");
    }

    /** 반품 안내 정보 없음. */
    public static OrderRefundNotice empty() {
        return new OrderRefundNotice("", "", "0", "");
    }
}
