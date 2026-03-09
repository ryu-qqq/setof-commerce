package com.ryuqq.setof.domain.order.vo;

/**
 * 주문 조회용 구매자 정보 VO.
 *
 * <p>payment_bill 테이블의 구매자 정보를 담는 불변 값 객체입니다.
 *
 * @param name 구매자명
 * @param phoneNumber 구매자 연락처
 * @param email 구매자 이메일
 */
public record OrderBuyerInfo(String name, String phoneNumber, String email) {

    public static OrderBuyerInfo of(String name, String phoneNumber, String email) {
        return new OrderBuyerInfo(
                name != null ? name : "",
                phoneNumber != null ? phoneNumber : "",
                email != null ? email : "");
    }
}
