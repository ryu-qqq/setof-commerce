package com.setof.connectly.module.discount.service;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.discount.DiscountOffer;
import com.setof.connectly.module.discount.dto.DiscountCacheDto;
import com.setof.connectly.module.discount.dto.DiscountCalculateDto;
import com.setof.connectly.module.discount.dto.DiscountRedisFetchDto;
import com.setof.connectly.module.discount.enums.IssueType;
import com.setof.connectly.module.discount.mapper.DiscountMapper;
import com.setof.connectly.module.discount.service.fetch.DiscountFindService;
import com.setof.connectly.module.discount.service.fetch.DiscountRedisFindService;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountApplyServiceImpl implements DiscountApplyService {

    private final DiscountFindService discountFindService;
    private final DiscountRedisFindService discountRedisFindService;
    private final DiscountRedisQueryService discountRedisQueryService;
    private final DiscountMapper discountMapper;

    @Override
    public <T extends DiscountOffer> void applyDiscountOffer(T discountOffer) {
        DiscountCalculateDto discountCalculate =
                new DiscountCalculateDto(
                        discountOffer.getProductGroupId(),
                        discountOffer.getSellerId(),
                        discountOffer.getPrice());
        DiscountRedisFetchDto fetchDto = discountMapper.toDiscountRedisFetchDto(discountCalculate);
        Optional<DiscountCacheDto> discountCacheDtoOptional =
                discountFindService.fetchDiscountCache(fetchDto);
        discountCacheDtoOptional.ifPresent(discount -> applyDiscount(discountCalculate, discount));
    }

    @Override
    public void applyDiscountsOffer(Collection<? extends DiscountOffer> discountOffers) {
        Set<DiscountCalculateDto> discountCalculates = toDiscountCalculateSet(discountOffers);
        List<DiscountRedisFetchDto> fetches = toDiscountRedisFetchList(discountCalculates);

        List<Long> productGroupIds =
                fetches.stream()
                        .map(DiscountRedisFetchDto::getProductGroupId)
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

        List<Long> sellerIds =
                fetches.stream()
                        .map(DiscountRedisFetchDto::getSellerId)
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

        List<DiscountCacheDto> discounts =
                discountFindService.fetchDiscountCaches(productGroupIds, sellerIds);
        Map<String, DiscountCacheDto> keyMap = generateKeyMap(discounts);

        List<DiscountCalculateDto> toFindDbDiscounts = new ArrayList<>();
        prepareDiscountCalculates(discountCalculates, keyMap, toFindDbDiscounts);

        if (!toFindDbDiscounts.isEmpty()) {
            updateDiscountsFromDb(discountCalculates, toFindDbDiscounts);
        }

        applyDiscountToProducts(discountOffers, discountCalculates);
    }

    private Set<DiscountCalculateDto> toDiscountCalculateSet(
            Collection<? extends DiscountOffer> discountOffers) {
        return discountOffers.stream()
                .map(
                        p ->
                                new DiscountCalculateDto(
                                        p.getProductGroupId(), p.getSellerId(), p.getPrice()))
                .collect(Collectors.toSet());
    }

    private List<DiscountRedisFetchDto> toDiscountRedisFetchList(
            Set<DiscountCalculateDto> discountCalculates) {
        return discountCalculates.stream()
                .map(discountMapper::toDiscountRedisFetchDto)
                .collect(Collectors.toList());
    }

    private void applyDiscountToProducts(
            Collection<? extends DiscountOffer> discountOffers,
            Set<DiscountCalculateDto> discountCalculates) {
        Map<Long, DiscountCalculateDto> discountCalculateDtoMap =
                discountCalculates.stream()
                        .collect(
                                Collectors.toMap(
                                        DiscountCalculateDto::getProductGroupId,
                                        Function.identity()));

        discountOffers.forEach(
                p -> {
                    DiscountCalculateDto dto = discountCalculateDtoMap.get(p.getProductGroupId());
                    if (dto != null) {
                        p.setPrice(dto.getPrice());
                        p.setShareRatio(dto.getShareRatio());
                    }
                });
    }

    private void prepareDiscountCalculates(
            Set<DiscountCalculateDto> discountCalculates,
            Map<String, DiscountCacheDto> keyMap,
            List<DiscountCalculateDto> toFindDb) {
        for (DiscountCalculateDto discountCalculate : discountCalculates) {
            String productKey =
                    generateDiscountKey(IssueType.PRODUCT, discountCalculate.getProductGroupId());
            String sellerKey =
                    generateDiscountKey(IssueType.SELLER, discountCalculate.getSellerId());

            DiscountCacheDto discount = keyMap.getOrDefault(productKey, keyMap.get(sellerKey));
            if (discount != null) {
                applyDiscount(discountCalculate, discount);
            } else {
                toFindDb.add(discountCalculate);
            }
        }
    }

    private void updateDiscountsFromDb(
            Set<DiscountCalculateDto> discountCalculates,
            List<DiscountCalculateDto> toFindDbDiscounts) {

        Map<Long, DiscountCalculateDto> productMap =
                discountCalculates.stream()
                        .collect(
                                Collectors.toMap(
                                        DiscountCalculateDto::getProductGroupId,
                                        Function.identity()));

        List<DiscountCacheDto> discountInfoInDb =
                discountFindService.fetchDiscountCaches(
                        toFindDbDiscounts.stream()
                                .map(DiscountCalculateDto::getProductGroupId)
                                .collect(Collectors.toList()),
                        toFindDbDiscounts.stream()
                                .map(DiscountCalculateDto::getSellerId)
                                .collect(Collectors.toList()));

        if (!discountInfoInDb.isEmpty()) {

            Map<String, DiscountCacheDto> discountDbMap = generateKeyMap(discountInfoInDb);

            discountCalculates.forEach(
                    discountCalculate -> {
                        String productKey =
                                generateDiscountKey(
                                        IssueType.PRODUCT, discountCalculate.getProductGroupId());
                        String sellerKey =
                                generateDiscountKey(
                                        IssueType.SELLER, discountCalculate.getSellerId());

                        DiscountCacheDto discount =
                                Optional.ofNullable(discountDbMap.get(productKey))
                                        .orElse(discountDbMap.get(sellerKey));

                        if (discount != null) {
                            calculateAndSaveInCache(discountCalculate, discount, productMap);
                        }
                    });
        }
    }

    private void calculateAndSaveInCache(
            DiscountCalculateDto discountCalculate,
            DiscountCacheDto discountCache,
            Map<Long, DiscountCalculateDto> productMap) {
        discountCalculate.calculateDiscountPrice(discountCache);
        DiscountCalculateDto discountCalculateInMap =
                productMap.get(discountCalculate.getProductGroupId());
        discountCalculateInMap.setPrice(discountCalculate.getPrice());
        discountCalculateInMap.setShareRatio(discountCache.getShareRatio());
        discountRedisQueryService.saveByIssueTypeAndTargetId(
                discountCache.getTargetId(), discountCache);
    }

    private void applyDiscount(
            DiscountCalculateDto discountCalculate, DiscountCacheDto discountCache) {
        discountCalculate.calculateDiscountPrice(discountCache);
        discountCalculate.setShareRatio(discountCache.getShareRatio());
    }

    private Map<String, DiscountCacheDto> generateKeyMap(List<DiscountCacheDto> discounts) {
        return discounts.stream()
                .collect(
                        Collectors.toMap(
                                DiscountCacheDto::makeCacheKey,
                                Function.identity(),
                                (existingValue, newValue) -> existingValue));
    }

    private String generateDiscountKey(IssueType issueType, long targetId) {
        return RedisKey.DISCOUNT.generateKey(issueType.name() + targetId);
    }
}
