package com.connectly.partnerAdmin.module.discount.service.fetch;

import com.connectly.partnerAdmin.module.discount.dto.DiscountTargetResponseDto;
import com.connectly.partnerAdmin.module.discount.entity.DiscountTarget;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DiscountTargetFetchService {

    List<DiscountTarget> fetchDiscountTargetEntities(List<Long> discountPolicyIds);

    Page<? extends DiscountTargetResponseDto> fetchDiscountTargets(long discountPolicyId, IssueType issueType, Pageable pageable);


}
