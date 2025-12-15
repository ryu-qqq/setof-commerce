package com.setof.connectly.module.product.service.price;

import com.setof.connectly.module.discount.service.DiscountApplyService;
import com.setof.connectly.module.exception.payment.InvalidPaymentPriceException;
import com.setof.connectly.module.exception.product.ProductNotFoundException;
import com.setof.connectly.module.order.dto.order.OrderSheet;
import com.setof.connectly.module.product.dto.price.ProductGroupPriceDto;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final ProductPriceFindService productPriceFindService;
    private final DiscountApplyService discountApplyService;

    @Override
    public void checkPrices(List<OrderSheet> orderSheets) {
        List<Long> productIds = getProductIds(orderSheets);
        List<ProductGroupPriceDto> productGroupPrices = fetchProductPrices(productIds);
        discountApplyService.applyDiscountsOffer(productGroupPrices);

        Map<Long, ProductGroupPriceDto> productGroupPriceMap =
                productGroupPrices.stream()
                        .collect(
                                Collectors.toMap(
                                        ProductGroupPriceDto::getProductId,
                                        Function.identity(),
                                        (existing, replacement) -> existing));

        orderSheets.forEach(
                orderSheet -> {
                    ProductGroupPriceDto pp = productGroupPriceMap.get(orderSheet.getProductId());
                    if (pp == null) throw new ProductNotFoundException();

                    long dbFindAmount = pp.getPrice().getSalePrice() * orderSheet.getQuantity();
                    long totalOrderAmount = orderSheet.getOrderAmount();

                    if (dbFindAmount != totalOrderAmount)
                        throw new InvalidPaymentPriceException(totalOrderAmount, dbFindAmount);
                });
    }

    public List<Long> getProductIds(List<OrderSheet> orderSheets) {
        return orderSheets.stream().map(OrderSheet::getProductId).collect(Collectors.toList());
    }

    private List<ProductGroupPriceDto> fetchProductPrices(List<Long> productIds) {
        return productPriceFindService.fetchProductGroupPrices(productIds);
    }
}
