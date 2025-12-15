package com.setof.connectly.module.discount.service.fetch;

import com.setof.connectly.module.discount.dto.DiscountCacheDto;
import com.setof.connectly.module.discount.dto.DiscountRedisFetchDto;
import com.setof.connectly.module.discount.enums.IssueType;
import com.setof.connectly.module.discount.repository.DiscountPolicyFindRepository;
import com.setof.connectly.module.discount.service.DiscountRedisQueryService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class DiscountFindServiceImpl implements DiscountFindService {

    private final DiscountPolicyFindRepository discountPolicyFindRepository;
    private final DiscountRedisFindService discountRedisFindService;
    private final DiscountRedisQueryService discountRedisQueryService;

    public Optional<DiscountCacheDto> fetchDiscountCache(
            DiscountRedisFetchDto discountRedisFetchDto) {
        //        Optional<DiscountCacheDto> highestPriorityDiscount =
        // discountRedisFindService.getHighestPriorityDiscount(discountRedisFetchDto);
        //        if(highestPriorityDiscount.isPresent()) return highestPriorityDiscount;
        //        else{
        Optional<DiscountCacheDto> discountCacheDto =
                fetchDiscountPolicyInDb(discountRedisFetchDto);
        discountCacheDto.ifPresent(
                discount -> {
                    long targetId;
                    if (discount.getIssueType().isProductIssue()) {
                        targetId = Long.parseLong(discountRedisFetchDto.getProductGroupId());
                    } else targetId = Long.parseLong(discountRedisFetchDto.getSellerId());
                    discountRedisQueryService.saveByIssueTypeAndTargetId(targetId, discount);
                });

        return discountCacheDto;
        // }
    }

    @Override
    public List<DiscountCacheDto> fetchDiscountCaches(
            List<Long> productGroupIds, List<Long> sellerIds) {
        List<DiscountCacheDto> results = new ArrayList<>();
        if (!productGroupIds.isEmpty())
            results.addAll(
                    discountPolicyFindRepository.fetchDiscountInfos(
                            new HashSet<>(productGroupIds), IssueType.PRODUCT));
        if (!sellerIds.isEmpty())
            results.addAll(
                    discountPolicyFindRepository.fetchDiscountInfos(
                            new HashSet<>(sellerIds), IssueType.SELLER));

        return results;
    }

    private Optional<DiscountCacheDto> fetchDiscountPolicyInDb(
            DiscountRedisFetchDto discountRedisFetchDto) {
        Optional<DiscountCacheDto> productDiscountCache =
                fetchDiscountInfoAboutProduct(
                        Long.parseLong(discountRedisFetchDto.getProductGroupId()));
        if (productDiscountCache.isPresent()) return productDiscountCache;
        else {
            return fetchDiscountInfoAboutSeller(
                    Long.parseLong(discountRedisFetchDto.getSellerId()));
        }
    }

    private Optional<DiscountCacheDto> fetchDiscountInfoAboutProduct(long targetId) {
        return fetchDiscountInfo(targetId, IssueType.PRODUCT);
    }

    private Optional<DiscountCacheDto> fetchDiscountInfoAboutSeller(long targetId) {
        return fetchDiscountInfo(targetId, IssueType.SELLER);
    }

    private Optional<DiscountCacheDto> fetchDiscountInfo(long targetId, IssueType issueType) {
        return discountPolicyFindRepository.fetchDiscountInfo(targetId, issueType);
    }
}
