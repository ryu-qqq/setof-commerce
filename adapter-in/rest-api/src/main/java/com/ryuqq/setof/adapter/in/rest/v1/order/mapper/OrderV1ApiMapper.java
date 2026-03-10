package com.ryuqq.setof.adapter.in.rest.v1.order.mapper;

import static com.ryuqq.setof.adapter.in.rest.common.util.DateTimeFormatUtils.format;

import com.ryuqq.setof.adapter.in.rest.v1.order.dto.request.SearchOrdersCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.request.UpdateOrderV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderCountV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderHistoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderSliceV1ApiResponse.PageableResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderSliceV1ApiResponse.SortResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderV1ApiResponse.BrandResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderV1ApiResponse.BuyerInfoResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderV1ApiResponse.OptionResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderV1ApiResponse.OrderProductResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderV1ApiResponse.PaymentResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderV1ApiResponse.ReceiverInfoResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderV1ApiResponse.RefundNoticeResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderV1ApiResponse.ShipmentInfoResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.UpdateOrderV1ApiResponse;
import com.ryuqq.setof.application.order.dto.command.UpdateLegacyOrderStatusCommand;
import com.ryuqq.setof.application.order.dto.query.OrderSearchParams;
import com.ryuqq.setof.application.order.dto.response.OrderDetailResult;
import com.ryuqq.setof.application.order.dto.response.OrderHistoryResult;
import com.ryuqq.setof.application.order.dto.response.OrderSliceResult;
import com.ryuqq.setof.application.order.dto.response.OrderStatusCountResult;
import com.ryuqq.setof.application.order.dto.response.UpdateOrderResult;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * OrderV1ApiMapper - 주문 V1 API Response 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class OrderV1ApiMapper {

    private static final int DEFAULT_SIZE = 20;

    /**
     * SearchOrdersCursorV1ApiRequest → OrderSearchParams 변환.
     *
     * <p>LocalDateTime → LocalDate 변환 수행. size 기본값 20.
     */
    public OrderSearchParams toSearchParams(Long userId, SearchOrdersCursorV1ApiRequest request) {
        LocalDate startDate = toLocalDate(request.startDate());
        LocalDate endDate = toLocalDate(request.endDate());
        int size = (request.size() != null) ? request.size() : DEFAULT_SIZE;
        return OrderSearchParams.of(
                userId, startDate, endDate, request.orderStatuses(), request.lastOrderId(), size);
    }

    private LocalDate toLocalDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : null;
    }

    /**
     * OrderStatusCountResult → OrderCountV1ApiResponse 변환.
     *
     * @param result 주문 상태별 건수 결과
     * @return OrderCountV1ApiResponse
     */
    public OrderCountV1ApiResponse toOrderCountResponse(OrderStatusCountResult result) {
        return new OrderCountV1ApiResponse(result.orderStatus(), result.count());
    }

    /**
     * OrderStatusCountResult 목록 → OrderCountV1ApiResponse 목록 변환.
     *
     * @param results 주문 상태별 건수 결과 목록
     * @return OrderCountV1ApiResponse 목록
     */
    public List<OrderCountV1ApiResponse> toOrderCountListResponse(
            List<OrderStatusCountResult> results) {
        return results.stream().map(this::toOrderCountResponse).toList();
    }

    /**
     * OrderHistoryResult → OrderHistoryV1ApiResponse 변환.
     *
     * @param result 주문 이력 결과
     * @return OrderHistoryV1ApiResponse
     */
    public OrderHistoryV1ApiResponse toOrderHistoryResponse(OrderHistoryResult result) {
        return new OrderHistoryV1ApiResponse(
                result.orderId(),
                result.changeReason(),
                result.changeDetailReason(),
                result.orderStatus(),
                result.invoiceNo(),
                result.shipmentCompanyCode(),
                format(result.updateDate()));
    }

    /**
     * OrderHistoryResult 목록 → OrderHistoryV1ApiResponse 목록 변환.
     *
     * @param results 주문 이력 결과 목록
     * @return OrderHistoryV1ApiResponse 목록
     */
    public List<OrderHistoryV1ApiResponse> toOrderHistoryListResponse(
            List<OrderHistoryResult> results) {
        return results.stream().map(this::toOrderHistoryResponse).toList();
    }

    /**
     * OrderSliceResult → OrderSliceV1ApiResponse 변환 (레거시 CustomSlice 호환).
     *
     * @param result 주문 슬라이스 결과
     * @param requestCursor 요청 시 전달된 커서 (first 판단용, null이면 첫 페이지)
     * @param requestSize 요청 시 전달된 페이지 크기
     * @return OrderSliceV1ApiResponse
     */
    public OrderSliceV1ApiResponse toOrderSliceResponse(
            OrderSliceResult result, Long requestCursor, int requestSize) {
        List<OrderV1ApiResponse> content =
                result.content().stream().map(this::toOrderResponse).toList();
        List<OrderCountV1ApiResponse> counts =
                result.orderCounts().stream().map(this::toOrderCountResponse).toList();

        boolean hasNext = result.hasNext();
        boolean isFirst = requestCursor == null;

        Long lastDomainId = hasNext ? result.lastOrderId() : null;
        String cursorValue = lastDomainId != null ? lastDomainId.toString() : null;

        SortResponse sort = SortResponse.defaultUnsorted();
        PageableResponse pageable = new PageableResponse(0, requestSize, sort, 0L, false, true);

        return new OrderSliceV1ApiResponse(
                content,
                !hasNext,
                isFirst,
                0,
                sort,
                requestSize,
                content.size(),
                content.isEmpty(),
                lastDomainId,
                cursorValue,
                counts,
                pageable);
    }

    private OrderV1ApiResponse toOrderResponse(OrderDetailResult result) {
        return new OrderV1ApiResponse(
                toPaymentResponse(result.payment()),
                toOrderProductResponse(result.orderProduct()),
                toBuyerInfoResponse(result.buyerInfo()),
                toReceiverInfoResponse(result.receiverInfo()),
                result.totalExpectedMileageAmount());
    }

    private PaymentResponse toPaymentResponse(OrderDetailResult.PaymentSummaryResult r) {
        return new PaymentResponse(
                r.paymentId(),
                r.paymentAgencyId(),
                r.paymentStatus(),
                r.paymentMethod(),
                format(r.paymentDate()),
                format(r.canceledDate()),
                r.paymentAmount(),
                r.usedMileageAmount(),
                r.cardName(),
                r.cardNumber(),
                r.totalExpectedMileageAmount());
    }

    /**
     * OrderProductResult → OrderProductResponse 변환.
     *
     * <p>결제 상세 응답에서도 재사용합니다.
     *
     * @param r 주문 상품 결과
     * @return OrderProductResponse
     */
    public OrderProductResponse toOrderProductResponse(OrderDetailResult.OrderProductResult r) {
        Set<OptionResponse> options =
                r.options().stream()
                        .map(
                                o ->
                                        new OptionResponse(
                                                o.optionGroupId(),
                                                o.optionDetailId(),
                                                o.optionName(),
                                                o.optionValue()))
                        .collect(Collectors.toSet());

        RefundNoticeResponse refundNotice =
                new RefundNoticeResponse(
                        r.refundNotice().returnMethodDomestic(),
                        r.refundNotice().returnCourierDomestic(),
                        r.refundNotice().returnChargeDomestic(),
                        r.refundNotice().returnAddress());

        ShipmentInfoResponse shipmentInfo =
                new ShipmentInfoResponse(
                        r.shipmentInfo().orderId(),
                        r.shipmentInfo().deliveryStatus(),
                        r.shipmentInfo().companyCode(),
                        r.shipmentInfo().invoiceNo(),
                        format(r.shipmentInfo().insertDate()));

        return new OrderProductResponse(
                r.orderId(),
                r.sellerId(),
                r.sellerName(),
                new BrandResponse(r.brand().brandId(), r.brand().brandName()),
                r.productGroupId(),
                r.productGroupName(),
                r.productId(),
                r.productGroupMainImageUrl(),
                r.productQuantity(),
                r.orderStatus(),
                r.regularPrice(),
                r.salePrice(),
                r.discountPrice(),
                r.directDiscountPrice(),
                r.orderAmount(),
                r.option(),
                options,
                refundNotice,
                shipmentInfo,
                r.reviewYn());
    }

    private BuyerInfoResponse toBuyerInfoResponse(OrderDetailResult.BuyerInfoResult r) {
        return new BuyerInfoResponse(r.name(), r.phoneNumber(), r.email());
    }

    private ReceiverInfoResponse toReceiverInfoResponse(OrderDetailResult.ReceiverInfoResult r) {
        return new ReceiverInfoResponse(
                r.receiverName(),
                r.receiverPhoneNumber(),
                r.addressLine1(),
                r.addressLine2(),
                r.zipCode(),
                r.country(),
                r.deliveryRequest(),
                r.phoneNumber());
    }

    /**
     * UpdateOrderV1ApiRequest → UpdateLegacyOrderStatusCommand 변환.
     *
     * <p>orderId가 null인 경우 (ORDER_FAILED 등) 0L로 처리합니다.
     *
     * @param userId 인증된 사용자 ID
     * @param request 주문 상태 변경 요청
     * @return UpdateLegacyOrderStatusCommand
     */
    public UpdateLegacyOrderStatusCommand toUpdateCommand(
            Long userId, UpdateOrderV1ApiRequest request) {
        long orderId = request.orderId() != null ? request.orderId() : 0L;
        return new UpdateLegacyOrderStatusCommand(
                orderId,
                userId,
                request.orderStatus(),
                request.changeReason(),
                request.changeDetailReason());
    }

    /**
     * UpdateOrderResult → UpdateOrderV1ApiResponse 변환.
     *
     * @param result 주문 상태 변경 결과
     * @return UpdateOrderV1ApiResponse
     */
    public UpdateOrderV1ApiResponse toUpdateOrderResponse(UpdateOrderResult result) {
        return new UpdateOrderV1ApiResponse(
                result.orderId(),
                result.userId(),
                result.toBeOrderStatus(),
                result.asIsOrderStatus(),
                result.changeReason(),
                result.changeDetailReason());
    }
}
