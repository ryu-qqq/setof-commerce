package com.ryuqq.setof.application.order.dto.command;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * 주문 생성 Command
 *
 * @param checkoutId 체크아웃 ID (UUID String)
 * @param paymentId 결제 ID (UUID String)
 * @param sellerId 판매자 ID
 * @param memberId 회원 ID (UUIDv7 String)
 * @param items 주문 상품 목록
 * @param receiverName 수령인 이름
 * @param receiverPhone 수령인 연락처
 * @param address 배송 주소
 * @param addressDetail 상세 주소
 * @param zipCode 우편번호
 * @param memo 배송 메모
 * @param discountAmount 총 할인 금액
 * @param discounts 적용된 할인 목록
 * @param shippingFee 배송비
 * @author development-team
 * @since 1.0.0
 */
public record CreateOrderCommand(
        String checkoutId,
        String paymentId,
        Long sellerId,
        String memberId,
        List<CreateOrderItemCommand> items,
        String receiverName,
        String receiverPhone,
        String address,
        String addressDetail,
        String zipCode,
        String memo,
        BigDecimal discountAmount,
        List<CreateOrderDiscountCommand> discounts,
        BigDecimal shippingFee) {

    public CreateOrderCommand {
        if (checkoutId == null || checkoutId.isBlank()) {
            throw new IllegalArgumentException("checkoutId는 필수입니다");
        }
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("paymentId는 필수입니다");
        }
        if (sellerId == null || sellerId <= 0) {
            throw new IllegalArgumentException("sellerId는 필수입니다");
        }
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("memberId는 필수입니다");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("주문 상품이 비어있습니다");
        }
        if (receiverName == null || receiverName.isBlank()) {
            throw new IllegalArgumentException("수령인 이름은 필수입니다");
        }
        if (receiverPhone == null || receiverPhone.isBlank()) {
            throw new IllegalArgumentException("수령인 연락처는 필수입니다");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("배송 주소는 필수입니다");
        }
        if (zipCode == null || zipCode.isBlank()) {
            throw new IllegalArgumentException("우편번호는 필수입니다");
        }
        // discountAmount와 discounts는 null 허용 (할인 없을 수 있음)
        discounts = discounts != null ? List.copyOf(discounts) : Collections.emptyList();
    }

    /**
     * 하위 호환 Static Factory - 할인 없는 주문 생성
     *
     * @deprecated 할인 정보를 포함하는 생성자 사용 권장
     */
    @Deprecated
    public static CreateOrderCommand withoutDiscount(
            String checkoutId,
            String paymentId,
            Long sellerId,
            String memberId,
            List<CreateOrderItemCommand> items,
            String receiverName,
            String receiverPhone,
            String address,
            String addressDetail,
            String zipCode,
            String memo,
            BigDecimal shippingFee) {
        return new CreateOrderCommand(
                checkoutId,
                paymentId,
                sellerId,
                memberId,
                items,
                receiverName,
                receiverPhone,
                address,
                addressDetail,
                zipCode,
                memo,
                BigDecimal.ZERO,
                Collections.emptyList(),
                shippingFee);
    }

    /**
     * 할인 적용 여부
     *
     * @return 할인이 적용되었으면 true
     */
    public boolean hasDiscount() {
        return discountAmount != null
                && discountAmount.compareTo(BigDecimal.ZERO) > 0
                && !discounts.isEmpty();
    }
}
