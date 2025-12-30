package com.connectly.partnerAdmin.module.discount.service.fetch;


import com.connectly.partnerAdmin.module.discount.dto.DiscountTargetResponseDto;
import com.connectly.partnerAdmin.module.discount.dto.ProductDiscountTarget;
import com.connectly.partnerAdmin.module.discount.dto.SellerDiscountTarget;
import com.connectly.partnerAdmin.module.discount.entity.DiscountTarget;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import com.connectly.partnerAdmin.module.discount.repository.DiscountTargetFetchRepository;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.product.service.group.ProductGroupFetchService;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class DiscountTargetFetchServiceImpl implements DiscountTargetFetchService {

    private final DiscountTargetFetchRepository discountTargetFetchRepository;
    private final ProductGroupFetchService productGroupFetchService;

    @Override
    public List<DiscountTarget> fetchDiscountTargetEntities(List<Long> discountPolicyIds) {
        return discountTargetFetchRepository.fetchDiscountTargetEntities(discountPolicyIds);
    }


    @Override
    public Page<? extends DiscountTargetResponseDto> fetchDiscountTargets(long discountPolicyId, IssueType issueType, Pageable pageable) {
        return switch (issueType) {
            case PRODUCT -> fetchProductTargets(discountPolicyId, pageable).map(target -> (DiscountTargetResponseDto) target);
            case SELLER -> fetchSellerTargets(discountPolicyId, pageable).map(target -> (DiscountTargetResponseDto) target);
            default -> throw new IllegalArgumentException("Unsupported issueType: " + issueType);
        };
    }


    private Page<ProductDiscountTarget> fetchProductTargets(long discountPolicyId, Pageable pageable){
        List<ProductDiscountTarget> results = discountTargetFetchRepository.fetchProductTargets(discountPolicyId, pageable);

        if(results.isEmpty()){
            return PageableExecutionUtils.getPage(results, pageable, () -> 0L);

        }

        Map<Long, ProductDiscountTarget> productGroupIdMap = results.stream().collect(Collectors.toMap(ProductDiscountTarget::getProductGroupId, Function.identity(), (v1, v2) -> v1));
        List<ProductGroupDetailResponse> productGroupDetailResponses = productGroupFetchService.fetchProductGroups(new ArrayList<>(productGroupIdMap.keySet()));
        Map<Long, ProductGroupDetailResponse> productGroupMap = productGroupDetailResponses.stream().collect(Collectors.toMap(ProductGroupDetailResponse::getProductGroupId, Function.identity(), (v1, v2) -> v2));
        productGroupIdMap.forEach((aLong, productDiscountTarget) -> {
            ProductGroupDetailResponse productGroupDetailResponse = productGroupMap.get(aLong);
            productDiscountTarget.setProductGroup(productGroupDetailResponse.getProductGroup());
            productDiscountTarget.setProducts(productGroupDetailResponse.getProducts());
        });

        return PageableExecutionUtils.getPage(results, pageable, () -> fetchProductCountQuery(discountPolicyId));
    }


    private Page<SellerDiscountTarget> fetchSellerTargets(long discountPolicyId, Pageable pageable){
        List<SellerDiscountTarget> results = discountTargetFetchRepository.fetchSellerTargets(discountPolicyId, pageable);
        return PageableExecutionUtils.getPage(results, pageable, () -> fetchSellerCountQuery(discountPolicyId));
    }


    private long fetchSellerCountQuery(long discountPolicyId) {
        JPAQuery<Long> longJPAQuery = discountTargetFetchRepository.fetchSellerTargetCountQuery(discountPolicyId);
        Long totalCount = longJPAQuery.fetchOne();
        if(totalCount == null) {
            return 0L;
        }
        return totalCount;
    }


    private long fetchProductCountQuery(long discountPolicyId) {
        JPAQuery<Long> longJPAQuery = discountTargetFetchRepository.fetchProductTargetCountQuery(discountPolicyId);
        Long totalCount = longJPAQuery.fetchOne();
        if(totalCount == null) {
            return 0L;
        }
        return totalCount;
    }


}
