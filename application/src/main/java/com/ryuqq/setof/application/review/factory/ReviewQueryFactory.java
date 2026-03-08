package com.ryuqq.setof.application.review.factory;

import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.application.review.dto.query.AvailableReviewSearchParams;
import com.ryuqq.setof.application.review.dto.query.MyReviewSearchParams;
import com.ryuqq.setof.application.review.dto.query.ProductGroupReviewSearchParams;
import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.review.query.AvailableReviewSearchCriteria;
import com.ryuqq.setof.domain.review.query.AvailableReviewSortKey;
import com.ryuqq.setof.domain.review.query.MyReviewSearchCriteria;
import com.ryuqq.setof.domain.review.query.MyReviewSortKey;
import com.ryuqq.setof.domain.review.query.ProductGroupReviewSearchCriteria;
import com.ryuqq.setof.domain.review.query.ProductGroupReviewSortKey;
import org.springframework.stereotype.Component;

/**
 * ReviewQueryFactory - 리뷰 조회 SearchParams → SearchCriteria 변환 Factory.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ReviewQueryFactory {

    private final CommonVoFactory commonVoFactory;

    public ReviewQueryFactory(CommonVoFactory commonVoFactory) {
        this.commonVoFactory = commonVoFactory;
    }

    public AvailableReviewSearchCriteria createAvailableReviewCriteria(
            AvailableReviewSearchParams params) {
        LegacyMemberId legacyMemberId =
                params.legacyUserId() != null ? LegacyMemberId.of(params.legacyUserId()) : null;
        MemberId memberId = params.memberId() != null ? MemberId.of(params.memberId()) : null;

        CursorPageRequest<Long> pageRequest =
                params.lastOrderId() != null
                        ? commonVoFactory.createCursorPageRequest(
                                params.lastOrderId(), params.sizeOrDefault())
                        : commonVoFactory.createCursorPageRequest(params.sizeOrDefault());

        CursorQueryContext<AvailableReviewSortKey, Long> queryContext =
                commonVoFactory.createCursorQueryContext(
                        AvailableReviewSortKey.defaultKey(), SortDirection.DESC, pageRequest);

        return AvailableReviewSearchCriteria.of(legacyMemberId, memberId, queryContext);
    }

    public MyReviewSearchCriteria createMyReviewCriteria(MyReviewSearchParams params) {
        LegacyMemberId legacyMemberId =
                params.legacyUserId() != null ? LegacyMemberId.of(params.legacyUserId()) : null;
        MemberId memberId = params.memberId() != null ? MemberId.of(params.memberId()) : null;

        CursorPageRequest<Long> pageRequest =
                params.lastReviewId() != null
                        ? commonVoFactory.createCursorPageRequest(
                                params.lastReviewId(), params.sizeOrDefault())
                        : commonVoFactory.createCursorPageRequest(params.sizeOrDefault());

        CursorQueryContext<MyReviewSortKey, Long> queryContext =
                commonVoFactory.createCursorQueryContext(
                        MyReviewSortKey.defaultKey(), SortDirection.DESC, pageRequest);

        return MyReviewSearchCriteria.of(legacyMemberId, memberId, queryContext);
    }

    public ProductGroupReviewSearchCriteria createProductGroupReviewCriteria(
            ProductGroupReviewSearchParams params) {
        ProductGroupReviewSortKey sortKey =
                ProductGroupReviewSortKey.fromOrderType(params.orderType());

        PageRequest pageRequest =
                commonVoFactory.createPageRequest(params.pageOrDefault(), params.sizeOrDefault());

        QueryContext<ProductGroupReviewSortKey> queryContext =
                commonVoFactory.createQueryContext(sortKey, SortDirection.DESC, pageRequest);

        return ProductGroupReviewSearchCriteria.of(params.productGroupId(), queryContext);
    }
}
