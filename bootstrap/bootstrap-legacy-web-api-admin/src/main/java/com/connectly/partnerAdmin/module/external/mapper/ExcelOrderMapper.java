package com.connectly.partnerAdmin.module.external.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.external.dto.order.ExcelOrderSheet;
import com.connectly.partnerAdmin.module.external.exception.ExternalMallProductNotFoundException;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.order.dto.query.BaseOrderSheet;
import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.payment.dto.query.CreatePayment;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodEnum;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;


@Component
public class ExcelOrderMapper extends AbstractExternalOrderMapper<ExcelOrderSheet, ExcelOrderSheet> {



    @Override
    public CreatePayment toCreatePayment(ExcelOrderSheet excelOrderSheet, List<OrderSheet> toCreateOrders) {
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
                .siteName(excelOrderSheet.getSiteName())
                .paymentUniqueId(excelOrderSheet.getUniqueOrderKey())
                .paymentDate(LocalDateTime.now())
                .build();
    }

    @Override
    public OrderSheet toCreateOrder(ExcelOrderSheet excelOrderSheet, ProductGroupDetailResponse productGroupsResponse) {
        long sellerId = productGroupsResponse.getProductGroup().getSellerId();
        Money salePrice = Money.wons((productGroupsResponse.getProductGroup().getPrice().getCurrentPrice()));

        Optional<ProductFetchResponse> product = getProduct(productGroupsResponse.getProducts(), excelOrderSheet);
        if (product.isEmpty())
            throw new ExternalMallProductNotFoundException(excelOrderSheet.getSiteName().getName(), String.valueOf(excelOrderSheet.getProductGroupId()));

        return BaseOrderSheet.builder()
                .orderAmount(salePrice)
                .productId(product.get().getProductId())
                .sellerId(sellerId)
                .quantity(excelOrderSheet.getQuantity())
                .userId(DEFAULT_USER_ID)
                .build();
    }

    private Optional<ProductFetchResponse> getProduct(Set<ProductFetchResponse> products, ExcelOrderSheet excelOrderSheet) {
        return products.stream().filter(p ->{
                    String optionName = p.getOption().replace(" ", "").replace("/", "");
                    String optionItemName = excelOrderSheet.getOptionName().replace(" ", "").replace("/", "");
                    return optionItemName.equals(optionName);
                }
        ).findFirst();
    }



}
