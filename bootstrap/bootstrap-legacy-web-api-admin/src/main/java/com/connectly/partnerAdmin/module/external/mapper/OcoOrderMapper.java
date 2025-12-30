package com.connectly.partnerAdmin.module.external.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrder;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrderItem;
import com.connectly.partnerAdmin.module.external.enums.oco.OcoPayMethod;
import com.connectly.partnerAdmin.module.external.exception.ExternalMallProductNotFoundException;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.order.dto.query.BaseOrderSheet;
import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.payment.dto.query.CreatePayment;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;


@Component
public class OcoOrderMapper extends AbstractExternalOrderMapper<OcoOrder, OcoOrderItem> {


    @Override
    public CreatePayment toCreatePayment(OcoOrder ocoOrder, List<OrderSheet> toCreateOrders) {

        Money payAmount = toCreateOrders.stream()
                .map(OrderSheet::getOrderAmount)
                .reduce(Money.ZERO, Money::plus);


        return CreatePayment.builder()
                .payAmount(payAmount)
                .mileageAmount(DEFAULT_MILEAGE_AMOUNT)
                .payMethod(OcoPayMethod.of(ocoOrder.getPaymentType()).getPaymentMethodEnum())
                .shippingInfo(toUserShippingInfo(ocoOrder))
                .orders(toCreateOrders)
                .userId(DEFAULT_USER_ID)
                .siteName(SiteName.OCO)
                .paymentUniqueId(generatePaymentUniqueKey(ocoOrder))
                .paymentDate(LocalDateTime.now())
                .build();
    }

    @Override
    public BaseOrderSheet toCreateOrder(OcoOrderItem ocoOrderItem, ProductGroupDetailResponse productGroupsResponse) {
        long sellerId = productGroupsResponse.getProductGroup().getSellerId();
        Money salePrice = Money.wons((productGroupsResponse.getProductGroup().getPrice().getCurrentPrice()));

        Optional<ProductFetchResponse> product = getProduct(productGroupsResponse.getProducts(), ocoOrderItem);
        if(product.isEmpty()) throw new ExternalMallProductNotFoundException(SiteName.OCO.getName(), String.valueOf(ocoOrderItem.getPid()));

        return BaseOrderSheet.builder()
                .orderAmount(salePrice)
                .productId(product.get().getProductId())
                .sellerId(sellerId)
                .quantity(ocoOrderItem.getQuantity())
                .userId(DEFAULT_USER_ID)
                .build();
    }

    private String generatePaymentUniqueKey(OcoOrder ocoOrder){
        return String.format("%s_%s", SiteName.OCO.getName(), ocoOrder.getOrderNumber());
    }


    private Optional<ProductFetchResponse> getProduct(Set<ProductFetchResponse> products, OcoOrderItem ocoOrderItem) {
        return products.stream().filter(p ->{
                    String optionName = p.getOption().replace(" ", "").replace("/", "");
                    String optionItemName = ocoOrderItem.getOptionItem().replace(" ", "").replace("/", "");
                    return optionItemName.equals(optionName);
                }
        ).findFirst();
    }


}
