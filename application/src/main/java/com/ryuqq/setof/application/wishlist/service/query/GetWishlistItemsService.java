package com.ryuqq.setof.application.wishlist.service.query;

import com.ryuqq.setof.application.wishlist.assembler.WishlistAssembler;
import com.ryuqq.setof.application.wishlist.dto.query.WishlistSearchParams;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemSliceResult;
import com.ryuqq.setof.application.wishlist.factory.WishlistQueryFactory;
import com.ryuqq.setof.application.wishlist.manager.WishlistReadManager;
import com.ryuqq.setof.application.wishlist.port.in.query.GetWishlistItemsUseCase;
import com.ryuqq.setof.domain.wishlist.query.WishlistSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetWishlistItemsService - 찜 목록 조회 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetWishlistItemsService implements GetWishlistItemsUseCase {

    private final WishlistQueryFactory queryFactory;
    private final WishlistReadManager readManager;
    private final WishlistAssembler assembler;

    public GetWishlistItemsService(
            WishlistQueryFactory queryFactory,
            WishlistReadManager readManager,
            WishlistAssembler assembler) {
        this.queryFactory = queryFactory;
        this.readManager = readManager;
        this.assembler = assembler;
    }

    @Override
    public WishlistItemSliceResult execute(WishlistSearchParams params) {
        WishlistSearchCriteria criteria = queryFactory.createCriteria(params);
        List<WishlistItemResult> items = readManager.fetchSlice(criteria);
        long totalElements = readManager.countByUserId(params.userId());
        return assembler.toSliceResult(items, criteria.size(), totalElements);
    }
}
