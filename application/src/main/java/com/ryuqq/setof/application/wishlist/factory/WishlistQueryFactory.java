package com.ryuqq.setof.application.wishlist.factory;

import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.application.wishlist.dto.query.WishlistSearchParams;
import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.wishlist.query.WishlistSearchCriteria;
import com.ryuqq.setof.domain.wishlist.query.WishlistSortKey;
import org.springframework.stereotype.Component;

/**
 * WishlistQueryFactory - 찜 조회 Criteria 생성 Factory.
 *
 * <p>WishlistSearchParams(Application DTO) → WishlistSearchCriteria(Domain) 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class WishlistQueryFactory {

    private final CommonVoFactory commonVoFactory;

    public WishlistQueryFactory(CommonVoFactory commonVoFactory) {
        this.commonVoFactory = commonVoFactory;
    }

    public WishlistSearchCriteria createCriteria(WishlistSearchParams params) {
        CursorPageRequest<Long> cursorPageRequest =
                commonVoFactory.createCursorPageRequestAfterCursor(
                        params.lastDomainId(), params.pageSize());

        CursorQueryContext<WishlistSortKey, Long> queryContext =
                commonVoFactory.createCursorQueryContext(
                        WishlistSortKey.defaultKey(), SortDirection.DESC, cursorPageRequest);

        return WishlistSearchCriteria.of(LegacyMemberId.of(params.userId()), queryContext);
    }
}
