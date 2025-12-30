package com.connectly.partnerAdmin.module.external.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.connectly.partnerAdmin.module.external.exception.ExternalMallProductNotFoundException;
import com.connectly.partnerAdmin.module.external.service.order.lf.LfOrderRequestResponseDto;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.order.dto.query.BaseOrderSheet;
import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.payment.dto.query.CreatePayment;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodEnum;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.product.enums.group.Origin;
import com.connectly.partnerAdmin.module.user.dto.UserShippingInfo;
import com.connectly.partnerAdmin.module.user.entity.embedded.ShippingDetails;

public class LfOrderMapper {

    protected static final long DEFAULT_USER_ID = 1L;
    protected static final Money DEFAULT_MILEAGE_AMOUNT = Money.ZERO;
    private static final String DEFAULT_EXMALL_SHIPPING_ADDRESS_NAME = "집";

    public static CreatePayment toCreatePayment(LfOrderRequestResponseDto.Order order, List<OrderSheet> toCreateOrders) {

        Money payAmount = toCreateOrders.stream()
            .map(OrderSheet::getOrderAmount)
            .reduce(Money.ZERO, Money::plus);

        return CreatePayment.builder()
            .payAmount(payAmount)
            .mileageAmount(DEFAULT_MILEAGE_AMOUNT)
            .payMethod(PaymentMethodEnum.CARD)
            .shippingInfo(toUserShippingInfo(order))
            .orders(toCreateOrders)
            .userId(DEFAULT_USER_ID)
            .siteName(SiteName.LF)
            .paymentUniqueId(generatePaymentUniqueKey(order))
            .paymentDate(LocalDateTime.now())
            .build();
    }

    public static BaseOrderSheet toCreateOrder(LfOrderRequestResponseDto.Order order, ProductGroupDetailResponse productGroupsResponse) {
        long sellerId = productGroupsResponse.getProductGroup().getSellerId();
        Money salePrice = Money.wons((productGroupsResponse.getProductGroup().getPrice().getCurrentPrice()));

        Optional<ProductFetchResponse> product = getProduct(productGroupsResponse.getProducts(), order.optionValue());
        if(product.isEmpty()) throw new ExternalMallProductNotFoundException(SiteName.LF.getName(),order.productCode());

        return BaseOrderSheet.builder()
            .orderAmount(salePrice)
            .productId(product.get().getProductId())
            .sellerId(sellerId)
            .quantity(order.orderQty())
            .userId(DEFAULT_USER_ID)
            .build();
    }

    private static String generatePaymentUniqueKey(LfOrderRequestResponseDto.Order order){
        return String.format("%s_%s", SiteName.LF.getName(), order.ordererId());
    }


    private static Optional<ProductFetchResponse> getProduct(Set<ProductFetchResponse> products, String optionValue) {
        return products.stream().filter(p ->{
                String optionName = p.getOption().replace(" ", "").replace("/", "");
                String replace = optionValue.replace(" ", "").replace("/", "");
            return replace.equals(optionName);
            }
        ).findFirst();
    }





    public static UserShippingInfo toUserShippingInfo(LfOrderRequestResponseDto.Order order){
        ShippingDetails shippingDetails = ShippingDetails.builder()
            .receiverName(order.receiverName())
            .phoneNumber(order.receiverCellPhone())
            .shippingAddressName(DEFAULT_EXMALL_SHIPPING_ADDRESS_NAME)
            .addressLine1(order.receiverAddr1())
            .addressLine2(order.receiverAddr2())
            .zipCode(order.receiverZipCode())
            .deliveryRequest(order.deliveryMemo() == null ? "" : order.deliveryMemo())
            .country(Origin.KR)
            .build();

        return new UserShippingInfo(shippingDetails);
    }

}
