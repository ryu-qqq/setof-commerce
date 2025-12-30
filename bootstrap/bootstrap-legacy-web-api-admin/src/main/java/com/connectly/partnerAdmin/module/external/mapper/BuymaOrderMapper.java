package com.connectly.partnerAdmin.module.external.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.external.dto.order.buyma.BuymaOrder;
import com.connectly.partnerAdmin.module.external.exception.ExternalMallProductNotFoundException;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.order.dto.query.BaseOrderSheet;
import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.payment.dto.query.CreatePayment;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodEnum;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;


@Component
public class BuymaOrderMapper extends AbstractExternalOrderMapper<BuymaOrder, BuymaOrder> {

    private static final String NO_COLOR = "No color specification";

    @Override
    public CreatePayment toCreatePayment(BuymaOrder buymaOrder, List<OrderSheet> toCreateOrders) {
        Money payAmount = toCreateOrders.stream()
                .map(OrderSheet::getOrderAmount)
                .reduce(Money.ZERO, Money::plus);


        return CreatePayment.builder()
                .payAmount(payAmount)
                .mileageAmount(DEFAULT_MILEAGE_AMOUNT)
                .payMethod(PaymentMethodEnum.CARD)
                .shippingInfo(defaultTrexiWareHouseShippingInfo())
                .orders(toCreateOrders)
                .userId(DEFAULT_USER_ID)
                .siteName(SiteName.BUYMA)
                .paymentUniqueId(generatePaymentUniqueKey(buymaOrder))
                .paymentDate(LocalDateTime.now())
                .build();
    }

    @Override
    public OrderSheet toCreateOrder(BuymaOrder buymaOrder, ProductGroupDetailResponse productGroupsResponse) {
        long sellerId = productGroupsResponse.getProductGroup().getSellerId();
        Money salePrice = Money.wons((productGroupsResponse.getProductGroup().getPrice().getCurrentPrice()));

        Optional<ProductFetchResponse> product = getProduct(productGroupsResponse.getProducts(), buymaOrder.getColorSizeText());
        if(product.isEmpty()) throw new ExternalMallProductNotFoundException(SiteName.BUYMA.getName(), String.valueOf(buymaOrder.getProduct().getId()));

        return BaseOrderSheet.builder()
                .orderAmount(salePrice)
                .productId(product.get().getProductId())
                .sellerId(sellerId)
                .quantity(buymaOrder.getAmount())
                .userId(DEFAULT_USER_ID)
                .build();

    }


    private String generatePaymentUniqueKey(BuymaOrder buymaOrder){
        return String.format("%s_%s", SiteName.BUYMA.getName(), buymaOrder.getId());
    }

    private Optional<ProductFetchResponse> getProduct(Set<ProductFetchResponse> products, String optionName){
        return products.stream().filter(p ->
                optionName
                        .replaceAll(NO_COLOR, "")
                        .replaceAll("/", "")
                        .replaceAll(" ", "")
                        .equals(
                                p.getOption()
                                        .replaceAll("OS", "")
                                        .replaceAll("/", "")
                                        .replaceAll(" ", "")
                        )
        ).findFirst();
    }


}
