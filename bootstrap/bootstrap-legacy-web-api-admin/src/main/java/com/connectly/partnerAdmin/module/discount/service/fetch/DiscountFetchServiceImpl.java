package com.connectly.partnerAdmin.module.discount.service.fetch;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.discount.core.BaseDiscountInfo;
import com.connectly.partnerAdmin.module.discount.core.PriceHolder;
import com.connectly.partnerAdmin.module.discount.dto.DiscountPolicyResponseDto;
import com.connectly.partnerAdmin.module.discount.filter.DiscountFilter;
import com.connectly.partnerAdmin.module.discount.entity.DiscountPolicy;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import com.connectly.partnerAdmin.module.discount.exception.DiscountNotFoundException;
import com.connectly.partnerAdmin.module.discount.mapper.DiscountPolicyPageableMapper;
import com.connectly.partnerAdmin.module.discount.repository.DiscountPolicyFetchRepository;
import com.connectly.partnerAdmin.module.discount.service.DiscountRedisQueryService;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class DiscountFetchServiceImpl implements DiscountFetchService {

    private final DiscountPolicyFetchRepository discountPolicyFetchRepository;
    private final DiscountRedisFetchService discountRedisFetchService;
    private final DiscountRedisQueryService discountRedisQueryService;
    private final DiscountPolicyPageableMapper discountPolicyPageableMapper;

    @Override
    public DiscountPolicyResponseDto fetchDiscountPolicy(long discountPolicyId) {
        return discountPolicyFetchRepository.fetchDiscountPolicy(discountPolicyId)
                .orElseThrow(DiscountNotFoundException::new);
    }

    @Override
    public CustomPageable<DiscountPolicyResponseDto> fetchDiscountPolicies(DiscountFilter filter, Pageable pageable) {
        List<DiscountPolicyResponseDto> discountPolicyResponses = discountPolicyFetchRepository.fetchDiscountPolicies(filter, pageable);
        long total = fetchDiscountPolicyCountQuery(filter);
        return discountPolicyPageableMapper.toProductCategoryContext(discountPolicyResponses, pageable, total);
    }

    private long fetchDiscountPolicyCountQuery(DiscountFilter filter) {
        JPAQuery<Long> longJPAQuery = discountPolicyFetchRepository.fetchDiscountPolicyCountQuery(filter);
        Long totalCount = longJPAQuery.fetchOne();
        if(totalCount == null) {
            return 0L;
        }
        return totalCount;
    }

    @Override
    public DiscountPolicy fetchDiscountEntity(long discountPolicyId) {
        return discountPolicyFetchRepository.fetchDiscountEntity(discountPolicyId)
                .orElseThrow(DiscountNotFoundException::new);
    }

    @Override
    public List<DiscountPolicy> fetchDiscountEntities(List<Long> discountPolicyIds) {
        return discountPolicyFetchRepository.fetchDiscountEntities(discountPolicyIds);
    }


    @Override
    public Optional<BaseDiscountInfo> fetchDiscountCache(PriceHolder priceHolder) {
        Optional<BaseDiscountInfo> highestPriorityDiscount = discountRedisFetchService.getHighestPriorityDiscount(priceHolder);
        if(highestPriorityDiscount.isPresent()) return highestPriorityDiscount;
        else{
            Optional<BaseDiscountInfo> baseDiscountInfo = fetchDiscountPolicyInDb(priceHolder);
            if(baseDiscountInfo.isPresent()) {
                discountRedisQueryService.saveDiscountCache(baseDiscountInfo.get());
                return baseDiscountInfo;
            }
            return Optional.empty();
        }
    }


    @Override
    public List<BaseDiscountInfo> fetchDiscountCaches(List<? extends PriceHolder> priceHolders) {
        List<BaseDiscountInfo> baseDiscountInfos = new ArrayList<>();

        List<BaseDiscountInfo> highestPriorityDiscounts = discountRedisFetchService.getHighestPriorityDiscounts(priceHolders);

        Set<Long> redisProductGroupIds = highestPriorityDiscounts.stream()
                .filter(baseDiscountInfo -> baseDiscountInfo.getIssueType().isProductIssue())
                .map(BaseDiscountInfo::getTargetId)
                .collect(Collectors.toSet());

        Set<Long> redisSellerIds = highestPriorityDiscounts.stream()
                .filter(baseDiscountInfo -> baseDiscountInfo.getIssueType().isSellerIssue())
                .map(BaseDiscountInfo::getTargetId)
                .collect(Collectors.toSet());

        Set<Long> missingProductGroupIds = priceHolders.stream()
                .map(PriceHolder::getProductGroupId)
                .filter(id -> !redisProductGroupIds.contains(id))
                .collect(Collectors.toSet());

        Set<Long> missingSellerIds = priceHolders.stream()
                .map(PriceHolder::getSellerId)
                .filter(id -> !redisSellerIds.contains(id))
                .collect(Collectors.toSet());


        baseDiscountInfos.addAll(discountPolicyFetchRepository.fetchDiscountInfos(missingProductGroupIds, IssueType.PRODUCT));
        baseDiscountInfos.addAll(discountPolicyFetchRepository.fetchDiscountInfos(missingSellerIds, IssueType.SELLER));


        baseDiscountInfos.addAll(highestPriorityDiscounts);

        return baseDiscountInfos;
    }


    private Optional<BaseDiscountInfo> fetchDiscountPolicyInDb(PriceHolder priceHolder){
        Optional<BaseDiscountInfo> productDiscountCache = fetchDiscountInfo(priceHolder.getProductGroupId(), IssueType.PRODUCT);
        if(productDiscountCache.isPresent()) return productDiscountCache;
        else{
            return fetchDiscountInfo(priceHolder.getSellerId(), IssueType.SELLER);
        }
    }

    private Optional<BaseDiscountInfo> fetchDiscountInfo(long targetId, IssueType issueType){
        return discountPolicyFetchRepository.fetchDiscountInfo(targetId, issueType);
    }

}
