package com.setof.connectly.module.discount.repository;

import com.setof.connectly.module.discount.dto.DiscountCacheDto;
import com.setof.connectly.module.discount.enums.IssueType;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DiscountPolicyFindRepository {

    Optional<DiscountCacheDto> fetchDiscountInfo(long targetId, IssueType issueType);

    List<DiscountCacheDto> fetchDiscountInfos(Set<Long> targetIds, IssueType issueType);
}
