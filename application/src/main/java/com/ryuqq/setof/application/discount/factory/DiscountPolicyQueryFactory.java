package com.ryuqq.setof.application.discount.factory;

import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchParams;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.discount.query.DiscountPolicySearchCriteria;
import com.ryuqq.setof.domain.discount.query.DiscountPolicySortKey;
import com.ryuqq.setof.domain.discount.vo.ApplicationType;
import com.ryuqq.setof.domain.discount.vo.PublisherType;
import com.ryuqq.setof.domain.discount.vo.StackingGroup;
import com.ryuqq.setof.domain.seller.id.SellerId;
import org.springframework.stereotype.Component;

/**
 * DiscountPolicyQueryFactory - 할인 정책 검색 조건 변환 Factory.
 *
 * <p>{@link DiscountPolicySearchParams}를 도메인 레이어의 {@link DiscountPolicySearchCriteria}로 변환합니다.
 * nullable String enum 값의 안전한 변환 및 QueryContext 조립 책임을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountPolicyQueryFactory {

    /**
     * {@link DiscountPolicySearchParams}로부터 {@link DiscountPolicySearchCriteria}를 생성합니다.
     *
     * @param params 외부 검색 파라미터
     * @return 도메인 검색 조건
     */
    public DiscountPolicySearchCriteria create(DiscountPolicySearchParams params) {
        SellerId sellerId = params.sellerId() != null ? SellerId.of(params.sellerId()) : null;
        ApplicationType applicationType = parseApplicationType(params.applicationType());
        PublisherType publisherType = parsePublisherType(params.publisherType());
        StackingGroup stackingGroup = parseStackingGroup(params.stackingGroup());

        DiscountPolicySortKey sortKey = parseSortKey(params.sortKey());
        SortDirection sortDirection = SortDirection.fromString(params.sortDirection());
        PageRequest pageRequest = PageRequest.of(params.page(), params.size());
        QueryContext<DiscountPolicySortKey> queryContext =
                QueryContext.of(sortKey, sortDirection, pageRequest);

        return DiscountPolicySearchCriteria.of(
                sellerId,
                applicationType,
                publisherType,
                stackingGroup,
                params.activeOnly(),
                queryContext);
    }

    /**
     * String 값을 {@link ApplicationType}으로 변환합니다. null 또는 빈 문자열이면 null을 반환합니다.
     *
     * @param value 변환할 문자열
     * @return 변환된 enum (nullable)
     */
    private ApplicationType parseApplicationType(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return ApplicationType.valueOf(value.toUpperCase().trim());
    }

    /**
     * String 값을 {@link PublisherType}으로 변환합니다. null 또는 빈 문자열이면 null을 반환합니다.
     *
     * @param value 변환할 문자열
     * @return 변환된 enum (nullable)
     */
    private PublisherType parsePublisherType(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return PublisherType.valueOf(value.toUpperCase().trim());
    }

    /**
     * String 값을 {@link StackingGroup}으로 변환합니다. null 또는 빈 문자열이면 null을 반환합니다.
     *
     * @param value 변환할 문자열
     * @return 변환된 enum (nullable)
     */
    private StackingGroup parseStackingGroup(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return StackingGroup.valueOf(value.toUpperCase().trim());
    }

    /**
     * String 값을 {@link DiscountPolicySortKey}로 변환합니다. null 또는 유효하지 않은 값이면 기본 키를 반환합니다.
     *
     * @param value 변환할 문자열
     * @return 변환된 정렬 키 (기본값: CREATED_AT)
     */
    private DiscountPolicySortKey parseSortKey(String value) {
        if (value == null || value.isBlank()) {
            return DiscountPolicySortKey.defaultKey();
        }
        try {
            return DiscountPolicySortKey.valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return DiscountPolicySortKey.defaultKey();
        }
    }
}
