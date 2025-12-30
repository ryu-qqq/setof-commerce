package com.ryuqq.setof.adapter.in.rest.admin.v1.order.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1PageResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.query.OrderFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderListV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderV1ApiResponse.BuyerInfoV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderV1ApiResponse.OrderProductV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderV1ApiResponse.OrderProductV1ApiResponse.BrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderV1ApiResponse.OrderProductV1ApiResponse.ClothesDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderV1ApiResponse.OrderProductV1ApiResponse.OptionV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderV1ApiResponse.OrderProductV1ApiResponse.PriceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderV1ApiResponse.OrderProductV1ApiResponse.ProductGroupSnapShotDetailsV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderV1ApiResponse.PaymentDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderV1ApiResponse.PaymentShipmentInfoV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderV1ApiResponse.ReceiverInfoV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response.OrderV1ApiResponse.SettlementInfoV1ApiResponse;
import com.ryuqq.setof.application.common.response.SliceResponse;
import com.ryuqq.setof.application.order.dto.query.GetAdminOrdersQuery;
import com.ryuqq.setof.application.order.dto.response.OrderItemResponse;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * V1 Order API Mapper (Admin)
 *
 * <p>V2 Application Response를 V1 API Response로 변환합니다. Strangler Fig 패턴에 따라 V2 UseCase를 재사용하면서 V1 호환
 * 응답을 제공합니다.
 *
 * <p><strong>TODO 필드 목록:</strong>
 *
 * <ul>
 *   <li>buyerInfo - GetMemberUseCase 개발 후 연동 필요
 *   <li>usedMileageAmount - GetMileageUseCase 개발 후 연동 필요
 *   <li>siteName - 시스템 설정에서 조회 필요
 *   <li>shipmentCompanyCode, invoice - GetShipmentUseCase 개발 후 연동 필요
 *   <li>settlementInfo - GetSettlementUseCase 개발 후 연동 필요
 *   <li>productGroupDetails - GetProductSnapshotUseCase 개발 후 연동 필요
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OrderAdminV1ApiMapper {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final String DEFAULT_COUNTRY = "KR";
    private static final String DEFAULT_SITE_NAME = "SETOF";

    /**
     * V2 OrderResponse + PaymentResponse를 V1 OrderV1ApiResponse로 변환
     *
     * @param orderResponse V2 주문 응답
     * @param paymentResponse V2 결제 응답 (nullable)
     * @return V1 주문 응답
     */
    public OrderV1ApiResponse toOrderV1Response(
            OrderResponse orderResponse, PaymentResponse paymentResponse) {

        return new OrderV1ApiResponse(
                extractLegacyOrderId(orderResponse.orderId()),
                toBuyerInfo(orderResponse),
                toPaymentDetail(orderResponse, paymentResponse),
                toReceiverInfo(orderResponse),
                toPaymentShipmentInfo(orderResponse),
                toSettlementInfo(),
                toOrderProduct(orderResponse));
    }

    /**
     * UUID orderId에서 Legacy Long ID 추출
     *
     * <p>V2는 UUID를 사용하고 V1은 Long을 사용합니다. 현재는 해시 기반 변환을 사용하며, 추후 ID 매핑 테이블 도입을 고려해야 합니다.
     *
     * @param orderId UUID 형식의 주문 ID
     * @return Long 형식의 주문 ID (해시 기반)
     */
    private Long extractLegacyOrderId(String orderId) {
        // TODO: UUID → Long 매핑 테이블 도입 검토
        // 현재는 해시코드 절대값 사용 (임시)
        return (long) Math.abs(orderId.hashCode());
    }

    /**
     * 구매자 정보 변환
     *
     * <p>TODO: GetMemberUseCase 개발 후 memberId로 실제 구매자 정보 조회 필요
     */
    private BuyerInfoV1ApiResponse toBuyerInfo(OrderResponse orderResponse) {
        // TODO: GetMemberUseCase로 memberId 기반 조회 필요
        // orderResponse.memberId()로 Member 정보 조회 후 buyerName, buyerEmail, buyerPhoneNumber 설정
        return new BuyerInfoV1ApiResponse(
                null, // TODO: member.getName()
                null, // TODO: member.getEmail()
                null // TODO: member.getPhoneNumber()
                );
    }

    /** 결제 상세 정보 변환 */
    private PaymentDetailV1ApiResponse toPaymentDetail(
            OrderResponse orderResponse, PaymentResponse paymentResponse) {

        if (paymentResponse == null) {
            return new PaymentDetailV1ApiResponse(
                    null, null, null, null, null, null, null, DEFAULT_SITE_NAME, null, null, null);
        }

        return new PaymentDetailV1ApiResponse(
                extractLegacyPaymentId(paymentResponse.paymentId()),
                paymentResponse.pgTransactionId(),
                paymentResponse.status(),
                paymentResponse.method(),
                toLocalDateTime(paymentResponse.approvedAt()),
                toLocalDateTime(paymentResponse.cancelledAt()),
                extractLegacyMemberId(orderResponse.memberId()),
                DEFAULT_SITE_NAME,
                paymentResponse.requestedAmount(),
                paymentResponse.approvedAmount(),
                null // TODO: GetMileageUseCase로 usedMileageAmount 조회 필요
                );
    }

    /** 수령인 정보 변환 */
    private ReceiverInfoV1ApiResponse toReceiverInfo(OrderResponse orderResponse) {
        return new ReceiverInfoV1ApiResponse(
                orderResponse.receiverName(),
                orderResponse.receiverPhone(),
                orderResponse.address(),
                orderResponse.addressDetail(),
                orderResponse.zipCode(),
                DEFAULT_COUNTRY,
                orderResponse.memo());
    }

    /**
     * 배송 정보 변환
     *
     * <p>TODO: GetShipmentUseCase 개발 후 shipmentCompanyCode, invoice 조회 필요
     */
    private PaymentShipmentInfoV1ApiResponse toPaymentShipmentInfo(OrderResponse orderResponse) {
        return new PaymentShipmentInfoV1ApiResponse(
                orderResponse.status(),
                null, // TODO: GetShipmentUseCase로 shipmentCompanyCode 조회 필요
                null, // TODO: GetShipmentUseCase로 invoice 조회 필요
                toLocalDateTime(orderResponse.deliveredAt()));
    }

    /**
     * 정산 정보 변환
     *
     * <p>TODO: GetSettlementUseCase 개발 후 전체 정산 정보 조회 필요
     */
    private SettlementInfoV1ApiResponse toSettlementInfo() {
        // TODO: GetSettlementUseCase로 정산 정보 조회 필요
        // 현재 모든 필드 null 반환
        return new SettlementInfoV1ApiResponse(
                null, // TODO: commissionRate
                null, // TODO: fee
                null, // TODO: expectationSettlementAmount
                null, // TODO: settlementAmount
                null, // TODO: shareRatio
                null, // TODO: expectedSettlementDay
                null // TODO: settlementDay
                );
    }

    /**
     * 주문 상품 정보 변환
     *
     * <p>TODO: GetProductSnapshotUseCase 개발 후 상품 스냅샷 상세 정보 조회 필요
     */
    private OrderProductV1ApiResponse toOrderProduct(OrderResponse orderResponse) {
        if (orderResponse.items() == null || orderResponse.items().isEmpty()) {
            return null;
        }

        // 첫 번째 상품 정보로 대표 상품 생성 (Legacy V1은 단일 상품 구조)
        OrderItemResponse firstItem = orderResponse.items().get(0);

        return new OrderProductV1ApiResponse(
                extractLegacyOrderId(orderResponse.orderId()),
                toProductGroupSnapShotDetails(firstItem),
                toBrand(firstItem),
                null, // TODO: productGroupId - GetProductSnapshotUseCase 필요
                firstItem.productId(),
                firstItem.sellerName(),
                firstItem.productImage(),
                null, // TODO: deliveryArea - GetShippingPolicyUseCase 필요
                firstItem.orderedQuantity(),
                firstItem.status(),
                firstItem.unitPrice(),
                firstItem.totalPrice(),
                null, // TODO: totalExpectedRefundMileageAmount - GetMileageUseCase 필요
                firstItem.optionName(),
                null, // TODO: skuNumber - GetProductSnapshotUseCase 필요
                toOptions(firstItem));
    }

    /**
     * 상품 그룹 스냅샷 상세 변환
     *
     * <p>TODO: GetProductSnapshotUseCase 개발 후 상세 정보 조회 필요
     */
    private ProductGroupSnapShotDetailsV1ApiResponse toProductGroupSnapShotDetails(
            OrderItemResponse item) {
        // TODO: GetProductSnapshotUseCase로 상품 스냅샷 상세 조회 필요
        return new ProductGroupSnapShotDetailsV1ApiResponse(
                item.productName(),
                null, // TODO: optionType
                null, // TODO: managementType
                toPriceFromItem(item),
                item.status(),
                toClothesDetail(), // TODO: 실제 의류 상세 정보
                null, // TODO: sellerId
                null, // TODO: categoryId
                null // TODO: brandId
                );
    }

    /** OrderItemResponse에서 가격 정보 추출 */
    private PriceV1ApiResponse toPriceFromItem(OrderItemResponse item) {
        long unitPrice = item.unitPrice() != null ? item.unitPrice().longValue() : 0L;
        return new PriceV1ApiResponse(
                unitPrice, // regularPrice
                unitPrice, // currentPrice
                unitPrice, // salePrice
                0, // TODO: directDiscountRate
                0L, // TODO: directDiscountPrice
                0 // TODO: discountRate
                );
    }

    /** 브랜드 정보 변환 */
    private BrandV1ApiResponse toBrand(OrderItemResponse item) {
        return new BrandV1ApiResponse(
                null, // TODO: brandId - GetProductSnapshotUseCase 필요
                item.brandName());
    }

    /**
     * 의류 상세 정보 변환
     *
     * <p>TODO: GetProductSnapshotUseCase 개발 후 실제 데이터 조회 필요
     */
    private ClothesDetailV1ApiResponse toClothesDetail() {
        // TODO: GetProductSnapshotUseCase로 의류 상세 조회 필요
        return new ClothesDetailV1ApiResponse(
                null, // TODO: productCondition
                null, // TODO: origin
                null // TODO: styleCode
                );
    }

    /**
     * 옵션 목록 변환
     *
     * <p>TODO: GetProductSnapshotUseCase 개발 후 상세 옵션 정보 조회 필요
     */
    private Set<OptionV1ApiResponse> toOptions(OrderItemResponse item) {
        if (item.optionName() == null || item.optionName().isEmpty()) {
            return Collections.emptySet();
        }

        // TODO: GetProductSnapshotUseCase로 상세 옵션 정보 조회 필요
        // 현재는 optionName만으로 단일 옵션 생성
        OptionV1ApiResponse option =
                new OptionV1ApiResponse(
                        null, // TODO: optionGroupId
                        null, // TODO: optionDetailId
                        "OPTION", // TODO: 실제 옵션명
                        item.optionName());

        return Set.of(option);
    }

    /** UUID paymentId에서 Legacy Long ID 추출 */
    private Long extractLegacyPaymentId(String paymentId) {
        if (paymentId == null) {
            return null;
        }
        // TODO: UUID → Long 매핑 테이블 도입 검토
        return (long) Math.abs(paymentId.hashCode());
    }

    /** UUID memberId에서 Legacy Long ID 추출 */
    private Long extractLegacyMemberId(String memberId) {
        if (memberId == null) {
            return null;
        }
        // TODO: UUID → Long 매핑 테이블 도입 검토
        return (long) Math.abs(memberId.hashCode());
    }

    /** Instant를 LocalDateTime으로 변환 (KST 기준) */
    private LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, KST);
    }

    /**
     * V2 OrderResponse를 V1 OrderListV1ApiResponse로 변환
     *
     * <p>목록 조회 전용 변환 메서드 (결제 정보 없이 주문 정보만으로 변환)
     *
     * @param orderResponse V2 주문 응답
     * @return V1 주문 목록 응답
     */
    public OrderListV1ApiResponse toOrderListV1Response(OrderResponse orderResponse) {
        return new OrderListV1ApiResponse(
                extractLegacyOrderId(orderResponse.orderId()),
                toBuyerInfo(orderResponse),
                toPaymentDetailForList(orderResponse),
                toReceiverInfo(orderResponse),
                toPaymentShipmentInfo(orderResponse),
                toSettlementInfo(),
                toOrderProduct(orderResponse),
                Collections.emptyList() // TODO: OrderHistory 조회 연동 필요
                );
    }

    /** 목록 조회용 결제 상세 정보 변환 (결제 정보 없이 기본값 사용) */
    private PaymentDetailV1ApiResponse toPaymentDetailForList(OrderResponse orderResponse) {
        return new PaymentDetailV1ApiResponse(
                null,
                null,
                null,
                null,
                null,
                null,
                extractLegacyMemberId(orderResponse.memberId()),
                DEFAULT_SITE_NAME,
                orderResponse.totalAmount(),
                orderResponse.totalAmount(),
                null);
    }

    /**
     * OrderFilterV1ApiRequest를 GetAdminOrdersQuery로 변환
     *
     * @param filter V1 주문 필터 요청
     * @return Admin 주문 조회 Query
     */
    public GetAdminOrdersQuery toGetAdminOrdersQuery(OrderFilterV1ApiRequest filter) {
        Instant startDate =
                filter.startDate() != null ? filter.startDate().atZone(KST).toInstant() : null;
        Instant endDate =
                filter.endDate() != null ? filter.endDate().atZone(KST).toInstant() : null;

        String lastOrderId = null;
        if (filter.lastDomainId() != null) {
            // Legacy Long ID → UUID 변환이 필요하지만,
            // 현재는 cursor 문자열로 전달 (Repository에서 처리)
            lastOrderId = filter.lastDomainId().toString();
        }

        return GetAdminOrdersQuery.of(
                filter.sellerId(),
                filter.orderStatusList(),
                filter.searchKeyword(),
                startDate,
                endDate,
                lastOrderId,
                DEFAULT_PAGE_SIZE);
    }

    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * SliceResponse를 V1PageResponse로 변환
     *
     * <p>Cursor 기반 페이징을 Offset 기반 Legacy 응답으로 변환합니다. totalElements는 알 수 없으므로 content.size()를 사용합니다.
     *
     * @param sliceResponse 커서 기반 Slice 응답
     * @return V1 Legacy 페이지 응답
     */
    public V1PageResponse<OrderListV1ApiResponse> toV1PageResponse(
            SliceResponse<OrderListV1ApiResponse> sliceResponse) {
        List<OrderListV1ApiResponse> content = sliceResponse.content();

        // lastDomainId 추출: nextCursor(UUID) → Legacy Long ID 변환
        Long lastDomainId = null;
        if (!content.isEmpty()) {
            OrderListV1ApiResponse lastItem = content.get(content.size() - 1);
            lastDomainId = lastItem.orderId();
        }

        // Cursor 기반이므로 totalElements는 정확히 알 수 없음
        // Legacy API 호환을 위해 hasNext 기반으로 처리
        long estimatedTotal = sliceResponse.hasNext() ? content.size() + 1L : content.size();

        return V1PageResponse.of(
                content,
                0, // cursor 기반에서는 page 개념이 없으므로 0 고정
                sliceResponse.size(),
                estimatedTotal,
                lastDomainId);
    }
}
