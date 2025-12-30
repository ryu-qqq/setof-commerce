package com.connectly.partnerAdmin.module.discount.service;


import com.connectly.partnerAdmin.module.common.enums.RedisKey;
import com.connectly.partnerAdmin.module.discount.core.BaseDiscountInfo;
import com.connectly.partnerAdmin.module.discount.core.DiscountInfo;
import com.connectly.partnerAdmin.module.discount.core.PriceHolder;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import com.connectly.partnerAdmin.module.discount.service.fetch.DiscountFetchService;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DiscountApplyServiceImpl implements DiscountApplyService{

    private final DiscountFetchService discountFetchService;

    @Override
    public void applyDiscount(PriceHolder priceHolder) {
        Optional<BaseDiscountInfo> discountCacheDtoOptional = discountFetchService.fetchDiscountCache(priceHolder);
        discountCacheDtoOptional.ifPresent(discount -> processApplyDiscount(priceHolder.getPrice(), discount));
    }

    public void processApplyDiscount(Price price, DiscountInfo discountInfo) {
        if (discountInfo.getDiscountType().isPriceType()) {
            applyPriceTypeDiscount(price, discountInfo);
        } else {
            applyRatioTypeDiscount(price, discountInfo);
        }
    }


    private void applyPriceTypeDiscount(Price price, DiscountInfo discountInfo) {
        Money maxDiscount = Money.wons(discountInfo.getMaxDiscountPrice());
        Money newSalePrice = Money.wons(price.getCurrentPrice()).minus(maxDiscount);
        int discountRate = calculateDiscountRate(Money.wons(price.getRegularPrice()), newSalePrice);
        int directDiscountRate = calculateDiscountRate(Money.wons(price.getCurrentPrice()), newSalePrice);
        price.setSalePrice(newSalePrice.getAmount());
        price.setDiscountRate(discountRate);
        price.setDirectDiscountRate(directDiscountRate);
        price.setDirectDiscountPrice(maxDiscount.getAmount());
    }


    private void applyRatioTypeDiscount(Price price, DiscountInfo discountInfo) {
        BigDecimal discountRatio = BigDecimal.valueOf(discountInfo.getDiscountRatio() * 0.01);
        Money potentialDiscount = Money.wons(price.getCurrentPrice()).times(discountRatio.doubleValue());
        Money maxDiscount = Money.wons(discountInfo.getMaxDiscountPrice());

        Money newSalePrice = discountInfo.getDiscountLimitYn().isYes() && maxDiscount.isLessThan(potentialDiscount) ?
                Money.wons(price.getCurrentPrice()).minus(maxDiscount) :
                Money.wons(price.getCurrentPrice()).minus(potentialDiscount);

        int discountRate = calculateDiscountRate(Money.wons(price.getRegularPrice()), newSalePrice);
        int directDiscountRate = calculateDiscountRate(Money.wons(price.getCurrentPrice()), newSalePrice);
        price.setSalePrice(newSalePrice.getAmount());
        price.setDiscountRate(discountRate);
        price.setDirectDiscountRate(directDiscountRate);
        price.setDirectDiscountPrice(calculateDirectDiscountPrice(Money.wons(price.getCurrentPrice()), directDiscountRate).getAmount());
    }

    private int calculateDiscountRate(Money basePrice, Money salePrice) {
        Money discount = basePrice.minus(salePrice);
        BigDecimal discountRate = discount.divide(basePrice, 2, RoundingMode.HALF_UP); // Money 객체의 divide 사용
        return discountRate.multiply(BigDecimal.valueOf(100)).intValueExact();
    }

    private Money calculateDirectDiscountPrice(Money currentPrice, int directDiscountRate) {
        BigDecimal discountValue = BigDecimal.valueOf(directDiscountRate * 0.01).multiply(currentPrice.getAmount());
        return Money.wons(discountValue.setScale(0, RoundingMode.HALF_UP));
    }

    @Override
    public void applyDiscount(List<? extends PriceHolder> priceHolders) {
        List<BaseDiscountInfo> discounts = discountFetchService.fetchDiscountCaches(priceHolders);
        Map<String, BaseDiscountInfo> StringKeyMap = generateKeyMap(discounts);

        for (PriceHolder priceHolder : priceHolders) {

            String productKey = generateDiscountKey(IssueType.PRODUCT, priceHolder.getProductGroupId());
            String sellerKey = generateDiscountKey(IssueType.SELLER, priceHolder.getSellerId());

            BaseDiscountInfo baseDiscountInfo = StringKeyMap.getOrDefault(productKey, StringKeyMap.get(sellerKey));
            if (baseDiscountInfo != null) {
                processApplyDiscount(priceHolder.getPrice(), baseDiscountInfo);
            }
        }
    }

    private Map<String, BaseDiscountInfo> generateKeyMap(List<BaseDiscountInfo> discountInfos) {
        return discountInfos.stream()
                .collect(Collectors.toMap(
                        discountInfo -> generateDiscountKey(discountInfo.getIssueType(), discountInfo.getTargetId()),
                        discountInfo -> discountInfo,
                        (v1, v2) -> v1));
    }

    private String generateDiscountKey(IssueType issueType, long targetId) {
        return RedisKey.DISCOUNT.generateKey(issueType.name() + targetId);
    }

}
