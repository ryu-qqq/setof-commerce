package com.ryuqq.setof.adapter.in.rest.v1.payment.mapper;

import static com.ryuqq.setof.adapter.in.rest.common.util.DateTimeFormatUtils.format;

import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.order.mapper.OrderV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request.CreateOrderInCartV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request.CreateOrderV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request.CreatePaymentInCartV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request.CreatePaymentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request.RefundAccountInfoV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request.SearchPaymentsCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request.ShippingInfoV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentDetailV1ApiResponse.BuyerInfoV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentDetailV1ApiResponse.ReceiverInfoV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentDetailV1ApiResponse.RefundAccountV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentDetailV1ApiResponse.VBankV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentGatewayV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentListItemV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PaymentSliceV1ApiResponse;
import com.ryuqq.setof.application.payment.dto.command.CartOrderItemCommand;
import com.ryuqq.setof.application.payment.dto.command.CreatePaymentCommand;
import com.ryuqq.setof.application.payment.dto.command.CreatePaymentInCartCommand;
import com.ryuqq.setof.application.payment.dto.command.PaymentOrderItemCommand;
import com.ryuqq.setof.application.payment.dto.command.RefundAccountInfoCommand;
import com.ryuqq.setof.application.payment.dto.command.ShippingInfoCommand;
import com.ryuqq.setof.application.payment.dto.query.PaymentSearchParams;
import com.ryuqq.setof.application.payment.dto.response.PaymentDetailResult;
import com.ryuqq.setof.application.payment.dto.response.PaymentGatewayResult;
import com.ryuqq.setof.application.payment.dto.response.PaymentOverviewResult;
import com.ryuqq.setof.application.payment.dto.response.PaymentSliceResult;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * PaymentV1ApiMapper - 결제 V1 API Response 변환 매퍼.
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
public class PaymentV1ApiMapper {

    private static final int DEFAULT_SIZE = 20;
    private final OrderV1ApiMapper orderV1ApiMapper;

    public PaymentV1ApiMapper(OrderV1ApiMapper orderV1ApiMapper) {
        this.orderV1ApiMapper = orderV1ApiMapper;
    }

    /**
     * SearchPaymentsCursorV1ApiRequest → PaymentSearchParams 변환.
     *
     * <p>LocalDateTime → LocalDate 변환 수행. size 기본값 20.
     *
     * @param userId 인증된 사용자 ID
     * @param request 결제 목록 검색 요청
     * @return PaymentSearchParams
     */
    public PaymentSearchParams toSearchParams(
            Long userId, SearchPaymentsCursorV1ApiRequest request) {
        LocalDate startDate = toLocalDate(request.startDate());
        LocalDate endDate = toLocalDate(request.endDate());
        int size = (request.size() != null) ? request.size() : DEFAULT_SIZE;
        return PaymentSearchParams.of(
                userId, startDate, endDate, request.orderStatuses(), request.lastPaymentId(), size);
    }

