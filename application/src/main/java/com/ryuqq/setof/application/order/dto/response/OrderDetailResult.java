package com.ryuqq.setof.application.order.dto.response;

import com.ryuqq.setof.domain.order.vo.OptionSnapshot;
import com.ryuqq.setof.domain.order.vo.OrderBuyerInfo;
import com.ryuqq.setof.domain.order.vo.OrderDetail;
import com.ryuqq.setof.domain.order.vo.OrderPaymentSummary;
import com.ryuqq.setof.domain.order.vo.OrderProductSnapshot;
import com.ryuqq.setof.domain.order.vo.OrderRefundNotice;
import com.ryuqq.setof.domain.order.vo.OrderShipmentSummary;
import com.ryuqq.setof.domain.order.vo.ReceiverInfo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * OrderDetailResult - 주문 상세 Application Result DTO.
 *
 * <p>APP-DTO-001: Application Result는 Record로 정의.
 *
 * <p>APP-DTO-004: Domain VO → Application Result 변환은 from() 팩토리 메서드.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record OrderDetailResult(
        PaymentSummaryResult payment,
        OrderProductResult orderProduct,
        BuyerInfoResult buyerInfo,
        ReceiverInfoResult receiverInfo,
        double totalExpectedMileageAmount) {

    public static OrderDetailResult from(OrderDetail detail) {
        return new OrderDetailResult(
                PaymentSummaryResult.from(detail.paymentSummary()),
                OrderProductResult.from(detail),
                BuyerInfoResult.from(detail.buyerInfo()),
                ReceiverInfoResult.from(detail.receiverInfo()),
                detail.totalExpectedMileageAmount());
    }

    public record PaymentSummaryResult(
            long paymentId,
            String paymentAgencyId,
            String paymentStatus,
            String paymentMethod,
            LocalDateTime paymentDate,
            LocalDateTime canceledDate,
            long paymentAmount,
            double usedMileageAmount,
            String cardName,
            String cardNumber,
            double totalExpectedMileageAmount) {

        public static PaymentSummaryResult from(OrderPaymentSummary vo) {
            return new PaymentSummaryResult(
                    vo.paymentId(),
                    vo.paymentAgencyId(),
                    vo.paymentStatus(),
                    vo.paymentMethod(),
                    vo.paymentDate(),
                    vo.canceledDate(),
                    vo.paymentAmount(),
                    vo.usedMileageAmount(),
                    vo.cardName(),
                    vo.cardNumber(),
                    0);
        }
    }

    public record OrderProductResult(
            long orderId,
            long sellerId,
            String sellerName,
            BrandResult brand,
            long productGroupId,
            String productGroupName,
            long productId,
            String productGroupMainImageUrl,
            int productQuantity,
            String orderStatus,
            long regularPrice,
            long salePrice,
            long discountPrice,
            long directDiscountPrice,
            long orderAmount,
            String option,
            Set<OptionResult> options,
            RefundNoticeResult refundNotice,
            ShipmentInfoResult shipmentInfo,
            String reviewYn) {

        public static OrderProductResult from(OrderDetail detail) {
            OrderProductSnapshot ps = detail.productSnapshot();
            List<OptionSnapshot> opts = ps.options();
            Set<OptionResult> optionResults =
                    opts != null
                            ? opts.stream()
                                    .map(OptionResult::from)
                                    .collect(java.util.stream.Collectors.toSet())
                            : Set.of();

            return new OrderProductResult(
                    detail.orderId(),
                    ps.sellerId(),
                    ps.sellerName(),
                    new BrandResult(ps.brandId(), ps.brandName()),
                    ps.productGroupId(),
                    ps.productGroupName(),
                    ps.productId(),
                    ps.mainImageUrl(),
                    detail.quantity(),
                    detail.orderStatus(),
                    detail.regularPrice(),
                    detail.salePrice(),
                    detail.discountPrice(),
                    detail.directDiscountPrice(),
                    detail.orderAmount(),
                    ps.optionLabel(),
                    optionResults,
                    RefundNoticeResult.from(detail.refundNotice()),
                    ShipmentInfoResult.from(detail.shipmentSummary()),
                    detail.reviewYn());
        }
    }

    public record BrandResult(long brandId, String brandName) {}

    public record OptionResult(
            long optionGroupId, long optionDetailId, String optionName, String optionValue) {

        public static OptionResult from(OptionSnapshot vo) {
            return new OptionResult(
                    vo.optionGroupId(), vo.optionDetailId(), vo.optionName(), vo.optionValue());
        }
    }

    public record RefundNoticeResult(
            String returnMethodDomestic,
            String returnCourierDomestic,
            String returnChargeDomestic,
            String returnAddress) {

        public static RefundNoticeResult from(OrderRefundNotice vo) {
            if (vo == null) {
                return new RefundNoticeResult("", "", "0", "");
            }
            return new RefundNoticeResult(
                    vo.returnMethodDomestic(),
                    vo.returnCourierDomestic(),
                    vo.returnChargeDomestic(),
                    vo.returnAddress());
        }
    }

    public record ShipmentInfoResult(
            long orderId,
            String deliveryStatus,
            String companyCode,
            String invoiceNo,
            LocalDateTime insertDate) {

        public static ShipmentInfoResult from(OrderShipmentSummary vo) {
            if (vo == null) {
                return new ShipmentInfoResult(0, "", "", "", null);
            }
            return new ShipmentInfoResult(
                    vo.orderId(),
                    vo.deliveryStatus(),
                    vo.companyCode(),
                    vo.invoiceNo(),
                    vo.insertDate());
        }
    }

    public record BuyerInfoResult(String name, String phoneNumber, String email) {

        public static BuyerInfoResult from(OrderBuyerInfo vo) {
            if (vo == null) {
                return new BuyerInfoResult("", "", "");
            }
            return new BuyerInfoResult(vo.name(), vo.phoneNumber(), vo.email());
        }
    }

    public record ReceiverInfoResult(
            String receiverName,
            String receiverPhoneNumber,
            String addressLine1,
            String addressLine2,
            String zipCode,
            String country,
            String deliveryRequest,
            String phoneNumber) {

        public static ReceiverInfoResult from(ReceiverInfo vo) {
            if (vo == null) {
                return new ReceiverInfoResult("", "", "", "", "", "", "", "");
            }
            return new ReceiverInfoResult(
                    vo.receiverName(),
                    vo.receiverPhone(),
                    vo.addressLine1(),
                    vo.addressLine2(),
                    vo.zipCode(),
                    vo.country(),
                    vo.deliveryRequest(),
                    vo.receiverPhone());
        }
    }
}
