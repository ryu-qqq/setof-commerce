package com.ryuqq.setof.application.shippingpolicy.factory;

import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.application.shippingpolicy.dto.query.ShippingPolicySearchParams;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.shippingpolicy.query.ShippingPolicySearchCriteria;
import com.ryuqq.setof.domain.shippingpolicy.query.ShippingPolicySortKey;
import org.springframework.stereotype.Component;

/**
 * ShippingPolicyQueryFactory - 배송 정책 Query Factory
 *
 * <p>SearchParams → SearchCriteria 변환을 담당합니다.
 *
 * @author ryu-qqq
 */
@Component
public class ShippingPolicyQueryFactory {

    private final CommonVoFactory commonVoFactory;

    public ShippingPolicyQueryFactory(CommonVoFactory commonVoFactory) {
        this.commonVoFactory = commonVoFactory;
    }

    public ShippingPolicySearchCriteria createCriteria(ShippingPolicySearchParams params) {
        ShippingPolicySortKey sortKey = resolveSortKey(params.sortKey());
        SortDirection sortDirection = commonVoFactory.parseSortDirection(params.sortDirection());
        PageRequest pageRequest = commonVoFactory.createPageRequest(params.page(), params.size());

        QueryContext<ShippingPolicySortKey> queryContext =
                commonVoFactory.createQueryContext(
                        sortKey,
                        sortDirection,
                        pageRequest,
                        params.searchParams().includeDeleted());

        SellerId sellerId = SellerId.of(params.sellerId());

        return new ShippingPolicySearchCriteria(sellerId, queryContext);
    }

    private ShippingPolicySortKey resolveSortKey(String sortKeyString) {
        if (sortKeyString == null || sortKeyString.isBlank()) {
            return ShippingPolicySortKey.defaultKey();
        }

        for (ShippingPolicySortKey key : ShippingPolicySortKey.values()) {
            if (key.fieldName().equalsIgnoreCase(sortKeyString)
                    || key.name().equalsIgnoreCase(sortKeyString)) {
                return key;
            }
        }

        return ShippingPolicySortKey.defaultKey();
    }
}
