package com.connectly.partnerAdmin.module.discount.service;


import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.discount.dto.DiscountPolicyResponseDto;
import com.connectly.partnerAdmin.module.discount.dto.query.*;
import com.connectly.partnerAdmin.module.discount.entity.DiscountPolicy;
import com.connectly.partnerAdmin.module.discount.mapper.DiscountMapper;
import com.connectly.partnerAdmin.module.discount.repository.DiscountPolicyRepository;
import com.connectly.partnerAdmin.module.discount.service.fetch.DiscountFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class DiscountQueryServiceImpl implements DiscountQueryService{

    private final DiscountFetchService discountFetchService;
    private final DiscountPolicyRepository discountPolicyRepository;
    private final DiscountTargetQueryService discountTargetQueryService;
    private final DiscountMapper discountMapper;

    @Override
    public DiscountPolicyResponseDto createDiscount(CreateDiscount createDiscount) {
        handleCopyCreateIfNeeded(createDiscount);

        DiscountPolicy discountPolicy = discountMapper.toEntity(createDiscount);
        DiscountPolicy saveDiscountPolicy = discountPolicyRepository.save(discountPolicy);

        return DiscountPolicyResponseDto.from(saveDiscountPolicy);
    }


    @Override
    public List<DiscountPolicyResponseDto> createDiscountFromExcel(List<CreateDiscountFromExcel> createDiscountFromExcels) {
        List<DiscountPolicyResponseDto> results = new ArrayList<>();
        for(CreateDiscountFromExcel createDiscountFromExcel: createDiscountFromExcels){
            DiscountPolicyResponseDto discount = createDiscount(new CreateDiscount(createDiscountFromExcel.getDiscountDetails()));

            CreateDiscountTarget createDiscountTarget = CreateDiscountTarget.builder()
                    .issueType(discount.getDiscountDetails().getIssueType())
                    .targetIds(createDiscountFromExcel.getTargetIds())
                    .build();

            discountTargetQueryService.createDiscountTargets(discount.getDiscountPolicyId(), createDiscountTarget);
            results.add(discount);
        }

        return results;
    }

    @Override
    public DiscountPolicyResponseDto updateDiscount(long discountPolicyId, UpdateDiscount updateDiscount) {
        DiscountPolicy discountPolicy = discountFetchService.fetchDiscountEntity(discountPolicyId);
        discountPolicy.setDiscountDetails(updateDiscount.getDiscountDetails());
        discountTargetQueryService.updateDiscountTargets(discountPolicy.getId(), new CreateDiscountTarget(discountPolicy.getDiscountDetails().getIssueType(), updateDiscount.getTargetIds()));
        return DiscountPolicyResponseDto.from(discountPolicy);
    }


    private void handleCopyCreateIfNeeded(CreateDiscount createDiscount) {
        if (createDiscount.isCopyCreate()) {
            UpdateUseDiscount updateUseDiscount = new UpdateUseDiscount(Collections.singletonList(createDiscount.getDiscountPolicyId()), Yn.N);
            updateDiscountUseYn(updateUseDiscount);
        }
    }


    @Override
    public List<DiscountPolicyResponseDto> updateDiscountUseYn(UpdateUseDiscount updateUseDiscount) {
        List<DiscountPolicy> discountPolicies = discountFetchService.fetchDiscountEntities(updateUseDiscount.getDiscountPolicyIds());

        if(updateUseDiscount.isActive()) updateActiveYn(discountPolicies, Yn.Y);
        else updateActiveYn(discountPolicies, Yn.N);

        discountTargetQueryService.updateDiscountTargetUseYn(discountPolicies, updateUseDiscount.getActiveYn());

        return discountPolicies.stream()
                .map(DiscountPolicyResponseDto::from)
                .collect(Collectors.toList());
    }


    private void updateActiveYn(List<DiscountPolicy> discountPolicies, Yn activeYn) {
        discountPolicies.forEach(discountPolicy -> discountPolicy.setActiveYn(activeYn));
    }


}
