package com.ryuqq.setof.domain.claim.vo;

/**
 * ReturnShippingMethod - 반품 배송 방식
 *
 * <p>반품 상품의 수거/배송 방식을 정의합니다.
 *
 * <p>업계 통계:
 *
 * <ul>
 *   <li>SELLER_PICKUP (방문수거): 70-80% - 가장 일반적
 *   <li>SELLER_PREPAID_LABEL (착불송장): 15-20%
 *   <li>CUSTOMER_SHIP (고객선불): 5-10%
 *   <li>CUSTOMER_VISIT (직접내방): 1-5%
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
public enum ReturnShippingMethod {

    /**
     * 판매자 수배 - 방문수거
     *
     * <p>택배사 API를 통해 자동으로 수거 요청됩니다. 고객은 지정된 일시에 상품을 준비만 하면 됩니다.
     */
    SELLER_PICKUP("방문수거", true, true),

    /**
     * 판매자 수배 - 착불 송장 발급
     *
     * <p>판매자가 착불 송장을 발급하고, 고객은 편의점/택배함에 접수합니다. 배송비는 판매자 부담입니다.
     */
    SELLER_PREPAID_LABEL("착불송장", true, false),

    /**
     * 고객 직접 발송 (선불)
     *
     * <p>고객이 택배사를 선택하고 선불로 발송합니다. 주로 해외 반품이나 특수 상품에 사용됩니다.
     */
    CUSTOMER_SHIP("고객발송", false, false),

    /**
     * 고객 직접 내방
     *
     * <p>고객이 오프라인 매장에 직접 방문하여 반품합니다. 오프라인 매장을 보유한 경우에만 사용 가능합니다.
     */
    CUSTOMER_VISIT("직접내방", false, false);

    private final String description;
    private final boolean sellerArranged;
    private final boolean requiresPickupSchedule;

    ReturnShippingMethod(
            String description, boolean sellerArranged, boolean requiresPickupSchedule) {
        this.description = description;
        this.sellerArranged = sellerArranged;
        this.requiresPickupSchedule = requiresPickupSchedule;
    }

    /**
     * 배송 방식 설명 반환
     *
     * @return 설명 (한글)
     */
    public String description() {
        return description;
    }

    /**
     * 판매자가 배송을 수배하는 방식인지 확인
     *
     * @return 판매자 수배면 true
     */
    public boolean isSellerArranged() {
        return sellerArranged;
    }

    /**
     * 수거 예약이 필요한 방식인지 확인
     *
     * @return 방문수거(SELLER_PICKUP)면 true
     */
    public boolean requiresPickupSchedule() {
        return requiresPickupSchedule;
    }

    /**
     * 고객이 직접 처리하는 방식인지 확인
     *
     * @return 고객 발송/내방이면 true
     */
    public boolean isCustomerHandled() {
        return !sellerArranged;
    }
}