    private LocalDate toLocalDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : null;
    }

    /**
     * PaymentSliceResult → PaymentSliceV1ApiResponse 변환 (레거시 CustomSlice 호환).
     *
     * @param result 결제 슬라이스 결과
     * @return PaymentSliceV1ApiResponse
     */
    public PaymentSliceV1ApiResponse toPaymentSliceResponse(PaymentSliceResult result) {
        List<PaymentListItemV1ApiResponse> content =
                result.content().stream().map(this::toPaymentListItem).toList();
        return new PaymentSliceV1ApiResponse(
                content,
                result.last(),
                result.first(),
                result.number(),
                PaymentSliceV1ApiResponse.SortV1ApiResponse.defaultUnsorted(),
                result.size(),
                result.numberOfElements(),
                result.empty(),
                result.lastDomainId(),
                result.cursorValue(),
                result.totalElements());
    }

    private PaymentListItemV1ApiResponse toPaymentListItem(PaymentOverviewResult result) {
        PaymentListItemV1ApiResponse.PaymentInfo paymentInfo =
                new PaymentListItemV1ApiResponse.PaymentInfo(
                        result.paymentId(),
                        result.paymentAgencyId(),
                        result.paymentStatus(),
                        result.paymentMethodEnum(),
                        result.paymentMethod(),
                        format(result.paymentDate()),
                        format(result.canceledDate()),
                        result.userId(),
                        result.siteName(),
                        result.preDiscountAmount(),
                        result.paymentAmount(),
                        result.usedMileageAmount(),
                        result.cardName(),
                        result.cardNumber(),
                        result.totalExpectedMileageAmount());

        PaymentListItemV1ApiResponse.VBankAccountInfo vBankInfo =
                toVBankAccountInfo(result.vBank());

        List<OrderV1ApiResponse.OrderProductResponse> orderProducts =
                result.orderProducts().stream()
                        .map(orderV1ApiMapper::toOrderProductResponse)
                        .toList();

        return new PaymentListItemV1ApiResponse(paymentInfo, vBankInfo, orderProducts);
    }

    private PaymentListItemV1ApiResponse.VBankAccountInfo toVBankAccountInfo(
            PaymentOverviewResult.VBankResult vBank) {
        if (vBank == null) {
            return new PaymentListItemV1ApiResponse.VBankAccountInfo(null, null, 0, null);
        }
        return new PaymentListItemV1ApiResponse.VBankAccountInfo(
                vBank.bankName(),
                vBank.accountNumber(),
                vBank.paymentAmount(),
                format(vBank.dueDate()));
    }

    /**
     * PaymentDetailResult → PaymentDetailV1ApiResponse 변환.
     *
     * @param result 결제 단건 상세 결과
     * @return PaymentDetailV1ApiResponse
     */
    public PaymentDetailV1ApiResponse toPaymentDetailResponse(PaymentDetailResult result) {
        VBankV1ApiResponse vBank = toDetailVBankResponse(result.vBank());
        BuyerInfoV1ApiResponse buyerInfo = toBuyerInfoResponse(result.buyerInfo());
        ReceiverInfoV1ApiResponse receiverInfo = toReceiverInfoResponse(result.receiverInfo());
        RefundAccountV1ApiResponse refundAccount = toRefundAccountResponse(result.refundAccount());
        List<OrderV1ApiResponse.OrderProductResponse> orderProducts =
                result.orderProducts().stream()
                        .map(orderV1ApiMapper::toOrderProductResponse)
                        .toList();
        return new PaymentDetailV1ApiResponse(
                result.paymentId(),
                result.paymentStatus(),
                result.paymentMethod(),
                format(result.paymentDate()),
                format(result.canceledDate()),
                result.paymentAmount(),
                result.usedMileageAmount(),
                result.paymentAgencyId(),
                result.cardName(),
                result.cardNumber(),
                result.orderIds(),
                orderProducts,
                vBank,
                buyerInfo,
                receiverInfo,
                refundAccount);
    }

    private VBankV1ApiResponse toDetailVBankResponse(PaymentDetailResult.VBankResult vBank) {
        if (vBank == null) {
            return null;
        }
        return new VBankV1ApiResponse(
                vBank.bankName(),
                vBank.accountNumber(),
                vBank.paymentAmount(),
                format(vBank.dueDate()));
    }

    private BuyerInfoV1ApiResponse toBuyerInfoResponse(PaymentDetailResult.BuyerInfoResult r) {
        return new BuyerInfoV1ApiResponse(r.name(), r.email(), r.phoneNumber());
    }

    private ReceiverInfoV1ApiResponse toReceiverInfoResponse(
            PaymentDetailResult.ReceiverInfoResult r) {
        return new ReceiverInfoV1ApiResponse(
                r.receiverName(),
                r.receiverPhoneNumber(),
                r.addressLine1(),
                r.addressLine2(),
                r.zipCode(),
                r.country(),
                r.deliveryRequest(),
                r.phoneNumber());
    }

    private RefundAccountV1ApiResponse toRefundAccountResponse(
            PaymentDetailResult.RefundAccountResult r) {
        if (r == null) {
            return null;
        }
        return new RefundAccountV1ApiResponse(
                r.refundAccountId(), r.bankName(), r.accountNumber(), r.holderName());
    }

    /**
     * 직접 구매 요청 → CreatePaymentCommand 변환.
     *
     * @param userId 인증된 사용자 ID
     * @param request 직접 구매 결제 요청
     * @return CreatePaymentCommand
     */
    public CreatePaymentCommand toCreatePaymentCommand(
            Long userId, CreatePaymentV1ApiRequest request) {
        ShippingInfoCommand shippingInfo = toShippingInfoCommand(request.shippingInfo());
        RefundAccountInfoCommand refundAccount =
                toRefundAccountInfoCommand(request.refundAccount());
        List<PaymentOrderItemCommand> orderItems =
                request.orders().stream().map(this::toPaymentOrderItemCommand).toList();
        return new CreatePaymentCommand(
                userId,
                request.payAmount(),
                request.mileageAmount(),
                request.payMethod(),
                shippingInfo,
                refundAccount,
                orderItems);
    }

    /**
     * 장바구니 구매 요청 → CreatePaymentInCartCommand 변환.
     *
     * @param userId 인증된 사용자 ID
     * @param request 장바구니 구매 결제 요청
     * @return CreatePaymentInCartCommand
     */
    public CreatePaymentInCartCommand toCreatePaymentInCartCommand(
            Long userId, CreatePaymentInCartV1ApiRequest request) {
        ShippingInfoCommand shippingInfo = toShippingInfoCommand(request.shippingInfo());
        RefundAccountInfoCommand refundAccount =
                toRefundAccountInfoCommand(request.refundAccount());
        List<CartOrderItemCommand> orderItems =
                request.orders().stream().map(this::toCartOrderItemCommand).toList();
        return new CreatePaymentInCartCommand(
                userId,
                request.payAmount(),
                request.mileageAmount(),
                request.payMethod(),
                shippingInfo,
                refundAccount,
                orderItems);
    }

    /**
     * PaymentGatewayResult → PaymentGatewayV1ApiResponse 변환.
     *
     * @param result 결제 준비 결과
     * @return PaymentGatewayV1ApiResponse
     */
    public PaymentGatewayV1ApiResponse toPaymentGatewayResponse(PaymentGatewayResult result) {
        return new PaymentGatewayV1ApiResponse(
                result.paymentUniqueId(),
                result.paymentId(),
                result.orderIds(),
                result.expectedMileageAmount());
    }

    private ShippingInfoCommand toShippingInfoCommand(ShippingInfoV1ApiRequest request) {
        if (request == null || request.shippingDetails() == null) {
            return null;
        }
        ShippingInfoV1ApiRequest.ShippingDetailsV1ApiRequest d = request.shippingDetails();
        return new ShippingInfoCommand(
                d.receiverName(),
                d.phoneNumber(),
                d.zipCode(),
                d.addressLine1(),
                d.addressLine2(),
                d.deliveryRequest());
    }

    private RefundAccountInfoCommand toRefundAccountInfoCommand(
            RefundAccountInfoV1ApiRequest request) {
        if (request == null) {
            return null;
        }
        return new RefundAccountInfoCommand(
                request.bankCode(), request.accountNumber(), request.accountHolderName());
    }

    private PaymentOrderItemCommand toPaymentOrderItemCommand(CreateOrderV1ApiRequest request) {
        return new PaymentOrderItemCommand(
                request.productGroupId(),
                request.productId(),
                request.sellerId(),
                request.quantity(),
                request.orderAmount(),
                request.orderStatus());
    }

    private CartOrderItemCommand toCartOrderItemCommand(CreateOrderInCartV1ApiRequest request) {
        return new CartOrderItemCommand(
                request.cartId(),
                request.productGroupId(),
                request.productId(),
                request.sellerId(),
                request.quantity(),
                request.orderAmount(),
                request.orderStatus());
    }
}
